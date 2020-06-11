package com.information.rxjavaapplication.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.information.rxjavaapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class HelloActivityComposite extends AppCompatActivity {
    public static final String TAG = HelloActivityComposite.class.getSimpleName();

    private Unbinder mUnbinder;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @BindView(R.id.hello_composite_textView)
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_composite);
        mUnbinder = ButterKnife.bind(this);

        Disposable disposable = Observable.create((ObservableEmitter<String> emitter) -> {
            emitter.onNext("hello world");
            emitter.onComplete();
        }).subscribe(mTextView::setText);

        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder!=null) mUnbinder.unbind();
        if(!mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    }
}

package com.information.rxjavaapplication.activitys;

import android.os.Bundle;

import com.information.rxjavaapplication.R;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class HelloActivity extends AppCompatActivity {
    @BindView(R.id.hello_textView)
    TextView mTextView;
    @BindView(R.id.hello_textView2)
    TextView mTextView2;

    Unbinder mUnbinder;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloactivity);
        mUnbinder = ButterKnife.bind(this);
        basic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 강제로 dispose시켜서 메모리 누수를 방지
        if (!mDisposable.isDisposed()) mDisposable.dispose();
        if(mUnbinder!=null) mUnbinder.unbind();
    }

    @OnClick(R.id.hello_button)
    void clickFilter() {
        filter();
    }

    private void filter() {
        Iterable<String> samples = Arrays.asList("banana", "apple", "orange", "mango", "melon");
        Observable.fromIterable(samples)
                .filter(s -> s.contains("apple"))
                .first("Not Found")
                .subscribe(o -> mTextView.setText(o));
    }

    private void basic() {
        // 액티비티가 비정상적으로 종료되는 경우, 구독자가 텍스트 뷰를 참조하기 때문에 가비지 컬렉션의 대상이 되지 못함
//        Observer<String> observer = new DisposableObserver<String>() {
//            @Override
//            public void onNext(String s) {
//                mTextView2.setText(s);
//            }
//            @Override
//            public void onError(Throwable e) { }
//            @Override
//            public void onComplete() { }
//        };
//
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("Hello world!");
//                e.onComplete();
//            }
//        }).subscribeWith(observer);

        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) { mTextView2.setText(s); }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        };

//        mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("Hello world!");
//                e.onComplete();
//            }
//        }).subscribeWith(observer);
        mDisposable = Observable.create((ObservableEmitter<String> e) -> {
            e.onNext("hello");
            e.onComplete();
        }).subscribeWith(observer);
    }

    private void lambda() {
        Observable.<String>create(e -> {
           e.onNext("Hello World2");
           e.onComplete();
        }).subscribe(mTextView2::setText);
    }
}

package com.information.rxjavaapplication;

import android.os.Bundle;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class HelloActivityV3 extends RxAppCompatActivity {
    public static final String TAG = HelloActivityV3.class.getSimpleName();

    @BindView(R.id.hellov3_textview)
    TextView hello_textView;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hellov3);

        mUnbinder = ButterKnife.bind(this);

        Observable.just("Hello Rx Workld")
                .compose(bindToLifecycle())
                .subscribe(hello_textView::setText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}

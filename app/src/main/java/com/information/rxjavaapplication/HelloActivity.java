package com.information.rxjavaapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;

public class HelloActivity extends AppCompatActivity {
    private TextView hello_textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                final Iterable<String> samples = Arrays.asList("banana", "apple", "orange", "mango", "melon");
                Observable.fromIterable(samples)
                        .filter(s -> s.contains("apple"))
                        .first("Not Found")
                        .subscribe(o -> hello_textView.setText(o));
            }
        });

        hello_textView = findViewById(R.id.hello_textView);
    }

    private void basic() {
        Observer<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                hello_textView.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello World!");
                emitter.onComplete();
            }
        }).subscribe(observer);
    }

    private void lambda() {
        Observable.<String>create(e -> {
           e.onNext("Hello World2");
           e.onComplete();
        }).subscribe(hello_textView::setText);
    }
}

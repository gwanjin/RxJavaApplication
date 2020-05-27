package com.information.rxjavaapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.information.rxjavaapplication.R;
import com.information.rxjavaapplication.logs.LogAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    private Unbinder mUnbinder;
    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    @BindView(R.id.mainfragment_textview)
    TextView mTextView;

    @BindView(R.id.mainfragment_button)
    Button mButton;

    @BindView(R.id.mainfragment_lambda_button)
    Button mLambdaButton;

    @BindView(R.id.mainfragment_binding_button)
    Button mBindingButton;

    @BindView(R.id.mainfragment_listview)
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        setUpLogger();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getClickEventObservable().map(s -> "clicked").subscribe(getObservable());
        getClickEventObservableWithLambda().map(s -> "clicked lambda").subscribe(getObservable());
        getClickEventObservableWithBiding().subscribe(this::log);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private Observable<String> getClickEventObservableWithBiding() {
        return RxView.clicks(mBindingButton).map(s -> "clicked Biding");
    }

    private Observable<View> getClickEventObservableWithLambda() {
        return Observable.create(e -> mLambdaButton.setOnClickListener(e::onNext));
        /*
        *
        *
        * return Observable.create(new ObservableOnSubscribe<View>() {
        *   @Override
        *    public void subscribe(ObservableEmitter<View> emitter) throws Exception {
        *        mButton.setOnClickListener(new View.OnClickListener() {
        *            @Override
        *            public void onClick(View v) {
        *                emitter.onNext(v);
        *            }
        *        });
        *    }
        *});
        *
        * => view -> emitter.onNext(view) => emitter::onNext
        *
        *
        * return Observable.create(new ObservableOnSubscribe<View>() {
        *   public void subscribe(ObservableEmitter<View> emitter) throws Exception {
        *       mButton.setOnClickListener(emitter::onNext);
        *   }
        * };
        *
        *
        *
        * return Observable.create(e -> mLambdaButton.setOnClickListener(e::onNext));
        *
        *
        *
        *
        * */
    }

    private Observable<View> getClickEventObservable() {
        return Observable.create(new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(ObservableEmitter<View> emitter) throws Exception {
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitter.onNext(v);
                    }
                });
            }
        });
    }

    private DisposableObserver<? super String> getObservable() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                log(s);
            }

            @Override
            public void onError(Throwable e) {
                android.util.Log.e("AAA", e.getMessage());
            }

            @Override
            public void onComplete() {
                log("onComplete");
            }
        };
    }

    private void log(String log) {
        mLogs.add(log);
        mLogAdapter.clear();
        mLogAdapter.addAll(mLogs);
    }

    private void setUpLogger() {
        mLogs = new ArrayList<>();
        mLogAdapter = new LogAdapter(getActivity(), new ArrayList<>());
        mListView.setAdapter(mLogAdapter);
    }
}

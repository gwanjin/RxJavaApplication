package com.information.rxjavaapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.information.rxjavaapplication.R;
import com.information.rxjavaapplication.logs.LogAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PollingFragment extends Fragment {
    private static final long INITIAL_DELAY = 0L;
    private static final long PERIOD = 3L;

    @BindView(R.id.lv_polling_log)
    ListView mLogView;

    Unbinder mUnbinder;
    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_polling, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        setupLogger();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ( mUnbinder != null )
            mUnbinder.unbind();
    }

    @OnClick(R.id.btn_polling)
    void polling() { startPollingV1(); }
    @OnClick(R.id.btn_polling2)
    void polling2() { startPollingV2(); }

    private void startPollingV1() {
        Observable<String> ob = Observable.interval(INITIAL_DELAY, PERIOD, TimeUnit.SECONDS)
                .flatMap(o -> Observable.just("Polling #1 Thread : " + Thread.currentThread()));

        ob.subscribeOn(Schedulers.io())
                .doOnNext(s -> { android.util.Log.d("AAA", "doOnNext " + Thread.currentThread()); })
                .doOnComplete(() -> android.util.Log.d("AAA", "doOnComplete " + Thread.currentThread()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::log);
    }

    private void startPollingV2() {
        Observable<String> ob = Observable.just("Polling #2 Thread : " + Thread.currentThread())
                .repeatWhen(o -> o.delay(3, TimeUnit.SECONDS)); // repeatWhen()함수를 이용해 동일한 Observable객체를 계속 발행, delay함수로 3초 지연

        ob.subscribeOn(Schedulers.io())
                .doOnNext(s -> { android.util.Log.d("AAA", "doOnNext " + Thread.currentThread()); })
                .doOnComplete(() -> android.util.Log.d("AAA", "doOnComplete " + Thread.currentThread()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::log);
    }

    private void log(String log) {
        mLogs.add(log);
        mLogAdapter.clear();
        mLogAdapter.addAll(mLogs);
    }

    private void setupLogger() {
        mLogs = new ArrayList<>();
        mLogAdapter = new LogAdapter(getActivity(), new ArrayList<>());
        mLogView.setAdapter(mLogAdapter);
    }
}
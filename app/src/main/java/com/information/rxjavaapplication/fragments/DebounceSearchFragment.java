package com.information.rxjavaapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.information.rxjavaapplication.R;
import com.information.rxjavaapplication.logs.LogAdapter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class DebounceSearchFragment extends Fragment {
    private Unbinder mUnbinder;
    private Disposable mDisposable;
    private List<String> mLogs;
    private LogAdapter mLogAdapter;

    @BindView(R.id.search_editView)
    EditText mEditText;

    @BindView(R.id.search_listView)
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_debounce_search, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        setUpLogger();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mDisposable = getObservable().debounce(500, TimeUnit.MILLISECONDS)
//                .filter(s -> !TextUtils.isEmpty(s))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(getObserver());

        mDisposable = RxTextView.textChangeEvents(mEditText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> !TextUtils.isEmpty(s.text().toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserverLib());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null)
            mUnbinder.unbind();
    }

    private Observable<CharSequence> getObservable() {
        return Observable.create(emitter -> {
           mEditText.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {
                   emitter.onNext(s);
               }

               @Override
               public void afterTextChanged(Editable s) {

               }
           });
        });
    }

    private DisposableObserver<TextViewTextChangeEvent> getObserverLib() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                log("Search " + textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<CharSequence> getObserver() {
        return new DisposableObserver<CharSequence>() {
            @Override
            public void onNext(CharSequence charSequence) {
                log("Search" + charSequence);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

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
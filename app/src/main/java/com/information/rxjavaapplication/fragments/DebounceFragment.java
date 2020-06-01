package com.information.rxjavaapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.information.rxjavaapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebounceFragment extends Fragment {
    private Unbinder mUnbinder;
    private Disposable mDisposable;

    @BindView(R.id.debounce_textView)
    TextView mTextView;
    @BindView(R.id.debounce_button)
    Button mButton;

    public DebounceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_debounce, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDisposable = getObservable()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);
                    String time = sdf.format(Calendar.getInstance().getTime());
                    mTextView.setText("Clicked : " + time);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) mUnbinder.unbind();
        mDisposable.dispose();
    }

    private Observable<View> getObservable() {
        return Observable.create(emitter -> mButton.setOnClickListener(emitter::onNext));
    }
}

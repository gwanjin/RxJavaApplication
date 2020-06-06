package com.information.rxjavaapplication.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.information.rxjavaapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class AsyncActivity extends AppCompatActivity {
    private static final String TAG = AsyncActivity.class.getSimpleName();
    private MyAsyncTask mAsyncTask;
    private Unbinder mUnbinder;

    @BindView(R.id.textView)
    TextView mAndroidTextView;
    @BindView(R.id.textView2)
    TextView mRxTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        mUnbinder = ButterKnife.bind(this);
        initAsyncTask();
        initRxAsyncTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initAsyncTask() {
        mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute("Hello", "Async", "Task");
    }

    private void initRxAsyncTask() {
        Observable.just("Hello", "Rx", "Java")
                .reduce((x,y) -> x+" "+y)
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getObserver())
                // onSuccess, onError, onComplete順
                .subscribe(mRxTextView::setText,
                        e -> Log.e(TAG, e.getMessage()),
                        () -> Log.i(TAG, "done"));
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAndroidTextView.setText(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder word = new StringBuilder();
            for (String s : strings) {
                word.append(s).append(" ");
            }
            return word.toString();
        }
    }

    private MaybeObserver<String> getObserver() {
        return new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String s) {
                mRxTextView.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "done");
            }
        };
    }
}
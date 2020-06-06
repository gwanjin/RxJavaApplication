package com.information.rxjavaapplication.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.information.rxjavaapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AsyncTaskFragment extends Fragment {
    private MyAsyncView mMyAsyncView;
    private Unbinder mUnbinder;

    @BindView(R.id.async_textView)
    TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_async_task, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMyAsyncView = new MyAsyncView();
        mMyAsyncView.execute("Hello", "Async", "Task");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null)
            mUnbinder.unbind();
    }

    class MyAsyncView extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mTextView.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            for (String s : strings) {
                sb.append(s).append(" ");
            }
            return sb.toString();
        }
    }
}
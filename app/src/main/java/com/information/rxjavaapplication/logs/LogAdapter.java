package com.information.rxjavaapplication.logs;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.information.rxjavaapplication.R;

import java.util.List;

public class LogAdapter extends ArrayAdapter {
    public LogAdapter(Context context, List<String> logs) {
        super(context, R.layout.textview_log, R.id.tv_log, logs);
    }
}

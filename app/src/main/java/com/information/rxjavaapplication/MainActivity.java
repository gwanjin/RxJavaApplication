package com.information.rxjavaapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.information.rxjavaapplication.activitys.AsyncActivity;
import com.information.rxjavaapplication.activitys.HelloActivity;
import com.information.rxjavaapplication.activitys.HelloRxAppActivity;
import com.information.rxjavaapplication.activitys.TimerActivity;
import com.information.rxjavaapplication.fragments.AsyncTaskFragment;
import com.information.rxjavaapplication.fragments.DebounceFragment;
import com.information.rxjavaapplication.fragments.DebounceSearchFragment;
import com.information.rxjavaapplication.fragments.MainFragment;
import com.information.rxjavaapplication.fragments.OkHttpFragment;
import com.information.rxjavaapplication.fragments.PollingFragment;
import com.information.rxjavaapplication.fragments.RecyclerViewFragment;
import com.information.rxjavaapplication.fragments.VolleyFragment;
import com.information.rxjavaapplication.volley.LocalVolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });

        if(savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new MainFragment(), MainFragment.TAG).commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new DebounceFragment()).commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new DebounceSearchFragment()).commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new RecyclerViewFragment()).commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new AsyncTaskFragment()).commit();
//            Intent intent = new Intent(getApplicationContext(), AsyncActivity.class);
//            startActivity(intent);
//            Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
//            startActivity(intent);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new PollingFragment()).commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, new VolleyFragment()).commit();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(android.R.id.content, OkHttpFragment.newInstance()).commit();
//            Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(getApplicationContext(), HelloRxAppActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

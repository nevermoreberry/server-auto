package com.keqin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton get = findViewById(R.id.get);
        get.setOnClickListener(v -> {
//            MainApiServer.get()
//                    .config(new ConfigRequest("0"))
//                    .subscribe(configBean -> {
//                        L.i("Tool", configBean.toString());
//                    }, throwable -> {
//                        L.e("Tool", throwable);
//                    });
        });

        FloatingActionButton post = findViewById(R.id.post);
        post.setOnClickListener(v -> {
//            MainApiServer.get()
//                    .verifyCode(new VerifyCodeRequest("15757188505"))
//                    .subscribe(result -> {
//                        L.i("Tool", result.toString());
//                    }, throwable -> {
//                        L.e("Tool", throwable);
//                    });
        });
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

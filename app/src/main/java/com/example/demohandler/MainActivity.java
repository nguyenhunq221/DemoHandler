package com.example.demohandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    static Button button;
    static ProgressBar progressBar;
    boolean isStart;
    static int value = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        button.setOnClickListener(v -> {
            isStart = !isStart;
            doWork();
            button.setText(isStart ? "Stop" : "Start");
        });
    }

//    public void doWork(){
//        AsyncTask<Integer, Integer, String> task = new AsyncTask<Integer, Integer, String>() {
//            @Override
//            protected String doInBackground(Integer... integers) {
//                int i = 0;
//                while (i < integers[0] + integers[1]){
//                    i++;
//                    publishProgress(i);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return "Finish " + (integers[0] + integers[1]);
//            }
//
//            @Override
//            protected void onProgressUpdate(Integer... values) {
//                super.onProgressUpdate(values);
//                button.setText("Hello world " + values[0]);
//                progressBar.setProgress(values[0]);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                button.setText(s);
//            }
//        };
//
//        task.execute(70, 30);
//    }

    public static final Handler handler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE_PROGRESS:
                    int i = (int) msg.arg1;
                    if(progressBar != null) {
                        progressBar.setProgress(i);
                    }
                    break;
            }
        }
    };

    public static final int MSG_UPDATE_PROGRESS = 100;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int i = value;
            while (i < 100 && isStart){
                i++;
                value = i;
                Message msg = handler.obtainMessage(MSG_UPDATE_PROGRESS, i, 0, null);
                handler.sendMessage(msg);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(isStart){
                value = 0;
            }
            isStart = false;
        }
    };

    Thread background = new Thread(runnable);

    public void doWork(){
        background = new Thread(runnable);
        background.start();

    }
}
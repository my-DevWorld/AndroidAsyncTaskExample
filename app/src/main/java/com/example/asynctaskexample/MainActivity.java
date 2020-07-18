package com.example.asynctaskexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_horizontal);
        button = findViewById(R.id.start);
        button.setOnClickListener(v -> {startAsyncTask();});
    }

    private void startAsyncTask() {
        button.setClickable(false);
        AsyncTaskExample asyncTaskExample = new AsyncTaskExample(this);
        asyncTaskExample.execute(10);
    }

    private static class AsyncTaskExample extends AsyncTask<Integer, Integer, String>{
        private WeakReference<MainActivity> mainActivityWeakReference;

        public AsyncTaskExample(MainActivity activity) {
            mainActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = mainActivityWeakReference.get();
            if (activity == null || activity.isFinishing()){
                return;
            }
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for(int i = 0; i < integers[0]; i++){
                publishProgress((i * 100) / integers[0]);
                System.out.println("||||||||||||||||||" + (i * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = mainActivityWeakReference.get();
            if (activity == null || activity.isFinishing()){
                return;
            }
            activity.progressBar.setProgress(values[0]);
            activity.button.setText("Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity activity = mainActivityWeakReference.get();
            if (activity == null || activity.isFinishing()){
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.button.setClickable(true);
            activity.button.setText("Start");
        }
    }
}
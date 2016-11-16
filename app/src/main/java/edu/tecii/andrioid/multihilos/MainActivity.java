package edu.tecii.andrioid.multihilos;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    ProgressBar bar1;
    ProgressBar bar2;

    TextView msgWorking;
    TextView msgReturned;
    ScrollView myScrollView;

    protected boolean isRunning = false;
    protected final int MAX_SEC = 30;
    protected int globalIntTest = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String returnedValue = (String)msg.obj;
            msgReturned.append("\n returned value: " + returnedValue);
            myScrollView.fullScroll(View.FOCUS_DOWN);
            bar1.incrementProgressBy(1);

            if(bar1.getProgress() == MAX_SEC){
                msgReturned.append(" \nDone \n back thread has been stopped");
                isRunning = false;
            }

            if (bar1.getProgress() == bar1.getMax()){
                msgWorking.setText("Done");
                bar1.setVisibility(View.INVISIBLE);
                bar2.setVisibility(View.INVISIBLE);
            }
            else {
                msgWorking.setText("Working..." + bar1.getProgress());
            }
        }
    };


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        bar1 = (ProgressBar) findViewById(R.id.progress1);
        bar1.setProgress(0);
        bar1.setMax(MAX_SEC);
        bar2 = (ProgressBar) findViewById(R.id.progress2);
        msgWorking = (TextView)findViewById(R.id.txtWorkProgress);
        msgReturned = (TextView)findViewById(R.id.txtReturnedValues);
        myScrollView = (ScrollView)findViewById(R.id.myscroLLer);
        globalIntTest = 1;
    }

    public void onStart(){
        super.onStart();
        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < MAX_SEC && isRunning; i++){
                        Thread.sleep(1000);
                        Random rnd = new Random();
                        int localData =  (int) rnd.nextInt(101);
                        String data = "Data-" + getGlobalIntTest() + ""
                                + localData;
                        IncreaseGlobalIntTest(1);
                        Message msg = handler
                                .obtainMessage(1, (String)data);
                        if(isRunning){
                            handler.sendMessage(msg);
                        }
                    }
                }
                catch (Throwable t){
                    isRunning = false;
                }
            }
        });
        isRunning = true;
        background.start();
    }

    public void onStop(){
        super.onStop();
        isRunning = false;
    }

    public synchronized int getGlobalIntTest(){
        return globalIntTest;
    }

    public synchronized int IncreaseGlobalIntTest(int inc){
        return globalIntTest += inc;
    }
}

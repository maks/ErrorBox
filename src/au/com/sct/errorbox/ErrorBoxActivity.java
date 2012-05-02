package au.com.sct.errorbox;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ErrorBoxActivity extends Activity implements OnClickListener,
                OnCheckedChangeListener {

    private static final String TAG = ErrorBoxActivity.class.getSimpleName();

    private static final long TIMER_PERIOD = 5 * 1000;

    private Timer testTimer = new Timer("Test Timer");
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating ErrorBox");

        setContentView(R.layout.main);
        
        ((Button) this.findViewById(R.id.error_button))
                        .setOnClickListener(this);
        ((CheckBox) this.findViewById(R.id.use_errorhandler_checkbox))
                        .setOnCheckedChangeListener(this);

        // start timer
        TestTimerTask testTask = new TestTimerTask();
        testTimer.scheduleAtFixedRate(testTask,
                        TIMER_PERIOD, TIMER_PERIOD);
    }
    
    @Override
    public void onClick(View v) {
        Log.e(TAG, "throwing error...");
        throw new RuntimeException("intentional error");
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setExceptionHandler(isChecked);
    }

    private void setExceptionHandler(boolean set) {
        TestExceptionHandler handler = null;
        if (set) {
            handler = new TestExceptionHandler(this);
            Log.d(TAG, "setting Error Handler");
        } else {
            Log.d(TAG, "disabling Error Handler");
        }
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
    
    class TestTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "timer reporting in");
        }
    }
    
    public class TestExceptionHandler implements UncaughtExceptionHandler {
        
        private Activity activity;
        
        public TestExceptionHandler(Activity context) {
            this.activity = context;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e(TAG, "Caught Unhandled exception", ex);
            // throw new RuntimeException("we're done");
            Intent intent = new Intent(activity, ErrorBoxActivity.class);
            activity.finish();
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
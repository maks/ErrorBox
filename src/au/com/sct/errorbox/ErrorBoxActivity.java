package au.com.sct.errorbox;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ErrorBoxActivity extends Activity implements OnClickListener {
    
    private static final String TAG = ErrorBoxActivity.class.getSimpleName();

    private static final long TIMER_PERIOD = 5 * 1000;

    private Timer testTimer = new Timer("Test Timer");

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button) this.findViewById(R.id.error_button))
                        .setOnClickListener(this);

        // start timer
        TestTimerTask testTask = new TestTimerTask();
        testTimer.scheduleAtFixedRate(testTask,
                        TIMER_PERIOD, TIMER_PERIOD);
    }
    
    class TestTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "timer reporting in");
        }
    }
    
    @Override
    public void onClick(View v) {
        Log.e(TAG, "throwing error...");
        throw new RuntimeException("intentional error");
    }
}
package edu.buffalo.samplewakelock;

import java.util.Date;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener{

	Button start = null;
	Button stop = null;
	TextView timerLogs = null;
	TextView status = null;
	WakeLock timerLock = null;
	PowerManager powerManager = null;
	TimerThread timerThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		status = (TextView) findViewById(R.id.status);
		timerLogs = (TextView) findViewById(R.id.timerLogs);
		powerManager = (PowerManager) MainActivity.this.getSystemService(POWER_SERVICE);

		start.setOnClickListener(this); 
		stop.setOnClickListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.start) {
			timerLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "timerWakeLock");
			timerLock.acquire();
			timerLogs.setText("Timer : \n");
			status.setText("Timer started...");
			timerThread = new TimerThread();
			timerThread.startTimer();
			timerThread.start();
		}

		if(v.getId() == R.id.stop) {
			timerThread.stopTimer();
			status.setText("Timer stopped.");
			timerLock.release();
			timerThread.interrupt();
		}
	}



	class TimerThread extends Thread
	{

		boolean isTimerRunning = false;

		public void startTimer()
		{
			isTimerRunning = true;
		}

		public void stopTimer()
		{
			isTimerRunning = false;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isTimerRunning)
			{
				try {
					final String currentTime = new Date(
							System.currentTimeMillis())
					.toGMTString();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							timerLogs.append(currentTime + "\n");
						}
					});
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//			if(!isTimerRunning)
			//				this.interrupt();

		}

	}


}

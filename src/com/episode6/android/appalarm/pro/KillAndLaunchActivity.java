package com.episode6.android.appalarm.pro;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class KillAndLaunchActivity extends Activity {
	public static final String EXTRA_PACKAGE_TO_RESTART = "package_to_restart";
	public static final String EXTRA_INTENT_TO_LAUNCH = "intent_to_launch";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String packRestart = getIntent().getStringExtra(
				EXTRA_PACKAGE_TO_RESTART);
		try {
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			am.restartPackage(packRestart);
		} catch (Exception e) {
			Toast.makeText(this,
					"There was a problem killing package: " + packRestart,
					Toast.LENGTH_LONG).show();
		}

		Intent i = getIntent().getParcelableExtra(EXTRA_INTENT_TO_LAUNCH);
		try {
			startActivity(i);
			new CountDownTimer(9000, 3000) {
				

				@Override
				public void onFinish() {
					finish();
				}

				@Override
				public void onTick(long arg0) {
					AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					if(!am.isMusicActive()){
						Intent i = new Intent("com.android.music.musicservicecommand");
						i.putExtra("command", "play");
						sendBroadcast(i);
						Intent downIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON,
								null);
						KeyEvent downEvent2 = new KeyEvent(KeyEvent.ACTION_DOWN,
								KeyEvent.KEYCODE_MEDIA_PLAY);
						downIntent2.putExtra(Intent.EXTRA_KEY_EVENT, downEvent2);
						sendOrderedBroadcast(downIntent2, null);
					}

				}

			}.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method m = ActivityManager.class.getMethod(
					"killBackgroundProcesses", new Class[] { String.class });
			// setContentView(R.layout.froyo_kill_screen);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			Log.i("AppAlarm", "old version finishing activity");
			finish();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// setContentView(R.layout.froyo_kill_screen);
		super.onConfigurationChanged(newConfig);
	}

}

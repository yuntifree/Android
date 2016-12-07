package com.yunxingzh.wireless.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.utils.NetUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/***
 * 当界面网络异常时
 */
public class HttpErrorActivity extends Activity {

	private final static int MSG_TIMER_RUNNING = 1;
	private final static int DELAY = 1000;
	private final static int PERIOD = 5000;

	private Timer mTimer = new Timer();
	private Handler mHandler = new WeakHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_http_error);
		mTimer.schedule(mTimerTask, DELAY, PERIOD);
	}

	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(MSG_TIMER_RUNNING);
		}
	};

	static class WeakHandler extends Handler {
		WeakReference<Activity> mActivityReference;

		public WeakHandler(Activity activity) {
			mActivityReference = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			HttpErrorActivity activity = (HttpErrorActivity) mActivityReference.get();
			if (activity != null) {
				if (NetUtils.isNetworkAvailable(activity.getApplicationContext())) {
					activity.mTimer.cancel();
					activity.finish();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN || super.onKeyDown(keyCode, event);
	}
}

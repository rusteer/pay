package com.baidu.opengame.sdk.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.opengame.sdk.BDGameSDK;

public class RunningStateService extends Service {

	private static final int STATE_HOME = 0;
	private static final int STATE_INNER = 1;
	private static final int STATE_OTHER = 2;

	private static String PKG_NAME_BASE = BDGameSDK.getInstance()
			.getAppPackageName();

//	public static final String TAG = "RunningStateService";

	private RunningStateService mServices = null;
	private RunningStateListenerTask mListenerTask = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mServices = this;
		mListenerTask = new RunningStateListenerTask();
		mListenerTask.execute();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		mListenerTask.stopTask();
		mListenerTask = null;
		super.onDestroy();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STATE_HOME:
				BDGameSDK.getInstance().hideFloatView();
				break;
			case STATE_INNER:
				BDGameSDK.getInstance().showFloatView();
				break;
			case STATE_OTHER:
				BDGameSDK.getInstance().hideFloatView();
				break;
			}
		};
	};

	class RunningStateListenerTask extends AsyncTask<Object, Integer, Boolean> {
		private boolean isStoped = false;
		private List<String> homeLists = null;
		private int lastState = -1;

		public RunningStateListenerTask() {
			isStoped = false;
			homeLists = getHomes();
			lastState = getCurrentState();
			sendBroadcast(lastState);
		}

		private void stopTask() {
			isStoped = true;
		}

		private void sendBroadcast(int state) {
			// Log.d(TAG, "sendBroadcast() state = " + state);
			switch (state) {
			case STATE_HOME:
				mHandler.sendEmptyMessage(STATE_HOME);
				break;
			case STATE_INNER:
				mHandler.sendEmptyMessage(STATE_INNER);
				break;
			case STATE_OTHER:
				mHandler.sendEmptyMessage(STATE_OTHER);
				break;
			}
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			while (!isStoped) {
				int state = getCurrentState();
				if (lastState != state) {
					// Log.d(TAG, "doInBackground() state = " + state);
					// Log.d(TAG, "doInBackground() lastState = " + lastState);
					sendBroadcast(state);
					lastState = state;
				}

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return true;
		}

		private int getCurrentState() {
			if (isHome())
				return STATE_HOME;
			else if (isInner())
				return STATE_INNER;
			else
				return STATE_OTHER;
		}

		private boolean isInner() {
			boolean isInner = false;
			try {
				ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningAppProcessInfo> localList = manager
						.getRunningAppProcesses();
				if (localList == null) {
					return false;
				}

				if (PKG_NAME_BASE == null || TextUtils.isEmpty(PKG_NAME_BASE)) {

					PKG_NAME_BASE = getApplicationContext().getPackageName();
				}
//				Log.d(TAG, "PKG_NAME_BASE = " + PKG_NAME_BASE);
				for (ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo : localList) {

					if ((localRunningAppProcessInfo.processName
							.equals(PKG_NAME_BASE))
							&& (localRunningAppProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
						isInner = true;
						// Log.d(TAG, "isInner() isInner = " + isInner);
						return isInner;
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return isInner;
		}

		private boolean isHome() {
			boolean isHome = false;
			ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> info = manager.getRunningTasks(1);
			isHome = homeLists.contains(info.get(0).topActivity
					.getPackageName());
			// Log.d(TAG, "isHome() isHome = " + isHome);
			return isHome;
		}

		private List<String> getHomes() {
			List<String> packages = new ArrayList<String>();
			PackageManager packageManager = mServices.getPackageManager();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			List<ResolveInfo> resolveInfo = packageManager
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			for (ResolveInfo info : resolveInfo) {
				packages.add(info.activityInfo.packageName);
			}
			return packages;
		}
	}
}

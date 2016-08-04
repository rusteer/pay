package com.baidu.opengame.sdk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.baidu.opengame.sdk.BDGameSDK;
import com.baifubao.mpay.ifmgr.UserCenterManager.OnLogoutLinstener;

public class UserCenterActivity extends Activity {
	
	public static final int FLAG_INT = 4;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preferences = getSharedPreferences("mycfg",
				MODE_PRIVATE);
		int storedPreference = preferences.getInt("mykey", 0);
		if (storedPreference == 0) {// 横屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
		} else {// 竖屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		}

		BDGameSDK.getInstance().userCenter(this, new OnLogoutLinstener() {

			@Override
			public void logoutSuc() {
				// TODO Auto-generated method stub
				Log.d("ss", "ss");
			}

			@Override
			public void logoutCancle() {
				// TODO Auto-generated method stub

				finish();

			}

			@Override
			public void logoutFail() {
				// TODO Auto-generated method stub
				;
			}

			@Override
			public void noLoginState() {
				finish();
			}

		});

		initBroadCaseRev();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getIntExtra("type", 0)!=FLAG_INT){
				finish();
			}

		}
	};

	private void initBroadCaseRev() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TiebaActivity.CLOSE_ACTION);
		registerReceiver(mBroadcastReceiver, intentFilter);

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	protected void gotoFeedBackView() {

	}

}

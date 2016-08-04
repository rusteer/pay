package com.baidu.opengame.sdk.activity;

import com.baidu.opengame.sdk.utils.ResUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FeedbackMessageDetailsActivity extends Activity {

	public static final int FLAG_INT = 5;

	public static final String SHOW_TYPE = "showType";
	
	public static final String SHOW_USER_MES = "user_mes";
	
	public static final String SHOW_SERVICE_MES = "service_mes";
	
	public static final String SHOW_TITLE_MES = "mes_title";

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
		initBroadCaseRev();
		int showType = getIntent().getIntExtra(SHOW_TYPE, -1);

		if (showType == 0) {
			gotoFeedBackDetailsView();
		} else if (showType == 1) {
			gotoGmMessageDetailsView();
		} else {
			finish();
		}

	}

	protected void gotoFeedBackDetailsView() {
		setContentView(ResUtil.get_R_Layout(this, "bd_feedback_msg_details"));
		String userMes = getIntent().getStringExtra(SHOW_USER_MES);
		String serviceMes= getIntent().getStringExtra(SHOW_SERVICE_MES);
		TextView userTv = (TextView)findViewById(ResUtil.get_R_id(this, "msg_content_tv"));
		TextView serviceTv = (TextView)findViewById(ResUtil.get_R_id(this, "msg_service_tv"));
		userTv.setText(userMes);
		serviceTv.setText(serviceMes);
		findViewById(ResUtil.get_R_id(this, "title_back")).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
	}

	protected void gotoGmMessageDetailsView() {
		setContentView(ResUtil.get_R_Layout(this, "bd_gm_msg_details"));
		String userMes = getIntent().getStringExtra(SHOW_USER_MES);
		String serviceMes= getIntent().getStringExtra(SHOW_SERVICE_MES);
		String titleMes= getIntent().getStringExtra(SHOW_TITLE_MES);
		TextView userTv = (TextView)findViewById(ResUtil.get_R_id(this, "gm_mes_tv"));
		TextView serviceTv = (TextView)findViewById(ResUtil.get_R_id(this, "gm_time_tv"));
		TextView titleTv = (TextView)findViewById(ResUtil.get_R_id(this, "gm_mes_title"));
		userTv.setText(userMes);
		serviceTv.setText(serviceMes);
		titleTv.setText(titleMes);
		findViewById(ResUtil.get_R_id(this, "title_back")).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
	
	public static void actionToFeedbackMesDetail(Activity activity,int type,String userMes,String serviceMes){
		Intent intent = new Intent(activity, FeedbackMessageDetailsActivity.class);
		intent.putExtra(SHOW_TYPE, type);
		intent.putExtra(SHOW_USER_MES, userMes);
		intent.putExtra(SHOW_SERVICE_MES, serviceMes);
		activity.startActivity(intent);
	}
	
	public static void actionToFeedbackMesDetail(Activity activity,int type,String userMes,String serviceMes,String title){
		Intent intent = new Intent(activity, FeedbackMessageDetailsActivity.class);
		intent.putExtra(SHOW_TYPE, type);
		intent.putExtra(SHOW_USER_MES, userMes);
		intent.putExtra(SHOW_SERVICE_MES, serviceMes);
		intent.putExtra(SHOW_TITLE_MES, title);
		activity.startActivity(intent);
	}

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getIntExtra("type", 0) != FLAG_INT) {
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

}

package com.baidu.opengame.sdk.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.utils.FileConfig;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.widget.BtnFeedbackView;
import com.baidu.opengame.sdk.widget.GmMessageView;
import com.baidu.opengame.sdk.widget.MyFeedbackView;
import com.baidu.opengame.sdk.widget.NormalQuestionView;

public class TiebaActivity extends Activity {

	public static final String CLOSE_ACTION = "com.baidu.gamesdk.feedbackclose";

	public static final int FLAG_INT = 1;

	public static final int FLAG_INT2 = 2;

	private int mType;

	private ViewPager mPager;
	private List<View> listViews;
	private ViewGroup cursor;
	private ImageView imageCursor;
	private TextView t1, t2, t3, t4;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;

	MyFeedbackView mMyFeedbackView;

	BtnFeedbackView mFeedbackView;

	NormalQuestionView mNormalQuestionView;

	GmMessageView mGmMessageView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preferences = getSharedPreferences("mycfg",
				MODE_PRIVATE);
		int storedPreference = preferences.getInt("mykey", 0);
		if (storedPreference == 0) {// 横屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
		} else {// 竖屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(ResUtil.get_R_Layout(this, "trade_details_layout"));
		mType = getIntent().getIntExtra("type", 0);
		InitImageView();
		InitTextView();
		InitViewPager();
		findViewById(ResUtil.get_R_id(this, "title_back")).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
		initBroadCaseRev();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (currIndex == 3) {
			mGmMessageView.notifyDataChange();
		}
	}

	private void saveReadedNewsIds() {
		StringBuilder sb = new StringBuilder();
		if (BDGameSDK.getInstance().getMsgReadedList().size() > 0) {
			int index = 0;
			for (String id : BDGameSDK.getInstance().getMsgReadedList()) {
				sb.append(id);
				if (index > 0) {
					sb.append("&");
				}
				index++;
			}
		}
		FileConfig.Instance(this).setValue("bdgame_mes_list", "list",
				sb.toString());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		if (intent.getIntExtra("type", 0) == FLAG_INT) {
			t1.performClick();
		} else {
			t4.performClick();
		}

	}

	@Override
	protected void onDestroy() {
		saveReadedNewsIds();
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getIntExtra("type", 0) == FLAG_INT
					|| intent.getIntExtra("type", 0) == FLAG_INT2) {

			} else {
				finish();
			}

		}
	};

	private void initBroadCaseRev() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TiebaActivity.CLOSE_ACTION);
		registerReceiver(mBroadcastReceiver, intentFilter);

	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	private void InitTextView() {
		t1 = (TextView) findViewById(ResUtil.get_R_id(this, "text1"));
		t2 = (TextView) findViewById(ResUtil.get_R_id(this, "text2"));
		t3 = (TextView) findViewById(ResUtil.get_R_id(this, "text3"));
		t4 = (TextView) findViewById(ResUtil.get_R_id(this, "text4"));

		t1.setText(ResUtil.get_R_String(this, "tab2_header_title1"));
		t2.setText(ResUtil.get_R_String(this, "tab2_header_title2"));
		t3.setText(ResUtil.get_R_String(this, "tab2_header_title3"));
		t4.setText(ResUtil.get_R_String(this, "tab1_header_title4"));
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));

		findViewById(ResUtil.get_R_id(this, "text1frame")).setOnClickListener(
				new MyOnClickListener(0));
		findViewById(ResUtil.get_R_id(this, "text2frame")).setOnClickListener(
				new MyOnClickListener(1));
		findViewById(ResUtil.get_R_id(this, "text3frame")).setOnClickListener(
				new MyOnClickListener(2));
		findViewById(ResUtil.get_R_id(this, "text4frame")).setOnClickListener(
				new MyOnClickListener(3));

		if (mType == FLAG_INT) {
			setPageTitleColor(0);
		} else {
			setPageTitleColor(3);
		}

	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(ResUtil.get_R_id(this, "vPager"));
		listViews = new ArrayList<View>();

		mMyFeedbackView = new MyFeedbackView(this);
		mFeedbackView = new BtnFeedbackView(this);
		mNormalQuestionView = new NormalQuestionView(this);

		mGmMessageView = new GmMessageView(this);

		listViews.add(mFeedbackView);
		listViews.add(mMyFeedbackView);
		listViews.add(mNormalQuestionView);
		listViews.add(mGmMessageView);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		if (mType == FLAG_INT) {
			mPager.setCurrentItem(0);
		} else {
			mPager.setCurrentItem(3);
			int one = offset * 2 + bmpW;
			int three = one * 3;
			Animation animation = new TranslateAnimation(offset, three, 0, 0);
			currIndex = 3;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	private void InitImageView() {
		cursor = (ViewGroup) findViewById(ResUtil.get_R_id(this, "cursor"));
		imageCursor = (ImageView) findViewById(ResUtil.get_R_id(this,
				"cursorImage"));
		bmpW = BitmapFactory.decodeResource(getResources(),
				ResUtil.get_R_Drawable(this, "page_line")).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 4 - bmpW) / 2;
		imageCursor.setMinimumWidth(screenW / 4);
	}

	private void setPageTitleColor(int index) {

		t1.setTextColor(index == 0 ? getResources().getColor(
				ResUtil.get_R_Color(this, "page_title_select"))
				: getResources().getColor(ResUtil.get_R_Color(this, "black")));
		t2.setTextColor(index == 1 ? getResources().getColor(
				ResUtil.get_R_Color(this, "page_title_select"))
				: getResources().getColor(ResUtil.get_R_Color(this, "black")));
		t3.setTextColor(index == 2 ? getResources().getColor(
				ResUtil.get_R_Color(this, "page_title_select"))
				: getResources().getColor(ResUtil.get_R_Color(this, "black")));
		t4.setTextColor(index == 3 ? getResources().getColor(
				ResUtil.get_R_Color(this, "page_title_select"))
				: getResources().getColor(ResUtil.get_R_Color(this, "black")));

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;
		int two = one * 2;
		int three = one * 3;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			setPageTitleColor(arg0);
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				mMyFeedbackView.getTradeList();
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				break;

			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				mGmMessageView.getTradeList();
				break;
			}
			try {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(TiebaActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

}

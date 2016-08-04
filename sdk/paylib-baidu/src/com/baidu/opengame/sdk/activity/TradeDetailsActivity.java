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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.widget.TradeDetailView;

public class TradeDetailsActivity extends Activity {

	public static final int FLAG_INT = 3;

	private ViewPager mPager;
	private List<View> listViews;
	private ViewGroup cursor;
	private ImageView imageCursor;
	private TextView t1, t2, t3, t4;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;

	TradeDetailView mAllTradeView;

	TradeDetailView mSucessTradeView;

	TradeDetailView mFailTradeView;

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
	protected void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
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

		t1.setText(ResUtil.get_R_String(this, "tab1_header_title1"));
		t2.setText(ResUtil.get_R_String(this, "tab1_header_title2"));
		t3.setText(ResUtil.get_R_String(this, "tab1_header_title3"));
		// t4.setText(R.string.tab2_header_title4);
		findViewById(ResUtil.get_R_id(this, "text4frame")).setVisibility(
				View.GONE);
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
		setPageTitleColor(0);
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(ResUtil.get_R_id(this, "vPager"));
		listViews = new ArrayList<View>();

		mAllTradeView = new TradeDetailView(this, 0);
		mSucessTradeView = new TradeDetailView(this, 1);
		mFailTradeView = new TradeDetailView(this, -1);

		listViews.add(mAllTradeView);
		listViews.add(mSucessTradeView);
		listViews.add(mFailTradeView);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
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
		offset = (screenW / 3 - bmpW) / 2;
		imageCursor.setMinimumWidth(screenW / 3);
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
				mAllTradeView.getTradeList();
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				mSucessTradeView.getTradeList();
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				mFailTradeView.getTradeList();
				break;

			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				break;
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

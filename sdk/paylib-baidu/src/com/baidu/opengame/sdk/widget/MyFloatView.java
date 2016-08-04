package com.baidu.opengame.sdk.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.baidu.android.pay.util.NetworkUtil;
import com.baidu.opengame.sdk.activity.ForumActivity;
import com.baidu.opengame.sdk.activity.TiebaActivity;
import com.baidu.opengame.sdk.activity.TradeDetailsActivity;
import com.baidu.opengame.sdk.activity.UserCenterActivity;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.utils.ToastUtils;

public class MyFloatView extends LinearLayout {

	public static int mLastX = -1;

	public static int mLastY = -1;

	private static int WAIT_TIME = 4 * 1000;

	private static int MES_CHANGE_IMAGE = 111;

	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private Context mContext;

	private float moveX;
	private float moveY;

	private float upX;
	private float upY;

	private ImageView mImageView;
	private RelativeLayout mLeftFrame;
	private RelativeLayout mRightFrame;
	private LayoutInflater mLayoutInflater;
	private WindowManager.LayoutParams mLayoutParams;

	private int mScreenHeight;

	private int mScreenWidh;

	boolean mIsButtons;

	private static boolean mIsLeft = true;
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MES_CHANGE_IMAGE) {
				changeImage();
			}
		};
	};
	
	public void changeImage() {
		if (!mIsButtons) {
			if (mIsLeft) {
				mImageView.setImageResource(ResUtil.get_R_Drawable(mContext,
						"dk_suspension_left_window_normal"));
			} else {
				mImageView.setImageResource(ResUtil.get_R_Drawable(mContext,
						"dk_suspension_right_window_normal"));
			}
		}
		
	}


	LinearLayout.LayoutParams lp = new LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);

	View.OnClickListener mImageViewClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			changeMode();
		}
	};

	private void toOneBtn() {
		if (mIsButtons) {
			if (mIsLeft) {
				removeView(mLeftFrame);
			} else {
				removeView(mRightFrame);
			}
			addView(mImageView, lp);
			mIsButtons = false;
		}
	}

	private void changeMode() {

		if (mIsButtons) {
			if (mIsLeft) {
				removeView(mLeftFrame);
			} else {
				removeView(mRightFrame);
			}
			mImageView.setImageResource(ResUtil.get_R_Drawable(mContext,
					"dk_suspension_window_trans"));
			addView(mImageView, lp);
			mIsButtons = false;
			mHandler.removeMessages(MES_CHANGE_IMAGE);
			mHandler.sendEmptyMessageDelayed(MES_CHANGE_IMAGE, WAIT_TIME);
		} else {
			removeView(mImageView);
			if (mIsLeft) {
				addView(mLeftFrame, lp);
			} else {
				addView(mRightFrame, lp);
			}
			mIsButtons = true;
		}
	}

	View.OnClickListener mUserBtnClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mImageView.performClick();
			if (NetworkUtil.isNetworkAvailable(mContext)) {
				Intent intent = new Intent(mContext, UserCenterActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				Intent broadcaseIntent = new Intent(TiebaActivity.CLOSE_ACTION);
				broadcaseIntent.putExtra("type", UserCenterActivity.FLAG_INT);
				mContext.sendBroadcast(broadcaseIntent);
			} else {
				ToastUtils.showShortToast(mContext,
						ResUtil.get_R_String(mContext, "pay_net_error"));
			}

		}
	};

	View.OnClickListener mTiebaClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mImageView.performClick();
			if (NetworkUtil.isNetworkAvailable(mContext)) {
				Intent intent = new Intent(mContext, TiebaActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("type", TiebaActivity.FLAG_INT);
				mContext.startActivity(intent);
				Intent broadcaseIntent = new Intent(TiebaActivity.CLOSE_ACTION);
				broadcaseIntent.putExtra("type", TiebaActivity.FLAG_INT);
				mContext.sendBroadcast(broadcaseIntent);
			} else {
				ToastUtils.showShortToast(mContext,
						ResUtil.get_R_String(mContext, "pay_net_error"));
			}

		}
	};

	View.OnClickListener mMsgClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mImageView.performClick();
			if (NetworkUtil.isNetworkAvailable(mContext)) {
				Intent intent = new Intent(mContext, TiebaActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("type", TiebaActivity.FLAG_INT2);
				mContext.startActivity(intent);
				Intent broadcaseIntent = new Intent(TiebaActivity.CLOSE_ACTION);
				broadcaseIntent.putExtra("type", TiebaActivity.FLAG_INT2);
				mContext.sendBroadcast(broadcaseIntent);
			} else {
				ToastUtils.showShortToast(mContext,
						ResUtil.get_R_String(mContext, "pay_net_error"));
			}

		}
	};

	View.OnClickListener mZqClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mImageView.performClick();
			if (NetworkUtil.isNetworkAvailable(mContext)) {
				Intent intent = new Intent(mContext, ForumActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("type", ForumActivity.FLAG_INT);
				mContext.startActivity(intent);
				Intent broadcaseIntent = new Intent(TiebaActivity.CLOSE_ACTION);
				broadcaseIntent.putExtra("type", ForumActivity.FLAG_INT);
				mContext.sendBroadcast(broadcaseIntent);
			} else {
				ToastUtils.showShortToast(mContext,
						ResUtil.get_R_String(mContext, "pay_net_error"));
			}

		}
	};

	View.OnClickListener mTradeClicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mImageView.performClick();
			if (NetworkUtil.isNetworkAvailable(mContext)) {
				Intent intent = new Intent(mContext, TradeDetailsActivity.class);
				intent.putExtra("type", 0);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				Intent broadcaseIntent = new Intent(TiebaActivity.CLOSE_ACTION);
				broadcaseIntent.putExtra("type", TradeDetailsActivity.FLAG_INT);
				mContext.sendBroadcast(broadcaseIntent);
			} else {
				ToastUtils.showShortToast(mContext,
						ResUtil.get_R_String(mContext, "pay_net_error"));
			}

		}
	};

	private void updateViewOnDown() {
		try {
			wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;// 不允许获取焦点
			wm.updateViewLayout(this, wmParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateViewOnTouchOutside() {
		try {
			wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 不允许获取焦点
			wm.updateViewLayout(MyFloatView.this, wmParams);
			toOneBtn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isMove;

	View.OnTouchListener mTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {

			if (mIsButtons) {
				return false;
			}

			x = event.getRawX();
			y = event.getRawY() - 25;
			Log.i("currP", "currX" + x + "====currY" + y);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTouchStartX = event.getX();
				mTouchStartY = event.getY();

				Log.i("startP", "startX" + mTouchStartX + "====startY"
						+ mTouchStartY);
				
				updateViewOnDown();
				mHandler.removeMessages(MES_CHANGE_IMAGE);
				mImageView.setImageResource(ResUtil.get_R_Drawable(mContext,
						"dk_suspension_window_trans"));
				break;
			case MotionEvent.ACTION_MOVE:
				moveX = event.getX();
				moveY = event.getY();
				if (Math.abs(moveX - mTouchStartX) > 5.0F
						|| Math.abs(moveY - mTouchStartY) > 5.0F) {
					mHandler.removeMessages(MES_CHANGE_IMAGE);
					updateViewPosition();
					isMove = true;
				}
				break;

			case MotionEvent.ACTION_UP:
				upX = event.getX();
				upY = event.getY();

				if (x > mScreenWidh / 2) {
					mIsLeft = false;
				} else {
					mIsLeft = true;
				}
				updateViewPositionAfterUp();
				mTouchStartX = mTouchStartY = 0;
				if (isMove) {
					isMove = false;
					return true;
				}

				break;
			}
			return false;

		}
	};

	private WindowManager wm = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	private WindowManager.LayoutParams wmParams;

	public MyFloatView(Context context, WindowManager.LayoutParams params) {
		super(context);
		mContext = context;
		wmParams = params;
		mLayoutInflater = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		mScreenHeight = metrics.heightPixels;
		mScreenWidh = metrics.widthPixels;
		initView();
	}

	private void initView() {

		mImageView = new ImageView(mContext);
		mImageView.setImageResource(ResUtil.get_R_Drawable(mContext,
				"dk_suspension_window_trans"));

		mLeftFrame = (RelativeLayout) (mLayoutInflater
				.inflate(ResUtil.get_R_Layout(mContext,
						"dk_suspension_window_left_view"), null));
		mRightFrame = (RelativeLayout) (mLayoutInflater.inflate(ResUtil
				.get_R_Layout(mContext, "dk_suspension_window_right_view"),
				null));

		addView(mImageView, lp);

		mLeftFrame.findViewById(
				ResUtil.get_R_id(mContext, "dk_iv_user_account"))
				.setOnClickListener(mUserBtnClicker);

		mLeftFrame.findViewById(ResUtil.get_R_id(mContext, "dk_iv_prefecture"))
				.setOnClickListener(mTiebaClicker);

		mLeftFrame.findViewById(
				ResUtil.get_R_id(mContext, "dk_iv_trade_account"))
				.setOnClickListener(mTradeClicker);

		mLeftFrame.findViewById(ResUtil.get_R_id(mContext, "dk_iv_message"))
				.setOnClickListener(mMsgClicker);

		mLeftFrame.findViewById(ResUtil.get_R_id(mContext, "dk_iv_zhuanqu"))
				.setOnClickListener(mZqClicker);

		mRightFrame.findViewById(
				ResUtil.get_R_id(mContext, "dk_iv_user_account"))
				.setOnClickListener(mUserBtnClicker);

		mRightFrame
				.findViewById(ResUtil.get_R_id(mContext, "dk_iv_prefecture"))
				.setOnClickListener(mTiebaClicker);

		mRightFrame.findViewById(
				ResUtil.get_R_id(mContext, "dk_iv_trade_account"))
				.setOnClickListener(mTradeClicker);

		mRightFrame.findViewById(ResUtil.get_R_id(mContext, "dk_iv_message"))
				.setOnClickListener(mMsgClicker);

		mRightFrame.findViewById(ResUtil.get_R_id(mContext, "dk_iv_zhuanqu"))
				.setOnClickListener(mZqClicker);

		mImageView.setOnClickListener(mImageViewClicker);
		mImageView.setOnTouchListener(mTouchListener);
		mLeftFrame.setOnClickListener(mImageViewClicker);
		mLeftFrame.setOnTouchListener(mTouchListener);
		mRightFrame.setOnClickListener(mImageViewClicker);
		mRightFrame.setOnTouchListener(mTouchListener);

		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					updateViewOnTouchOutside();
				}
				return false;
			}
		});
	}

	// private int dip2px(Context context, float dipValue) {
	// final float scale = context.getResources().getDisplayMetrics().density;
	// return (int) (dipValue * scale + 0.5f);
	// }

	private void updateViewPositionAfterUp() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(metrics);
			mScreenHeight = metrics.heightPixels;
			mScreenWidh = metrics.widthPixels;
			if (mIsLeft) {
				mLastX = 0;
				wmParams.x = (int) (0 - mTouchStartX);
			} else {
				wmParams.x = (int) (mScreenWidh);
				mLastX = mScreenWidh;
			}

			mLastY = wmParams.y;
			// wmParams.y = (int) (y - mTouchStartY);
			wm.updateViewLayout(this, wmParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHandler.removeMessages(MES_CHANGE_IMAGE);
		mHandler.sendEmptyMessageDelayed(MES_CHANGE_IMAGE, WAIT_TIME);
	}

	private void updateViewPosition() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(metrics);
			mScreenHeight = metrics.heightPixels;
			mScreenWidh = metrics.widthPixels;
			wmParams.x = (int) (x - mTouchStartX);
			wmParams.y = (int) (y - mTouchStartY);
			wm.updateViewLayout(this, wmParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

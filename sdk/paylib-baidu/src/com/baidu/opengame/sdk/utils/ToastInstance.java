package com.baidu.opengame.sdk.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToastInstance {
	private static int Y_OFFSET = 0;
	private static ToastInstance instance = null;
	private Context mContext;

	private ToastInstance(Context context) {
		mContext = context;
	}

	public static ToastInstance getInstance(Context context) {
		if (instance == null) {
			instance = new ToastInstance(context);
		}

		return instance;
	}

	private Toast mToast;

	public void showShortToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
			View view = mToast.getView(); 
			view.setBackgroundColor(Color.parseColor("#f0a86d"));
			mToast.setView(view); 
			int xOffset = mToast.getXOffset();
			int yOffset = mToast.getYOffset() + Y_OFFSET;
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER,
					xOffset, yOffset);
		} else {
//			mToast.cancel();
			mToast.setText(text);
		}
		if (mLongToast != null)
			mLongToast.cancel();

		mToast.show();
	}

	public void showShortToast(Context context, int resid) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, mContext.getResources()
					.getString(resid), Toast.LENGTH_SHORT);
			int xOffset = mToast.getXOffset();
			int yOffset = mToast.getYOffset() + Y_OFFSET;
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		} else {
//			mToast.cancel();
			mToast.setText(resid);
		}
		if (mLongToast != null)
			mLongToast.cancel();
		mToast.show();
	}

	private Toast mLongToast;

	public void showLongToast(Context context, String text) {
		if (mLongToast == null) {
			mLongToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
			int xOffset = mLongToast.getXOffset();
			int yOffset = mLongToast.getYOffset() + Y_OFFSET;
			mLongToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		} else {
//			mLongToast.cancel();
			mLongToast.setText(text);
		}
		if (mToast != null)
			mToast.cancel();
		mLongToast.show();
	}

	public void showLongToast(Context context, int resid) {
		if (mLongToast == null) {
			mLongToast = Toast.makeText(mContext, mContext.getResources()
					.getString(resid), Toast.LENGTH_LONG);
			int xOffset = mLongToast.getXOffset();
			int yOffset = mLongToast.getYOffset() + Y_OFFSET;
			mLongToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		} else {
//			mLongToast.cancel();
			mLongToast.setText(resid);
		}
		if (mToast != null)
			mToast.cancel();
		mLongToast.show();
	}

}

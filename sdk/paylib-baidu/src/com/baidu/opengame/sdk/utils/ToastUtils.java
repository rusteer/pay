package com.baidu.opengame.sdk.utils;

import android.content.Context;

public class ToastUtils {

	public static void showShortToast(Context context, String text) {
		ToastInstance mToast = ToastInstance.getInstance(context);
		mToast.showShortToast(context, text);
		// Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showShortToast(Context context, int strId) {
		ToastInstance.getInstance(context).showShortToast(context, strId);
		// Toast.makeText(context, context.getResources().getString(strId),
		// Toast.LENGTH_SHORT).show();
	}

	public static void showShortToast(Context context, int resid, String str) {
		String message = context.getString(resid, str);
		ToastInstance.getInstance(context).showShortToast(context, message);
		// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, String text) {
		ToastInstance.getInstance(context).showLongToast(context, text);
		// Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void showLongToast(Context context, int strId) {
		ToastInstance.getInstance(context).showLongToast(context, strId);
		// Toast.makeText(context, context.getResources().getString(strId),
		// Toast.LENGTH_LONG).show();
	}

	public static void showLongToast(Context context, int resid, String str) {
		String message = context.getString(resid, str);
		ToastInstance.getInstance(context).showLongToast(context, message);
		// Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}

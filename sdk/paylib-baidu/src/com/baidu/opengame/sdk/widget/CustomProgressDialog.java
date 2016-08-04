/**************************************************************************************
 * [Project]
 *       MyProgressDialog
 * [Package]
 *       com.lxd.widgets
 * [FileName]
 *       CustomProgressDialog.java
 * [Copyright]
 *       Copyright 2012 LXD All Rights Reserved.
 * [History]
 *       Version          Date              Author                        Record
 *--------------------------------------------------------------------------------------
 *       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 **************************************************************************************/

package com.baidu.opengame.sdk.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.opengame.sdk.utils.ResUtil;

/********************************************************************
 * [Summary] TODO
 * ���ڴ˴���Ҫ����������ʵ�ֵĹ��ܡ���Ϊ����ע����Ҫ��Ϊ����IDE���������tip������ؼ�����Ҫ
 * [Remarks] TODO ���ڴ˴���ϸ������Ĺ��ܡ����÷�����ע������Լ���������Ĺ�ϵ.
 *******************************************************************/

public class CustomProgressDialog extends Dialog {
	private static CustomProgressDialog customProgressDialog = null;

	private ImageView mLoading = null;
	private AnimationDrawable mLoadingDrawable = null;

	private Context mActivity;

	public CustomProgressDialog(Context context) {
		super(context);
		mActivity = context;
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
		mActivity = context;
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context,
				ResUtil.get_R_Style(context, "CustomProgressDialog"));
		customProgressDialog.setContentView(ResUtil.get_R_Layout(context,
				"customprogressdialog"));
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoading = (ImageView) findViewById(ResUtil.get_R_id(mActivity,
				"title_load_pb"));
		mLoadingDrawable = (AnimationDrawable) mLoading.getBackground();
		mLoadingDrawable.start();
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		// ImageView imageView = (ImageView)
		// customProgressDialog.findViewById(ResUtil.get_R_id(this,
		// "loadingImageView);
		// AnimationDrawable animationDrawable = (AnimationDrawable)
		// imageView.getBackground();
		// animationDrawable.start();
	}

	/**
	 * 
	 * [Summary] setTitile ����
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public CustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage ��ʾ����
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public CustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog.findViewById(ResUtil
				.get_R_id(mActivity, "id_tv_loadingmsg"));

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}
}

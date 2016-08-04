package com.baidu.opengame.sdk;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.baidu.opengame.sdk.service.RunningStateService;
import com.baidu.opengame.sdk.utils.FileConfig;
import com.baidu.opengame.sdk.widget.MyFloatView;
import com.baifubao.mpay.ifmgr.ILoginCallback;
import com.baifubao.mpay.ifmgr.ILoginCallbackEx;
import com.baifubao.mpay.ifmgr.IPayResultCallback;
import com.baifubao.mpay.ifmgr.LoginSuccessCallback;
import com.baifubao.mpay.ifmgr.SDKApi;
import com.baifubao.mpay.ifmgr.UserCenterManager;
import com.baifubao.plat.MyApplication;

public class BDGameSDK {

	public static final int LANDSCAPE = SDKApi.LANDSCAPE;
	public static final int PORTRAIT = SDKApi.PORTRAIT;
	public static final int PAY_SUCCESS = SDKApi.PAY_SUCCESS;
	public static final int PAY_FAIL = SDKApi.PAY_FAIL;
	public static final int PAY_CANCEL = SDKApi.PAY_CANCEL;
	public static final int PAY_HANDLING = SDKApi.PAY_HANDLING;
	public static final int ENTRY_USERCENTER = SDKApi.ENTRY_USERCENTER;
	public static final int ENTRY_PAYHUB = SDKApi.ENTRY_PAYHUB;

	private static BDGameSDK instance = null;

	private static Object INSTANCE_LOCK = new Object();

	WindowManager wm = null;
	WindowManager.LayoutParams wmParams = null;
	MyFloatView myFV = null;

	private String mUserId;
	private String mUserName;
	private String mAppId;
	private String mAppKey;

	public String getAppKey() {
		return mAppKey;
	}

	public String getToken() {
		return mToken;
	}

	private String mToken;

	private String mAppPackageName;

	private boolean mIsExit;

	public String getUserId() {
		return mUserId;
	}

	public String getUserName() {
		return mUserName;
	}

	public String getAppId() {
		return mAppId;
	}

	private Context mAppcontext;

	public String getAppPackageName() {
		try {
			if (mAppcontext != null) {
				mAppPackageName = mAppcontext.getPackageName();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mAppPackageName;
	}

	private UserCenterManager.OnLogoutLinstener mOnLogoutLinstener;

	public void setOnLogoutLinstener(
			UserCenterManager.OnLogoutLinstener linstener) {
		mOnLogoutLinstener = linstener;
	}

	public void appExit(Activity activity, String appID) {
		mIsExit = true;
		try {
			mAppcontext.stopService(new Intent(mAppcontext,
					RunningStateService.class));
			SDKApi.appExit(activity, appID);
			MyFloatView.mLastX = -1;
			MyFloatView.mLastY = -1;
			hideFloatView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 私有化构造方法
	 */
	private BDGameSDK() {

	}

	/**
	 * 实现单例方法
	 * 
	 * @return
	 */
	public static BDGameSDK getInstance() {
		if (instance == null) {
			synchronized (INSTANCE_LOCK) {
				if (instance == null) {
					instance = new BDGameSDK();
				}
			}
		}
		return instance;
	}

	
	ArrayList<String> mMsgReadedList = new ArrayList<String>();

	public ArrayList<String> getMsgReadedList() {
		return mMsgReadedList;
	}

	private void initReadedNewsIds() {
		String readedListString = FileConfig.Instance(mAppcontext).getValue(
				"bdgame_mes_list", "list", "");
		if (!TextUtils.isEmpty(readedListString)) {
			String[] arrays = readedListString.split("&");
			int lenth = arrays.length;
			for (int i = 0; i < lenth; i++) {
				mMsgReadedList.add(arrays[i]);
			}
		}

	}

	public void init(Activity activity, int sdkType, String waresID,
			String waresKey) {
		mIsExit = false;
		mAppId = waresID;
		mAppKey = waresKey;
		mAppcontext = activity.getApplicationContext();
		SharedPreferences preferences = mAppcontext.getSharedPreferences(
				"mycfg", mAppcontext.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("mykey", sdkType);
		editor.commit();
		SDKApi.init(activity, sdkType, waresID);
		SDKApi.setLoginSuccessCallBack(new LoginSuccessCallback() {

			@Override
			public void onCallBack(int arg0) {
				mAppcontext.startService(new Intent(mAppcontext,
						RunningStateService.class));

			}
		});
		initReadedNewsIds();
	}

	public void startPay(Activity activity, String paramUrl,
			IPayResultCallback iAppCallbackListener) {
		SDKApi.startPay(activity, paramUrl, iAppCallbackListener);
	}

	public void loginUIEx(Activity activity, String appid, String time,
			String sign, final ILoginCallbackEx callback, boolean isThird,
			boolean isDuoku) {
		ILoginCallbackEx localCallback = new ILoginCallbackEx() {

			@Override
			public void onCallBack(int retcode, String token, String uid,
					String uname, String sign) {

				if (retcode == ILoginCallback.RETCODE_SUCCESS) {
					mAppcontext.startService(new Intent(mAppcontext,
							RunningStateService.class));
					mUserId = uid + "";
					mUserName = uname;
					mToken = MyApplication.getInstance().mAccessToken;
					// showFloatView();
				} else {
					;
				}
				callback.onCallBack(retcode, token, uid, uname, sign);

			}

		};
		SDKApi.loginUIEx(activity, appid, time, sign, localCallback, isThird,
				isDuoku);
	}

	public void userCenter(Activity activity,
			final UserCenterManager.OnLogoutLinstener ls) {
		UserCenterManager.OnLogoutLinstener localLs = new UserCenterManager.OnLogoutLinstener() {

			@Override
			public void logoutSuc() {
				mAppcontext.stopService(new Intent(mAppcontext,
						RunningStateService.class));
				hideFloatView();
				ls.logoutSuc();
				try {
					mMsgReadedList.clear();
					FileConfig.Instance(mAppcontext).setValue(
							"bdgame_mes_list", "list", "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mOnLogoutLinstener != null) {
					mOnLogoutLinstener.logoutSuc();
				}
			}

			@Override
			public void logoutCancle() {
				// TODO Auto-generated method stub
				ls.logoutCancle();
				if (mOnLogoutLinstener != null) {
					mOnLogoutLinstener.logoutCancle();
				}
			}

			@Override
			public void noLoginState() {
				// TODO Auto-generated method stub
				mAppcontext.stopService(new Intent(mAppcontext,
						RunningStateService.class));
				hideFloatView();
				ls.noLoginState();
				if (mOnLogoutLinstener != null) {
					mOnLogoutLinstener.noLoginState();
				}
			}

			@Override
			public void logoutFail() {
				ls.logoutFail();
				if (mOnLogoutLinstener != null) {
					mOnLogoutLinstener.logoutFail();
				}
			}
		};
		SDKApi.userCenter(activity, localLs);
	}

	public void hideFloatView() {
		try {
			if (myFV != null) {
				wm.removeView(myFV);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showFloatView() {

		if (mAppcontext == null || mIsExit)
			return;

		// 获取WindowManager
		wm = (WindowManager) mAppcontext.getSystemService("window");
		// 设置LayoutParams(全局变量）相关参数
		wmParams = new WindowManager.LayoutParams();
		try {
			if (myFV != null) {
				wm.removeView(myFV);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myFV = new MyFloatView(mAppcontext, wmParams);
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		int mScreenHeight = metrics.heightPixels;

		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		if (MyFloatView.mLastX > -1) {
			wmParams.x = MyFloatView.mLastX;
			wmParams.y = MyFloatView.mLastY;
			myFV.changeImage();
		} else {
			wmParams.x = 0;
			wmParams.y = mScreenHeight / 2;
		}

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		;

		// 显示myFloatView图像
		wm.addView(myFV, wmParams);
	}

}
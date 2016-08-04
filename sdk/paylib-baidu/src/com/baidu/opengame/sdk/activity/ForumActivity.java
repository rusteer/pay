package com.baidu.opengame.sdk.activity;

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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.opengame.sdk.BDConst;
import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.utils.MD5;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baifubao.plat.MyApplication;

public class ForumActivity extends Activity {

	public static final int FLAG_INT = 6;

	private final String REDIRECTURL = BDConst.TRADE_DK_FORUM_URL;

	WebView mWebView;

	private View mLoadingLayout;

	TextView mLoadingLayoutTv;

	private int mPageNum;

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
		setContentView(ResUtil.get_R_Layout(this, "bd_activity_forumactivity"));
		initBroadCaseRev();
		initView();
		findViewById(ResUtil.get_R_id(this, "title_back")).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
	}

	private void initView() {
		mWebView = (WebView) findViewById(ResUtil.get_R_id(this, "pay_h5_webview"));
		mLoadingLayout = findViewById(ResUtil.get_R_id(this, "loading_layout"));
		mLoadingLayoutTv = (TextView) findViewById(ResUtil.get_R_id(this, "loading_layout_tv"));
		WebSettings setting = mWebView.getSettings();
		setting.setSupportZoom(false);
		setting.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		// setting.setBuiltInZoomControls(false);
		setting.setSaveFormData(false);
		setting.setJavaScriptEnabled(true);
		// 提高渲染优先级
		setting.setRenderPriority(RenderPriority.HIGH);
		setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		setting.setSavePassword(false);
		// InJavaScriptLocalObj obj = new InJavaScriptLocalObj(new MyHandler(
		// mActivity));
		// obj.showSource(null);
		// mWebView.addJavascriptInterface(obj, "local_obj");
		mWebView.setWebViewClient(webClient);
		mWebView.setWebChromeClient(new WebChromeClient());
		String url = REDIRECTURL
				+ "&user_id="
				+ BDGameSDK.getInstance().getUserId()
				+ "&appkey="
				+ BDGameSDK.getInstance().getAppKey()
				+ "&appid="
				+ BDGameSDK.getInstance().getAppId()
				+ "&token="
				+  MyApplication.getInstance().mAccessToken
				+ "&key="
				+ MD5.crypt("2013baidusdk@conf!()"
						+ BDGameSDK.getInstance().getUserId());
		;

		mWebView.loadUrl(url);
	}

	private WebViewClient webClient = new WebViewClient() {

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// webView.setVisibility(View.INVISIBLE);
			// loginFail(description);
		};

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageStarted(WebView view, String url,
				android.graphics.Bitmap favicon) {
			if (mPageNum == 0) {
				mLoadingLayout.setVisibility(View.VISIBLE);
				mWebView.setVisibility(View.GONE);

			}

		};

		public void onPageFinished(WebView view, String url) {
			if (mPageNum == 0) {
				mWebView.setVisibility(View.VISIBLE);
				mLoadingLayout.setVisibility(View.GONE);
				mWebView.requestFocus();
			}
			mPageNum++;
		};

	};

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

	@Override
	public void onBackPressed() {
		finish();
	}

}

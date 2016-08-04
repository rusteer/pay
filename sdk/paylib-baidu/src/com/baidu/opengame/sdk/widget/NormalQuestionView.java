package com.baidu.opengame.sdk.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.opengame.sdk.BDConst;
import com.baidu.opengame.sdk.utils.ResUtil;

public class NormalQuestionView extends LinearLayout {

	Activity mActivity;

	private final String REDIRECTURL = BDConst.HELP_URL;

	WebView mWebView;

	private View mLoadingLayout;

	TextView mLoadingLayoutTv;

	private int mPageNum;

	public NormalQuestionView(Activity activity) {

		super(activity);

		mActivity = activity;

		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		setPadding(0, 0, 0, 0);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);

		View view = layoutInflater.inflate(
				ResUtil.get_R_Layout(mActivity, "bd_normal_question_view"),
				this);

		mWebView = (WebView) findViewById(ResUtil.get_R_id(activity,
				"pay_h5_webview"));
		mLoadingLayout = findViewById(ResUtil.get_R_id(activity,
				"loading_layout"));
		mLoadingLayoutTv = (TextView) findViewById(ResUtil.get_R_id(activity,
				"loading_layout_tv"));
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
		String url = REDIRECTURL;
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

}

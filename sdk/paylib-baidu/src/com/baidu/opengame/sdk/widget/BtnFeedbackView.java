package com.baidu.opengame.sdk.widget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.opengame.sdk.BDConst;
import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.framework.AsyncHttpClient;
import com.baidu.opengame.sdk.framework.AsyncHttpResponseHandler;
import com.baidu.opengame.sdk.utils.MD5;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.utils.ToastUtils;

public class BtnFeedbackView extends LinearLayout implements OnClickListener {

	Activity mActivity;

	private EditText contactInputText;
	private EditText issuesInputText;
	private TextView mQqText;
	private TextView mPhoneText;
	private TextView mTimeText;
	private Button submitBtn;

	private String mServiceQQ;
	private String mServicePhone;
	private String mServiceTime;

	protected CustomProgressDialog progressingDialog;

	public BtnFeedbackView(Activity activity) {

		super(activity);

		mActivity = activity;
		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		setPadding(0, 0, 0, 0);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);

		View view = layoutInflater.inflate(
				ResUtil.get_R_Layout(mActivity, "layout_pay_feedback_view"),
				this);

		mQqText = (TextView) findViewById(ResUtil.get_R_id(mActivity,
				"baidu_feed_back_qq"));
		mPhoneText = (TextView) findViewById(ResUtil.get_R_id(mActivity,
				"baidu_feed_back_mobile"));
		mTimeText = (TextView) findViewById(ResUtil.get_R_id(mActivity,
				"baidu_feed_back_service_time"));

		contactInputText = (EditText) findViewById(ResUtil.get_R_id(mActivity,
				"baidu_feed_back_contact_input"));
		issuesInputText = (EditText) findViewById(ResUtil.get_R_id(mActivity,
				"baidu_feed_back_issues_input"));
		submitBtn = (Button) findViewById(ResUtil.get_R_id(mActivity,
				"baidu_feed_back_submitBtn"));
		submitBtn.setOnClickListener(this);

		mPhoneText.setOnClickListener(this);

		submitBtn.setEnabled(false);

		contactInputText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

				String content = issuesInputText.getText().toString();
				content = content.replace(" ", "");
				if (TextUtils.isEmpty(arg0.toString())
						|| TextUtils.isEmpty(content)) {
					submitBtn.setEnabled(false);
				} else {
					submitBtn.setEnabled(true);
				}

			}
		});

		issuesInputText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				String content = arg0.toString();
				content = content.replace(" ", "");
				if (TextUtils.isEmpty(content)
						|| TextUtils.isEmpty(contactInputText.getText()
								.toString())) {
					submitBtn.setEnabled(false);
				} else {
					submitBtn.setEnabled(true);
				}
			}
		});

		progressingDialog = CustomProgressDialog.createDialog(mActivity);
		WindowManager windowManager1 = mActivity.getWindowManager();
		Display display1 = windowManager1.getDefaultDisplay();
		WindowManager.LayoutParams lp1 = progressingDialog.getWindow()
				.getAttributes();
		lp1.width = (int) (display1.getWidth()) - 80; // 设置宽度
		progressingDialog.getWindow().setAttributes(lp1);

		getSericeWay();
	}

	public void showDialog(String message) {
		progressingDialog.show();
		progressingDialog.setMessage(message);
	}

	public void dismisDialog() {
		progressingDialog.dismiss();
	}

	private void getSericeWay() {
		String url = BDConst.SERVICE_GET_URL + "?&user_id="
				+ BDGameSDK.getInstance().getUserId() + "&app_id="
				+ BDGameSDK.getInstance().getAppId();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				// System.out.println(response);
				try {
					JSONObject object = new JSONObject(response);
					int flag = object.optInt("flag");
					if (flag == 1) {
						JSONArray jsonArray = object.optJSONArray("list");
						if (jsonArray != null && jsonArray.length() > 0) {
							JSONObject dataObject = jsonArray.getJSONObject(0);
							mServiceQQ = dataObject.optString("qq_num");
							mServicePhone = dataObject
									.optString("service_info");
							mServiceTime = dataObject.optString("work_time");
							showServiceInfo();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		});

	}

	private void highlightHotTitle(Spannable paramSpannable, String filter) {
		try {
			Pattern hotPattern = Pattern.compile(filter,
					Pattern.CASE_INSENSITIVE);
			Matcher hotMatcher = hotPattern.matcher(paramSpannable);
			ForegroundColorSpan localForegroundColorSpan3 = new ForegroundColorSpan(
					Color.rgb(108, 148, 216));
			while (hotMatcher.find()) {
				int st = hotMatcher.start();
				int end = hotMatcher.end();
				paramSpannable.setSpan(localForegroundColorSpan3, st, end,
						Spanned.SPAN_POINT_MARK);
			}
		} catch (PatternSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void showServiceInfo() {
		if (!TextUtils.isEmpty(mServiceQQ)) {
			mQqText.setText(mActivity.getResources().getString(
					ResUtil.get_R_String(mActivity, "service_qq"), mServiceQQ));
			mQqText.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(mServicePhone)) {
			SpannableString trackTitle = new SpannableString(mActivity
					.getResources().getString(
							ResUtil.get_R_String(mActivity, "service_mail"),
							mServicePhone));
			highlightHotTitle(trackTitle, mServicePhone);
			mPhoneText.setText(trackTitle);
			mPhoneText.setVisibility(View.VISIBLE);
		}

		if (!TextUtils.isEmpty(mServiceTime)) {
			mTimeText.setText(mActivity.getResources().getString(
					ResUtil.get_R_String(mActivity, "service_time"),
					mServiceTime));
			mTimeText.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtil.get_R_id(mActivity, "title_back")) {
			mActivity.onBackPressed();
		} else if (v.getId() == ResUtil.get_R_id(mActivity,
				"baidu_feed_back_submitBtn")) {
			sendFeedback();
		} else if (v.getId() == ResUtil.get_R_id(mActivity,
				"baidu_feed_back_mobile")) {
			callPhone();
		}

	}

	private void callPhone() {
		try {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ mServicePhone));
			mActivity.startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearStatus() {
		issuesInputText.setText("");
		contactInputText.setText("");
	}

	private void sendFeedback() {
		String message = issuesInputText.getText().toString();
		String contact_way = contactInputText.getText().toString();
		if (TextUtils.isEmpty(message)) {
			ToastUtils.showShortToast(mActivity,
					ResUtil.get_R_String(mActivity, "feed_back_input_des"));
			return;
		}

		if (TextUtils.isEmpty(contact_way)) {
			ToastUtils.showShortToast(mActivity,
					ResUtil.get_R_String(mActivity, "feed_back_input_contact"));
			return;
		}

		try {
			message = URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = BDConst.HOST_URL
				+ "?r=BaiduSdkAction"
				+ "&m=feedback"
				+ "&user_id="
				+ BDGameSDK.getInstance().getUserId()
				+ "&username="
				+ BDGameSDK.getInstance().getUserName()
				+ "&app_id="
				+ BDGameSDK.getInstance().getAppId()
				+ "&page_num=1"
				+ "&page_size=1000"
				// + "&access_token="
				// + BDGameSDK.getInstance().getLoginInfo(mActivity)
				// .getAccessToken()
				+ "&key="
				+ MD5.crypt("2013baidusdk@conf!()"
						+ BDGameSDK.getInstance().getUserId())
				+ "&contact_way=" + contact_way + "&message=" + message;
		// + "&cp_id="
		// + BDGameSDK.getInstance().getBDGameSDKConfig().getCpId();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				showDialog(mActivity.getString(ResUtil.get_R_String(mActivity,
						"feed_back_submiting")));
				super.onStart();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				ToastUtils.showShortToast(mActivity,
						ResUtil.get_R_String(mActivity, "pay_net_error"));
			}

			@Override
			public void onFinish() {
				dismisDialog();
				super.onFinish();
			}

			@Override
			public void onSuccess(String response) {
				// System.out.println(response);
				try {
					JSONObject object = new JSONObject(response);
					int flag = object.optInt("flag");
					if (flag == 1) {
						ToastUtils.showLongToast(mActivity, ResUtil
								.get_R_String(mActivity, "feed_back_finished"));
						mActivity.onBackPressed();
					} else {
						String msg = object.optString("msg");
						ToastUtils.showShortToast(mActivity, msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		});

	}
}

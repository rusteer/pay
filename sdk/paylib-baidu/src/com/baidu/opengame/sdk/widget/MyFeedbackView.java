package com.baidu.opengame.sdk.widget;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.opengame.sdk.BDConst;
import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.activity.FeedbackMessageDetailsActivity;
import com.baidu.opengame.sdk.framework.AsyncHttpClient;
import com.baidu.opengame.sdk.framework.AsyncHttpResponseHandler;
import com.baidu.opengame.sdk.utils.MD5;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.utils.ToastUtils;

public class MyFeedbackView extends LinearLayout {

	Activity mActivity;

	ListView mListView;

	ViewGroup mLoadingFrame;

	public MyFeedbackView(Activity activity) {

		super(activity);

		mActivity = activity;

		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		setPadding(0, 0, 0, 0);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);

		View view = layoutInflater.inflate(
				ResUtil.get_R_Layout(mActivity, "bd_myfeedback_view"), this);

		mListView = (ListView) findViewById(ResUtil.get_R_id(mActivity,
				"orderListview"));

		mLoadingFrame = (ViewGroup) findViewById(ResUtil.get_R_id(mActivity,
				"loading_frame"));

		getTradeList();

	}

	public void getTradeList() {

		String url = BDConst.TRADE_MYFEEDBACK_GET_URL + "&user_id=" +  BDGameSDK.getInstance().getUserId()
				+ "&app_id=" + BDGameSDK.getInstance().getAppId() + "&page_num=1" + "&page_size=1000"
				+ "&key=" + MD5.crypt("2013baidusdk@conf!()" +  BDGameSDK.getInstance().getUserId());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				mListView.setEmptyView(null);
				mListView.setAdapter(null);
				mListView.setVisibility(View.GONE);
				findViewById(ResUtil.get_R_id(mActivity,
						"empty")).setVisibility(View.GONE);
				mLoadingFrame.setVisibility(View.VISIBLE);
				ImageView mLoading = (ImageView) findViewById(ResUtil.get_R_id(mActivity,
						"title_load_pb"));
				AnimationDrawable mLoadingDrawable = (AnimationDrawable) mLoading.getBackground();
				mLoadingDrawable.start();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				ToastUtils.showShortToast(mActivity,
						ResUtil.get_R_String(mActivity, "pay_net_error"));
				mListView.setAdapter(null);
				mListView.setVisibility(View.VISIBLE);
				mLoadingFrame.setVisibility(View.GONE);
				mListView.setEmptyView(findViewById(ResUtil.get_R_id(mActivity,
						"empty")));
			}

			@Override
			public void onSuccess(String response) {
				// System.out.println(response);
				try {
					JSONObject object = new JSONObject(response);
					int flag = object.optInt("flag");
					if (flag == 1) {
						JSONArray jsonArray = object.optJSONArray("data");
						if (jsonArray != null && jsonArray.length() > 0) {
							List<FeedbackRecord> dataList = new ArrayList<FeedbackRecord>();
							int lenth = jsonArray.length();
							for (int i = 0; i < lenth; i++) {
								FeedbackRecord record = new FeedbackRecord();
								JSONObject dataObject = jsonArray
										.getJSONObject(i);
								record.setApp_id(dataObject.optString("app_id"));
								record.setUser_id(dataObject
										.optString("user_id"));
								record.setCreate_time(dataObject
										.optLong("create_time"));
								record.setContact_way(dataObject
										.optString("contact_way"));
								record.setMessage(dataObject
										.optString("message"));
								String reply = dataObject
										.optString("reply_message");
								if ("null".equalsIgnoreCase(reply)) {
									reply = mActivity.getString(ResUtil
											.get_R_String(mActivity,
													"reply_null"));
								}
								record.setReply_message(reply);
								record.setReply_time(dataObject
										.optLong("reply_time"));
								dataList.add(record);
							}
							showRecords(dataList);
						} else {
							mListView.setAdapter(null);
							mListView.setVisibility(View.VISIBLE);
							mLoadingFrame.setVisibility(View.GONE);
							mListView.setEmptyView(findViewById(ResUtil
									.get_R_id(mActivity, "empty")));
						}
					} else {
						String msg = object.optString("msg");
						if (!"empty".equalsIgnoreCase(msg)) {
							ToastUtils.showShortToast(mActivity, msg);
						}
						mLoadingFrame.setVisibility(View.GONE);
						mListView.setEmptyView(findViewById(ResUtil.get_R_id(
								mActivity, "empty")));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		});

	}

	protected void showRecords(final List<FeedbackRecord> dataList) {
		mLoadingFrame.setVisibility(View.GONE);
		if (null != dataList && dataList.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			mListView
					.setAdapter(new FeedbackRecordAdapter(mActivity, dataList));
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					FeedbackRecord record = dataList.get(position);
					FeedbackMessageDetailsActivity.actionToFeedbackMesDetail(
							mActivity, 0, record.getMessage(),
							record.getReply_message());

				}
			});
		} else {
			mListView.setEmptyView(findViewById(ResUtil.get_R_id(mActivity,
					"empty")));
		}

	}

}

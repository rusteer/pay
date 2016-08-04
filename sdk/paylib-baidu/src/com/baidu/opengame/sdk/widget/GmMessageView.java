package com.baidu.opengame.sdk.widget;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.opengame.sdk.BDConst;
import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.activity.FeedbackMessageDetailsActivity;
import com.baidu.opengame.sdk.framework.AsyncHttpClient;
import com.baidu.opengame.sdk.framework.AsyncHttpResponseHandler;
import com.baidu.opengame.sdk.utils.MD5;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.utils.ToastUtils;

public class GmMessageView extends LinearLayout {

	Activity mActivity;

	ListView mListView;

	ViewGroup mLoadingFrame;

	public GmMessageView(Activity activity) {

		super(activity);

		mActivity = activity;

		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		setPadding(0, 0, 0, 0);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);

		View view = layoutInflater.inflate(
				ResUtil.get_R_Layout(mActivity, "bd_gm_msg_view"), this);

		mListView = (ListView) findViewById(ResUtil.get_R_id(mActivity,
				"orderListview"));

		mLoadingFrame = (ViewGroup) findViewById(ResUtil.get_R_id(mActivity,
				"loading_frame"));

		getTradeList();

	}

	public void getTradeList() {

		String url = BDConst.TRADE_GM_MESSAGE_GET_URL
				+ "&user_id="
				+ BDGameSDK.getInstance().getUserId()
				+ "&app_id="
				+ BDGameSDK.getInstance().getAppId()
				+ "&page_num=1"
				+ "&page_size=1000"
				+ "&key="
				+ MD5.crypt("2013baidusdk@conf!()"
						+ BDGameSDK.getInstance().getUserId());

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
							List<GmMessageRecord> dataList = new ArrayList<GmMessageRecord>();
							int lenth = jsonArray.length();
							for (int i = 0; i < lenth; i++) {
								GmMessageRecord record = new GmMessageRecord();
								JSONObject dataObject = jsonArray
										.getJSONObject(i);
								record.setApp_id(dataObject.optString("app_id"));

								record.setCreate_time(dataObject
										.optLong("create_time"));

								try {
									record.setContent(URLDecoder.decode(
											dataObject.optString("content"),
											"UTF-8"));

									record.setTitle(URLDecoder.decode(
											dataObject.optString("title"),
											"UTF-8"));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								record.setId(dataObject.optString("id"));

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

	private String getNowTime(long time) {
		Date now = new Date(time * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	public void notifyDataChange() {
		if (mGmMessageRecordAdapter != null) {
			mGmMessageRecordAdapter.notifyDataSetChanged();
		}
	};

	GmMessageRecordAdapter mGmMessageRecordAdapter;

	protected void showRecords(final List<GmMessageRecord> dataList) {
		mLoadingFrame.setVisibility(View.GONE);
		if (null != dataList && dataList.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			mGmMessageRecordAdapter = new GmMessageRecordAdapter(mActivity,
					dataList);
			mListView.setAdapter(mGmMessageRecordAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					GmMessageRecord record = dataList.get(position);
					FeedbackMessageDetailsActivity.actionToFeedbackMesDetail(
							mActivity, 1, record.getContent(),
							getNowTime(record.getCreate_time()),record.getTitle());
					BDGameSDK.getInstance().getMsgReadedList()
							.add(record.getId());
				}
			});
		} else {
			mListView.setEmptyView(findViewById(ResUtil.get_R_id(mActivity,
					"empty")));
		}

	}

}

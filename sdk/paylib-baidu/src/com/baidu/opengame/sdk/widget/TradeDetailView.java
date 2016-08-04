package com.baidu.opengame.sdk.widget;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.opengame.sdk.BDConst;
import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.framework.AsyncHttpClient;
import com.baidu.opengame.sdk.framework.AsyncHttpResponseHandler;
import com.baidu.opengame.sdk.utils.MD5;
import com.baidu.opengame.sdk.utils.ResUtil;
import com.baidu.opengame.sdk.utils.ToastUtils;
import com.baifubao.plat.MyApplication;

public class TradeDetailView extends LinearLayout {

	Activity mActivity;

	int mType;

	ListView mListView;

	ViewGroup mLoadingFrame;

	public TradeDetailView(Activity activity, int type) {

		super(activity);

		mActivity = activity;

		mType = type;
		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		setPadding(0, 0, 0, 0);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);

		View view = layoutInflater.inflate(
				ResUtil.get_R_Layout(mActivity, "bd_tradedetails_view"), this);

		mListView = (ListView) findViewById(ResUtil.get_R_id(mActivity,
				"orderListview"));

		mLoadingFrame = (ViewGroup) findViewById(ResUtil.get_R_id(mActivity,
				"loading_frame"));

		getTradeList();

	}

	public void getTradeList() {
		
		
		String token=MyApplication.getInstance().mAccessToken;
		try {
			token = URLEncoder.encode(MyApplication.getInstance().mAccessToken, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	 
		String url = BDConst.TRADE_DETAILS_GET_URL + "&user_id=" + BDGameSDK.getInstance().getUserId()
				+ "&app_id=" + BDGameSDK.getInstance().getAppId() + "&status=" + mType + "&page_num=1"
				+ "&page_size=1000" + "&key="
				+ MD5.crypt("2013baidusdk@conf!()" + BDGameSDK.getInstance().getUserId())+"&token="+token;

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
				super.onStart();
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
							List<OrderRecord> dataList = new ArrayList<OrderRecord>();
							int lenth = jsonArray.length();
							for (int i = 0; i < lenth; i++) {
								OrderRecord record = new OrderRecord();
								JSONObject dataObject = jsonArray
										.getJSONObject(i);
								record.setPro_name(dataObject
										.optString("pro_name"));
								record.setApp_id(dataObject.optString("app_id"));
								record.setOrder_id(dataObject
										.optString("order_id"));
								record.setOrder_time(dataObject
										.optLong("order_time"));
								record.setPrice(dataObject.optDouble("price"));
								record.setStatus(dataObject.optString("stauts"));
								record.setPaytype(dataObject
										.optString("paytype"));
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
						if(!"empty".equalsIgnoreCase(msg)){
							ToastUtils.showShortToast(mActivity, msg);
						}
						mLoadingFrame.setVisibility(View.GONE);
						mListView.setEmptyView(findViewById(ResUtil.get_R_id(mActivity,
								"empty")));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		});

	}

	protected void showRecords(List<OrderRecord> dataList) {
		mLoadingFrame.setVisibility(View.GONE);
		if (null != dataList && dataList.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			mListView.setAdapter(new PayRecordAdapter(mActivity, dataList));
		} else {
			mListView.setEmptyView(findViewById(ResUtil.get_R_id(mActivity,
					"empty")));
		}

	}

}

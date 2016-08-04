package com.baidu.opengame.sdk.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.opengame.sdk.BDGameSDK;
import com.baidu.opengame.sdk.utils.ResUtil;

public class GmMessageRecordAdapter extends BaseAdapter {

	private List<GmMessageRecord> orders;
	private LayoutInflater mInflater;

	Context context;

	public GmMessageRecordAdapter(Activity activity,
			List<GmMessageRecord> orders) {
		this.mInflater = LayoutInflater.from(activity);
		this.orders = orders;
		context = activity;

	}

	@Override
	public int getCount() {
		return null != orders ? orders.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int count = orders.size();
		Log.i("msg", count + "," + position);

		if (convertView == null) {
			convertView = mInflater.inflate(
					ResUtil.get_R_Layout(context, "bd_gm_message_item"), null);
		}
		TextView orderLabel = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "gmMesTitle"));
		orderLabel.setText(this.orders.get(position).getTitle());

		if (BDGameSDK.getInstance().getMsgReadedList()
				.contains(orders.get(position).getId())) {
			orderLabel.setTextColor(context.getResources().getColorStateList(
					ResUtil.get_R_Color(context, "feedback_time")));
		}

		TextView amountT = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "gmMesTime"));
		amountT.setText(getNowTime(this.orders.get(position).getCreate_time()));

		return convertView;
	}

	private String getNowTime(long time) {
		Date now = new Date(time * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

}

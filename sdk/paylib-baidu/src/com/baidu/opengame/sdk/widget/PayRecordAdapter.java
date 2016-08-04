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

import com.baidu.opengame.sdk.utils.ResUtil;

public class PayRecordAdapter extends BaseAdapter {

	private List<OrderRecord> orders;
	private LayoutInflater mInflater;

	Context context;

	public PayRecordAdapter(Activity activity, List<OrderRecord> orders) {
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
					ResUtil.get_R_Layout(context, "baidu_pay_record_item"),
					null);
		}
		TextView orderLabel = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "baidu_pay_record_order"));
		orderLabel.setText(this.orders.get(position).getOrder_id());

		TextView amountT = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "baidu_pay_record_amount"));
		amountT.setText(this.orders.get(position).getPrice() / 100 + "元");

		TextView serverT = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "baidu_pay_record_server"));
		serverT.setText(this.orders.get(position).getPaytype());

		TextView productT = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "baidu_pay_order_product_name"));
		productT.setText(this.orders.get(position).getPro_name());

		TextView statusT = (TextView) convertView.findViewById(ResUtil
				.get_R_id(context, "baidu_pay_record_status"));

		String statusStr = this.orders.get(position).getStatus();

		statusT.setText(statusStr);

		TextView timeT = (TextView) convertView.findViewById(ResUtil.get_R_id(
				context, "baidu_pay_record_date"));
		timeT.setText(getNowTime(orders.get(position).getOrder_time()));

		return convertView;
	}

	private String getNowTime(long time) {
		Date now = new Date(time * 1000);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

}

package com.baidu.opengame.sdk.widget;

public class FeedbackRecord {

	String app_id;

	String user_id;

	String contact_way;

	String message;

	long create_time;

	String reply_message;

	long reply_time;

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getContact_way() {
		return contact_way;
	}

	public void setContact_way(String contact_way) {
		this.contact_way = contact_way;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getReply_message() {
		return reply_message;
	}

	public void setReply_message(String reply_message) {
		this.reply_message = reply_message;
	}

	public long getReply_time() {
		return reply_time;
	}

	public void setReply_time(long reply_time) {
		this.reply_time = reply_time;
	}

}

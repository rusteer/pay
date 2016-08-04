package com.baidu.opengame.sdk;

public class BDConst {

//	 public static final String HOST_URL = "http://rd.opengame.baidu.com";

	public static final String HOST_URL = "http://opengame.baidu.com";

	public final static String SERVICE_GET_URL = BDConst.HOST_URL
			+ "/baidu_sdk_interface/server_info/";
	
	public final static String TRADE_DETAILS_GET_URL = BDConst.HOST_URL
			+ "/index.php?r=BaiduSdkAction&m=get_order_details";
	
	public final static String TRADE_MYFEEDBACK_GET_URL = BDConst.HOST_URL
			+ "/index.php?r=BaiduSdkAction&m=get_feedback_list";
	
	public final static String TRADE_GM_MESSAGE_GET_URL = BDConst.HOST_URL
			+ "/index.php?r=BaiduSdkAction&m=app_notice";
	
	public final static String TRADE_DK_FORUM_URL = BDConst.HOST_URL
			+ "/index.php?r=BaiduSdkAction&m=loginDuoku";
	
	public final static String HELP_URL = "http://co.baifubao.com/content/sdkhelp/";

}

package com.baidu.opengame.sdk.utils;

import android.text.TextUtils;

public class ValidUtils {

	public static final String PHONE_FORMAT = "(^[0-9]{3,4}-[0-9]{3,8}$)|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
	public static final int PHONE_LENTH = 11;

	public static boolean verifyPhoneNumberFormat(String photo) {

		if (!TextUtils.isEmpty(photo)) {
			return photo.matches(PHONE_FORMAT);
		} else {
			return false;
		}
	}

	public static boolean verifyTimeFormat(String time) {
		if (time.length() == 4) {
			return true;
		}

		return false;
	}
	
	public static boolean verifyMobileCodeFormat(String time) {
		if (time.length() == 6) {
			return true;
		}

		return false;
	}
	
	public static boolean verifyCvv2(String cvv2) {
		if (cvv2.length() == 3) {
			return true;
		}

		return false;
	}
	
	public static boolean verifyCreditCardNum(String cardNum) {
		if (cardNum.length() >= 15) {
			return true;
		}

		return false;
	}

}

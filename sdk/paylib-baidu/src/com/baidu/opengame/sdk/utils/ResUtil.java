package com.baidu.opengame.sdk.utils;

import android.content.Context;

public class ResUtil {
	public static int get_R_id(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id",
				paramContext.getPackageName());
	}

	public static int get_R_String(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string",
				paramContext.getPackageName());
	}

	public static int get_R_Drawable(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString,
				"drawable", paramContext.getPackageName());
	}

	public static int get_R_Color(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color",
				paramContext.getPackageName());

	}

	public static int get_R_Raw(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "raw",
				paramContext.getPackageName());

	}

	public static int get_R_Layout(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout",
				paramContext.getPackageName());

	}

	public static int get_R_Style(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style",
				paramContext.getPackageName());

	}
	
	public static int get_R_Dimen(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "dimen",
				paramContext.getPackageName());

	}
}

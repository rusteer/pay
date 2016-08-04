package com.baidu.opengame.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * for  File Config. (By SharedPreference)
 * @author lm
 *
 */
public class FileConfig implements IConfig {
	private static IConfig ourInstance;
	
	public static IConfig Instance(Context context) {
		if(ourInstance != null){
			return ourInstance;
		}else{
			ourInstance = new FileConfig(context);
			return ourInstance;
		}
	}

	private FileConfig(Context context) {
		mContext = context;
		ourInstance = this;
	}

	Context mContext;

	@Override
	public String getValue(String group, String name, String defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(group,
				Context.MODE_WORLD_READABLE);
		try {
			return sp.getString(name, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	@Override
	public void removeGroup(String name) {
		SharedPreferences sp = mContext.getSharedPreferences(name,
				Context.MODE_WORLD_READABLE);
		sp.edit().clear().commit();
	}

	@Override
	public void setValue(String group, String name, String value) {
		SharedPreferences sp = mContext.getSharedPreferences(group,
				Context.MODE_WORLD_WRITEABLE);
		sp.edit().putString(name, value).commit();
	}

	@Override
	public void deleteValue(String group, String name) {
		SharedPreferences sp = mContext.getSharedPreferences(group,
				Context.MODE_WORLD_READABLE);
		sp.edit().remove(name).commit();
	}

	@Override
	public boolean getValue(String group, String name, boolean defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(group,
				Context.MODE_WORLD_READABLE);
		try {
			return sp.getBoolean(name, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	@Override
	public void setValue(String group, String name, boolean value) {
		SharedPreferences sp = mContext.getSharedPreferences(group,
				Context.MODE_WORLD_WRITEABLE);
		sp.edit().putBoolean(name, value).commit();
		
	}

}

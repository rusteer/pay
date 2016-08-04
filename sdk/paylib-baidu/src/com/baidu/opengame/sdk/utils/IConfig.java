package com.baidu.opengame.sdk.utils;

/**
 * Config Interface
 * @author lm
 *
 */
public interface IConfig {

	public String getValue(String group, String name, String defaultValue);

	public void setValue(String group, String name, String value);
	
	public boolean getValue(String group, String name, boolean defaultValue);

	public void setValue(String group, String name, boolean value);

	public void deleteValue(String group, String name);

	public void removeGroup(String name);
}

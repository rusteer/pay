package com.baidu.opengame.sdk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.baidu.sapi2.share.NativeCrypto;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

	private static final String FILE_PATH = Environment
			.getExternalStorageDirectory().toString() + "/.baidugame/login";

	public static synchronized boolean writeString(Context context, String str) {
		try {
			str = NativeCrypto.encrypt(context, str);
			FileWriter fw = null;
			try {
				File file = makeDIRAndCreateFile(FILE_PATH);
				fw = new FileWriter(file, false);
				fw.write(str);

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					fw.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static synchronized String getString() {
		BufferedReader br = null;
		try {
			File file = makeDIRAndCreateFile(FILE_PATH);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			StringBuilder sb = new StringBuilder();
			String data = "";
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}

			String str = sb.toString();
			return str;

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";

	}

	public static synchronized String getString(Context context) {
		BufferedReader br = null;
		try {
			File file = makeDIRAndCreateFile(FILE_PATH);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			StringBuilder sb = new StringBuilder();
			String data = "";
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}

			String str = sb.toString();
			return NativeCrypto.decrypt(context, str);

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";

	}

	public static synchronized File makeDIRAndCreateFile(String filePath)
			throws IOException {
		// Auto-generated method stub
		File file = new File(filePath);
		String parent = file.getParent();
		File parentFile = new File(parent);
		if (!parentFile.exists()) {
			if (parentFile.mkdirs()) {
				file.createNewFile();
			} else {
				throw new IOException("创建目录失败！");
			}
		} else {
			if (!file.exists()) {
				file.createNewFile();
			}
		}
		return file;
	}
}

package com.thumb.payapi.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;

public class FileUtils {
    public static String readAssetFile(Context context, String fileName) {
        BufferedReader bufReader = null;
        String result = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(fileName)));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {}
            }
        }
        return result;
    }
    public static String readEncodedAssetFile(Context context, String fileName) {
        String encodedContent = readAssetFile(context, fileName);
        String password = CommonUtils.getPassword(context);
        return AES.decode(encodedContent, password);
    }
}

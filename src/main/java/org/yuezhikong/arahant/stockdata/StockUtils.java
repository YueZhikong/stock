package org.yuezhikong.arahant.stockdata;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class StockUtils {
    public static String gettoken(String strURL, String params) {
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String lines;
                String jsonString = "";
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    jsonString+=lines;
                }
                JSONObject jb = JSONObject.fromObject(jsonString);
                String access_token = "";
                access_token=jb.getString("access_token");
                reader.close();
                return access_token;
            }catch(IOException e1)
            {
                e1.printStackTrace();
                return "error";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "error"; // 自定义错误信息
    }

    public static String getpage(String tempurl,String bm) {
        String result="";
        try {
            URL url = new URL(tempurl);
            InputStream is = null;
            URLConnection con=url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.setConnectTimeout(120000);
            con.setReadTimeout(120000);
            con.connect();
            try {
                is = con.getInputStream();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(is,bm));
                    String s="";
                    String linesep = System.getProperty("line.separator");
                    while((s = reader.readLine())!=null){//使用readLine方法，一次读一行
                        result += s+linesep ;
                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e1) {
                        }
                    }
                }
                is.close();
            }catch (FileNotFoundException e2) {
                ;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }
}

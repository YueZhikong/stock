package org.yuezhikong.arahant.stockdata;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class StockDataApplication implements CommandLineRunner {
	@Autowired
	JdbcTemplate jdbcTemplate;
	public static void main(String[] args) {
		SpringApplication.run(StockDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		String sql = "insert into category (id,name) values (?,?)";
//		jdbcTemplate.update(sql,new Object[]{"1","test"});
//		String sql = "select * from category where id = ? and name = ?";
//		List<Category>  categories=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Category.class),new Object[]{"1","test"});
//		System.out.println(categories);
		webapi_example();
	}

	public String gettoken(String strURL, String params) {
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
				System.out.println(jsonString);
				JSONObject jb = JSONObject.fromObject(jsonString);
				String access_token = "";
				access_token=jb.getString("access_token");
				System.out.println(access_token);
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

	public String getpage(String tempurl,String bm) {
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

	public void webapi_example() {
		String token=gettoken("http://webapi.cninfo.com.cn/api-cloud-platform/oauth2/token",
				"grant_type=client_credentials&client_id=Mcioq2pC7YkWiWg75WYjL3eYvEeWVhs6&client_secret=f955c5b209cf406a9fc964c17bf55fee");  //请在平台注册后并填入个人中心-我的凭证中的Access Key，Access Secret
//		String url="http://webapi.cninfo.com.cn/api/stock/p_stock2402?&access_token="+token+"&scode=000001&sdate=20180306&edate=20180306";//接口名、参数名、参数值请按实际情况填写
		String url="http://webapi.cninfo.com.cn/api/stock/p_stock2402?&access_token="+token+"edate=20201231";//接口名、参数名、参数值请按实际情况填写
		String page = getpage(url,"utf-8") ;
		System.out.println(page);
		JSONObject jb = JSONObject.fromObject(page);
		JSONArray ja = jb.getJSONArray("records");
		try{
			for (int i = 0; i < ja.size(); i++) {
				ArrayList<String> args = new ArrayList<>(8);
				//stock_code
				System.out.println(ja.getJSONObject(i).getString("SECCODE"));
				args.add(ja.getJSONObject(i).getString("SECCODE"));
				//stock_name
				System.out.println(ja.getJSONObject(i).getString("SECNAME"));
				args.add(ja.getJSONObject(i).getString("SECNAME"));
				//trade_date
				System.out.println(ja.getJSONObject(i).getString("TRADEDATE"));
				args.add(ja.getJSONObject(i).getString("TRADEDATE"));
				//yesterday_end_price;
				System.out.println(ja.getJSONObject(i).getString("F002N"));
				args.add(ja.getJSONObject(i).getString("F002N"));
				//today_start_price;
				System.out.println(ja.getJSONObject(i).getString("F003N"));
				args.add(ja.getJSONObject(i).getString("F003N"));
				//max_trade_price;
				System.out.println(ja.getJSONObject(i).getString("F005N"));
				args.add(ja.getJSONObject(i).getString("F005N"));
				//min_trade_price;
				System.out.println(ja.getJSONObject(i).getString("F006N"));
				args.add(ja.getJSONObject(i).getString("F006N"));
				//lately_trade_price;
				System.out.println(ja.getJSONObject(i).getString("F007N"));
				args.add(ja.getJSONObject(i).getString("F007N"));
				String sql = "insert into stock (stock_code,stock_name,trade_date,yesterday_end_price,today_start_price,max_trade_price,min_trade_price,lately_trade_price) values (?,?,?,?,?,?,?,?)";
				Object[] objects = args.toArray();
				jdbcTemplate.update(sql,objects);
			}
		}catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}

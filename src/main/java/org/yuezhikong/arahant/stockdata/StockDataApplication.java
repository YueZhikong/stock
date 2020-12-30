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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class StockDataApplication implements CommandLineRunner {
	@Autowired
	JdbcTemplate jdbcTemplate;
	public static void main(String[] args) {
		SpringApplication.run(StockDataApplication.class, args);
	}
	public  static  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
	@Override
	public void run(String... args) throws Exception {
		List<String> scode =returnStockCode("20201230");
		insert(scode,"19890101","20201230");
	}

	private List<String> returnStockCode(String edate){
		String token=StockUtils.gettoken("http://webapi.cninfo.com.cn/api-cloud-platform/oauth2/token",
				"grant_type=client_credentials&client_id=Mcioq2pC7YkWiWg75WYjL3eYvEeWVhs6&client_secret=f955c5b209cf406a9fc964c17bf55fee");  //请在平台注册后并填入个人中心-我的凭证中的Access Key，Access Secret
		String url="http://webapi.cninfo.com.cn/api/stock/p_stock2402?&access_token="+token+"&sdate="+edate+"&edate="+edate;//接口名、参数名、参数值请按实际情况填写
		String page = StockUtils.getpage(url,"utf-8") ;
		JSONObject jb = JSONObject.fromObject(page);
		JSONArray ja = jb.getJSONArray("records");
		List<String> codes = new ArrayList<>();
		try{
			for (int i = 0; i < ja.size(); i++) {
				codes.add(ja.getJSONObject(i).getString("SECCODE"));
			}
		}catch (JSONException e)
		{
			e.printStackTrace();
		}
		return codes;
	}

	private void insert(List<String> codes,String sdate,String edate){
		String token=StockUtils.gettoken("http://webapi.cninfo.com.cn/api-cloud-platform/oauth2/token",
				"grant_type=client_credentials&client_id=Mcioq2pC7YkWiWg75WYjL3eYvEeWVhs6&client_secret=1df864d1219648fc9653ba2ff0d35ba4");  //请在平台注册后并填入个人中心-我的凭证中的Access Key，Access Secret
		for (String code:codes){
			String url="http://webapi.cninfo.com.cn/api/stock/p_stock2402?&access_token="+token+"&scode="+code+"&sdate="+sdate+"&edate="+edate;//接口名、参数名、参数值请按实际情况填写
			String page = StockUtils.getpage(url,"utf-8") ;
			JSONObject jb = JSONObject.fromObject(page);
			JSONArray ja = jb.getJSONArray("records");
			List<Object[]> batchArgs = new ArrayList<>(300);
			String sql = "insert into stock (stock_code,stock_name,trade_date,yesterday_end_price,today_start_price,max_trade_price,min_trade_price,lately_trade_price) values (?,?,?,?,?,?,?,?)";
			try{
				for (int i = 0; i < ja.size(); i++) {
					ArrayList<String> args = new ArrayList<>(8);
					//stock_code
					args.add(ja.getJSONObject(i).getString("SECCODE"));
					//stock_name
					args.add(ja.getJSONObject(i).getString("SECNAME"));
					//trade_date
					args.add(ja.getJSONObject(i).getString("TRADEDATE"));
					//yesterday_end_price;
					args.add(ja.getJSONObject(i).getString("F002N"));
					//today_start_price;
					args.add(ja.getJSONObject(i).getString("F003N"));
					//max_trade_price;
					args.add(ja.getJSONObject(i).getString("F005N"));
					//min_trade_price;
					args.add(ja.getJSONObject(i).getString("F006N"));
					//lately_trade_price;
					args.add(ja.getJSONObject(i).getString("F007N"));
					Object[] objects = args.toArray();
					batchArgs.add(objects);
				}
				jdbcTemplate.batchUpdate(sql,batchArgs);
				System.out.println("从"+sdate+"到"+edate+"代码"+code+"插入成功");
				Thread.sleep(15000);
			}catch (JSONException | InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}

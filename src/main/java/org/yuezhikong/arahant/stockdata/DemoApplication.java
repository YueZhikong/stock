//package org.yuezhikong.arahant.stockdata;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.math.BigDecimal;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@SpringBootApplication
//public class DemoApplication implements CommandLineRunner {
//    @Autowired
//	JdbcTemplate jdbcTemplate;
//    public static void main(String[] args) {
//        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.NONE).run(args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        String basePath = "G:\\test02";
//        String[] list = new File(basePath).list();
//        for (int i = 0; i < list.length; i++) {
//            readFileByLine(basePath+"\\"+list[i],list[i]);
//        }
//    }
//
//    public void readFileByLine(String strFile,String fileName){
//        try {
//            File file = new File(strFile);
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(file, Charset.forName("gbk")));
//            String strLine = null;
//            int lineCount = 1;
//            fileName=fileName.replaceAll(".txt","").replaceAll("#","");
//            String name=null;
//            List<Object[]> batch = new ArrayList<>(240);
//            while(null != (strLine = bufferedReader.readLine())){
//                if (lineCount==1){
//                    String[] aaa = strLine.split(" ");
//                    name = aaa[1];
//                }
//                else if (lineCount==2){
//
//                }
//                else {
//                    String[] sss = strLine.split(",");
//                    if (sss.length>1){
//                        List<String> args = new ArrayList<>();
//                        args.add(fileName+","+sss[0]);
//                        args.add(fileName);
//                        args.add(name);
//                        args.addAll(Arrays.asList(sss));
//                        BigDecimal a = new BigDecimal(sss[2]);
//                        BigDecimal b = new BigDecimal(sss[3]);
//                        BigDecimal c = a.add(b).divide(new BigDecimal(2));
//                        args.add(c.toString());
//                        batch.add(args.toArray());
//                    }
//                }
//                lineCount++;
//            }
//            String sql = "insert into stock_former_restoration (id,code,name,date,start,high,low,end,trade_number,trade_value,middle) values (?,?,?,?,?,?,?,?,?,?,?)";
//            jdbcTemplate.batchUpdate(sql,batch);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//}

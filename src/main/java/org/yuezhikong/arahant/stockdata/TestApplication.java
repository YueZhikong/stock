//package org.yuezhikong.arahant.stockdata;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//@SpringBootApplication
//public class TestApplication implements CommandLineRunner {
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    public static void main(String[] args) {
//        new SpringApplicationBuilder(TestApplication.class).web(WebApplicationType.NONE).run(args);
//    }
//    @Override
//    public void run(String... args) throws Exception {
//        boolean flag = true;
//        while (flag) {
//            String sql = "SELECT * FROM stock_no_restoration limit 0,5000";
//            List<StockEntity> aa = jdbcTemplate.query(sql, new RowMapper<StockEntity>() {
//                @Override
//                public StockEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
//                    StockEntity stockEntity = new StockEntity();
//                    stockEntity.setId(rs.getString("id"));
//                    return stockEntity;
//                }
//            });
//            if (aa.size()==0){
//                flag=false;
//            }
//            List<Object[]> batch = new ArrayList<>();
//            for (StockEntity stockEntity : aa) {
//                batch.add(new Object[]{stockEntity.getId()});
//            }
//            String sql01 = "delete from stock_no_restoration where id =?";
//            jdbcTemplate.batchUpdate(sql01, batch);
//        }
//    }
//}

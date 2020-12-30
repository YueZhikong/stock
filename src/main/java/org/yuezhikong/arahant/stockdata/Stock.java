package org.yuezhikong.arahant.stockdata;

import java.math.BigDecimal;
import java.util.Date;

public class Stock {
    private String stock_code;
    private String stock_name;
    private Date trade_date;
//    private String exchange;
    private BigDecimal yesterday_end_price;
    private BigDecimal today_start_price;
//    private int trade_number;
    private BigDecimal max_trade_price;
    private BigDecimal min_trade_price;
    private BigDecimal lately_trade_price;
//    private int total_number;
}

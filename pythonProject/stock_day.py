# coding=utf-8
from __future__ import print_function, absolute_import

import pymysql
from gm.api import *

# 可以直接提取数据，掘金终端需要打开，接口取数是通过网络请求的方式，效率一般，行情数据可通过subscribe订阅方式
# 设置token， 查看已有token ID,在用户-秘钥管理里获取
set_token('576fb1e82267084b0ba05dd34cc79e46e2e3ab42')
connection = pymysql.connect(host='localhost',
                             user='root',
                             password='123456',
                             db='stock',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
# 查询历史行情, 采用定点复权的方式， adjust指定前复权，adjust_end_time指定复权时间点
data = history(symbol='	SZSE.300454', frequency='1d', start_time='2005-01-01', end_time='2021-01-01',
               fields='symbol,open,close,high,low,amount,volume,bob,eob', adjust=ADJUST_NONE, df=True)
print(data)
batch = []
for index, row in data.iterrows():
    batch.append((row['symbol'], row['open'], row['close'], row['high'], row['low'], row['amount'], row['volume'],
                  row['bob'], row['eob']))

    # print(data)
    # for i in range(len(data)):
    #     for k in data[i]:
    #         print(k)
    #         print(data[i][k])
    # print(data)
try:
    with connection.cursor() as cursor:
        sql = "INSERT INTO `stock_day`(`sysmbol`, `open`, `close`, `high`, `low`, `amount`, `volome`, `bob`, `eob`) " \
              "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)"
        cursor.executemany(sql, batch)
        connection.commit()
finally:
    connection.close()

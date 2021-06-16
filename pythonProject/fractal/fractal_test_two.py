import pymysql


# 疑问2 在底分型买入，等下一个顶分型卖出，成功率多少？收益多少？ 单位为一手
# 同理 顶分型空，底分型多呢？

# 0.38461538461538464
# -408.00
# 0.6923076923076923
# -400.00

# 都是负数呢，难怪说k线形态没有任何意义呢
def test_buy(k_lines):
    # 买入总次数
    total_buy = 0
    # 获利次数
    profit_number = 0
    # 获利数
    profit = 0
    # 临时存放 买入点
    temp = None
    for i in range(0, len(k_lines)):
        if k_lines[i]['is_of_classified'] == 1 and temp is not None:
            if k_lines[i + 1]['close'] >= temp['close']:
                profit_number = profit_number + 1
                profit = k_lines[i + 1]['close'] - temp['close'] + profit
                temp = None
            else:
                profit = k_lines[i + 1]['close'] - temp['close'] + profit
                temp = None
        if k_lines[i]['is_of_classified'] == -1 and temp is None:
            temp = k_lines[i + 1]
            total_buy = total_buy + 1

    print(profit_number / total_buy)
    print(profit)


def test_sell(k_lines):
    # 买入总次数
    total_sell = 0
    # 获利次数
    profit_number = 0
    # 获利数
    profit = 0
    # 临时存放 买入点
    temp = None
    for i in range(0, len(k_lines)):
        if k_lines[i]['is_of_classified'] == -1 and temp is not None:
            if k_lines[i + 1]['close'] >= temp['close']:
                profit_number = profit_number + 1
                profit = temp['close'] - k_lines[i + 1]['close'] + profit
                temp = None
            else:
                profit = temp['close'] - k_lines[i + 1]['close'] + profit
                temp = None
        if k_lines[i]['is_of_classified'] == 1 and temp is None:
            temp = k_lines[i + 1]
            total_sell = total_sell + 1

    print(profit_number / total_sell)
    print(profit)


connection = pymysql.connect(host='localhost',
                             user='root',
                             password='123456',
                             db='stock',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
sql = "select * from future_day_merge where symbol='CZCE.RM109' order by `bob` asc "
cursor = connection.cursor()
try:
    cursor.execute(sql)
    data = cursor.fetchall()
    print(data)
    test_buy(data)
    test_sell(data)
finally:
    connection.close()

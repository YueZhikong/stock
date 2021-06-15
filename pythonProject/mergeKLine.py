# 尝试合并k线
import pymysql


# 比较两根k线，前一根与后一根是否能够包含
def is_contain(previous_k_line, latter_k_line):
    previous_open = previous_k_line['open']
    previous_close = previous_k_line['close']
    latter_open = latter_k_line['open']
    latter_close = latter_k_line['close']
    previous_max = max(previous_open, previous_close)
    previous_min = min(previous_open, previous_close)
    latter_max = max(latter_open, latter_close)
    latter_min = min(latter_open, latter_close)
    # 前包后
    if previous_max >= latter_max and previous_min <= latter_min:
        return True
    # 后包前
    if latter_max >= previous_max and latter_min <= previous_min:
        return True
    return False


def attempt_contain(previous_k_line, latter_k_line):
    d = {'symbol': previous_k_line['symbol']}
    if abs(previous_k_line['open'] - previous_k_line['close']) >= abs(latter_k_line['open'] - latter_k_line['close']):
        d['open'] = previous_k_line['open']
        d['close'] = previous_k_line['close']
    else:
        d['open'] = latter_k_line['open']
        d['close'] = latter_k_line['close']

    if previous_k_line['high'] >= latter_k_line['high']:
        d['high'] = previous_k_line['high']
    else:
        d['high'] = latter_k_line['high']

    if previous_k_line['low'] <= latter_k_line['low']:
        d['low'] = previous_k_line['low']
    else:
        d['low'] = latter_k_line['low']

    d['amount'] = previous_k_line['amount'] + latter_k_line['amount']
    d['volume'] = previous_k_line['volume'] + latter_k_line['volume']

    d['bob'] = previous_k_line['bob']
    d['eob'] = latter_k_line['eob']

    d['position'] = previous_k_line['position'] + latter_k_line['position']
    return d


def insert(dictionary, connection=pymysql.connect,
           insert_sql="INSERT INTO `future_day_merge`(`symbol`, `open`, `close`, `high`, `low`, `amount`, `volume`, `bob`, `eob`,`position`,`is_of_classified`) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s)"):
    cursor_insert = connection.cursor()
    cursor_insert.execute(insert_sql, (
        str(dictionary['symbol']), str(dictionary['open']), str(dictionary['close']), str(dictionary['high']),
        str(dictionary['low']), str(dictionary['amount']), str(dictionary['volume']), str(dictionary['bob']),
        str(dictionary['eob']), str(dictionary['position']), str(0)))
    connection.commit()


db = pymysql.connect(host='localhost',
                     user='root',
                     password='123456',
                     db='stock',
                     charset='utf8',
                     cursorclass=pymysql.cursors.DictCursor)
cursor = db.cursor()

sql = "select `symbol`, `open`, `close`, `high`, `low`, `amount`, `volume`, `bob`, `eob`,`position` from future_day where symbol='CZCE.RM109' order by bob asc "
try:
    cursor.execute(sql)
    results = cursor.fetchall()
    temp = {}
    for i in range(1, len(results), 1):
        if temp:
            if is_contain(temp, results[i]):
                temp = attempt_contain(temp, results[i])
            else:
                # print(temp)
                insert(temp, db)
                temp = {}
        else:
            if is_contain(results[i - 1], results[i]):
                temp = attempt_contain(results[i - 1], results[i])
            else:
                # 向数据库里插入这组数
                # print(results[i - 1])
                insert(results[i - 1], db)
    # 没做完合并的也输出
    if temp:
        # print(temp)
        insert(temp, db)
    else:
        # print(results[len(results) - 1])
        insert(results[len(results) - 1], db)
finally:
    db.close()

import pymysql
import datetime

def is_fractal(previous_k_line, middle_k_line, latter_k_line):
    previous_open = previous_k_line['open']
    previous_close = previous_k_line['close']
    middle_open = middle_k_line['open']
    middle_close = middle_k_line['close']
    latter_open = latter_k_line['open']
    latter_close = latter_k_line['close']
    previous_max = max(previous_open, previous_close)
    previous_min = min(previous_open, previous_close)
    middle_max = max(middle_open, middle_close)
    middle_min = min(middle_open, middle_close)
    latter_max = max(latter_open, latter_close)
    latter_min = min(latter_open, latter_close)

    if middle_max == max(previous_max, middle_max, latter_max):
        return 1
    elif middle_min == min(previous_min, middle_min, latter_min):
        return -1
    else:
        return 0


connection = pymysql.connect(host='localhost',
                             user='root',
                             password='123456',
                             db='stock',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
sql = "select `symbol`, `open`, `close`, `high`, `low`, `amount`, `volume`, `bob`, `eob`,`position`,`is_of_classified` from future_day_merge where symbol='CZCE.RM109' order by bob asc"
cursor = connection.cursor()
try:
    cursor.execute(sql)
    results = cursor.fetchall()
    update_sql = "update future_day_merge set `is_of_classified`=%s where `symbol`=%s and `bob`=%s and `eob`=%s"
    li = []
    for i in range(0, 1):
        li.append((0, results[i]['symbol'], results[i]['bob'],
                   results[i]['eob']))
    for i in range(1, len(results) - 1):
        temp = is_fractal(results[i - 1], results[i], results[i + 1])
        if temp == 1 or temp == -1:
            li.append((temp, results[i]['symbol'], results[i]['bob'],
                       results[i]['eob']))
        else:
            li.append((0, results[i]['symbol'], results[i]['bob'],
                       results[i]['eob']))
    li.append((0, results[len(results) - 1]['symbol'], results[len(results) - 1]['bob'],
               results[len(results) - 1]['eob']))
    # cursor.executemany(update_sql, li)
    cursor.execute(update_sql, li[0])
    connection.commit()
finally:
    connection.close()

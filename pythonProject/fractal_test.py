import pymysql


# 疑问1 顶分型与底分型的数量比是n:n+1吗？
# 顶分型和底分型的数量并不是一比一的，也不是n:n+1的 菜粕2021-3-22是例子
connection = pymysql.connect(host='localhost',
                             user='root',
                             password='123456',
                             db='stock',
                             charset='utf8',
                             cursorclass=pymysql.cursors.DictCursor)
sql1 = "select count(*) from future_day_merge where symbol='CZCE.RM109' and `is_of_classified`=1"
sql2 = "select count(*) from future_day_merge where symbol='CZCE.RM109' and `is_of_classified`=-1"
cursor = connection.cursor()
try:
    cursor.execute(sql1)
    data1 = cursor.fetchall()
    print(data1)
    cursor.execute(sql2)
    data2 = cursor.fetchall()
    print(data2)
finally:
    connection.close()

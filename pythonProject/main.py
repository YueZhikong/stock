# coding=utf-8
from __future__ import print_function, absolute_import
from gm.api import *


# 可以直接提取数据，掘金终端需要打开，接口取数是通过网络请求的方式，效率一般，行情数据可通过subscribe订阅方式
# 设置token， 查看已有token ID,在用户-秘钥管理里获取
set_token('576fb1e82267084b0ba05dd34cc79e46e2e3ab42')

# 查询历史行情, 采用定点复权的方式， adjust指定前复权，adjust_end_time指定复权时间点
data = history('SHSE.600000', '1d', '2020-01-01 09:00:00', '2020-12-31 16:00:00',
               'open,high,low,close', ADJUST_PREV, '2020-12-31', True)
print(data)

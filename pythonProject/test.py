from __future__ import print_function, absolute_import
from gm.api import *

set_token('576fb1e82267084b0ba05dd34cc79e46e2e3ab42')
all_stocks = get_instruments(exchanges=None, sec_types=1,
                             fields='symbol', df=1)['symbol'].tolist()
print(all_stocks)
print(len(all_stocks))

all_futures = get_instruments(exchanges=None, sec_types=4,
                             fields='symbol', df=1)['symbol'].tolist()
print(all_futures)
print(len(all_futures))

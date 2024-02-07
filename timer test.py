# timer test
import time as time
from datetime import datetime
from math import floor

timecount = 0
while timecount < 527040:
        print(
                str("January" if timecount < 44640 else "February" if timecount < 86400 else "") + " " + # month
                str(floor(timecount / 1440) + 1) + " " + # day
                str(floor(timecount / 60) % 24) + ":" + str(timecount - (60 * floor(timecount / 60))) # time
        )
        #print(str(floor(timecount / 60)) + ":" + str(timecount - (60 * floor(timecount / 60))))

        #print(timecount)
        timecount += 1
        time.sleep(1/60)
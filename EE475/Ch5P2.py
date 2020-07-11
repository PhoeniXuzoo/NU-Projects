import csv
import numpy as np
import math

def readData():
    rawData = csv.reader(open("kleibers_law_data.csv", 'r'))

    xList = []
    yList = []
    i = 0
    for raw in rawData:
        if i == 0:
            xList = raw
            i += 1
        else:
            yList = raw

    for i in range(len(xList)):
        xList[i] = math.log(float(xList[i]))
        yList[i] = math.log(float(yList[i]))

    return xList, yList



if  __name__  ==  "__main__":
    xList, yList = readData()
    xBar = np.mean(xList)
    yBar = np.mean(yList)

    a = 0
    b = 0
    for i in range(len(xList)):
        a += (xList[i] - xBar) * (yList[i] - yBar)
        b += (xList[i] - xBar) ** 2

    w1 = a / b
    w2 = yBar - w1 * xBar
    print(w1, w2)


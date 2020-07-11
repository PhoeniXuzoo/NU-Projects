import numpy as np
import matplotlib.pyplot as plt

def readData(csvname):
    data = np.loadtxt(csvname, delimiter=',')
    x = data[:-1, :]
    y = data[-1:, :]

    return x, y

fot = lambda x : np.exp(-x) / (1 + np.exp(-x))
sot = lambda x : ( 1 / (1 + np.exp(x))) * (1 - ( 1 / (1 + np.exp(x))))

# power is y_p * x_p.T * w
# firstOrderTerm is e^-power / (1 + e^-power)
def first_order(x, y, w, class_weight, power):
    total = np.zeros(w.shape)
    firstOrderTerm = fot(power)

    for i in range(np.size(y)):
        total += class_weight[i] * firstOrderTerm[:,i] * y[:,i] * x[:,[i]]

    return (-1) * (total / np.size(y))


def second_order(x, y, w, class_weight, power):
    total = np.zeros([x.shape[0], x.shape[0]])
    secondOrderTerm = sot(power)

    for i in range(np.size(y)):
        total += class_weight[i] * secondOrderTerm[:, i] * x[:, [i]] * x[:, [i]].T

    return total / np.size(y)


def newton_method(x, y, w, class_weight):
    power = y * np.transpose(np.dot(x.T, w))
    firstOrder = first_order(x, y, w, class_weight, power)
    secondOrder = second_order(x, y, w, class_weight, power)

    return w - np.dot(np.linalg.inv(secondOrder), firstOrder)

def costFunc(x, y, w, class_weight):
    temp = np.log(1 + np.exp(-y*np.transpose(np.dot(np.transpose(x), w))))
    cost = 0
    for i in range(np.size(y)):
        cost += temp[0][i] * class_weight[i]
    return cost / float(np.size(y))

if  __name__  ==  "__main__":
    csvname = '3d_classification_data_v2_mbalanced.csv'
    x, y = readData(csvname)
    w = np.ones([x.shape[0] + 1, 1])
    x = np.insert(x, 0, values=np.ones([1, x.shape[1]]), axis=0)

    positiveOneWeight = 7/11
    negativeOneWeight = 4/11

    class_weight = []
    for i in range(np.size(y)):
        if (y[:, i] > 0):
            class_weight.append(positiveOneWeight)
        else:
            class_weight.append(negativeOneWeight)

    position = x[[1, 2]]
    positiveOneXList = []
    positiveOneYList = []
    negativeOneXList = []
    negativeOneYList = []
    for i in range(position.shape[1]):
        if (y[0][i] > 0):
            positiveOneXList.append(position[0][i])
            positiveOneYList.append(position[1][i])
        else:
            negativeOneXList.append(position[0][i])
            negativeOneYList.append(position[1][i])

    plt.scatter(positiveOneXList, positiveOneYList, color='red')
    plt.scatter(negativeOneXList, negativeOneYList, color='blue')

    for i in range(5):
        w = newton_method(x, y, w, class_weight)

    a = -(w[1][0]/w[2][0])
    b = -(w[0][0]/w[2][0])

    foo = lambda x : a * x + b
    i = -0.1
    xList = []
    yList = []
    while (i < 1.1):
        xList.append(i)
        yList.append(foo(i))
        i += 0.1

    plt.plot(xList, yList)
    plt.show()




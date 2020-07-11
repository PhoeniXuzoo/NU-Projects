import numpy as np
import matplotlib.pyplot as plt
import copy

def readData(csvname):
    data = np.loadtxt(csvname, delimiter=',')
    x = data[:-1, :]
    y = data[-1:, :]

    return x, y

def drawpoint(x, y):
    position = x[[1,2]]
    zeroXList = []
    zeroYList = []
    oneXList = []
    oneYList = []
    twoXList = []
    twoYList = []
    threeXList = []
    threeYList = []
    for i in range(position.shape[1]):
        if (int(y[0][i]) == 0):
            zeroXList.append(position[0][i])
            zeroYList.append(position[1][i])
        elif (int(y[0][i]) == 1):
            oneXList.append(position[0][i])
            oneYList.append(position[1][i])
        elif (int(y[0][i]) == 2):
            twoXList.append(position[0][i])
            twoYList.append(position[1][i])
        elif (int(y[0][i]) == 3):
            threeXList.append(position[0][i])
            threeYList.append(position[1][i])

    plt.scatter(zeroXList, zeroYList, color = 'blue')
    plt.scatter(oneXList, oneYList, color = 'red')
    plt.scatter(twoXList, twoYList, color='green')
    plt.scatter(threeXList, threeYList, color='yellow')
    return plt

def drawline(w, c):
    a = -(w[1][0] / w[2][0])
    b = -(w[0][0] / w[2][0])

    foo = lambda x: a * x + b
    i = 0.0
    xList = []
    yList = []
    while (i < 1.0):
        xList.append(i)
        yList.append(foo(i))
        i += 0.1

    plt.plot(xList, yList, color = c)


class Logistic:
    def __init__(self, label, x, y, w, epoch, alpha):
        self.label = label
        self.epoch = epoch
        self.alpha = alpha
        self.x = copy.deepcopy(x)
        self.w = copy.deepcopy(w)
        self.y = copy.deepcopy(y)
        for i in range(np.size(self.y)):
            if (int(self.y[0][i]) == int(label)):
                self.y[0][i] = 1.0
            else:
                self.y[0][i] = -1.0

    # def softmaxCostFunc(self):
    #     cost = np.sum(np.log(1 + np.exp(-(self.y)*np.transpose(np.dot(np.transpose(self.x), self.w)))))
    #     return cost / float(np.size(self.y))
    #
    # def gradientDescentOneStepForSoftmax(self, alpha):
    #     total = np.zeros([3, 1])
    #     for i in range(np.size(self.y)):
    #         power = np.exp(-self.y[:, i] * np.dot(self.x[:, i], self.w))
    #         term = power / (1 + power)
    #         total += term * self.y[:, i] * self.x[:, [i]]
    #
    #     self.w = self.w + alpha * (1 / np.size(self.y)) * total

    def perceptronCostFunc(self):
        cost = 0
        a = (-(self.y) * np.transpose(np.dot(np.transpose(self.x), self.w)))[0]
        for i in range(len(a)):
            cost += a[i] if (a[i] > 0) else 0
        return cost / float(np.size(self.y))

    def gradientDescentOneStepForPerceptron(self, alpha):
        total = np.zeros([3, 1])
        for i in range(np.size(self.y)):
            term = -self.y[:, i] * np.dot(self.x[:, [i]].T, self.w)
            total += 0 if term <= 0 else -self.y[:, i] * self.x[:, [i]]

        self.w = self.w - alpha * (1 / np.size(self.y)) * total

    def normalizeW(self):
        i = 1
        sum = 0.0
        while (i < np.size(self.w)):
            sum += self.w[i][0] ** 2
            i += 1
        return self.w / (sum ** 0.5)

    # def softmaxTrain(self):
    #     for i in range(self.epoch):
    #         self.gradientDescentOneStepForSoftmax(self.alpha)
    #     return self.normalizeW()

    def perceptronTrain(self):
        for i in range(self.epoch):
            self.gradientDescentOneStepForPerceptron(self.alpha)
        return self.normalizeW()


def statistic(x, y, zeroW, oneW, twoW, threeW):
    wrong = 0
    for i in range(np.shape(x)[1]):
        zero = np.dot(x[:,[i]].T, zeroW)
        one = np.dot(x[:,[i]].T, oneW)
        two = np.dot(x[:,[i]].T, twoW)
        three = np.dot(x[:,[i]].T, threeW)

        if (int(y[0][i]) == 0):
            if not (zero >= one and zero >= two and zero >= three):
                wrong += 1
        elif (int(y[0][i]) == 1):
            if not (one >= zero and one >= two and one >= three):
                wrong += 1
        elif (int(y[0][i]) == 2):
            if not (two >= zero and two >= one and two >= three):
                wrong += 1
        elif (int(y[0][i]) == 3):
            if not (three >= zero and three >= one and three >= two):
                wrong += 1
    return wrong


if  __name__  ==  "__main__":
    csvname = '4class_data.csv'
    x, y = readData(csvname)
    w = np.ones([x.shape[0] + 1, 1])
    x = np.insert(x, 0, values=np.ones([1, x.shape[1]]), axis=0)

    plt = drawpoint(x, y)

    zeroClassifier = Logistic(0.0, x, y, w, 1400, 0.05)
    zeroW = zeroClassifier.perceptronTrain()
    drawline(zeroW, 'blue')
    oneClassifier = Logistic(1.0, x, y, w, 250, 0.05)
    oneW = oneClassifier.perceptronTrain()
    drawline(oneW, 'red')

    w = np.zeros([x.shape[0], 1])
    w += 0.05
    twoClassifier = Logistic(2.0, x, y, w, 120, 0.01)
    twoW = twoClassifier.perceptronTrain()
    drawline(twoW, 'green')

    threeClassifier = Logistic(3.0, x, y, w, 505, 0.01)
    threeW = threeClassifier.perceptronTrain()
    drawline(threeW, 'yellow')

    plt.show()

    print("MisClassifications: ", statistic(x, y, zeroW, oneW, twoW, threeW))



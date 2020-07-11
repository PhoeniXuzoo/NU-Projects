import numpy as np
import matplotlib.pyplot as plt

## softmax: 0.1 600
## perceptron: 0.05 550

def readData(csvname):
    data = np.loadtxt(csvname, delimiter=',')
    x = data[:-1, :]
    y = data[-1:, :]

    return x, y


def softmaxCostFunc(x, y, w):
    cost = np.sum(np.log(1 + np.exp(-y*np.transpose(np.dot(np.transpose(x), w)))))
    return cost / float(np.size(y))


def gradientDescentOneStepForSoftmax(x, y, w, alpha=0.1):
    total = np.zeros([9,1])
    for i in range(np.size(y)):
        power = np.exp(-y[:,i] * np.dot(x[:,i], w))
        term = power / (1 + power)
        total += term * y[:,i] * x[:,[i]]

    w = w + alpha * (1/np.size(y)) * total
    return w


def perceptronCostFunc(x, y, w):
    cost = 0
    a = (-y*np.transpose(np.dot(np.transpose(x), w)))[0]
    for i in range(len(a)):
        cost += a[i] if (a[i] > 0) else 0
    return cost / float(np.size(y))


def gradientDescentOneStepForPerceptron(x, y, w, alpha=0.05):
    total = np.zeros([9,1])
    for i in range(np.size(y)):
        term = -y[:,i] * np.dot(x[:,[i]].T, w)
        total += 0 if term <= 0 else -y[:,i] * x[:,[i]]

    w = w - alpha * (1/np.size(y)) * total
    return w


if  __name__  ==  "__main__":
    csvname = 'breast_cancer_data.csv'
    x, y = readData(csvname)
    w = np.ones([x.shape[0] + 1, 1])
    x = np.insert(x, 0, values=np.ones([1, x.shape[1]]), axis=0)

    xSoftList = [0]
    ySoftList = [softmaxCostFunc(x, y, w)]
    for i in range(600):
        w = gradientDescentOneStepForSoftmax(x, y, w)
        xSoftList.append(i+1)
        ySoftList.append(softmaxCostFunc(x, y, w))

    yPredic = np.transpose(np.dot(np.transpose(x), w))
    wrong = 0
    for i in range(np.size(yPredic)):
        if ((yPredic[0][i] > 0) != (y[0][i] > 0)):
            wrong += 1
    print("Softmax Wrong Prediction: ", wrong)

    w = np.ones([x.shape[0], 1])
    xPerceptronList = [0]
    yPerceptronList = [perceptronCostFunc(x, y, w)]
    for i in range(550):
        w = gradientDescentOneStepForPerceptron(x, y, w)
        xPerceptronList.append(i+1)
        yPerceptronList.append(perceptronCostFunc(x, y, w))

    plt.plot(xSoftList, ySoftList, label="Softmax Cost Function",color="#F08080")
    plt.plot(xPerceptronList, yPerceptronList, label="Perceptro Cost Function")
    plt.legend(loc="upper right")
    plt.show()
    plt.close()

    yPredic = np.transpose(np.dot(np.transpose(x), w))
    wrong = 0
    for i in range(np.size(yPredic)):
        if ((yPredic[0][i] > 0) != (y[0][i] > 0)):
            wrong += 1
    print("Perceptron Wrong Prediction: ", wrong)

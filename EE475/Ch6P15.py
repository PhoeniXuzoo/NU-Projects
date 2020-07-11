import numpy as np
import matplotlib.pyplot as plt

def readData(csvname):
    data = np.loadtxt(csvname, delimiter=',')
    x = data[:-1, :]
    y = data[-1:, :]

    for i in range(len(x)):
        x[i] = (x[i] - np.mean(x[i])) / np.std(x[i])

    return x, y

def perceptronCostFunc(x, y, w, class_index):
    cost = 0
    a = (-y*np.transpose(np.dot(np.transpose(x), w)))[0]
    for i in range(len(a)):
        cost += (class_index[i] * a[i]) if (a[i] > 0) else 0
    return cost / float(np.size(y))


def gradientDescentOneStepForPerceptron(x, y, w, class_index, alpha=0.00020):
    total = np.zeros([21,1])
    for i in range(np.size(y)):
        term = -y[:,i] * np.dot(x[:,[i]].T, w)
        total += 0 if term <= 0 else -y[:,i] * x[:,[i]] * class_index[i]

    w = w - alpha * (1/np.size(y)) * total
    return w

if  __name__  ==  "__main__":
    csvname = 'credit_dataset.csv'
    x, y = readData(csvname)
    w = np.zeros([x.shape[0] + 1, 1])
    w += 0.5
    alpha = 0.0025
    goodWeight = 1
    badWeight = 1
    iteration = 5000
    x = np.insert(x, 0, values=np.ones([1, x.shape[1]]), axis=0)

    print("alpha: ", alpha)
    print("iteration: ", iteration)

    class_index = []
    for i in range(np.size(y)):
        if (y[:, i] > 0):
            class_index.append(goodWeight)
        else:
            class_index.append(badWeight)

    xPerceptronList = [0]
    yPerceptronList = [perceptronCostFunc(x, y, w, class_index)]
    for i in range(iteration):
        w = gradientDescentOneStepForPerceptron(x, y, w, class_index, alpha)
        xPerceptronList.append(i + 1)
        yPerceptronList.append(perceptronCostFunc(x, y, w, class_index))

    plt.plot(xPerceptronList, yPerceptronList)
    plt.show()
    plt.close()

    yPredic = np.transpose(np.dot(np.transpose(x), w))
    correct = 0
    for i in range(np.size(yPredic)):
        if ((yPredic[0][i] > 0) == (y[0][i] > 0)):
            correct += 1
    print("Accuracy: ", correct / 1000)
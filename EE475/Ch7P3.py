import autograd.numpy as np
import matplotlib.pyplot as plt
from autograd import grad
from autograd.misc.flatten import flatten_func

def readData(csvname):
    data = np.loadtxt(csvname, delimiter=',')
    x = data[:-1, :]
    y = data[-1:, :]
    return x, y

def model(x, w):
    a =  w[0] + np.dot(x.T, w[1:])
    return a.T

def multiclass_perceptron (w):
    all_evals = model(x, w)
    a = np.max(all_evals, axis = 0)
    b = all_evals[y.astype(int).flatten(), np.arange(np.size(y))]
    cost = np.sum(a - b)
    cost = cost + lam*np.linalg.norm(w[1: ,:], 'fro')**2
    return cost / float(np.size(y))

def gradientDescent(w, mp_flat):
    gradient = grad(mp_flat)
    grad_eval = gradient(w)
    return w - 0.1 * grad_eval

def statistic(x, y, w):
    prediction = np.argmax(model(x,w),axis=0)
    wrong = 0
    for i in range(np.size(y)):
        if prediction[i] != y[0][i]:
            wrong += 1
    return wrong

# global variable
lam = 10 ** -5
csvname = '3class_data.csv'
x, y = readData(csvname)
lam = 10 ** -5

if  __name__  ==  "__main__":
    w = np.ones([x.shape[0] + 1, 3])
    w += 1
    mp_flat, unflatten_func, w = flatten_func(multiclass_perceptron, w)

    xList = [0]
    yList = [mp_flat(w)]
    for i in range(100):
        w = gradientDescent(w, mp_flat)
        xList.append(i+1)
        yList.append(mp_flat(w))

    w = w.reshape(3, 3)
    plt.plot(xList, yList)
    plt.show()
    print("misclassification: ", statistic(x, y, w))




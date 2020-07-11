import ssl
from sklearn.datasets import fetch_openml
import autograd.numpy as np
from autograd import grad
from autograd.misc.flatten import flatten_func
import matplotlib.pyplot as plt
import random

# import MNIST
x, y = fetch_openml('mnist_784', version=1, return_X_y=True)

# re-shape input/output data
x = x.T
y = np.array([int(v) for v in y])[np.newaxis,:]

for i in range(len(x)):
    if np.average(x[i]) == 0.0:
        continue
    x[i] = (x[i] - np.mean(x[i])) / np.std(x[i])

num_sample = 50000
inds = np.random.permutation(y.shape[1])[:num_sample]
x = x[:,inds]
y = y[:,inds]

lam=0.00001
print(np.shape(x))
print(np.shape(y))
x_1=x
y_1=y

import data_transformer
x_sample_edgebased_features = data_transformer.edge_transformer(x)
x_2=x_sample_edgebased_features
print('shape of transformed input ', x_sample_edgebased_features.shape)

def model(x,w):
    a = w[0] + np.dot(x.T, w[1:])
    return a.T

def multiclass_softmax(w):
    all_evals = model(x, w)
    a = np.log(np.sum(np.exp(all_evals), axis=0))
    b = all_evals[y.astype(int).flatten(), np.arange(np.size(y))]
    cost = np.sum(a - b)
    cost = cost + lam * np.linalg.norm(w[1:, :], 'fro') ** 2
    return cost / float(np.size(y))

def gradientDescent(w, mp_flat):
    gradient = grad(mp_flat)
    grad_eval = gradient(w)
    return w - 0.01 * grad_eval


#w = 0.1*np.ones([x.shape[0] + 1, 10])
#ms_flat, unflatten_func, w = flatten_func(multiclass_softmax, w)
def training(x_1):

    w = 0.01 * np.ones([x_1.shape[0] + 1, 10])
    ms_flat, unflatten_func, w = flatten_func(multiclass_softmax, w)
    xList = [0]
    yList = [ms_flat(w)]
    for epoch in range(20):
        for i in range(250):
            x = x_1[:, i*200:i*200 + 200]
            y = y_1[:, i*200:i*200 + 200]
            w = gradientDescent(w, ms_flat)
        xList.append(epoch + 1)
        yList.append(ms_flat(w))
    plt.plot(xList, yList)

training(x_1)
print("Succuss!")
x=x_2
training(x_2)
print("Finish!")

plt.show()
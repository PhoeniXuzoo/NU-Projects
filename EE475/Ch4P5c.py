from autograd import grad
from autograd import hessian
import autograd.numpy as np
import matplotlib.pyplot as plt


def newtons_method(g, max_its, w):
    gradient = grad(g)
    hess = hessian(g)

    epsilon = 10 ** (-7)

    weight_history = [w]
    cost_history = [g(w)]

    for k in range(max_its):
        grad_eval = gradient(w)
        hess_eval = hess(w)

        hess_eval.shape = (int((np.size(hess_eval)) ** (0.5)), int((np.size(hess_eval)) ** (0.5)))
        A = hess_eval + epsilon * np.eye(w.size)
        b = grad_eval
        w = np.linalg.solve(A, np.dot(A, w) - b)

        weight_history.append(w)
        cost_history.append(g(w))

    return weight_history, cost_history


def g(w):
    return np.log(1 + np.exp(np.dot(np.transpose(w), w)))


if __name__ == "__main__":
    w = 1 * np.ones([2, 1])
    max_its = 10

    weight_history, cost_history = newtons_method(g, max_its, w)

    print(weight_history)
    print(cost_history)

    xList = []
    yList = []
    for i in range(max_its):
        xList.append(i+1)
        yList.append(cost_history[i][0][0])

    fig = plt.figure()
    plt.title("Exercise 4.5 (c)")
    plt.plot(xList, yList)
    plt.show()
    plt.close()




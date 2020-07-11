import ssl
from sklearn.datasets import fetch_openml
import autograd.numpy as np
import matplotlib.pyplot as plt
from matplotlib.pyplot import MultipleLocator
from autograd import grad
from autograd.misc.flatten import flatten_func
import copy

ssl._create_default_https_context = ssl._create_unverified_context

class tensor_conv_layer:
    def conv_function(self, tensor_window):
        tensor_window = np.reshape(tensor_window, (
        np.shape(tensor_window)[0], np.shape(tensor_window)[1] * np.shape(tensor_window)[2]))
        t = np.dot(self.kernels, tensor_window.T)
        return t

    def pool_function(self, tensor_window):
        t = np.max(tensor_window, axis=(1, 2))
        return t

    def activation(self, tensor_window):
        return np.maximum(0, tensor_window)

    def pad_tensor(self, tensor, kernel_size):
        odd_nums = np.array([int(2 * n + 1) for n in range(100)])
        pad_val = np.argwhere(odd_nums == kernel_size)[0][0]
        tensor_padded = np.zeros(
            (np.shape(tensor)[0], np.shape(tensor)[1] + 2 * pad_val, np.shape(tensor)[2] + 2 * pad_val))
        tensor_padded[:, pad_val:-pad_val, pad_val:-pad_val] = tensor
        return tensor_padded

    def sliding_window_tensor(self, tensor, window_size, stride, func):
        image_size = np.shape(tensor)[1]
        results = []
        for i in np.arange(0, image_size - window_size + 1, stride):
            for j in np.arange(0, image_size - window_size + 1, stride):
                tensor_window = tensor[:, i:i + window_size, j:j + window_size]
                tensor_window = np.array(tensor_window)
                yo = func(tensor_window)
                results.append(yo)
        results = np.array(results)
        results = results.swapaxes(0, 1)
        if func == self.conv_function:
            results = results.swapaxes(1, 2)
        return results

    def make_feature_tensor(self, tensor):
        conv_stride = 1
        feature_tensor = self.sliding_window_tensor(tensor, self.kernel_size, conv_stride, self.conv_function)
        num_filters = np.shape(feature_tensor)[0]
        num_images = np.shape(feature_tensor)[1]
        square_dim = int((np.shape(feature_tensor)[2]) ** (0.5))
        feature_tensor = np.reshape(feature_tensor, (num_filters, num_images, square_dim, square_dim))
        feature_tensor = self.activation(feature_tensor)
        pool_stride = 2
        pool_window_size = 3
        downsampled_feature_map = []
        for t in range(np.shape(feature_tensor)[0]):
            temp_tens = feature_tensor[t, :, :, :]
            d = self.sliding_window_tensor(temp_tens, pool_window_size, pool_stride, self.pool_function)
            downsampled_feature_map.append(d)
        downsampled_feature_map = np.array(downsampled_feature_map)
        return downsampled_feature_map

    def normalize(self, data, data_mean, data_std):
        normalized_data = (data - data_mean) / (data_std + 10 ** (-5))
        return normalized_data

    def conv_layer(self, tensor, kernels):
        num_images = np.shape(tensor)[0]
        num_kernels = np.shape(kernels)[0]
        tensor = np.reshape(tensor, (
        np.shape(tensor)[0], int((np.shape(tensor)[1]) ** (0.5)), int((np.shape(tensor)[1]) ** (0.5))), order='F')
        kernel = kernels[0]
        self.kernel_size = np.shape(kernel)[0]
        padded_tensor = self.pad_tensor(tensor, self.kernel_size)
        self.kernels = np.reshape(kernels, (np.shape(kernels)[0], np.shape(kernels)[1] * np.shape(kernels)[2]))
        feature_tensor = self.make_feature_tensor(padded_tensor)

        feature_tensor = feature_tensor.swapaxes(0, 1)
        feature_tensor = np.reshape(feature_tensor, (
        np.shape(feature_tensor)[0], np.shape(feature_tensor)[1] * np.shape(feature_tensor)[2]), order='F')

        return feature_tensor

def readData():
    x, y = fetch_openml('mnist_784', version=1, return_X_y=True)
    x = x.T
    y = np.array([int(v) for v in y])[np.newaxis,:]
    for i in range(len(x)):
        if np.average(x[i]) == 0.0:
            continue
        x[i] = (x[i] - np.mean(x[i])) / np.std(x[i])
    return x[:, 0:50000], y[:, 0:50000]

def edge_transformer(x):
    kernels = np.array([
           [[-1, -1, -1],
            [ 0,  0,  0],
            [ 1,  1,  1]],

           [[-1, -1,  0],
            [-1,  0,  1],
            [ 0,  1,  1]],

            [[-1,  0,  1],
            [-1,  0,  1],
            [-1,  0,  1]],

           [[ 0,  1,  1],
            [-1,  0,  1],
            [-1, -1,  0]],

           [[ 1,  0, -1],
            [ 1,  0, -1],
            [ 1,  0, -1]],

           [[ 0, -1, -1],
            [ 1,  0, -1],
            [ 1,  1,  0]],

           [[ 1,  1,  1],
            [ 0,  0,  0],
            [-1, -1, -1]],

           [[ 1,  1,  0],
            [ 1,  0, -1],
            [ 0, -1, -1]]])

    demo = tensor_conv_layer()
    x_transformed = demo.conv_layer(x.T,kernels).T
    return x_transformed

#global variable
x_batch, y_batch = readData()
x = x_batch
y = y_batch
x_edgebased_features = edge_transformer(x)
lam = 10**(-5)

def model(vector,w):
    a = w[0] + np.dot(vector.T, w[1:])
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

if  __name__  ==  "__main__":
    plt.figure()
    w = np.ones([x_batch.shape[0] + 1, 10])
    w *= 0.01
    y1wrong = []
    prediction = np.argmax(model(x_batch, w), axis=0)
    wrong = 0
    for i in range(np.size(y_batch)):
        if prediction[i] != y_batch[0][i]:
            wrong += 1
    y1wrong.append(wrong)

    ms_flat, unflatten_func, w = flatten_func(multiclass_softmax, w)
    xList = [0]
    x = x_batch[:, 0:200]
    y = y_batch[:, 0:200]
    yList = [ms_flat(w)]

    for epoch in range(20):
        for i in range(250):
            x = x_batch[:, i * 200: i * 200 + 200]
            y = y_batch[:, i * 200: i * 200 + 200]
            w = gradientDescent(w, ms_flat)
        xList.append(epoch + 1)
        yList.append(ms_flat(w))
        w_temp = copy.deepcopy(w)
        w_temp = w_temp.reshape(x_batch.shape[0] + 1, 10)
        prediction = np.argmax(model(x_batch, w_temp), axis=0)
        wrong = 0
        for i in range(np.size(y_batch)):
            if prediction[i] != y_batch[0][i]:
                wrong += 1
        y1wrong.append(wrong)
    plt.plot(xList, yList, label="raw data point", color='blue')

    w = np.ones([x_edgebased_features.shape[0] + 1, 10])
    w *= 0.01
    y2wrong = []
    prediction = np.argmax(model(x_edgebased_features, w), axis=0)
    wrong = 0
    for i in range(np.size(y_batch)):
        if prediction[i] != y_batch[0][i]:
            wrong += 1
    y2wrong.append(wrong)

    ms_flat, unflatten_func, w = flatten_func(multiclass_softmax, w)
    xList = [0]
    x = x_edgebased_features[:, 0:200]
    y = y_batch[:, 0:200]
    yList = [ms_flat(w)]
    for epoch in range(20):
        for i in range(250):
            x = x_edgebased_features[:, i * 200: i * 200 + 200]
            y = y_batch[:, i * 200: i * 200 + 200]
            w = gradientDescent(w, ms_flat)
        xList.append(epoch + 1)
        yList.append(ms_flat(w))
        w_temp = copy.deepcopy(w)
        w_temp = w_temp.reshape(x_edgebased_features.shape[0] + 1, 10)
        prediction = np.argmax(model(x_edgebased_features, w_temp), axis=0)
        wrong = 0
        for i in range(np.size(y_batch)):
            if prediction[i] != y_batch[0][i]:
                wrong += 1
        y2wrong.append(wrong)
    plt.plot(xList, yList, label="edge histogram based", color="red")
    plt.legend(loc="upper right")
    xmajorLocator = MultipleLocator(5)
    ax = plt.gca()
    ax.xaxis.set_major_locator(xmajorLocator)
    plt.show()
    plt.close()

    plt.figure()
    plt.plot(xList, y1wrong, label="raw data point", color="blue")
    plt.plot(xList, y2wrong, label="edge histogram based", color="red")
    plt.legend(loc="upper right")
    ymajorLocator = MultipleLocator(1000)
    ax = plt.gca()
    ax.xaxis.set_major_locator(xmajorLocator)
    ax.yaxis.set_major_locator(ymajorLocator)
    plt.ylim(0, 10000)
    plt.show()
    plt.close()

import numpy as np

def readData(csvname):
    data = np.loadtxt(csvname, delimiter=',')
    x = data[:-1, :]
    y = data[-1:, :]

    for i in range(len(x)):
        x[i] = (x[i] - np.mean(x[i])) / np.std(x[i])
    return x, y

def gradientDescentEachStep(w, x, y):
    w_T = np.transpose(w)
    new_W = []
    for i in range(len(w)):
        total = 0
        for j in range(len(y[0])):
            total += (np.dot(w_T, np.transpose(np.array([x[:,j]]))) - y[0][j]) * x[i][j]
        new_W.append(w[i][0] - 0.1 * (1/len(y[0])) * total)

    for i in range(len(w)):
        w[i][0] = new_W[i]

    return w

if  __name__  ==  "__main__":
    csvname = 'auto_data.csv'
    x, y = readData(csvname)
    w = np.ones([x.shape[0] + 1,1])
    x = np.insert(x, 0, values=np.ones([1, x.shape[1]]), axis=0)

    for i in range(1000):
        w = gradientDescentEachStep(w, x, y)

    totalForRMSE = 0
    totalForMAD = 0
    w_T = np.transpose(w)
    for i in range(len(y[0])):
        temp = (np.dot(w_T, np.transpose(np.array([x[:,i]]))) - y[0][i])
        totalForRMSE += temp ** 2
        totalForMAD += abs(temp)

    MSE = totalForRMSE / len(y[0])
    RMSE = MSE ** 0.5
    MAD = totalForMAD / len(y[0])

    print("RMSE is ", RMSE)
    print("MAD is ", MAD)

import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

def gradientDescentEveryStep(derivative, startPoint, stepLength):
    return startPoint - stepLength * derivative(startPoint)

def constFunc(w):
    total = 0
    for i in w:
        total += i*i
    return  total

if  __name__  ==  "__main__"  :
    # g(w) = w^T * w     w is a 10*1 vector
    with PdfPages('Chapter3Practice8.pdf') as pdf:
        derivate = lambda w: 2 * w

        stepLength = [0.001, 0.1, 1]

        for alpha in stepLength:
            startPoint = np.ones(10) * 10
            fig = plt.figure()
            plt.title("Exercise 3.8: steplength is " + str(alpha))
            xList = [0]
            yList = [constFunc(startPoint)]
            for i in range(100):
                startPoint = gradientDescentEveryStep(derivate, startPoint, alpha)
                xList.append(i+1)
                yList.append(constFunc(startPoint))
            plt.plot(xList, yList)
            plt.show()
            pdf.savefig(fig)
            plt.close()
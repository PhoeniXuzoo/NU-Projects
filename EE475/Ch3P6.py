import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

def gradientDescentEveryStep(derivative, startPoint, stepLength):
    return startPoint - stepLength * derivative(startPoint)

if  __name__  ==  "__main__":
    # g(w) = |x|
    with PdfPages('Chapter3Practice6.pdf') as pdf:
        costFunc = lambda x : abs(x)
        derivative = lambda x: 1 if (x >= 0) else -1

        # fixed steplength
        startPoint = 1.75
        fig = plt.figure()
        plt.title("Exercise 3.6: fixed steplength is 0.5")
        xList = [0]
        yList = [costFunc(startPoint)]
        for i in range(20):
            startPoint = gradientDescentEveryStep(derivative, startPoint, 0.5)
            xList.append(i+1)
            yList.append(costFunc(startPoint))
        plt.plot(xList, yList)
        plt.show()
        pdf.savefig(fig)
        plt.close()

        # diminishing step length
        startPoint = 1.75
        fig = plt.figure()
        plt.title("Exercise 3.6: diminishing steplength")
        xList = [0]
        yList = [costFunc(startPoint)]
        for i in range(20):
            startPoint = gradientDescentEveryStep(derivative, startPoint, 1/(i+1))
            xList.append(i + 1)
            yList.append(costFunc(startPoint))
        plt.plot(xList, yList)
        plt.show()
        pdf.savefig(fig)
        plt.close()














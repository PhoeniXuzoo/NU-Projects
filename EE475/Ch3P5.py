import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

def gradientDescentEveryStep(derivative, startPoint, stepLength):
    return startPoint - stepLength * derivative(startPoint)

if  __name__  ==  "__main__":
    # g(w) = (1/50) * ( w^4 + w^2 + w)
    with PdfPages('Chapter3Practice5.pdf') as pdf:
        polynomial = np.poly1d([1/50, 0, 1/50, 1/5, 0])
        # derivative of g(w)
        derivative = polynomial.deriv()

        stepLength = [1, 0.1, 0.01]

        for alpha in stepLength:
            startPoint = 2
            fig = plt.figure()
            plt.title("Exercise 3.5: steplength is " + str(alpha))
            xList = [0]
            yList = [polynomial(startPoint)]
            for i in range(1000):
                startPoint = gradientDescentEveryStep(derivative, startPoint, alpha)
                xList.append(i+1)
                yList.append(polynomial(startPoint))
            plt.plot(xList, yList)
            plt.show()
            pdf.savefig(fig)
            plt.close()










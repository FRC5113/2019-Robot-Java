import cv2
import math
import numpy as np

def ylevelTest(rectY, yTresh):
    bounds = [yTresh[0] - yTresh[1] / 2, yTresh[0] + yTresh[1]]
    return rectY >= bounds[0] and rectY <= bounds[1]

def solidityTest(contourArea, convexHull, minSolidity):
    hullArea = cv2.contourArea(convexHull)
    return float(contourArea / hullArea) >= minSolidity

def quadrilateralTest(contour, tolerance, numSides=4): # numSides can be a different integer to find polygons of alternate side counts (e.g. 5 for pentagon)
    for epsilon in range(1, tolerance):
        approx = cv2.approxPolyDP(contour, epsilon, True)

        if len(approx) == numSides:
            return True

    return False

def parallaxCorrection(contour, pointsDestination, delete):
    left, right, top, bottom = getExtremePoints(contour)

    pointsSource = np.array([left, right, top, bottom])
    h, status = cv2.findHomography(pointsSource, pointsDestination)

def getExtremePoints(contour): # returns in order left, right, top, buttom
    left = tuple(contour[contour[:,:,0].argmin()][0])
    right = tuple(contour[contour[:,:,0].argmax()][0])
    top = tuple(contour[contour[:,:,1].argmin()][0])
    bottom = tuple(contour[contour[:,:,1].argmax()][0])

    return left, right, top, bottom

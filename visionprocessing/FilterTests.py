import cv2
import math
import numpy as np

def ylevelTest(rectY, yTresh):
    bounds = [yTresh[0] - yTresh[1] / 2, yTresh[0] + yTresh[1]]
    return rectY >= bounds[0] and rectY <= bounds[1]

def solidityTest(contourArea, convexHull, minSolidity):
    hullArea = cv2.contourArea(convexHull)
    return float(contourArea / hullArea) >= minSolidity

def aspectRatioTest(rectWidth, rectHeight, desiredRatio, tolerance, angle=0): # desiredRatio = width/height, e.g. 4:3 becomes 4/3
    b, h = getBH(rectWidth, rectHeight, angle)

    ratio = b / h

    return math.fabs(ratio - desired_radio) < tolerance

def quadrilateralTest(contour, tolerance, numSides=4): # numSides can be a different integer to find polygons of alternate side counts (e.g. 5 for pentagon)
    epsilon = 0

    while epsilon < tolerance:
        epsilon += 1
        approx = cv2.approxPolyDP(contour, epsilon, True)

        if len(approx) == numSides and epsilon < tolerance:
            return True

    return False

def getBH(rectWidth, rectHeight, angle):
    b = rectHeight / math.cos(angle)
    h = rectWidth * math.cos(angle)

    return b, h

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

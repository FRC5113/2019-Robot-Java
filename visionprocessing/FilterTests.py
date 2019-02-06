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

def areaTest(contourArea, minArea):
    return contourArea > minArea

'''
This is a test specific to the 2019 season. Given eight points
(the corners of the two vision targets), make sure that the
points are within a certain radius of where they should be
if they are actually a vision target.
'''
def superStrictTest(rect1, rect2, destPoints, tolerance):
    for i in range(4):
        if not withinRadius(rect1[i], destPoints[i], tolerance):
            return False

    for i in range(4):
        if not withinRadius(rect2[i], destPoints[4+i], tolerance):
            return False

    return True

def withinRadius(point, center, radius):
    return math.sqrt(math.pow(point[0] - center[0], 2) + math.pow(point[1] - center[1], 2)) < radius # distance formula

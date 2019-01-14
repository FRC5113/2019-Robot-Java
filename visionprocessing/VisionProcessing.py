import numpy as np
import cv2
from networktables import NetworkTables as nettab#import NetworkTablesInstance as nettab
import math
import logging
import copy
import threading
import FilterTests as ft
import ConfigHandler as ch

CONFIG = ch.getConfig('vision_processing.cfg')

cap = cv2.VideoCapture(CONFIG['camera'])

# Constants
RESOLUTION = (cap.get(cv2.CAP_PROP_FRAME_WIDTH), cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
#LOWER_THRESH = np.array([45, 81, 0])
#UPPER_THRESH = np.array([138, 215, 44])
LOWER_THRESH = np.array([178, 190, 128])
UPPER_THRESH = np.array([246, 247, 242])

# Contour Test Constants
Y_THRESH = [96, 240] # (y-center, range) or should I make it (lower, upper)?
RECT_TOLERANCE = 200
VISION_TAPE_ANGLE = 14.5 * math.pi / 180 # radians
MIN_SOLIDITY = 0.94

'''
# Network Tables
#nettab.setIPAddress('10.51.13.2')
nettab.setClientMode()
nettab.initialize(server='roborio-5113-frc.local')
table = nettab.getTable('contoursReport')
table.putNumber('x_resolution', RESOLUTION[0])
'''
########### Connects RaspberryPi to roboRIO ##############################
def connectionListener(connected, info):
    print(info, '; Connected=%s' % connected)
    with cond:
        notified[0] = True
        cond.notify()

if not CONFIG['offline']:
    cond = threading.Condition()
    notified = [False]

    nettab.initialize(server='10.51.13.2')
    nettab.addConnectionListener(connectionListener, immediateNotify=True)

    table = nettab.getTable('contoursReport')
    with cond:
        print("Waiting")
        if not notified[0]:
            cond.wait()
##########################################################################

########### This is only used for finding new values #####################
def nothing(x):
    pass

cv2.namedWindow('threshold_tester')
cv2.createTrackbar('h', 'threshold_tester', LOWER_THRESH[0], 360, nothing)
cv2.createTrackbar('s', 'threshold_tester', LOWER_THRESH[1], 255, nothing)
cv2.createTrackbar('v', 'threshold_tester', LOWER_THRESH[2], 255, nothing)
cv2.createTrackbar('H', 'threshold_tester', UPPER_THRESH[0], 360, nothing)
cv2.createTrackbar('S', 'threshold_tester', UPPER_THRESH[1], 255, nothing)
cv2.createTrackbar('V', 'threshold_tester', UPPER_THRESH[2], 255, nothing)
cv2.createTrackbar('y-level[0]', 'threshold_tester', int(Y_THRESH[0]), int(RESOLUTION[1]), nothing)
cv2.createTrackbar('y-level[1]', 'threshold_tester', int(Y_THRESH[1]), int(RESOLUTION[1] / 2), nothing)
cv2.createTrackbar('solidity', 'threshold_tester', int(MIN_SOLIDITY * 100), 100, nothing)
cv2.createTrackbar('rect test', 'threshold_tester', RECT_TOLERANCE, 1000, nothing)
##########################################################################

while True:
    ########### This is only used for finding new values #################
    LOWER_THRESH[0] = cv2.getTrackbarPos('h', 'threshold_tester')
    LOWER_THRESH[1] = cv2.getTrackbarPos('s', 'threshold_tester')
    LOWER_THRESH[2] = cv2.getTrackbarPos('v', 'threshold_tester')
    UPPER_THRESH[0] = cv2.getTrackbarPos('H', 'threshold_tester')
    UPPER_THRESH[1] = cv2.getTrackbarPos('S', 'threshold_tester')
    UPPER_THRESH[2] = cv2.getTrackbarPos('V', 'threshold_tester')
    Y_THRESH[0] = cv2.getTrackbarPos('y-level[0]', 'threshold_tester')
    Y_THRESH[1] = cv2.getTrackbarPos('y-level[1]', 'threshold_tester')
    RECT_TEST_TOLERANCE = cv2.getTrackbarPos('rect-tolerance', 'threshold_tester')
    MIN_SOLIDITY = cv2.getTrackbarPos('solidity', 'threshold_tester') / 100
    RECT_TOLERANCE = cv2.getTrackbarPos('rect test', 'threshold_tester')
    ######################################################################

    ### Save new frame ###
    ret, frame = cap.read()

    ############## Process Image #########################################
    ### Threshold image ###
    thresholdedImage = cv2.inRange(frame, LOWER_THRESH, UPPER_THRESH)

    ### Dilate image ###
    kernel = np.ones((5, 5), np.uint8)
    dilatedImage = cv2.dilate(thresholdedImage, kernel, iterations = 2) # change number of iterations to dilate further
    ######################################################################

    ############## Get & Filter Contours #################################
    ### Get contours ###
    contoursImage, rawContours, hierarchy = cv2.findContours(dilatedImage, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    ### Filter contours ###
    filteredContours = []

    for contour in rawContours:
        rectX, rectY, rectWidth, rectHeight = cv2.boundingRect(contour)
        contourArea = cv2.contourArea(contour)
        convexHull = cv2.convexHull(contour)

        yLevelTest = ft.ylevelTest(rectY, Y_THRESH) or not CONFIG['y-level']
        solidityTest = ft.solidityTest(contourArea, convexHull, MIN_SOLIDITY) or not CONFIG['solidity']
        quadrilateralTest = ft.quadrilateralTest(contour, 11) or not CONFIG['quadrilateralTest']

        if yLevelTest and solidityTest and quadrilateralTest:
            filteredContours.append(contour)
    ######################################################################

    ############## Show All Images #######################################
    contourImage = cv2.drawContours(frame, filteredContours, -1, (0, 0, 255), 2)

    cv2.imshow('thresholded', thresholdedImage)
    cv2.imshow('dilated', dilatedImage)
    cv2.imshow('contours', contourImage)
    ######################################################################

    ############## Send Contours #########################################
    # This is going to change, and rather than publishing all of
    # this info, we will instead send just: middle coordinate,
    # area, number of targets found (0, 1, or 2), and the
    # distance to the target.
    if not CONFIG['offline']:
        table.putNumber('numTargetsFound', len(filteredContours))

    '''
    table.putNumberArray('xs', xs)
    table.putNumberArray('ys', ys)
    table.putNumberArray('widths', widths)
    table.putNumberArray('heights', heights)
    table.putNumberArray('areas', areas)
    #table.putNumberArray('distance, distance)
    '''
    ######################################################################

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

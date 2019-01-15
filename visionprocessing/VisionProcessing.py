import numpy as np
import cv2
from networktables import NetworkTables as nettab
import math
import copy
import threading
import FilterTests as ft
import ConfigHandler as ch
import os

# The general loop of this file is explained here: https://docs.opencv.org/3.0-beta/doc/py_tutorials/py_gui/py_video_display/py_video_display.html

os.system('sh cameraconfig.sh') # This sets certain parameters for the camera such as disabling autoexposure, setting exposure, etc.
CONFIG = ch.getConfig('vision_processing.cfg') # This allows us to define constants from a configuration file,
												# rather than in the code
cap = cv2.VideoCapture(CONFIG['camera']) # defines our camera

# Constants
RESOLUTION = (cap.get(cv2.CAP_PROP_FRAME_WIDTH), cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
VT_HEIGHT = CONFIG['VT_HEIGHT']
LOWER_THRESH = np.array(CONFIG['LOWER_THRESH'])
UPPER_THRESH = np.array(CONFIG['UPPER_THRESH'])
#LOWER_THRESH = np.array([178, 190, 128])
#UPPER_THRESH = np.array([246, 247, 242])

# Contour Test Constants
Y_TEST_THRESH = CONFIG['Y_TEST_THRESH']
MIN_SOLIDITY = CONFIG['MIN_SOLIDITY']
VISION_TAPE_ANGLE = 14.5 * math.pi / 180 # radians

########### Connects RaspberryPi to roboRIO ##############################
# copied from here: https://robotpy.readthedocs.io/en/stable/guide/nt.html#client-initialization-driver-station-coprocessor
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

	table.putNumber('X_RESOLUTION', RESOLUTION[0])
##########################################################################

########### This is only used for finding new values #####################
def nothing(x): # defines an empty function. cv2.createTrackbar() requires a callback
    pass        # function, and since we don't use one, we just defined an empty one.

cv2.namedWindow('threshold_tester')
cv2.createTrackbar('h', 'threshold_tester', LOWER_THRESH[0], 360, nothing)
cv2.createTrackbar('s', 'threshold_tester', LOWER_THRESH[1], 255, nothing)
cv2.createTrackbar('v', 'threshold_tester', LOWER_THRESH[2], 255, nothing)
cv2.createTrackbar('H', 'threshold_tester', UPPER_THRESH[0], 360, nothing)
cv2.createTrackbar('S', 'threshold_tester', UPPER_THRESH[1], 255, nothing)
cv2.createTrackbar('V', 'threshold_tester', UPPER_THRESH[2], 255, nothing)
cv2.createTrackbar('y-level[0]', 'threshold_tester', int(Y_TEST_THRESH[0]), int(RESOLUTION[1]), nothing)
cv2.createTrackbar('y-level[1]', 'threshold_tester', int(Y_TEST_THRESH[1]), int(RESOLUTION[1] / 2), nothing)
cv2.createTrackbar('solidity', 'threshold_tester', int(MIN_SOLIDITY * 100), 100, nothing)
##########################################################################

while True:
    ########### This is only used for finding new values #################
    LOWER_THRESH[0] = cv2.getTrackbarPos('h', 'threshold_tester')
    LOWER_THRESH[1] = cv2.getTrackbarPos('s', 'threshold_tester')
    LOWER_THRESH[2] = cv2.getTrackbarPos('v', 'threshold_tester')
    UPPER_THRESH[0] = cv2.getTrackbarPos('H', 'threshold_tester')
    UPPER_THRESH[1] = cv2.getTrackbarPos('S', 'threshold_tester')
    UPPER_THRESH[2] = cv2.getTrackbarPos('V', 'threshold_tester')
    Y_TEST_THRESH[0] = cv2.getTrackbarPos('y-level[0]', 'threshold_tester')
    Y_TEST_THRESH[1] = cv2.getTrackbarPos('y-level[1]', 'threshold_tester')
    RECT_TEST_TOLERANCE = cv2.getTrackbarPos('rect-tolerance', 'threshold_tester')
    MIN_SOLIDITY = cv2.getTrackbarPos('solidity', 'threshold_tester') / 100
    ######################################################################

    ### Save new frame ###
    ret, frame = cap.read()

    ############## Process Image #########################################
    ### Threshold image ###
    thresholdedImage = cv2.inRange(frame, LOWER_THRESH, UPPER_THRESH)

    ### Dilate image ###
    kernel = np.ones((5, 5), np.uint8) # This is a line copied from OpenCV's website. It establishes the size of the dilation
    dilatedImage = cv2.dilate(thresholdedImage, kernel, iterations = 2) # change number of iterations to dilate fupallerther
    ######################################################################

    ############## Get & Filter Contours #################################
    contoursImage, rawContours, hierarchy = cv2.findContours(dilatedImage, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    '''
    rawContours is a list of contours in the binary image (thresholdedImage and dilatedImage are binary images)
    A contour in a binary image is a border around where the white meets the black. We can now filter these
    to remove everything that also fit our threshold, so we are left only with the actual vision target.
    '''

    filteredContours = []

    for contour in rawContours:
        rectX, rectY, rectWidth, rectHeight = cv2.boundingRect(contour)
        contourArea = cv2.contourArea(contour)
        convexHull = cv2.convexHull(contour)

	# The "or not CONFIG['test']" line allows us to override the boolean in the config file
        yLevelTest = not CONFIG['y-levelTest'] or ft.ylevelTest(rectY, Y_TEST_THRESH)
        solidityTest =  not CONFIG['solidityTest']or ft.solidityTest(contourArea, convexHull, MIN_SOLIDITY)
        quadrilateralTest = not CONFIG['quadrilateralTest'] or ft.quadrilateralTest(contour, 11)

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
	if len(filteredContours) < 2:
		table.putBoolean('targetDetected', False)
	else:
		'''
		If we have more than two filtered contours, overwrite the filteredContours
		array with the largest two contours
		'''
		if len(filteredContours) > 2:
			largestContour = {'contour': filteredContours[0], 'area': cv2.contourArea(filteredContours[0])}
			largestContour2 = {'contour': filteredContours[1], 'area': cv2.contourArea(filteredContours[1])}

			for i in range(2, len(filteredContours)):
				area = cv2.contourArea(filteredContours[i])

				if area > largestContour['area']:
					largestContour = {'contour': filteredContours[i], 'area': area}
				elif area > largestContour2['area']:
					largestContour2 = {'contour': filteredContours[i], 'area': area}

			filteredContours = [largestContour, largestContour2]

		'''
		Now that we have ensured that there are only two found contours, we send
		relevant data to the roboRIO.
		'''
        x1, y1, width1, height1 = cv2.boundingRect(filteredContours[0])
        x2, y2, width2, height2 = cv2.boundingRect(filteredContours[1])

        # if x1 < x2:
        #     leftTarget = filteredContours[0]
        #     rightTarget = filteredContours[1]
        # else:
        #     leftTarget = filteredContours[1]
        #     rightTarget = filteredContours[0]

        distance = CONFIG['VT_HEIGHT'] * CONFIG['FOCAL_RANGE'] / ((height1 + height2) / 2)

		table.putBoolean('targetDetected', True)
        table.putNumber('xCoord', (x1 + x2) / 2)
        table.putNumber('distance', distance)
    ######################################################################
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

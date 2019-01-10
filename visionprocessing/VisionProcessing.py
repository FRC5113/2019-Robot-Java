import numpy as np
import cv2
from networktables import NetworkTablesInstance as nettab
import math
import logging
import copy
import FilterTests as ft

cap = cv2.VideoCapture(0)

# Constants
RESOLUTION = (cap.get(cv2.CAP_PROP_FRAME_WIDTH), cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
LOWER_THRESH = np.array([0, 0, 0])
UPPER_THRESH = np.array([360, 255, 255])

# Contour Test Constants
Y_THRESH = [RESOLUTION[1] / 2, 200] # (y-center, range) or should I make it (lower, upper)?
RECT_TEST_TOLERANCE = 200
VISION_TAPE_ANGLE = 14.5 * math.pi / 180 # radians
MIN_SOLIDITY = 0.9

# Network Tables
'''
nettab.setIPAddress('10.51.13.2')
nettab.setClientMode()
nettab.initialize(server='10.51.13.88')
table = nettab.getTable('contoursReport')
table.putNumber('x_resolution', RESOLUTION[0])
'''
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
cv2.createTrackbar('rect-tolerance', 'threshold_tester', RECT_TEST_TOLERANCE, 200, nothing)
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
    Y_THRESH[0] = cv2.getTrackbarPos('y-level[0]', 'threshold_tester')
    Y_THRESH[1] = cv2.getTrackbarPos('y-level[1]', 'threshold_tester')
    RECT_TEST_TOLERANCE = cv2.getTrackbarPos('rect-tolerance', 'threshold_tester')
    MIN_SOLIDITY = cv2.getTrackbarPos('solidity', 'threshold_tester') / 100
    ######################################################################

    ### Save new frame ###
    ret, frame = cap.read()

    ############## Process Image #########################################
    ### Threshold image ###
    thresholded_image = cv2.inRange(frame, LOWER_THRESH, UPPER_THRESH)

    ### Dilate image ###
    kernel = np.ones((5, 5), np.uint8)
    dilated_image = cv2.dilate(thresholded_image, kernel, iterations = 3) # change number of iterations to dilate further
    ######################################################################

    ############## Get & Filter Contours #################################
    ### Get contours ###
    contoursImage, rawContours, hierarchy = cv2.findContours(dilated_image, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    ### Filter contours ###
    filteredContours = []

    for contour in rawContours:
        rect_x, rect_y, rect_width, rect_height = cv2.boundingRect(contour)
        contour_area = cv2.contourArea(contour)
        convex_hull = cv2.convexHull(contour)

        ylevel_test = ft.ylevel_test(rect_y, Y_THRESH)
        solidity_test = ft.solidity_test(contour_area, convex_hull, MIN_SOLIDITY)
        rectangle_test = ft.rectangle_test(rect_width, rect_height, contour_area, RECT_TEST_TOLERANCE, VISION_TAPE_ANGLE)

        #if ylevel_test and solidity_test and rectangle_test:
        if ylevel_test:
            filteredContours.append(contour)
    ######################################################################

    ############## Show All Images #######################################
    contour_img = cv2.drawContours(frame, filteredContours, -1, (0, 0, 255), 1)

    cv2.imshow('thresholded', thresholded_image)
    cv2.imshow('dilated', dilated_image)
    cv2.imshow('contours', contour_img)
    ######################################################################

    ############## Send Contours #########################################
    # This is going to change, and rather than publishing all of
    # this info, we will instead send just: middle coordinate,
    # area, number of targets found (0, 1, or 2), and the
    # distance to the target.

    xs = []
    ys = []
    widths = []
    heights = []
    areas = []
    distances = []
    
    for countor in filteredContours:
        x, y, w, h = cv2.boundingRect(contour)
        area = cv2.contourArea(contour)

        xs.append(x)
        ys.append(y)
        widths.append(w)
        heights.append(h)
        areas.append(area)

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

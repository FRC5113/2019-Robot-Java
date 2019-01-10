import cv2
import math

def ylevel_test(rect_y, y_thresh):
    bounds = [y_thresh[0] - y_thresh[1] / 2, y_thresh[0] + y_thresh[1]]
    return rect_y >= bounds[0] and rect_y <= bounds[1]

def solidity_test(contour_area, convex_hull, min_solidity):
    hull_area = cv2.contourArea(convex_hull)
    return float(contour_area / hull_area) >= min_solidity

def area_test(contour_area, area_threshold):
    return contour_area >= area_threshold[0] and contour_area <= area_threshold[1]

def rectangle_test(rect_width, rect_height, contour_area, tolerance, angle=0):
    b, h = get_bh(rect_width, rect_height, angle)

    rect_area = b * h

    return math.fabs(contour_area - rect_area) < tolerance

def aspectratio_test(rect_width, rect_height, desired_ratio, tolerance, angle=0): # desired_ratio = width/height, e.g. 4:3 becomes 4/3
    b, h = get_bh(rect_width, rect_height, angle)

    ratio = b / h

    return math.fabs(ratio - desired_radio) < tolerance

def get_bh(rect_width, rect_height, angle):
    b = rect_height / math.cos(angle)
    h = rect_width * math.cos(angle)

    return b, h

'''
There is a mathematical way of doing this, 
which involves the resolution and fov of the
camera, and using a known real world distance
between two pieces of vision tape to determine
how far you are from it, but I am going to
instead get a table of values of areas of
contours and their corresponding distances from
the camera, and approximate a function from that.
'''
def get_distance(contour_area):
    pass

# # Kashyap Radadiya
# import cv2
# # import mediapipe
# import pyautogui

# screen_w , screen_h = pyautogui.size()

# #opens the camera
# cam = cv2.VideoCapture(0)
# if not cam.isOpened():
#     print("Camera not opened !!!")
#     exit()
# print("Camera opened !!!")

# #create face mesh 
# faceMesh = mediapipe.solutions.face_mesh.FaceMesh(refine_landmarks=True)
 
# while True:
#     #captures the frames
#     ret, frame = cam.read()
#     #flip the frame
#     frame = cv2.flip(frame,1)
#     if not ret:
#         print("Frame not captured !!!")
#         break

#     #convert frames to more trackable video formate and trace face
#     rgbFrame = cv2.cvtColor(frame,cv2.COLOR_BGR2RGB)
#     output = faceMesh.process(rgbFrame)
#     landmarkPoints = output.multi_face_landmarks
#     # print(landmarkPoints)

#     frame_h , frame_w , _ = frame.shape 
    
#     #track landmarks on the face
#     if landmarkPoints:
#         landmarks = landmarkPoints[0].landmark
#         #calculate distance in pixal numbers
#             #[474:478] that can track only eye not whole face
#         for id , landmark in enumerate(landmarks[474:478]):
#             x = int(landmark.x * frame_w)
#             y = int(landmark.y * frame_h)
#             cv2.circle(frame,(x,y),2,(0,255,0))

#             if id == 1:
#                 screen_x = int(landmark.x * screen_w)    
#                 screen_y = int(landmark.y * screen_h)
#                 #move mouse cursor
#                 pyautogui.moveTo(screen_x,screen_y)
        
#         #click with eye
#         left = [landmarks[145],landmarks[159]]
#         for landmark in left:
#             x = int(landmark.x * frame_w)
#             y = int(landmark.y * frame_h)
#             cv2.circle(frame,(x,y),2,(0,255,255))
        
#         if (left[0].y - left[1].y) < 0.009:
#             pyautogui.click()
#             pyautogui.sleep(1.5)

#     #shows the facescreen
#     cv2.imshow("camera",frame)
#     if cv2.waitKey(1) & 0xff == ord('q'):
#         break

import cv2
import pyautogui
import dlib  # You need to install dlib (pip install dlib)

# Initialize screen width and height for mouse movement
screen_w, screen_h = pyautogui.size()

# Initialize the camera
cam = cv2.VideoCapture(0)
if not cam.isOpened():
    print("Camera not opened !!!")
    exit()
print("Camera opened !!!")

# Load the pre-trained face detector and facial landmarks detector (from dlib)
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('shape_predictor_68_face_landmarks.dat')  # Download the predictor from http://dlib.net/files/shape_predictor_68_face_landmarks.dat.bz2

while True:
    # Capture frames
    ret, frame = cam.read()
    frame = cv2.flip(frame, 1)
    if not ret:
        print("Frame not captured !!!")
        break

    # Convert frame to grayscale (required for face detection)
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    
    # Detect faces in the frame
    faces = detector(gray)
    
    for face in faces:
        # Get landmarks for the face
        landmarks = predictor(gray, face)

        # Track landmarks for the eyes (landmarks for the eyes are typically around indices 36-41 for the left eye and 42-47 for the right eye)
        left_eye = []
        right_eye = []

        # Left eye landmarks (36 to 41)
        for i in range(36, 42):
            x = landmarks.part(i).x
            y = landmarks.part(i).y
            left_eye.append((x, y))
            cv2.circle(frame, (x, y), 2, (0, 255, 0), -1)

        # Right eye landmarks (42 to 47)
        for i in range(42, 48):
            x = landmarks.part(i).x
            y = landmarks.part(i).y
            right_eye.append((x, y))
            cv2.circle(frame, (x, y), 2, (0, 255, 0), -1)

        # Calculate the center of the left eye
        left_eye_center = (int(sum([point[0] for point in left_eye]) / len(left_eye)),
                           int(sum([point[1] for point in left_eye]) / len(left_eye))))

        # Calculate the center of the right eye
        right_eye_center = (int(sum([point[0] for point in right_eye]) / len(right_eye)),
                            int(sum([point[1] for point in right_eye]) / len(right_eye))))

        # Move the mouse cursor to the center of the left eye
        screen_x = int(left_eye_center[0] * screen_w / frame.shape[1])
        screen_y = int(left_eye_center[1] * screen_h / frame.shape[0])
        pyautogui.moveTo(screen_x, screen_y)

        # Optionally click when the eyes are closed (you can use a threshold for eye aspect ratio, but here it's simplified)
        if abs(left_eye_center[1] - right_eye_center[1]) < 5:  # Simplified condition
            pyautogui.click()
            pyautogui.sleep(1.5)

    # Show the video feed with face and eye landmarks
    cv2.imshow("camera", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release the camera and close the window
cam.release()
cv2.destroyAllWindows()

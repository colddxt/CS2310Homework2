import serial,playsound,time,cv2

serial_port = '/dev/cu.usbmodem1411';
baud_rate = 9600;
write_to_file_path = 'data1';
count = 0
temp = 0
state = 0
count_video = 0


output_file = open(write_to_file_path, "w+");
ser = serial.Serial(serial_port, baud_rate)


while True:
    line = ser.readline();
    line = line.decode("utf-8")
    output_file.write(line);
    print(line);
    #print type(line)
    first_int = line.split(" ")
    line = first_int[0]
    print(line);
    temp = temp + 1
    if line ==' ' or line == '':
        continue
    if int(line) <= 200:

        print 'Normal'        
        state = 0
        
    if int(line) > 200 and int(line) <= 400:
        count = count + 1
        print 'Smoke'
       # playsound.playsound('maoyan.m4a', True)
        
        cap = cv2.VideoCapture(0)
        time.sleep(1);
        ret, frame = cap.read()
        #cv2.imwrite("1.jpeg",frame)
        playsound.playsound('/Users/ronian/Documents/cs2610/1/Smoke.m4a', True)
        cv2.imwrite(write_to_file_path+"smoke"+str(count)+".jpeg",frame)
        cap.release()
        state = 1
        

    if int(line) > 400:
        count = count + 1
        print 'Fire'
        state = 2
        playsound.playsound('/Users/ronian/Documents/cs2610/1/Fire.m4a', True)
        camera = cv2.VideoCapture(0)
        fourcc = cv2.VideoWriter_fourcc('m','p','4','v')
        video_writer = cv2.VideoWriter(write_to_file_path+"fire"+str(count)+".m4v", fourcc, 15.0, (640, 480))
        camera.set(3,640)
        camera.set(4,480)
        count_video = 0
        while True:
            (grabbed, frame) = camera.read()  # grab the current frame
            frame = cv2.resize(frame, (640,480))
            video_writer.write(frame)  # Write the video to the file system
            count_video = count_video+1
            if count_video == 70:
                break
        camera.release()
        video_writer.release()



    #time.sleep(1)

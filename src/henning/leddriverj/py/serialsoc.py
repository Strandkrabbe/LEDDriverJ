import socket
import serial
import sys

def initSoc():
	s1 = socket.socket(socket.AF_INET6,socket.SOCK_STREAM)
	s1.bind(("::0",22444))
	s1.listen(1)
	return s1

def initSerial():
	port = "COM4"
	if len(sys.argv) > 1:
		port = sys.argv[1]
	s2 = serial.Serial(port=port,baudrate=115200,bytesize=serial.EIGHTBITS,stopbits=serial.STOPBITS_TWO,parity=serial.PARITY_NONE)
	if not s2.isOpen():
		s2.open()
	return s2

s1 = initSoc()
s2 = initSerial()
s3,addr = s1.accept()
print(str(addr) + " connected")
s3.setblocking(True)
active = True
while active:
	r = s3.recv(1024)
	s2.write(r)
	if len(r) == 0:
		active = False

print("Socket closed")
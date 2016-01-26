import RPi.GPIO as GPIO
import time
import sys

def turnOn(ledPin):
  GPIO.setmode(GPIO.BCM)
  GPIO.setwarnings(False)
  GPIO.setup(ledPin,GPIO.OUT)
  print "LED on"
  GPIO.output(ledPin,GPIO.HIGH)
  time.sleep(0.5)
  print "LED off"
  GPIO.output(ledPin,GPIO.LOW)


def main():
	arg = int(sys.argv[1])
	turnOn(arg)

if __name__ == "__main__":
	main()
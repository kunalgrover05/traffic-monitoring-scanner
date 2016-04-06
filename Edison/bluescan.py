import subprocess
import time
import os
import signal
blueScan = ["hcitool", "scan"]

while True:
        process = subprocess.Popen(blueScan, stdout=subprocess.PIPE)
        process.wait()
        output = process.communicate()[0]
        print output
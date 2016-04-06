import subprocess
import time
import os
import signal
bleScan = ["hcitool", "lescan"]
while True:
        process = subprocess.Popen(bleScan, stdout=subprocess.PIPE)

        time.sleep(10)
        os.kill(process.pid, signal.SIGINT)
        
        output = process.communicate()[0]
        print output
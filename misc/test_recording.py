# This script is a client that connects to a TCP server opened by FlightGear on port 5400, in order to send line by line from a CSV file and show flight recording.
# Copy the playback_small.xml file to the \data\Protocol folder inside the location where FlightGear is installed, if you haven't already.
# For example: C:\Program Files\FlightGear 2020.3\data\Protocol
# Open FlightGear and add the following line to Additional Settings before starting the simulator:
# --generic=socket,in,10,127.0.0.1,5400,tcp,playback_small
# Start the simulator. When it finishes loading, run this script.

import socket
import time

HOST = "127.0.0.1"
PORT = 5400

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    with open("output.csv", "rb") as f:
        l = f.read(1024)
        while (l):
            s.send(l)
            time.sleep(0.1)
            l = f.read(1024)
s.close()

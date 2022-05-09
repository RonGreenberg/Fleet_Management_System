# This script opens a TCP server on port 5400, waiting for connection from FlightGear to receive flight data and store it in a CSV file.
# Copy the playback_small.xml file to the \data\Protocol folder inside the location where FlightGear is installed, if you haven't already.
# For example: C:\Program Files\FlightGear 2020.3\data\Protocol
# Run this script first of all, before starting the simulator.
# Open FlightGear and add the following line to Additional Settings before starting the simulator:
# --generic=socket,out,10,127.0.0.1,5400,tcp,playback_small
# Start the simulator and fly the plane to export flight data (previous contents of output.csv are overwritten!).

import socket

HOST = "127.0.0.1"
PORT = 5400

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        print(f"Connected by {addr}")
        with open("output.csv", "wb") as o:
            while True:
                data = conn.recv(1024)
                if not data:
                    break
                o.write(data)
s.close()
                
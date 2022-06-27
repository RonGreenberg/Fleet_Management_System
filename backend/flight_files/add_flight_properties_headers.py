import os
def prepend_line(file_name, line):
    """ Insert given string as a new line at the beginning of a file """
    # define name of temporary dummy file
    dummy_file = file_name + '.csv'
    # open original file in read mode and dummy file in write mode
    with open(file_name, 'r') as read_obj, open(dummy_file, 'w') as write_obj:
        if read_obj.readline().strip() == line:
            write_obj.close()
            os.remove(dummy_file)
            return
        read_obj.seek(0)
        # Write given line to the dummy file
        write_obj.write(line + '\n')
        # Read lines from original file one by one and append them to the dummy file
        for line in read_obj:
            write_obj.write(line)
    # remove original file
    os.remove(file_name)
    # Rename dummy file as the original file
    os.rename(dummy_file, file_name)
    
files = [f for f in os.listdir('.') if os.path.isfile(f) and f not in ("add_flight_properties_headers.py", "flight_files.rar")]
for f in files:
    prepend_line(f, "aileron,elevator,rudder,flaps,slats,speedbrake,throttle,throttle,engine-pump,engine-pump,electric-pump,electric-pump,external-power,APU-generator,latitude-deg,longitude-deg,altitude-ft,roll-deg,pitch-deg,heading-deg,side-slip-deg,airspeed-kt,glideslope,vertical-speed-fps,airspeed-indicator_indicated-speed-kt,altimeter_indicated-altitude-ft,altimeter_pressure-alt-ft,attitude-indicator_indicated-pitch-deg,attitude-indicator_indicated-roll-deg,attitude-indicator_internal-pitch-deg,attitude-indicator_internal-roll-deg,encoder_indicated-altitude-ft,encoder_pressure-alt-ft,gps_indicated-altitude-ft,gps_indicated-ground-speed-kt,gps_indicated-vertical-speed,indicated-heading-deg,magnetic-compass_indicated-heading-deg,slip-skid-ball_indicated-slip-skid,turn-indicator_indicated-turn-rate,vertical-speed-indicator_indicated-speed-fpm,engine_rpm")
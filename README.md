# poi-to-discover-media
Convert POIs to VW Discover Media &amp; Discover Pro navigators

## Getting started

- Download the latest release

- Unzip it in a folder

- Execute in the command line

Linux/MacOS
```
./bin/poi-to-discover-media
```

Windows
```
./bin/poi-to-discover-media.bat
```

Example of execution:
```
./bin/poi-to-discover-media input="/Users/rayco/Downloads/garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip"

ls -la output

total 2016
drwxr-xr-x  5 user  staff      160 Sep  1 16:13 .
drwxr-xr-x  5 user  staff      160 Sep  1 16:13 ..
drwxr-xr-x  5 user  staff      160 Sep  1 16:13 PersonalPOI
-rw-r--r--  1 user  staff  1026155 Sep  1 16:13 Release-2018-09-01-1535811228541.zip
-rwxr-xr-x  1 user  staff     2744 Sep  1 16:13 metainfo2.txt
```

Now just copy the `PersonalPOI` folder and the `metainfo2.txt` file into
a USB device or SD and insert it into your car.
Also you could just unzip the `Release-XXXX-XX-XX-XXXXXXXX.zip` in the USB/SD device.

## Input formats supported

- Lupof.net Danger zones Europe CSV from [lupof.net](https://lufop.net/zones-de-danger-france-et-europe-asc-et-csv/)
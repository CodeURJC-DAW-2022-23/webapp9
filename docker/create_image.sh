#!/bin/bash

if test $# -gt 1
then
    echo "Too many arguments. Call the script with ./create_image.sh DockerHubUsrname/tripscanner-webapp"
    exit 1

elif test $# -eq 0
then
    echo "Not enough arguments. Call the script with ./create_image.sh DockerHubUsrname/tripscanner-webapp"
    exit 1

elif [ $1 == "help" ]
then
    clear
    echo  -e "-----------------------------------------------------------------------
|                             DESCRIPTION                             |
-----------------------------------------------------------------------
Shell script to create the docker image for the webapp TripScanner\n
-----------------------------------------------------------------------
|                           HOW TO LAUNCH                             |
-----------------------------------------------------------------------
Call the command with 
	./create_image.sh DockerHubUsrname/tripscanner-webapp\n
Where DockerHubUsrname is your dockerhub.com username.
	
To view this help page, call create_image.sh script as follows:
            ./create_image.sh help\n"
    exit 0

else
    docker build -f Dockerfile -t $1 ../TripScanner
    docker push $1
    exit 0
fi

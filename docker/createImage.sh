#!/bin/sh

if ! [ -x "$(command -v brickstrap)" ]; then
    echo "brickstrap is not installed. We need to install brickstrap to continue..."
    sudo add-apt-repository ppa:ev3dev/tools
    sudo apt update
    sudo apt install brickstrap
fi

#echo "Creating a docker image"
docker build . -t achiyae/ev3dev-ev3-bpjs

#echo "Creating a tar file from the docker image"
brickstrap create-tar achiyae/ev3dev-ev3-bpjs ev3dev-ev3-bpjs.tar

#echo "Creating an img file from the tar file"
brickstrap create-image ev3dev-ev3-bpjs.tar ev3dev-ev3-bpjs.img

echo
echo "Done! Use https://www.balena.io/etcher/ to flash the image to an SD card."
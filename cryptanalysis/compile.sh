#!/bin/bash

g++ -I/usr/local/include -g -O2 -Wall -L/usr/local/lib /usr/local/lib/libgpg-error.dylib /usr/local/lib/libgcrypt.dylib -o rainbow rainbow.cpp
#gcc -I/usr/local/include -g -O2 -Wall -L/usr/local/lib /usr/local/lib/libgpg-error.dylib /usr/local/lib/libgcrypt.dylib -o redux-md5 redux-md5.c

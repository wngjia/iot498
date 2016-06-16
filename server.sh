#!/bin/bash

rm -f debug.log
java -ea -cp .:iot498-1.0.0-fat.jar \
  iot498.MyApp server iot498.json

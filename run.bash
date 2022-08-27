#!/bin/bash

# argument parsing

DEBUG=0
MAIN_CLASS=fr.poulpogaz.jam.Main
N=0

while getopts dm: opt; do
  case "$opt" in
    d)
      DEBUG=1
      ((N++))
      ;;
    m)
      MAIN_CLASS="$OPTARG"
      ((N += 2))
      ;;
    \?)
      exit 1
  esac
done

shift $N

# compile and run

# mvn compile

PATH_TO_PROJECT=$('pwd')

CLASSPATH="$PATH_TO_PROJECT"/target/classes:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl/3.2.3/lwjgl-3.2.3-natives-linux.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-assimp/3.2.3/lwjgl-assimp-3.2.3.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-assimp/3.2.3/lwjgl-assimp-3.2.3-natives-linux.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-glfw/3.2.3/lwjgl-glfw-3.2.3-natives-linux.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-opengl/3.2.3/lwjgl-opengl-3.2.3-natives-linux.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-shaderc/3.2.3/lwjgl-shaderc-3.2.3.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-shaderc/3.2.3/lwjgl-shaderc-3.2.3-natives-linux.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-stb/3.2.3/lwjgl-stb-3.2.3.jar:\
"$HOME"/.m2/repository/org/lwjgl/lwjgl-stb/3.2.3/lwjgl-stb-3.2.3-natives-linux.jar:\
"$HOME"/.m2/repository/org/joml/joml/1.10.4/joml-1.10.4.jar:\
"$HOME"/.m2/repository/org/apache/logging/log4j/log4j-core/2.18.0/log4j-core-2.18.0.jar:\
"$HOME"/.m2/repository/org/apache/logging/log4j/log4j-api/2.18.0/log4j-api-2.18.0.jar:\
"$HOME"/.m2/repository/io/github/poulpogaz/json/1.2.2/json-1.2.2.jar

JVM_ARGS="-Dfile.encoding=UTF-8 -classpath ${CLASSPATH} ${MAIN_CLASS}"

echo $JVM_ARGS $*

# word splitting is very important
if [ $DEBUG -ne 0 ]
then
  java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 $JVM_ARGS $*
else
  java $JVM_ARGS $*
fi
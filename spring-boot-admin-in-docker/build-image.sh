#!/bin/bash

echo '============================================================='
echo '$                                                           $'
echo '$                      liumapp                              $'
echo '$                                                           $'
echo '$                                                           $'
echo '$  email:    liumapp.com@gmail.com                          $'
echo '$  homePage: http://www.liumapp.com                         $'
echo '$  Github:   https://github.com/liumapp                     $'
echo '$                                                           $'
echo '============================================================='
echo '.'

mvn clean install -Dmaven.test.skip=true

cd admin-client

mvn clean package -Dmaven.test.skip=true docker:build

cd ..

cd admin-eureka

mvn clean package -Dmaven.test.skip=true docker:build

cd ..

cd admin-server

mvn clean package -Dmaven.test.skip=true docker:build

cd ..

echo 'success'





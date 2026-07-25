#!/bin/bash

JAR=$(ls /home/ec2-user/app/*.jar | head -1)

cp "$JAR" /home/ec2-user/app/app.jar

sudo systemctl restart springboot

chmod +x scripts/*.sh
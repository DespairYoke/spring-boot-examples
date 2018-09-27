# spring-boot-admin-in-docker
在docker中部署一个由spring boot admin管理的服务集群 

Deploying a service cluster managed by spring boot admin in docker

[Spring Boot admin document](http://codecentric.github.io/spring-boot-admin/2.0.2/#getting-started)

[使用文档](https://github.com/liumapp/spring-boot-admin-in-docker/wiki)

## How to use

### Using Docker

update both application.yml in admin-server and admin-client


    spring:
         profiles:
             active: docker
             
run 

    ./build-image.sh
    
to install docker images

run 

    docker-compose up -d
    
wait a few seconds , and open your browser , and visit http://localhost:8766

the username is : admin

the password is : adminadmin

you can change it by update application.yml

to stop the docker container , run the following command:

    docker-compose down
    
to remove the docker images , run the following command:

    ./rm-image.sh                                

### Using IDEA

update both application.yml in admin-server and admin-client

    spring:
         profiles:
             active: dev
             
start admin-eureka first , then admin-server and admin-client

open your browser and visit http://localhost:8766

done.   







                      
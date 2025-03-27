FROM openjdk:17
WORKDIR /app
COPY target/auth-service-1.0.jar auth-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "auth-service.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]
# Start H2 in TCP mode
CMD java -cp auth-service.jar org.h2.tools.Server -tcp -tcpAllowOthers -ifNotExists & \
    java -jar auth-service.jar


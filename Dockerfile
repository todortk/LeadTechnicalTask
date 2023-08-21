FROM gradle:jdk20 as builder
# ARG CACHEBUST=0
COPY --chown=gradle:gradle . ./

RUN chmod +x ./gradlew

# use gradle wrapper (properties from /gradle/wrapper/gradle-wrapper.properties)
# upgrade gradle version with: ./gradlew wrapper --gradle-version <NEW VERSION>
# run Build and skip tests in "Build and Deploy" case
RUN ./gradlew build -x test

FROM openjdk:20-ea-20-jdk-slim
WORKDIR /app
COPY --from=builder /home/gradle/build/libs/lead-0.0.1-SNAPSHOT.jar .
EXPOSE 5192
ENTRYPOINT ["java","-jar","lead-0.0.1-SNAPSHOT.jar"]
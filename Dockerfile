# Step 1: Use the OpenJDK image as the base image
FROM openjdk:11

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the built JAR file into the container
# Replace 'your-application.jar' with the path to your actual JAR file
COPY ./build/libs/com.bobbyesp.spotifylyricsapi-all.jar /app/application.jar

# Step 4: Expose the port your application runs on
# Replace '8080' with your application's port
EXPOSE 8080

# Step 5: Run the application
CMD ["java", "-jar", "/app/application.jar"]
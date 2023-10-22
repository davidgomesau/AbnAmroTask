# AbnAmroTask
Interview task for ABN Amro

Author: David Gomes

## Executing the program
There are several ways to execute the program but probably the easiest is to use a Docker container

1. Build the Docker image (make sure the Docker daemon or Docker Desktop is running): `./mvnw spring-boot:build-image`
2. Verify that the image exists: `docker images` (an image called abnamro-task should be there)
3. Run the Docker container: `docker run -it -p 8080:8080 abnamro-task:0.0.1-SNAPSHOT`

To verify it works we can run a curl command against the endpoint:
`curl -v -F file=@Input.txt http://localhost:8080/api/upload -o Output.csv`

In this example, the Output.csv file will contain the result of processing the Input.txt file (based on requirements)

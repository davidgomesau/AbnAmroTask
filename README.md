# AbnAmroTask
Interview task for ABN Amro

Author: David Gomes

## Executing the backend
There are several ways to execute the program but probably the easiest is to use a Docker container

1. Build the Docker image (make sure the Docker daemon or Docker Desktop is running): `./mvnw spring-boot:build-image`
2. Verify that the image exists: `docker images` (an image called abnamro-task should be there)
3. Run the Docker container: `docker run -it -p 8080:8080 abnamro-task:0.0.1-SNAPSHOT`

To verify it works we can run a curl command against the endpoint:
`curl -v -F file=@Input.txt http://localhost:8080/api/upload -o Output.csv`

In this example, the Output.csv file will contain the result of processing the Input.txt file (based on requirements)

## Executing the frontend
There is a README file inside src/ui/file-upload-app that explains how to start the frontend

## Starting everything at once
If the images have already been built, then the easiest way to start all the elements is by using docker-compose:
`docker-compose up -d`

## Things I would do differently with more time
- Add more tests to the backend.
- Depending on requirements, I would use Spring Batch to process the file. This would allow us to save the job status
and also allow us to use FixedLengthTokenizer and FlatFileItemReader.
- Use routing within the Angular app.
- Better presentation for the Angular app (you can tell backend is my strength :smile:)
- Frontend and backend would live separately. I just thought it would be convenient to have them together for the
purpose of this exercise.
- The transactionMapper seems like it needs a refactoring. I don't like the fact that the fields are fixed inside this
file. This might be related to the Spring Batch point.
- "Productify" the container creation. This would take us closer to k8s.
- Maybe something else I'm missing. A codebase can always be improved, right?
# FileUploadApp

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.7.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Deploying

Create the docker image:
`docker build -t file-upload-app:latest .`

Run the container:
`docker run -it -p 4200:4200 file-upload-app:latest`
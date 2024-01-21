# video-streaming

This is a video streaming API.

The main purpose of the application is to:
* Store information related to videos
* Stream video content
* Keeps track of user engagement actions related to videos

## Decision

### Table definition

I have decided to create `Video` table and `EngagementEvent` table.
`Video` table is to manage information about video content, while `EngagementEvent` table tracks user engagement activities (such as views and impressions).

![Table definition](table-definition.png)

### File(Video) upload

This time, due to the time constraints, I have decided to upload a file to the local directory. Ideally, file uploads should be handled using cloud-based storage services (like Amazon S3).

Uploading large files directly through an API can be time-consuming and may lead to timeouts or performance issues. Therefore, asynchronous processing such as a queue system can be utilized in the future.

## Assumption

### Publish a video API

The API to publish a video accepts a video file and return the saved content.
The uploaded file is stored in the directory in this repository.

#### Example

```
% curl -X POST -F "file=@./{file-path}" http://localhost:8080/api/v1/videos

{"videoId":1,"title":null,"synopsis":null,"director":null,"yearOfRelease":null,"genre":null,"actor":null,"runningTime":null,"contentLink":"{file-path}","createdAt":"2024-01-21T09:15:04.605","updatedAt":"2024-01-21T09:15:04.605","engagementEventsCount":null,"deleted":false}
```

## Instructions on how to compile and run the solution

1. Set up the database

Run the following command

```
video-streaming % docker compose up -d
```

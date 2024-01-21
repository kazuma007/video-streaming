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

## Assumption

## Instructions on how to compile and run the solution

1. Set up the database

Run the following command

```
video-streaming % docker compose up -d
```

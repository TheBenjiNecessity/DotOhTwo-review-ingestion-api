# Review Ingestion API

Spring Boot service for ingesting reviews and replies, persisting them to Cassandra, and publishing review events to Kafka.

## Requirements

- Docker & Docker Compose
- Java 21 + Maven (for local builds)

## Running

### With local Kafka

Starts Cassandra and Kafka together:

```bash
docker compose --profile kafka up --build
```

### With external Kafka

Use this when Kafka is already running from the `DotOhTwo_springboot_restapi` project:

```bash
docker compose -f docker-compose.yml -f docker-compose.external-kafka.yml up --build
```

The external Kafka must be running before starting this service.

## API

Base URL: `http://localhost:8081`

### Create a review

```
POST /reviews
```

```json
{
    "productId": "46c4e6d6-c921-4f34-8a14-a1567ab8a1ca",
    "authorId": "63a91fe8-0164-4649-b8fb-301c75d5e925",
    "rating": 5,
    "content": "This is a review"
}
```

### Reply to a review

```
POST /reviews/{reviewId}/replies
```

```json
{
    "authorId": "63a91fe8-0164-4649-b8fb-301c75d5e925",
    "content": "This is a reply"
}
```

### Reply to a reply

```
POST /replies/{replyId}/replies
```

```json
{
    "authorId": "63a91fe8-0164-4649-b8fb-301c75d5e925",
    "content": "This is a reply"
}
```

## Events

When a review is created, a `ReviewCreatedEvent` is published to the `reviews` Kafka topic.

## AWS Deployment

The service is built and pushed to ECR via AWS CodeBuild using `buildspec.yml`. The following environment variables must be set in the CodeBuild project:

| Variable | Description |
|---|---|
| `AWS_DEFAULT_REGION` | AWS region |
| `AWS_ACCOUNT_ID` | AWS account ID |
| `IMAGE_REPO_NAME` | ECR repository name |
| `IMAGE_TAG` | Image tag (e.g. `latest`) |

The active Spring profile is controlled by the `SPRING_PROFILES_ACTIVE` environment variable on the container.

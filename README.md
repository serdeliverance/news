# Challenge

## Stack

- Scala
- Cats Effect (Fs2, Skunk, Redis4Cats)
- Postgres
- Redis
- Hexagonal Architecture

## Instructions

1. Run dockers:

```
docker-compose up
```

2. Run the app:

``` scala
sbt core/run
```

It will startup the app on `localhost:8080`

#### TODO

- implemente business logic
    - add logs
    - add unit tests
- graphql config with http4s
- add pure config

## Comments/Discussion (with myself)
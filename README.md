# Challenge

## Stack

- `Scala`
- `Cats Effect` (`Fs2`, `Skunk`, `Redis4Cats`)
- `Postgres`
- `Redis`
- `Hexagonal Architecture`

## Architecture overview

The application follows the `Hexagonal Architecture` (`Ports and Adapters` pattern) approach, having our `Domain` at core of the application layering and ony being accessed by the `Application`. Also we have different ports and adapters that allows the communication with the outside components in both directions (from outside to inside and viceversa).

![Alt text](diagrams/architecture.png?raw=true "Architecture")

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

- add bulk insert with `Skunk`
- add more logs
- add unit tests
- improve logging tracking info (nowaways it shows `sdev.Main`, instead of current service classes)
- graphql config with http4s
- add pure config
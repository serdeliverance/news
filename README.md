# News

The objetive of this app is to expose an `API` that retrieves info from [The NyTime's Website](https://www.nytimes.com/).

## Stack

- `Scala`
- `Cats Effect` (`Fs2`, `Skunk`, `Redis4Cats`)
- `Postgres`
- `Redis`
- `Hexagonal Architecture`

## Architecture overview

The application follows the `Hexagonal Architecture` (`Ports and Adapters` pattern) approach, having our `Domain` at core of the application layering and ony being accessed by the `Application`. Also we have different ports and adapters that allows the communication with the outside components in both directions (from outside to inside and viceversa).

![Alt text](diagrams/architecture.png?raw=true "Architecture")

`Disclaimer`: I know, maybe `hexagonal architecture` is an overkill for this problem. However, using this approach allows flexibility to add new adapters without chaging the `domain` and `application` layer (for example, when I added `GraphQL`). Also, it allows lot of flexility for refactoring. For example, at the beginning I start using `Skunk` for the `database` layer. At the end I refactored to use `Quill` instead and the transition was transparent for the inner layers.

## Instructions

1. Run dockers:

```
docker-compose up
```

2. Run the app:

``` scala
sbt run
```

It will startup the app on `localhost:8080`

## Endpoints

The endpoints exposed by the `API` are on [this file](requests.http)

## Some words about Styling

I tried to use `Hexagonal Architecture` in an `OOP` way but because of habit. For example, the following snippet:

``` scala
class CacheServiceImpl[F[_]: Logger](redisCommands: RedisCommands[F, String, String], config: CacheConfig) extends CacheService[F] {
    // logic
}
```

Could be implemented in a more `Scala FP` way:

``` scala
object CacheService {
    def apply[F[_]: Logger](redisCommands: RedisCommands[F, String, String], config: CacheConfig) =
        new CacheService[F] {
            ???
        }
}
```

But it is just a matter of style.

#### TODO

- graphql config with http4s
- add bulk insert with `Skunk` (refactor to add `Quill` instead)
- add unit tests
- add more logs

#### TODO / Improvements
- add docker artifact generation
- add api endpoints versioning
- improve logging tracking info (nowaways it shows `sdev.Main`, instead of current service classes)
- add github actions
- add kubernetes
- add scalasteward
- fix `scalaFix` command

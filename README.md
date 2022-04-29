# News

The objetive of this app is to expose an `API` that retrieves info from [The NyTime's Website](https://www.nytimes.com/).

## Stack

- `Scala`
- `Cats Effect` (`Http4s`, `Doobie`, `Redis4Cats`, `PureConfig`)
- `Sangria`
- `Quill`
- `Postgres`
- `Redis`
- `Hexagonal Architecture`

## Architecture overview

The application follows the `Hexagonal Architecture` (`Ports and Adapters` pattern) approach, having our `Domain` at core of the application layering and ony being accessed by the `Application`. Also we have different ports and adapters that allows the communication with the outside components in both directions (from outside to inside and viceversa).

![Alt text](diagrams/architecture.png?raw=true "Architecture")

`Just to mention`, I added `Redis` for caching news in order to avoid going every time to the `NyTimes` web site. In case of not being news in cache, a [fallback strategy](src/main/scala/io/github/sdev/application/GetNewsUseCaseService.scala) was implemented in order to go and scrap the web site. The key expiration is also [configurable](src/main/resources/application.conf).

`Disclaimer`: I know, maybe `hexagonal architecture` is an overkill for this problem. However, using this approach allows me to have flexibility when add new adapters without changing the `domain` and `application` layer (for example, when I added `GraphQL`). Also, it allows lot of flexility for refactoring. For example, at the beginning I start using `Skunk` for the `database` layer. At the end I refactored to use `Quill` instead and the transition was transparent for the inner layers.

## Instructions

1. Run dockers:

```
docker-compose up
```

2. Run the app:

``` scala
sbt run
```

It will startup the app on `localhost:8080`.

Also, you can visualize the data stored in the db checking out the `Adminer`, who is running on `localhost:8083`.

## Endpoints

The endpoints exposed by the `API` are on [this file](requests.http)

## Testing

Just the `application` layer [was tested](src/test/scala/io/github/sdev/application/GetNewsUseCaseServiceSpec.scala) via `unit test`. Maybe, some `it tests` could be made using `Test Containers` as an improvement of the `test coverage`.

## Some words about Styling

I tried to use `Hexagonal Architecture` in an `OOP` way but because of habit. For example, the following snippet:

``` scala
class CacheServiceImpl[F[_]: Logger](redisCommands: RedisCommands[F, String, String], config: CacheConfig) extends CacheService[F] {
    // logic
}
```

Is used instead of a more `value` oriented `Scala FP approach` like the following:

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

- `log4cats` is not working since some `sangria` and `quill` stuff was added. It needs some fixes.
- add error handling and http response codes in case of errors

#### Future Improvements
- add scalafix
- add scalasteward
- add github actions
- migrate to `Scala 3`
- some refactors in [NewsRepositoryImpl](src/main/scala/io/github/sdev/adapter/out/persistence/NewsRepositoryImpl.scala)
- removing warning related with versions of `log4j` versions
- add more logs
- add integration tests with test containers
- add docker artifact generation
- add kubernetes
- add api endpoints versioning
- improve logging tracking info (nowaways it shows `sdev.Main`, instead of current service classes)

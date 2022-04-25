# Challenge

## Approach

- hexagonal architecture
- TDD

## Disclaimer

I use `IO` directly because I hate parametric effects.

#### TODO

- business logic:
    - add logs
    - add unit tests
    - implement cache (redis) layer
- graphql config with http4s

## less priority
- (refactor) add tagless final
- adding tests

## Comments/Discussion (with myself)

- In [Main](./core/src/main/scala/io/github/sdev/Main.scala), I'm creating different resources (`postgres`, `redis clients`, etc) and lifted them into a monadic context trough `pure[F]`. that way, I can continue creating resource in the `for-comprehension`. I saw in [Practical Functiona Programming book]() that `Gabriel` creates an `AppResources` class where he creates all the resources as a hole and wraps then inside and parametric effect (the `AppResources` class). Evaluate if this approach is better than the one that I applied.
- Ask for help with server instantiation. To learn best practices for setting app the server on Main function.
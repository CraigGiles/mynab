# Arrow
Arrow is a framework that I've been toying with, heavily inspired by [Twitter Finagle](https://github.com/twitter/finagle)

The basics of the framework is the ability to use Service and Filter. Some of the main differences between Finagle and Arrow is Arrow is based on the Kleisli Arrow from Cats and not hard coded to a specific effect in the service.

In order to use a service:

```scala
  import cats.effect.IO
  final class EchoService extends Service[IO, String, String] {
    override def run(req: String): IO[String] = IO.pure(req)
  }

  final class ToUpperService extends Service[IO, String, String] {
    override def run(req: String): IO[String] = IO.pure(req.toUpperCase)
  }

  val echo = new EchoService()
  val upper = new ToUpperService()
  val pipeline: Kleisli[IO, String, String] = upper andThen echo
```

The same holds true for the filters! … Lets use a `Future` for this effect instead of `IO`

```scala
  final class IntToStringFilter extends Filter[Future, Int, String, String, String] {
    override def run(
      value: Int,
      service: Service[Future, String, String]
    ): Future[String] = service(value.toString)
  }

  final class AddEmphasisFilter extends SimpleFilter[Future, String, String] {
    override def run(
      str: String,
      service: Service[Future, String, String]
    ): Future[String] = service(str.concat("!"))
  }

  final class EchoService extends Service[Future, String, String] {
    override def run(req: String): Future[String] = Future.successful(req)
  }

  val intToString = new IntToStringFilter()
  val addEmphasis = new AddEmphasisFilter()
  val echo = new EchoService()
  val expected = "42!"
  val number = 42

  val pipeline: Kleisli[Future, Int, String] =
      intToString andThen addEmphasis andThen echo

  val result: Future[String] = pipeline(number)
```


More to come, I'm working on this as I develop the ambition. It's not really production ready or anything since there is no production code actively using it.




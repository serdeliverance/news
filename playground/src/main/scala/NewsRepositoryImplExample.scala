import cats.effect.IOApp
import cats.effect.kernel.Resource
import cats.effect.IO
import skunk.Session
import natchez.Trace.Implicits.noop
import io.github.sdev.adapter.out.persistence.NewsRepositoryImpl
import cats.effect.ExitCode
import io.github.sdev.scraper.News

object NewsRepositoryImplExample extends IOApp.Simple {

  val sessions: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 45432,
      user = "root",
      database = "news",
      password = Some("root")
    )

  val repo = new NewsRepositoryImpl(sessions)

  val run = {
    for {
      // _    <- repo.save(News("google", "https://google.com/v2"))
      // _    <- repo.save(News("google again", "https://google.com"))
      news <- repo.findAll()
      _    <- IO.println(news)
    } yield ()
  }.as(ExitCode.Success)
}

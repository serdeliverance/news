import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.all._
import natchez.Trace.Implicits.noop

object SkunkExample extends IOApp {

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 45432,
      user = "root",
      database = "news",
      password = Some("root")
    )

  def run(args: List[String]) =
    session.use { s =>
      for {
        d <- s.unique(sql"select current_date".query(date))
        _ <- IO.println(s"The current date is $d")
      } yield ExitCode.Success
    }

  val singleColumnQuery: Query[Void, String] =
    sql"SELECT name from country".query(varchar)
}

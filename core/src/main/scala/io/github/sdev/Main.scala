package example.io.github.sdev

import cats.effect.IOApp
import io.github.sdev.adapter.in.web.HttpServer
import cats.effect.ExitCode
import cats.effect.IO

object Main extends IOApp {
  def run(args: List[String]) =
    HttpServer.make[IO].compile.drain.as(ExitCode.Success)
}

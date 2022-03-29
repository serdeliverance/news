package example.io.github.sdev

import cats.effect.IOApp
import io.github.sdev.adapter.web.HttpServer
import cats.effect.ExitCode

object Main extends IOApp {
  def run(args: List[String]) =
    HttpServer.make.compile.drain.as(ExitCode.Success)
}

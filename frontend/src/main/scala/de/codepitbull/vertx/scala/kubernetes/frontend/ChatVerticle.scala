package de.codepitbull.vertx.scala.kubernetes.frontend

import java.util.Calendar

import io.vertx.core.json.JsonObject
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.bridge.PermittedOptions
import io.vertx.scala.ext.web.handler.StaticHandler
import io.vertx.scala.ext.web.handler.sockjs.{BridgeOptions, SockJSHandler}
import io.vertx.scala.ext.web.{Router, RoutingContext}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ChatVerticle extends ScalaVerticle{
  val Port = 8667


  override def startFuture(): Future[_] = {
    val router = Router.router(vertx)
    router.get("/react/alive").handler((r: RoutingContext) => r.response.end("I AM ALIVE"))
    router.route("/react/static/*").handler(StaticHandler.create("webroot-react"))
    router.route("/react/eventbus/*").handler(sockJsHandler)
    val sender = vertx.eventBus.publisher[JsonObject]("browser")
    vertx.eventBus
      .consumer[String]("server")
        .bodyStream()
      .handler((msg: String) =>
        vertx.eventBus().sendFuture[String]("profanitycheck", msg).onComplete {
          case Success(answer)  => sender.write(Json.obj("msg" -> answer.body, "date" -> Calendar.getInstance.getTime.toString))
          case Failure(_)       => sender.write(Json.obj("msg" ->  "----BIG BROTHER ISN'T WATCHING----", "date" -> Calendar.getInstance.getTime.toString))
        })

    vertx.createHttpServer.requestHandler(router.accept _).listenFuture(config.getInteger("port", Port))
  }

  def sockJsHandler: SockJSHandler = {
    val sockJSHandler = SockJSHandler.create(vertx)
    val options = BridgeOptions()
      .addOutboundPermitted(PermittedOptions().setAddress("browser"))
      .addInboundPermitted(PermittedOptions().setAddress("server"))
    sockJSHandler.bridge(options)
    sockJSHandler
  }
}

object ChatVerticle extends App {
  val vertx = Vertx.vertx()
  vertx.deployVerticle(nameForVerticle[ChatVerticle])
}

package de.codepitbull.vertx.scala.kubernetes.backend

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.lang.scala.json.Json
import io.vertx.scala.config.{ConfigRetriever, ConfigRetrieverOptions, ConfigStoreOptions}

import scala.concurrent.Future

class ProfanityFilterVerticle extends ScalaVerticle{

  private var blockList = Set.empty[String]

  override def startFuture(): Future[_] = {

    val store = ConfigStoreOptions().setType("configmap").setConfig(Json.obj("namespace" -> "default", "name" -> "profanity-config"))

    ConfigRetriever
      .create(vertx, ConfigRetrieverOptions().addStore(store))
      .configStream().handler(config => {
      Option(config.getString("profanity"))
        .foreach(profanity => blockList = profanity.split(" ").toSet)
    })

    vertx.eventBus().consumer[String]("profanitycheck")
      .handler(possibleProfanity =>
        if(blockList.intersect(possibleProfanity.body().toLowerCase.split(",").toSet).isEmpty)
          possibleProfanity.reply(s"${possibleProfanity.body()} [checked by ${hashCode()}]")
        else
          possibleProfanity.reply(s"[CENSORED] [checked by ${hashCode()}]")
      )
      .completionFuture()
  }
}

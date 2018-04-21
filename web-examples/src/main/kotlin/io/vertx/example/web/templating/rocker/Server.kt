package io.vertx.example.web.templating.rocker

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.RockerTemplateEngine

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // Populate context with data
    router.route().handler({ ctx ->
      ctx.put("title", "Vert.x Web Example Using Rocker")
      ctx.put("name", "Rocker")
      ctx.next()
    })

    // Render a custom template.
    router.route().handler(TemplateHandler.create(RockerTemplateEngine.create()))

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}

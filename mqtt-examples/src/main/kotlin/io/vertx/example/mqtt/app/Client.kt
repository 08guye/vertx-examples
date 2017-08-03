package io.vertx.example.mqtt.app

import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.mqtt.*
import io.vertx.mqtt.MqttClient
import io.vertx.mqtt.MqttClientOptions

class Client : io.vertx.core.AbstractVerticle()  {
  var SERVER_PORT = 1883
  var MQTT_MESSAGE = "Hello Vert.x MQTT Client"
  var MQTT_TOPIC = "/my_topic"
  var SERVER_HOST = "0.0.0.0"
  override fun start() {
    var options = MqttClientOptions(
      port = SERVER_PORT,
      host = SERVER_HOST,
      keepAliveTimeSeconds = 2)

    var client = MqttClient.create(Vertx.vertx(), options)


    // handler will be called when we have a message in topic we subscribing for
    client.publishHandler({ publish ->
      println("Just received message on [${publish.topicName()}] payload [${publish.payload().toString(java.nio.charset.Charset.defaultCharset())}] with QoS [${publish.qosLevel()}]")
    })

    // handle response on subscribe request
    client.subscribeCompleteHandler({ h ->
      println("Receive SUBACK from server with granted QoS : ${h.grantedQoSLevels()}")

      // let's publish a message to the subscribed topic
      client.publish(MQTT_TOPIC, Buffer.buffer(MQTT_MESSAGE), MqttQoS.AT_MOST_ONCE, false, false, { s ->
        println("Publish sent to a server")
      })

      // unsubscribe from receiving messages for earlier subscribed topic
      vertx.setTimer(5000, { l ->
        client.unsubscribe(MQTT_TOPIC)
      })
    })

    // handle response on unsubscribe request
    client.unsubscribeCompleteHandler({ h ->
      println("Receive UNSUBACK from server")
      vertx.setTimer(5000, { l ->
        client.disconnect({ d ->
          println("Disconnected form server")
        })
      })
    })

    // connect to a server
    client.connect({ ch ->
      if (ch.succeeded()) {
        println("Connected to a server")
        client.subscribe(MQTT_TOPIC, 0)
      } else {
        println("Failed to connect to a server")
        println(ch.cause())
      }
    })
  }
}

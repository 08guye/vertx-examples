package io.vertx.example.mqtt.simple

import io.netty.handler.codec.mqtt.MqttQoS
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.mqtt.*
import io.vertx.mqtt.MqttClient
import io.vertx.mqtt.MqttClientOptions

class Client : io.vertx.core.AbstractVerticle()  {
  var MQTT_MESSAGE = "Hello Vert.x MQTT Client"
  var BROKER_HOST = "localhost"
  var BROKER_PORT = 1883
  var MQTT_TOPIC = "/my_topic"
  override fun start() {
    var options = MqttClientOptions(
      port = BROKER_PORT,
      host = BROKER_HOST)

    var mqttClient = MqttClient.create(vertx, options)

    mqttClient.connect({ ch ->
      if (ch.succeeded()) {
        println("Connected to a server")

        mqttClient.publish(MQTT_TOPIC, Buffer.buffer(MQTT_MESSAGE), MqttQoS.AT_MOST_ONCE, false, false, { s ->
          mqttClient.disconnect({ d ->
            println("Disconnected from server")
          })
        })
      } else {
        println("Failed to connect to a server")
        println(ch.cause())
      }
    })
  }
}

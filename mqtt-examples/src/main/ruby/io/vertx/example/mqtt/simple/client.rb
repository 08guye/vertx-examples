require 'vertx-mqtt-server/mqtt_client'
require 'vertx/buffer'
@SERVER_PORT = 1883
@MQTT_MESSAGE = "Hello Vert.x MQTT Client"
@MQTT_TOPIC = "/my_topic"
@SERVER_HOST = "0.0.0.0"
options = {
  'port' => @SERVER_PORT,
  'host' => @SERVER_HOST
}

mqttClient = VertxMqttServer::MqttClient.create($vertx, options)

mqttClient.connect() { |ch_err,ch|
  if (ch_err == nil)
    puts "Connected to a server"

    mqttClient.publish(@MQTT_TOPIC, Vertx::Buffer.buffer(@MQTT_MESSAGE), :AT_MOST_ONCE, false, false) { |s_err,s|
      mqttClient.disconnect() { |d_err,d|
        puts "Disconnected from server"
      }
    }
  else
    puts "Failed to connect to a server"
    puts ch_err
  end
}

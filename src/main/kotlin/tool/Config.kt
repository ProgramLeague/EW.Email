package tool

import org.json.JSONObject
import org.json.JSONTokener
import org.simplejavamail.mailer.config.TransportStrategy
import util.EmailAddressConfig
import util.EmailHost
import util.EmailHostProtocol
import java.nio.file.Files
import java.nio.file.Paths

object Config {
	private val OBJECT =
			JSONTokener(Files.newBufferedReader(Paths.get("config.json"))).nextValue() as JSONObject
	val EMAIL_HOST_CONFIG = parseEmailHostConfig()

	val DEBUG = false

	private fun parseEmailHostConfig(): EmailAddressConfig {
		val host = OBJECT.getJSONObject("email")
		return EmailAddressConfig(
				host.getString("name"),
				host.getString("address"),
				host.getString("password"),
				EmailHost(
						let {
							val jsonObject = OBJECT.getJSONObject("sender")
							EmailHost.Sender(
									jsonObject.getString("host"),
									jsonObject.getInt("port"),
									EmailHostProtocol.valueOf(jsonObject.getString("protocol").toUpperCase()),
									TransportStrategy.valueOf(jsonObject.getString("transport_strategy").toUpperCase()))
						},
						let {
							val jsonObject = OBJECT.getJSONObject("receiver")
							EmailHost.Receiver(
									jsonObject.getString("host"),
									jsonObject.getInt("port"),
									EmailHostProtocol.valueOf(jsonObject.getString("protocol").toUpperCase()),
									jsonObject.getBoolean("ssl"))
						})
		)
	}
}
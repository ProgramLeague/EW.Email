package tool

import org.json.JSONObject
import org.json.JSONTokener
import org.simplejavamail.email.Recipient
import org.simplejavamail.mailer.config.TransportStrategy
import util.*
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import javax.mail.Message

object Config {
	private val OBJECT = JSONTokener(Files.newBufferedReader(Paths.get("config.json"))).nextValue() as JSONObject

	val EMAIL_HOST_CONFIG = parseEmailHostConfig()
	val DEBUG = OBJECT.getBoolean("debug")
	val DEBUG_RECEIVED_MAIL_SET =
			ReceivedEmailSet(
					1,
					1,
					listOf(
							ReceivedEmail(
									1,
									"ip",
									listOf(Recipient("Ray-Eldath", "ray.eldath@gmail.com", Message.RecipientType.TO)),
									12,
									LocalDate.now(),
									false,
									"")
					))

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
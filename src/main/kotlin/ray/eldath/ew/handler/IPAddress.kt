package ray.eldath.ew.handler

import org.json.JSONObject
import org.json.JSONTokener
import ray.eldath.ew.core.Sender
import ray.eldath.ew.util.ReceivedEmail
import java.net.URL

object IPAddress : Handler {

	override fun handle(receivedEmail: ReceivedEmail, sender: Sender) =
			receivedEmail.reply(sender, "IP address", queryIPAddress())

	private fun queryIPAddress(): String =
			(JSONTokener(URL("https://api.ipify.org/?format=json")
					.openStream()).nextValue() as JSONObject).getString("ip")

	override fun titleRegex(): Regex = Regex("ip")
}
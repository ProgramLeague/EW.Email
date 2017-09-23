package handler

import core.Sender
import org.json.JSONObject
import org.json.JSONTokener
import util.ReceivedEmail
import java.net.URL
import java.util.regex.Pattern

object IPAddress : Handler {

	override fun handle(receivedEmail: ReceivedEmail, sender: Sender) {
		receivedEmail.reply(sender, "IP address", queryIPAddress())
	}

	private fun queryIPAddress(): String {
		return (JSONTokener(URL("https://api.ipify.org/?format=json")
				.openStream()).nextValue() as JSONObject).getString("ip")
	}

	override fun titleRegex(): Pattern = Pattern.compile("ip")
}
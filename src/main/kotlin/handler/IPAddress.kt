package handler

import core.Sender
import util.ReceivedEmail
import java.util.regex.Pattern

object IPAddress : Handler {
	override fun handle(receivedEmail: ReceivedEmail, sender: Sender) {
	}

	override fun titleRegex(): Pattern = Pattern.compile("ip")
}
package handler

import core.Sender
import util.ReceivedEmail

interface Handler {
	fun handle(receivedEmail: ReceivedEmail, sender: Sender)

	fun titleRegex(): Regex
}
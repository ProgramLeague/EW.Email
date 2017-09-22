package handler

import core.Sender
import util.ReceivedEmail
import java.util.regex.Pattern

interface Handler {
	fun handle(receivedEmail: ReceivedEmail, sender: Sender)

	fun titleRegex(): Pattern
}
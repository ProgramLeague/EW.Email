package ray.eldath.ew.handler

import ray.eldath.ew.core.Sender
import ray.eldath.ew.util.ReceivedEmail

interface Handler {
	fun handle(receivedEmail: ReceivedEmail, sender: Sender)

	fun titleRegex(): Regex
}
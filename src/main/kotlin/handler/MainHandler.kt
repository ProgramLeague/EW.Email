package handler

import core.Pool
import org.slf4j.LoggerFactory
import tool.Config
import util.ReceivedEmail

fun <T> Collection<T>.containsAny(input: Collection<T>): Boolean {
	for (thisI in input)
		if (this.contains(thisI))
			return true
	return false
}

object MainHandler {
	private val LOGGER = LoggerFactory.getLogger(MainHandler.javaClass)
	private val allHandler = arrayListOf<Handler>(IPAddress)

	fun handle(receivedEmail: ReceivedEmail) {
		if (!Config.PERMITTED_ADDRESSES.containsAny(receivedEmail.from))
			return

		val subject = receivedEmail.subject
		for (handler in allHandler)
			if (subject.matches(handler.titleRegex())) {
				handler.handle(receivedEmail, Pool.SENDER)
				LOGGER.info("email with subject `$subject` handled by handler ${handler.javaClass.name}")
				Pool.RECEIVER.delete(receivedEmail)
				LOGGER.info("handled email deleted")
				return
			}
	}
}
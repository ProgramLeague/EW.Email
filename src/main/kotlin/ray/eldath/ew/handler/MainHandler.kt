package ray.eldath.ew.handler

import org.slf4j.LoggerFactory
import ray.eldath.ew.core.Pool
import ray.eldath.ew.tool.Config
import ray.eldath.ew.util.ReceivedEmail

fun <T> Collection<T>.containsAny(input: Collection<T>): Boolean = input.any { this.contains(it) }

object MainHandler {
	private val LOGGER = LoggerFactory.getLogger(MainHandler.javaClass)
	private val allHandler = arrayListOf(IPAddress, Wake) //TODO register ray.eldath.ew.handler here

	fun handle(receivedEmail: ReceivedEmail) {
		LOGGER.debug("\thandling ${if (receivedEmail.seen) "seen" else "unseen"} ray.eldath.ew.mail No.${receivedEmail.id}")
		if (!Config.PERMITTED_ADDRESSES.containsAny(receivedEmail.from.map { it.address }))
			return

		val subject = receivedEmail.subject
		LOGGER.debug("\tray.eldath.ew.mail subject: $subject")
		for (handler in allHandler) {
			if (subject.trim().matches(handler.titleRegex())) {
				handler.handle(receivedEmail, Pool.SENDER)
				LOGGER.info("email with subject `$subject` handled by ray.eldath.ew.handler ${handler.javaClass.name}")
				Pool.RECEIVER.delete(receivedEmail)
				LOGGER.info("handled email deleted")
				return
			}
		}
	}
}
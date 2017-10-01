package ray.eldath.ew.handler

import org.slf4j.LoggerFactory
import ray.eldath.ew.core.Pool
import ray.eldath.ew.tool.Config
import ray.eldath.ew.util.ReceivedEmail

fun <T> Collection<T>.containsAny(input: Collection<T>): Boolean = input.any { this.contains(it) }

object MainHandler {
	private val LOGGER = LoggerFactory.getLogger(MainHandler.javaClass)
	private val allHandler = arrayListOf(IPAddress, Wake, Heartbeat) //TODO register ray.eldath.ew.handler here

	fun handle(receivedEmail: ReceivedEmail) {
		LOGGER.debug("\thandling ${if (receivedEmail.seen) "seen" else "unseen"} mail No.${receivedEmail.id}")
		if (!Config.PERMITTED_ADDRESSES.containsAny(receivedEmail.from.map { it.address }))
			return

		val subject = receivedEmail.subject
		LOGGER.debug("\tmail subject: $subject")
		if (subject.contains("&")) {
			LOGGER.info("handling multi-commend mail...")
			val split = subject.split("&")
			split
					.filterNot { it.trim().isEmpty() }
					.forEach { handleT(receivedEmail.copy(it.trim())) }
		}
		handleT(receivedEmail)
		Pool.RECEIVER.delete(receivedEmail)
		LOGGER.info("handled email deleted")
	}

	private fun handleT(receivedEmail: ReceivedEmail) {
		val subject = receivedEmail.subject
		for (handler in allHandler) {
			if (subject.trim().matches(handler.titleRegex())) {
				handler.handle(receivedEmail, Pool.SENDER)
				LOGGER.info("email with subject `$subject` handled by handler ${handler.javaClass.name}")
				return
			}
		}
	}
}
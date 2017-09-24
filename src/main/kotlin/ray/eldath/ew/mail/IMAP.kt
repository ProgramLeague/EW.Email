package ray.eldath.ew.mail

import ray.eldath.ew.util.ReceivedEmail
import ray.eldath.ew.util.ReceivedEmailSet

class IMAP(
		private val server: String, private val port: Int,
		private val username: String, private val password: String) : ReceiveEmail {

	private val share = Share(server, port, username, password)

	override fun receive(ssl: Boolean): ReceivedEmailSet = share.receive("imap", ssl)

	override fun receive(): ReceivedEmailSet = receive(false)

	override fun delete(receivedEmail: ReceivedEmail) = share.delete(receivedEmail)

	override fun close() = share.close()
}
package ray.eldath.ew.mail

import ray.eldath.ew.util.ReceivedEmail
import ray.eldath.ew.util.ReceivedEmailSet

class IMAP(server: String, port: Int, username: String, password: String, ssl: Boolean) : ReceiveEmail {

	private val share = Share("imap", server, port, username, password, ssl)

	override fun receive(): ReceivedEmailSet = share.receive()

	override fun delete(receivedEmail: ReceivedEmail) = share.delete(receivedEmail)

	override fun close() = share.close()
}
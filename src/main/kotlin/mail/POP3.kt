package mail

import util.ReceivedEmail
import util.ReceivedEmailSet

class POP3(private val server: String, private val port: Int,
           private val username: String, private val password: String) : ReceiveEmail {

	private val share = Share(server, port, username, password)

	override fun receive(ssl: Boolean): ReceivedEmailSet = share.receive("pop3", ssl)

	override fun receive(): ReceivedEmailSet = receive(false)


	override fun delete(receivedEmail: ReceivedEmail) = share.delete(receivedEmail)


	override fun close() = share.close()
}
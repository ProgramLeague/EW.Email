package core

import mail.*
import org.simplejavamail.email.Email
import org.simplejavamail.mailer.config.TransportStrategy
import tool.Config.DEBUG
import tool.Config.DEBUG_RECEIVED_MAIL_SET
import tool.Config.EMAIL_HOST_CONFIG
import util.EmailHostProtocol
import util.ReceivedEmailSet
import java.security.InvalidParameterException

object Pool {

	private val SENDER_CONFIG = EMAIL_HOST_CONFIG.emailHost.sender
	private val RECEIVER_CONFIG = EMAIL_HOST_CONFIG.emailHost.receiver

	val SENDER = Sender(SMTP(
			SENDER_CONFIG.host,
			SENDER_CONFIG.port,
			EMAIL_HOST_CONFIG.address,
			EMAIL_HOST_CONFIG.password
	), SENDER_CONFIG.transportStrategy)

	val RECEIVER = handleReceiver(
			RECEIVER_CONFIG.host,
			RECEIVER_CONFIG.port,
			EMAIL_HOST_CONFIG.address,
			EMAIL_HOST_CONFIG.password
	)

	private fun handleReceiver(server: String, port: Int, username: String, password: String): Receiver =
			when {
				RECEIVER_CONFIG.protocol == EmailHostProtocol.IMAP ->
					Receiver(IMAP(server, port, username, password), RECEIVER_CONFIG.ssl)
				RECEIVER_CONFIG.protocol == EmailHostProtocol.POP3 ->
					Receiver(POP3(server, port, username, password), RECEIVER_CONFIG.ssl)
				else -> throw InvalidParameterException("unsupported protocol")
			}
}

class Sender(private val sendEmail: SendEmail, private val transportStrategy: TransportStrategy) {
	fun send(email: Email) =
			if (DEBUG) Displayer.displayEmail(System.out, email)
			else sendEmail.send(email, transportStrategy)
	//TODO 要不要就EmailFromDecoder给JavaMail提PR？？？？
}

class Receiver(private val receiveEmail: ReceiveEmail, private val ssl: Boolean) {
	fun receive(): ReceivedEmailSet = if (DEBUG) DEBUG_RECEIVED_MAIL_SET else receiveEmail.receive(ssl)
}
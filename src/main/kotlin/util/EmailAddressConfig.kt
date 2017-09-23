package util

import org.simplejavamail.mailer.config.TransportStrategy

class EmailHost(val sender: Sender, val receiver: Receiver) {
	class Sender(val host: String, val port: Int, val protocol: EmailHostProtocol, val transportStrategy: TransportStrategy)

	class Receiver(val host: String, val port: Int, val protocol: EmailHostProtocol, val ssl: Boolean)
}

enum class EmailHostProtocol {
	SMTP, IMAP, POP3;
}

class EmailAddressConfig(val name: String, val address: String, val password: String, val emailHost: EmailHost)
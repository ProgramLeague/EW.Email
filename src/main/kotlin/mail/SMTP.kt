package mail

import org.simplejavamail.email.Email
import org.simplejavamail.mailer.Mailer
import org.simplejavamail.mailer.config.ServerConfig
import org.simplejavamail.mailer.config.TransportStrategy


class SMTP(private val server: String, private val port: Int,
           private val username: String, private val password: String) : SendEmail {

	override fun send(email: Email, transportStrategy: TransportStrategy) =
			Mailer(ServerConfig(server, port, username, password), transportStrategy).sendMail(email)

	override fun send(email: Email) = send(email, TransportStrategy.SMTP_PLAIN)
}
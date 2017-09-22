package core

import mail.SMTP
import mail.SendEmail
import org.simplejavamail.email.Email
import org.simplejavamail.mailer.config.TransportStrategy

object Pool {
	val SENDER = Sender(SMTP("", 989, "", ""), TransportStrategy.SMTP_TLS)
}

class Sender(private val sendEmail: SendEmail, private val transportStrategy: TransportStrategy) {
	fun send(email: Email) = sendEmail.send(email, transportStrategy)
}
package ray.eldath.ew.mail

import org.simplejavamail.email.Email
import org.simplejavamail.mailer.config.TransportStrategy
import ray.eldath.ew.util.ReceivedEmail
import ray.eldath.ew.util.ReceivedEmailSet
import java.io.Closeable

interface ReceiveEmail : Closeable {
	fun receive(): ReceivedEmailSet

	fun delete(receivedEmail: ReceivedEmail)

	override fun close()
}

interface SendEmail {
	fun send(email: Email)

	fun send(email: Email, transportStrategy: TransportStrategy)
}
package util

import org.simplejavamail.email.Recipient
import java.time.LocalDate

class ReceivedEmail(val id: Int, val subject: String, val from: Collection<Recipient>, val size: Int,
                    val sentDate: LocalDate, val seen: Boolean, val content: String) {
	override fun toString(): String =
			"SUBJECT: \"$subject\"\nFROM: $from\nSIZE: $size\nSENT DATE: $sentDate\nSEEN: $seen\nCONTENT: $content"
}

class ReceivedEmailSet(val allMessageCount: Int, val unreadMessageCount: Int, val messages: List<ReceivedEmail>)
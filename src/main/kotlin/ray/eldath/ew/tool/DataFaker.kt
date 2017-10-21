package ray.eldath.ew.tool

import org.simplejavamail.email.Recipient
import ray.eldath.ew.util.ReceivedEmail
import ray.eldath.ew.util.ReceivedEmailSet
import java.time.LocalDate
import javax.mail.Message

object DataFaker {
	private var id = 0

	fun fakeReceivedEmail(contentAndSubject: String): ReceivedEmail =
			ReceivedEmail(
					id++,
					contentAndSubject,
					listOf(Recipient("Debug 测试人员", "debug@debug.com", Message.RecipientType.TO)),
					5,
					LocalDate.now(),
					false,
					contentAndSubject)

	fun fakeReceivedEmailSet(list: List<ReceivedEmail>): ReceivedEmailSet = ReceivedEmailSet(list.size, list.size, list)
}
package ray.eldath.ew.handler

import ray.eldath.ew.core.Sender
import ray.eldath.ew.tool.Config
import ray.eldath.ew.util.ReceivedEmail
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Heartbeat : Handler {
	override fun handle(receivedEmail: ReceivedEmail, sender: Sender) =
			receivedEmail.reply(sender, "Still alive", "EmailEverything still alive at ${convert(LocalDateTime.now())}.")

	private fun convert(localDateTime: LocalDateTime): String =
			ZonedDateTime.of(localDateTime,
					ZoneId.of(Config.getString("zone_id")))
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss '['VV']'"))

	override fun titleRegex(): Regex = Regex("heartbeat|alive")
}
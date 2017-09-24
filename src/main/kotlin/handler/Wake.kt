package handler

import core.Sender
import org.slf4j.LoggerFactory
import tool.Config
import util.ReceivedEmail
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.regex.Pattern

object Wake : Handler {
	private val LOGGER = LoggerFactory.getLogger(this.javaClass)

	override fun handle(receivedEmail: ReceivedEmail, sender: Sender) {
		val name = receivedEmail.subject.replace("wake ", "")
		val target = parseTarget(name)
		if (target == null) {
			receivedEmail.reply(sender, "Fatal error", "No target named $name.")
			LOGGER.info("handle failed. no target named $name")
			return
		}
		LOGGER.info("waking device ${target.name} - ${target.ip}[${target.mac}]...")
		try {
			WakeNow.wake(target.mac, target.ip, target.port)
		} catch (exception: IllegalArgumentException) {
			LOGGER.error("invalid MAC address, exited")
			return
		}
		LOGGER.info("magic package sent to ${target.name} successfully")
		receivedEmail.reply(
				sender,
				"Magic package sent",
				"Magic package sent to ${target.name} - ${target.ip}[${target.mac}] successfully"
		)
	}

	override fun titleRegex(): Regex = Regex("wake .+")

	private class TargetEntry(val name: String, val mac: String, val ip: String, val port: Int)

	/**
	 * Mostly copied from <a href=https://github.com/mafrosis/Wake-On-Lan/blob/master/src/net/mafro/android/wakeonlan/MagicPacket.java>MagicPacket.java</a>
	 */
	private object WakeNow {
		private const val SEPARATOR = ':'

		fun wake(mac: String, ip: String, port: Int): String {
			// validate MAC and chop into array
			val hex = validateMac(mac)
			// convert to base16 bytes
			val macBytes = ByteArray(6)
			for (i in 0..5)
				macBytes[i] = Integer.parseInt(hex[i], 16).toByte()
			val bytes = ByteArray(102)
			// fill first 6 bytes
			for (i in 0..5)
				bytes[i] = 0xff.toByte()
			// fill remaining bytes with target MAC
			var i = 6
			while (i < bytes.size) {
				System.arraycopy(macBytes, 0, bytes, i, macBytes.size)
				i += macBytes.size
			}
			// create socket to IP
			val address = InetAddress.getByName(ip)
			val packet = DatagramPacket(bytes, bytes.size, address, port)
			val socket = DatagramSocket()
			socket.send(packet)
			socket.close()
			return hex[0] + SEPARATOR + hex[1] + SEPARATOR + hex[2] + SEPARATOR + hex[3] + SEPARATOR + hex[4] + SEPARATOR + hex[5]
		}

		// like C6:19:58:D8:73:3F
		private fun validateMac(mac: String): Array<String> {
			var mac1 = mac
			// error handle semi colons
			mac1 = mac1.replace(";", ":").replace("-", ":")
			// attempt to assist the user a little
			var newMac = ""
			if (mac1.matches("([a-zA-Z0-9]){12}".toRegex())) {
				// expand 12 chars into a valid mac address
				for (i in 0..mac1.length - 1) {
					if (i > 1 && i % 2 == 0)
						newMac += ":"
					newMac += mac1[i]
				}
			} else
				newMac = mac1
			// regexp pattern match a valid MAC address
			val pat = Pattern.compile("((([0-9a-fA-F]){2}[-:]){5}([0-9a-fA-F]){2})")
			val m = pat.matcher(newMac)

			if (m.find()) {
				val result = m.group()
				return result.split("([:-])".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
			} else
				throw IllegalArgumentException("invalid MAC address")
		}
	}

	private fun parseTarget(name: String): TargetEntry? {
		val config = Config.getHandlerConfig("Wake").getJSONArray("target")
		for (i in 0 until config.length()) {
			val target = config.getJSONObject(i)
			val nameT = target.getString("name")
			if (name.contentEquals(nameT))
				return TargetEntry(
						nameT,
						target.getString("mac"),
						target.getString("ip"),
						if (target.has("port")) target.getInt("port") else 9
				)
		}
		return null
	}
}
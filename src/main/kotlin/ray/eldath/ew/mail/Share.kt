package ray.eldath.ew.mail

import org.simplejavamail.email.Recipient
import ray.eldath.ew.tool.EmailFromDecoder
import ray.eldath.ew.util.ReceivedEmail
import ray.eldath.ew.util.ReceivedEmailSet
import java.io.Closeable
import java.security.Security
import java.time.ZoneId
import java.util.*
import javax.mail.*
import kotlin.collections.ArrayList


class Share(protocol: String, server: String, port: Int,
            private val username: String, private val password: String, ssl: Boolean) : Closeable {

	companion object {
		private const val DEBUG = false
	}

	private val prop = Properties()

	private lateinit var store: Store
	private lateinit var folder: Folder
	private lateinit var messages: Array<Message>

	init {
		prop.put("mail.store.protocol", protocol)
		prop.put("mail.$protocol.host", server)
		prop.put("mail.$protocol.port", port)
		if (ssl) {
			Security.addProvider(com.sun.net.ssl.internal.ssl.Provider())
			prop.put("mail.$protocol.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
			prop.put("mail.$protocol.socketFactory.fallback", false)
			prop.put("mail.$protocol.socketFactory.port", port)
			prop.put("mail.$protocol.auth", true)
		}
	}

	fun receive(): ReceivedEmailSet {
		val session = Session.getDefaultInstance(prop)
		session.debug = DEBUG
		store = session.store
		store.connect(username, password)
		folder = store.getFolder("INBOX")  // 收件箱
		folder.open(Folder.READ_WRITE)
		// 获取总邮件数
		val all = folder.messageCount
		// 得到收件箱文件夹信息，获取邮件列表
		val unread = folder.unreadMessageCount
		messages = folder.messages
		return ReceivedEmailSet(all, unread, parseMessage(messages))
	}

	fun delete(mail: ReceivedEmail) = messages[mail.id].setFlag(Flags.Flag.DELETED, true)

	private fun parseMessage(messages: Array<Message>): List<ReceivedEmail> {
		val result: ArrayList<ReceivedEmail> = ArrayList()
		for (index in messages.indices) {
			val it = messages[index]
			val itFrom: Array<Address> = it.from
			val from = ArrayList<Recipient>()

			for (thisFrom in itFrom) {
				val string = thisFrom.toString().trim().replace(" ", "")
				val nameT = string.substringBefore('<')
				val name = EmailFromDecoder.decode(nameT)
				val addressT = string.removePrefix(nameT)
				val address = addressT.substring(1, addressT.length - 1)
				// =?utf-8?Q?=E5=9B=BE=E7=81=B5=E7=94=B5=E5=AD=90=E4=B9=A6?= <ebook@turingbook.com>
				// Twitter <info@twitter.com>
				from.add(Recipient(name, address, Message.RecipientType.TO))
			}
			result.add(ReceivedEmail(index, it.subject, from, it.size,
					it.sentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
					it.flags.contains(Flags.Flag.SEEN), it.content.toString()))
		}
		return result
	}

	override fun close() {
		folder.close(true)
		store.close()
	}
}
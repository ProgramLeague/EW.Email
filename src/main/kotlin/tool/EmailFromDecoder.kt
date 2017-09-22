package tool

import org.apache.commons.codec.net.QuotedPrintableCodec
import java.nio.charset.Charset
import java.util.*

interface IExample {
	fun doSomething()
	fun doSomething1()
	fun `($)`()
}

class Example : IExample {
	override fun doSomething() {
	}

	override fun doSomething1() {
	}

	override fun `($)`() {
	}

	// 哇
	fun `2333`() {
	}
}

object EmailFromDecoder {
	@JvmStatic
	fun decode(input: String): String {
		if (!(input.startsWith("=?") && input.endsWith("?=")))
			return input
		val split = input.substring(2, input.length - 2).split("?")
		val charset = Charset.forName(split[0])
		val encoding = split[1][0]
		val data = split[2]
		return String(
				if (encoding == 'Q')
					QuotedPrintableCodec.decodeQuotedPrintable(data.toByteArray(charset))
				else
					Base64.getDecoder().decode(data.toByteArray(charset)), charset
		)
	}
}
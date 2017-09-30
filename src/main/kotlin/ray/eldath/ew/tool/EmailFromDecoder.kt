package ray.eldath.ew.tool

import org.apache.commons.codec.net.QuotedPrintableCodec
import java.nio.charset.Charset
import java.util.*

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
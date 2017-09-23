package tool

import org.junit.Test

class TestClass {
	companion object Constants {
		private const val DATA = "=?utf-8?Q?=E5=9B=BE=E7=81=B5=E7=94=B5=E5=AD=90=E4=B9=A6?= <ebook@turingbook.com>"
	}

	@Test
	fun runTest() {
		val string = DATA.trim().replace(" ", "")
		val nameT = string.substringBefore('<')
		val name = EmailFromDecoder.decode(nameT)
		val addressT = string.removePrefix(nameT)
		val address = addressT.substring(1, addressT.length - 1)
		println("$name, $address")
	}
}


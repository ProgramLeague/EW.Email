package tool

import org.junit.Assert
import org.junit.Test

class TestClass {
	companion object Constants {
		private const val DATA = "=?utf-8?Q?=E5=9B=BE=E7=81=B5=E7=94=B5=E5=AD=90=E4=B9=A6?="
	}

	@Test
	fun runTest() = Assert.assertEquals("图灵电子书", EmailFromDecoder.decode(DATA))
}


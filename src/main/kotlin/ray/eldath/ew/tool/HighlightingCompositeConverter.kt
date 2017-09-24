package ray.eldath.ew.tool

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants.*
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

/**
 * Refer to [Configure Jansi highlight color](http://slf4j.42922.n3.nabble.com/Configure-Jansi-highlight-color-td4025744.html#a4025746)
 *
 * @author Daniel Felix Ferber
 */
class HighlightingCompositeConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent?): String {
		val level = event!!.level
		return when (level.toInt()) {
			Level.ERROR_INT -> BOLD + RED_FG
			Level.WARN_INT -> YELLOW_FG
			Level.INFO_INT -> CYAN_FG
			else -> DEFAULT_FG
		}
	}
}
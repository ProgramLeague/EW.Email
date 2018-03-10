package ray.eldath.ew.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

class HighLightConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent): String {
		return when (event.level.toInt()) {
			Level.ERROR_INT -> ANSIConstants.BOLD + ANSIConstants.RED_FG
			Level.WARN_INT -> ANSIConstants.RED_FG
			else -> ANSIConstants.DEFAULT_FG
		}
	}
}

class LoggerHighLightConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent): String {
		return when (event.level.toInt()) {
			Level.WARN_INT -> ANSIConstants.CYAN_FG
			Level.ERROR_INT -> ANSIConstants.BOLD + ANSIConstants.CYAN_FG
			else -> ANSIConstants.WHITE_FG
		}
	}
}
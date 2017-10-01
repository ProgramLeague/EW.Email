package ray.eldath.ew.tool

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase
import org.simplejavamail.mailer.config.TransportStrategy


/**
 * Mostly copied from [Logback Custom Highlighting Color](https://github.com/shuwada/logback-custom-color)
 *
 * @author Hiroshi Wada
 */
class HighlightingCompositeConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
	override fun getForegroundColorCode(event: ILoggingEvent): String {
		val level = event.level
		return when (level.toInt()) {
			Level.ERROR_INT -> ANSIConstants.BOLD + ANSIConstants.RED_FG // same as default color scheme
			Level.WARN_INT -> ANSIConstants.BOLD + ANSIConstants.YELLOW_FG // same as default color scheme
			Level.INFO_INT -> ANSIConstants.CYAN_FG // use CYAN instead of BLUE
			else -> ANSIConstants.DEFAULT_FG
		}
		TransportStrategy.SMTP_PLAIN
	}
}
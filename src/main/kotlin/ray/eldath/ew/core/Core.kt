package ray.eldath.ew.core

import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.slf4j.LoggerFactory
import ray.eldath.ew.handler.MainHandler
import ray.eldath.ew.tool.Config
import ray.eldath.ew.tool.Constants
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

object Core {
	private val LOGGER = LoggerFactory.getLogger(this.javaClass)

	@JvmStatic
	fun main(vararg args: String) {
		AnsiConsole.systemInstall()
		println(ansi().eraseScreen().fgBrightYellow()
				.a("Welcome to EmailEverything - v${Constants.VERSION}${if (Config.DEBUG) " - DEBUG is on" else ""}").reset())
		println("System initializing...")
		val service = ScheduledThreadPoolExecutor(3)
		service.scheduleAtFixedRate(
				ReceiveAndHandle(),
				5,
				if (Config.DEBUG) 10 else Config.getInteger("update_frequency").toLong(),
				TimeUnit.SECONDS
		)
		LOGGER.info("scheduled thread executor initialed")
		val config = Config.EMAIL_HOST_CONFIG
		println("System initialized. Now listening email from ${config.name} <${config.address}>...")
	}

	class ReceiveAndHandle : Runnable {

		private val LOGGER = LoggerFactory.getLogger(this.javaClass)

		override fun run() {
			val list = Pool.RECEIVER.receive()
			if (list.unreadMessageCount == 0) {
				LOGGER.info("no unread message, exited")
				return
			}
			LOGGER.info("received ${list.allMessageCount} mails, handling...")
			for (email in list.messages)
				MainHandler.handle(email)
			LOGGER.info("all mails handled")
			Pool.RECEIVER.close()
		}
	}
}
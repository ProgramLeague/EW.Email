package ray.eldath.ew.core

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import org.slf4j.LoggerFactory
import ray.eldath.ew.handler.MainHandler
import ray.eldath.ew.tool.Config
import ray.eldath.ew.tool.Constants
import java.io.InputStream
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


object Core {
	private val LOGGER = LoggerFactory.getLogger(this.javaClass)

	@JvmStatic
	fun main(vararg args: String) {
		if (Config.DAEMON)
			loadLogbackConfigFile(this.javaClass.getResourceAsStream("/logback-daemon.xml"))
		else {
			println("Welcome to EW.Email - v${Constants.VERSION}${if (Config.DEBUG) " - DEBUG is on" else ""}")
			println("Running in ${Config.CURRENT_PATH}.")
			println("System initializing...")
		}
		val service = ScheduledThreadPoolExecutor(3)
		service.scheduleAtFixedRate(
				ReceiveAndHandle(),
				5,
				if (Config.DEBUG) 10 else Config.getInteger("update_frequency").toLong(),
				TimeUnit.SECONDS
		)
		LOGGER.info("scheduled thread executor initialed")
		val config = Config.EMAIL_HOST_CONFIG
		LOGGER.info("System initialized. Now listening email from ${config.name} <${config.address}>...")
	}

	class ReceiveAndHandle : Runnable {

		private val logger = LoggerFactory.getLogger(this.javaClass)

		override fun run() {
			val list = Pool.RECEIVER.receive()
			if (list.unreadMessageCount == 0) {
				logger.info("no unread message, exited")
				return
			}
			logger.info("received ${list.allMessageCount} mails, handling...")
			for (email in list.messages)
				MainHandler.handle(email)
			logger.info("all mails handled")
			Pool.RECEIVER.close()
		}
	}

	private fun loadLogbackConfigFile(externalConfigFileStream: InputStream) {
		val lc = LoggerFactory.getILoggerFactory() as LoggerContext
		val configurator = JoranConfigurator()
		configurator.context = lc
		lc.reset()
		configurator.doConfigure(externalConfigFileStream)
	}
}
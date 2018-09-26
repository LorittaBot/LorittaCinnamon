package net.perfectdreams.loritta.platform.twitter

import net.perfectdreams.loritta.Loritta
import net.perfectdreams.loritta.commands.images.*
import net.perfectdreams.loritta.commands.misc.PingCommand
import net.perfectdreams.loritta.platform.PlatformType
import net.perfectdreams.loritta.platform.twitter.command.TwitterCommandManager
import net.perfectdreams.loritta.platform.twitter.entity.TwitterMessage
import net.perfectdreams.loritta.platform.twitter.entity.TwitterUser
import twitter4j.*
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import java.io.File
import kotlin.concurrent.thread

class TwitterLoritta : Loritta(PlatformType.TWITTER) {
	companion object {
		const val TWITTER_USER_ID = 963434985310048257
		lateinit var TWITTER: Twitter
	}

	init {
		val token = File("twitter_token.txt").readLines()
		val consumerKey = token[0]
		val consumerSecret = token[1]
		val accessToken = token[2]
		val accessTokenSecret = token[3]

		this.commandManager = TwitterCommandManager()

		commandManager.register(PingCommand())
		commandManager.register(RomeroBrittoCommand())
		commandManager.register(SAMCommand())
		commandManager.register(AmizadeCommand())
		commandManager.register(AtaCommand())
		commandManager.register(ContentAwareScaleCommand())

		val cb = ConfigurationBuilder()
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret)

		val tf = TwitterFactory(cb.build())
		val twitter = tf.instance
		TWITTER = twitter

		val twitterStream = TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
		twitterStream.oAuthAccessToken = AccessToken(accessToken, accessTokenSecret);
		twitterStream.addListener(object: StatusListener {
			override fun onException(p0: Exception) {
				p0.printStackTrace()
			}

			override fun onTrackLimitationNotice(p0: Int) {
			}

			override fun onStallWarning(p0: StallWarning?) {
			}

			override fun onDeletionNotice(p0: StatusDeletionNotice?) {
			}

			override fun onStatus(p0: Status) {
				if (p0.user.id == TWITTER_USER_ID)
					return

				println(p0.user.screenName + " - " + p0.text)

				val split = p0.text.split(" ").toMutableList()
				val newArgumentList = split.toMutableList()
				var lastMentionedUser: UserMentionEntity? = null
				val userMentionEntities = p0.userMentionEntities.toMutableList()

				for (idx in 0 until split.size) {
					val argument = split[idx]

					if (!argument.startsWith("@"))
						break

					val user = p0.userMentionEntities.firstOrNull { argument == "@" + it.screenName }

					if (user != null) {
						userMentionEntities.remove(user)
						lastMentionedUser = user
						newArgumentList.removeAt(0)
					}
				}

				val firstMention = lastMentionedUser
				println("Last mentioned user is $lastMentionedUser")

				if (firstMention != null && firstMention.id == TWITTER_USER_ID) {
					val author = TwitterUser(p0.user)
					val message = TwitterMessage(author, newArgumentList.joinToString(" "), p0, userMentionEntities.toTypedArray())

					commandManager.process(author, message)
				}
			}

			override fun onScrubGeo(p0: Long, p1: Long) {
			}

		})

		val tweetFilterQuery = FilterQuery()
		tweetFilterQuery.track("LorittaBot")
		twitterStream.filter(tweetFilterQuery)

		thread {
			while (true) {
				Thread.sleep(2500)
			}
		}
	}
}
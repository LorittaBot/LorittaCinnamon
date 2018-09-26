package net.perfectdreams.loritta.platform.twitter.command

import net.perfectdreams.loritta.api.command.CommandContext
import net.perfectdreams.loritta.api.entity.Message
import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.api.network.RequestAction
import net.perfectdreams.loritta.platform.twitter.TwitterLoritta
import net.perfectdreams.loritta.platform.twitter.entity.TwitterUser
import net.perfectdreams.loritta.platform.twitter.entity.TwitterMessage
import net.perfectdreams.loritta.utils.LorittaUtils
import twitter4j.StatusUpdate
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class TwitterCommandContext(val userHandle: TwitterUser, val messageHandle: TwitterMessage, args: Array<String>) : CommandContext(userHandle, messageHandle, args) {
	override fun reply(content: String): RequestAction<Message> {
		return RequestAction {
			val status = StatusUpdate("@${userHandle.twitterHandle} $content")
			status.inReplyToStatusId = messageHandle.handle.id
			val repliedStatus = TwitterLoritta.TWITTER.updateStatus(status)
			return@RequestAction TwitterMessage(TwitterUser(repliedStatus.user), repliedStatus.text, repliedStatus, repliedStatus.userMentionEntities)
		}
	}

	override fun reply(image: BufferedImage, content: String): RequestAction<Message> {
		return RequestAction {
			val status = StatusUpdate("@${userHandle.twitterHandle} $content")
			status.inReplyToStatusId = messageHandle.handle.id

			val os = ByteArrayOutputStream()
			ImageIO.write(image, "png", os)

			val inputStream = ByteArrayInputStream(os.toByteArray())
			status.media("image.png", inputStream)

			val repliedStatus = TwitterLoritta.TWITTER.updateStatus(status)
			return@RequestAction TwitterMessage(TwitterUser(repliedStatus.user), repliedStatus.text, repliedStatus, repliedStatus.userMentionEntities)
		}
	}

	override fun getUserAt(idx: Int): User? {
		val argument = args.getOrNull(idx)

		if (argument != null) {
			val mentionedUser = messageHandle.mentionedUsers.firstOrNull { "@${it.screenName}" == argument }

			if (mentionedUser != null) {
				val user = TwitterLoritta.TWITTER.lookupUsers(mentionedUser.id).firstOrNull()

				if (user != null) {
					return TwitterUser(user)
				}
			}
		}

		return null
	}

	override fun getImageAt(idx: Int): BufferedImage? {
		// @SparklyPower uwu @UnderTale
		val argument = args.getOrNull(idx)

		if (argument != null) {
			val mentionedUser = messageHandle.mentionedUsers.firstOrNull { "@${it.screenName}" == argument }

			if (mentionedUser != null) {
				val user = TwitterLoritta.TWITTER.lookupUsers(mentionedUser.id).firstOrNull()

				if (user != null) {
					return LorittaUtils.downloadImage(user.biggerProfileImageURL)
				}
			}
		}

		val mediaEntity = messageHandle.handle.mediaEntities.firstOrNull()

		if (mediaEntity != null) {
			return LorittaUtils.downloadImage(mediaEntity.mediaURL)
		}

		val inReplyToStatusId = messageHandle.handle.inReplyToStatusId

		println("in reply to status id: $inReplyToStatusId")

		if (inReplyToStatusId != -1L) {
			val repliedStatus = TwitterLoritta.TWITTER.lookup(inReplyToStatusId).firstOrNull()

			if (repliedStatus != null) {
				val mediaEntity = repliedStatus.mediaEntities.firstOrNull()

				if (mediaEntity != null) {
					return LorittaUtils.downloadImage(mediaEntity.mediaURL)
				}
			}
		}

		return null
	}
}
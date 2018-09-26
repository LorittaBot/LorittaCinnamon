package net.perfectdreams.loritta.platform.twitter.entity

import net.perfectdreams.loritta.api.entity.User
import net.perfectdreams.loritta.api.entity.Message
import twitter4j.Status
import twitter4j.UserMentionEntity

class TwitterMessage(override val user: User, override val content: String, val handle: Status, val mentionedUsers: Array<UserMentionEntity>) : Message
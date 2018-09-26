package net.perfectdreams.loritta.platform.twitter.entity

import twitter4j.User

class TwitterUser(val handle: User) : net.perfectdreams.loritta.api.entity.User {
	override val name: String
		get() = handle.name
	override val avatarUrl: String
		get() = handle.biggerProfileImageURLHttps
	val twitterHandle = handle.screenName
}
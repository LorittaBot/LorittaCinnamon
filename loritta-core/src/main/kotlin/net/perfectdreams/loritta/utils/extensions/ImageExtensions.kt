package net.perfectdreams.loritta.utils.extensions

import net.perfectdreams.loritta.utils.ImageUtils
import java.awt.Image

fun Image.toBufferedImage() = ImageUtils.toBufferedImage(this)
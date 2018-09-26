package net.perfectdreams.loritta.utils

import java.awt.image.BufferedImage
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

object LorittaUtils {
	/**
	 * Faz download de uma imagem e retorna ela como um BufferedImage
	 * @param url
	 * @return
	 */
	fun downloadImage(url: String): BufferedImage? {
		return downloadImage(url, 15)
	}

	/**
	 * Faz download de uma imagem e retorna ela como um BufferedImage
	 * @param url
	 * @param timeout
	 * @return
	 */
	fun downloadImage(url: String, timeout: Int): BufferedImage? {
		return downloadImage(url, timeout, 20000000)
	}

	fun downloadImage(url: String, timeout: Int, maxSize: Int): BufferedImage? {
		return downloadImage(url, timeout, maxSize, 512)
	}

	fun downloadImage(url: String, timeout: Int, maxSize: Int, maxWidthHeight: Int): BufferedImage? {
		try {
			val imageUrl = URL(url)
			val connection = imageUrl.openConnection() as HttpURLConnection
			connection.setRequestProperty("User-Agent",
					Constants.USER_AGENT)

			if (connection.getHeaderFieldInt("Content-Length", 0) > maxSize) {
				return null
			}

			if (timeout != -1) {
				connection.setReadTimeout(timeout)
				connection.setConnectTimeout(timeout)
			}

			val bi = ImageIO.read(connection.getInputStream())

			if (maxWidthHeight != -1) {
				if (bi.getWidth() > maxWidthHeight || bi.getHeight() > maxWidthHeight) {
					// Espero que isto não vá gastar tanto processamento...
					val img = LorittaImage(bi)
					img.resize(maxWidthHeight, maxWidthHeight, true)
					return img.getBufferedImage()
				}
			}

			return bi
		} catch (e: Exception) {
		}

		return null
	}
}
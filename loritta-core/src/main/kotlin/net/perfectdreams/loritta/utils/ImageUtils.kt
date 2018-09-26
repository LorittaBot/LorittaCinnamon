package net.perfectdreams.loritta.utils

import java.awt.*
import java.awt.image.BufferedImage

object ImageUtils {
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	fun toBufferedImage(img: Image): BufferedImage {
		if (img is BufferedImage) {
			return img
		}

		// Create a buffered image with transparency
		val bimage = BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)

		// Draw the image on to the buffered image
		val bGr = bimage.createGraphics()
		bGr.drawImage(img, 0, 0, null)
		bGr.dispose()

		// Return the buffered image
		return bimage
	}

	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param graphics The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	fun drawCenteredStringOutlined(graphics: Graphics, text: String, rect: Rectangle, font: Font) {
		val color = graphics.color
		var g2d: Graphics2D? = null
		var paint: Paint? = null
		if (graphics is Graphics2D) {
			g2d = graphics
			paint = g2d.paint
		}
		// Get the FontMetrics
		val metrics = graphics.getFontMetrics(font)
		// Determine the X coordinate for the text
		val x = rect.x + (rect.width - metrics.stringWidth(text)) / 2
		// Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
		val y = rect.y + (rect.height - metrics.height) / 2 + metrics.ascent
		// Draw the outline
		graphics.color = Color.BLACK
		graphics.drawString(text, x - 1, y)
		graphics.drawString(text, x + 1, y)
		graphics.drawString(text, x, y - 1)
		graphics.drawString(text, x, y + 1)
		// Draw the String
		graphics.color = color
		if (paint != null) {
			g2d!!.paint = paint
		}
		graphics.drawString(text, x, y)
	}
}
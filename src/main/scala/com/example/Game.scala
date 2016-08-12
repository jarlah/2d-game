package com.example

import java.awt.image.BufferedImage
import java.awt._
import javax.swing.JFrame

class Game(
  frame: JFrame
) extends Loop {
  name = frame.getName
  val image = new BufferedImage(frame.getWidth, frame.getHeight, BufferedImage.TYPE_INT_RGB);
  val canvas = new Canvas()
  canvas.setPreferredSize(frame.getPreferredSize)
  canvas.setMinimumSize(frame.getMinimumSize)
  canvas.setMaximumSize(frame.getMaximumSize)
  frame.add(canvas)
  canvas.requestFocus()
  start()

  def update() = println("tick")

  override def render(): Unit = {
    val bs = canvas.getBufferStrategy
    if (bs == null) {
      canvas.createBufferStrategy(3)
      return
    }

    val g2d = image.getGraphics
    g2d.setColor(Color.WHITE)
    g2d.clearRect(0, 0, canvas.getWidth, canvas.getHeight)
    g2d.setColor(Color.BLACK)
    g2d.drawString("Heisann", 10, 10)

    val g = bs.getDrawGraphics
    g.drawImage(image, 0, 0, canvas.getWidth, canvas.getHeight, null)
    Toolkit.getDefaultToolkit.sync()
    g.dispose()
    bs.show()
  }
}

object Game extends App {
  val frame = new JFrame("Test")
  val gameDimension = new Dimension(500, 500)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setMinimumSize(gameDimension)
  frame.setPreferredSize(gameDimension)
  frame.setMaximumSize(gameDimension)
  frame.setVisible(true)
  val game = new Game(frame)
}
package com.example

object Loop {
  private val GAME_HERTZ: Double = 60.0
  private val TIME_BETWEEN_UPDATES: Double = 1000000000.0 / GAME_HERTZ
  private val TARGET_FPS: Double = 60.0
  private val TARGET_TIME_BETWEEN_RENDERS: Double = 1000000000.0 / TARGET_FPS
  private val MAX_UPDATES_BEFORE_RENDER: Double = 5
}

trait Loop extends Runnable {
  var name: String = ""
  private var thread: Thread = null
  private var running: Boolean = false
  private var paused: Boolean = false
  private var fps: Int = 0
  private var ups: Int = 0

  this.running = false

  def start() {
    this.running = true
    this.thread = new Thread(this, name)
    this.thread.start()
  }

  def stop() {
    this.running = false
    try {
      this.thread.join()
    }
    catch {
      case e: InterruptedException => {
        e.printStackTrace()
      }
    }
  }

  def run() = {
    var frameCount: Int = 0
    var lastUpdateTime: Double = System.nanoTime
    var lastRenderTime: Double = .0
    var lastSecondTime: Int = (lastUpdateTime / 1000000000).toInt
    while (running) {
      {
        if (!paused) {
          var now: Double = System.nanoTime
          var updateCount: Int = 0
          while (now - lastUpdateTime > Loop.TIME_BETWEEN_UPDATES && updateCount < Loop.MAX_UPDATES_BEFORE_RENDER) {
            {
              update()
              lastUpdateTime += Loop.TIME_BETWEEN_UPDATES
              updateCount += 1
            }
          }
          if (now - lastUpdateTime > Loop.TIME_BETWEEN_UPDATES) {
            lastUpdateTime = now - Loop.TIME_BETWEEN_UPDATES
          }
          render()
          frameCount += 1
          lastRenderTime = now
          val thisSecond: Int = (lastUpdateTime / 1000000000).toInt
          if (thisSecond > lastSecondTime) {
            fps = frameCount
            frameCount = 0
            ups = updateCount
            lastSecondTime = thisSecond
          }
          while ((now - lastRenderTime) < Loop.TARGET_TIME_BETWEEN_RENDERS && (now - lastUpdateTime) < Loop.TIME_BETWEEN_UPDATES) {
            {
              Thread.`yield`()
              try {
                Thread.sleep(1)
              }
              catch {
                case ignored: Exception => {
                }
              }
              now = System.nanoTime
            }
          }
        }
        else {
          try {
            Thread.sleep(250)
          } catch {
            case ignored: Exception => {}
          }
        }
      }
    }
    stop()
  }

  def update()
  def render()
  def pause() = paused = true
  def resume() = paused = false
}
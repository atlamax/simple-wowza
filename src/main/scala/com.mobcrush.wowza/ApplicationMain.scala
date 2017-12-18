package com.mobcrush.wowza

/**
  * Created by msekerjitsky on 18.12.2017.
  */
object ApplicationMain {

  def main(args: Array[String]): Unit = {
    val runner = new FFMpegRunner()
    runner.run()
  }

}

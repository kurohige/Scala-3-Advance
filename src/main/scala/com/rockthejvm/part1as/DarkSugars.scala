package com.rockthejvm.part1as

object DarkSugars {

  // 1 - sugar for methods with one argument
  def singleArgMethod(arg: Int): Int = arg + 1

  val aMethodCall = singleArgMethod {
    // code
    42
  }

  val aMethodCall_v2 = singleArgMethod(42)

  def main(args: Array[String]): Unit = {

  }

}

package com.rockthejvm.part2afp

object LazyEvaluation {

  // lazy vals - the keyword is lazy DELAYs the evaluation of a value until the first use.
  lazy val x: Int = {
    println("Computing x")
    42
  }

  // call by need = call by name + lazy values
  def byNameMethod(n: => Int): Int = n + n + n + 1

  def retrieveMagicValue = {
    println("Waiting")
    Thread.sleep(1000)
    42
  }

  def demoByName(): Unit = {
    // call by name
    println(byNameMethod(retrieveMagicValue))
    // call by need
    println(byNameMethod(x))
  }

  def byNeedMethod(n: => Int): Int = {
    lazy val lazyN = n
    lazyN + lazyN + lazyN + 1
  }

  def demoByNeed(): Unit = {
    println(byNeedMethod(retrieveMagicValue))
    println(byNeedMethod(x))
  }


  def main(args: Array[String]): Unit = {

  }
}

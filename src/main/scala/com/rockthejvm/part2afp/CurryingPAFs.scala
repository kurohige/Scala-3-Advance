package com.rockthejvm.part2afp

object CurryingPAFs {

  // currying
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3: Int => Int = superAdder(3) // y => 3 + y
  val eight = add3(5)
  val eight_v2 = superAdder(3)(5) // curried function

  // Curried methods
  def curriedAdder(x: Int)(y: Int): Int = x + y

  // methods != functions values
  val add4 = curriedAdder(4) // ETA-expansion
  val nine = add4(5) // 9

  def increment(x: Int): Int = x + 1
  val aList = List(1,2,3)
  val anIncrementedList = aList.map(increment) // List(2,3,4)

  // underscores are powerful: allows you to decide the shapes of lambdas obtained from methods
  def concatenator(a: String, b: String, c: String): String = a + b + c
  val concatenatorFun = concatenator("Hello ", _: String, " Scala!")
  val insertName = concatenator(
    "Hello, my name is ",
    _: String,
    " I'm going to show you a nice scala trick!"
  ) // x => concatenator("Hello, my name is ", x, " I'm going to show you a nice scala trick!")

  val danielsGreeting = insertName("Daniel") // concatenator("...", "Daniel", "...")
  val fillInTheBlanks = concatenator(_: String, " Daniel ", _: String) // (x,y) => concatenator(x, " Daniel ", y)


  /**
   * Exercises
   *
   * 1. Process a list of numbers and return their string representations with different formats
   *    Use the %4.2f, %8.6f and %14.12f with a curried formatter function
   */
  val simpleAddFunction= (x: Int, y: Int) => x + y
  def simpleAddMethod = (x: Int, y: Int) => x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // 1 - obtain an add7 function: x => x + 7 out of these 3 definitions

  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_v2 = (x: Int) => simpleAddMethod(7, x)
  val add7_v3 = curriedAddMethod(7) _ // PAF = partially applied function
  val add7_v4 = curriedAddMethod(7)(_) // PAF = partially applied function
  val add7_v5 = curriedAddMethod(7)
  val add7_v6 = simpleAddMethod(7, _)
  val add7_v7 = simpleAddMethod(_, 7)

  // 2 - process a list of numbers and return their string representations with different formats
  // step 1: create a curried formatter function with a formatting string and a value
  def curriedFormatter(format: String)(value: Double): String = format.format(value)
  // step 2: process a list of numbers with various formats
  val piWith2Dec = "%4.2f".format(Math.PI)
  val someDecimals = List(Math.PI, Math.E, 1.0/3, 1.0/6, 1, 9.8, 1.3e-12)

  // methods vs functions + by-name vs 0-lambda
  def byName(n: => Int): Int = n + 1
  def byLambda(f: () => Int): Int = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  def main(args: Array[String]): Unit = {
    println(someDecimals.map(curriedFormatter("%4.2f")))
  }

}

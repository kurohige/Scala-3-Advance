package com.rockthejvm.part4context

import scala.concurrent.{ExecutionContext, Future}

object ContextFunctions {

  val aList = List(1,2,3,4,5)
  val sortedList = aList.sorted // List(1,2,3,4,5)

  // defs can take using clauses
  def methodWithoutContextArguments(nonContextArg: Int)(nonContextArg2: String): Int = nonContextArg + nonContextArg2.toInt

  def methodWithContextArguments(nonContextArg: Int)(using nonContextArg2: String): Int = nonContextArg + nonContextArg2.toInt


  // eta - expansion
  val func1 = methodWithoutContextArguments(5) _ // String => Int
  // val func2 = methodWithContextArguments(5) _ // String => Int

  // context functions
  val functionWithContextArguments: Int => String ?=> Int = methodWithContextArguments

  val someResult = functionWithContextArguments(5)(using "45") // 50

  /*
    Use cases:
    - convert methods with using clauses to function values
    - HOF with function values taking given instances as arguments
    - requiring given instance at the call site
   */

  // execution context here
  // val incrementAsync: Int => Future[Int] = (x: Int) => Future(x + 1) // doesn't work without a given EC in scope

  val incrementAsync: ExecutionContext ?=> Int => Future[Int] = (x: Int) => Future(x + 1) // works with a given EC in scope


  def main(args: Array[String]): Unit = {

  }

}

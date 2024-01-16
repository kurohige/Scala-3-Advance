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

  def main(args: Array[String]): Unit = {

  }

}

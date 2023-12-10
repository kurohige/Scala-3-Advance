package com.rockthejvm.part2afp

object PartialFunctions {

  val aFunction: Int => Int = x => x + 1 // a function that takes an Int and returns an Int

  val aFussyFunction = (x:Int) => {
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new RuntimeException("no suitable cases possible")
  }

  val aFussyFunction_v2: Int => Int = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  // partial function
  val aPartialFunction : PartialFunction[Int, Int]= { // x -> x match { ... }
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  val canCallOn37 = aPartialFunction.isDefinedAt(37) // false
  val liftedPF = aPartialFunction.lift // Int => Option[Int]

  val anotherPF: PartialFunction[Int, Int] = {
    case 45 => 67
  }

  val pfChain = aPartialFunction.orElse(anotherPF) // partial function chaining

  // HOFs accept partial functions as well
  val aList = List(1,2,3)
  val aChangedList = aList.map {
    case 1 => 42
    case 2 => 56
    case 3 => 999
    case 4 => 1000
    case _ => 0
  }

  val aChangedList_v2 = aList.map({
    case 1 => 42
    case 2 => 56
    case 3 => 999
    case 4 => 1000
    case _ => 0
  })

  case class Person(name: String, age: Int)
  val somePeople = List(Person("Alice", 23), Person("Bob", 25), Person("Charlie", 27))
  val peopleGrowingUp = somePeople.map {
    case Person(name, age) => Person(name, age + 1)
  }

  def main(args: Array[String]): Unit = {
    println(aPartialFunction(2))
    //println(aPartialFunction(57273)) // MatchError
    println(liftedPF(2)) // Some(56)
    println(liftedPF(57273)) // None
  }

}

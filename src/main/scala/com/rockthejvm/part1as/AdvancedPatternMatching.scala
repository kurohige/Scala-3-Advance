package com.rockthejvm.part1as

object AdvancedPatternMatching {

  /*
  PM:
    - constants
    - objects
    - wildcards
    - variables
    - infix patterns
    - lists
    - case classes
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] = // person match { case Person(n, a) => ... }
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =  // int match { case Person(string) => ... }
      if (age < 21) Some("minor")
      else Some("legally allowed to drink")
  }

  val daniel = new Person("Daniel", 102)
  val danielPM = daniel match { // Person.unapply(daniel) => Some((daniel.name, daniel.age))
    case Person(n, a) => s"Hi, my name is $n and I am $a years old."
    case _ => "I don't know who I am."
  }

  val danielsLegalStatus = daniel.age match {
    case Person(status) => s"Daniel's legal drinking status is $status."
  }

  // boolean patterns
  object even{
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit{
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n: Int = 45
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "even number"
    case _ => "no property"
  }

  // infix patterns




  def main(args: Array[String]): Unit = {
    println(mathProperty)
  }

}

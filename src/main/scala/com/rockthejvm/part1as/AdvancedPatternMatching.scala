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
  infix case class Or[A, B](a: A, b: B) // Or(2, "two") => 2 Or "two"
  val anEither = Or(2, "two")
  val humanDescription = anEither match {
    case number Or string => s"$number is written as $string"
  }

  val aList = List(1,2,3,4,5)
  val listPM = aList match {
    case 1:: rest => "a list starting with 1"
    case _ => "something else"
  }

  // decomposing sequences

  val vararg = aList match {
    case List(1, _*) => "a list starting with 1"
    case _ => "something else"
  }

  abstract class MyList[+A] {
    def head: A = throw new NoSuchElementException
    def tail: MyList[A] = throw new NoSuchElementException
  }

  case class Empty[A]() extends MyList[A]
  case class Cons[a](override val head: a, override val tail: MyList[a]) extends MyList[a]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList : MyList[Int] = Cons(1, Cons(2, Cons(3, Empty())))
  val varargCustom = myList match {
    case MyList(1, _*) => "a list starting with 1"
    case _ => "something else"
  }

  // custom return types for unapply
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  val weirdPersonPM = daniel match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  }

  def main(args: Array[String]): Unit = {
    println(mathProperty)
  }

}

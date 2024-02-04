package com.rockthejvm.part4context

object Givens {

  // list sorting
  val aList = List(2, 1, 3)
  val anOrderedList = aList.sorted //(descendingOrdering)

  // given is a keyword to define a value that can be used as an implicit parameter
  given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  val anInverseOrderedList = aList.sorted(descendingOrdering)

  // custom sorting
  case class Person(name: String, age: Int)
  val people = List(
    Person("Alice", 29),
    Person("Sarah", 34),
    Person("Jim", 23)
  )

  given personOrdering: Ordering[Person] = new Ordering[Person] {
    override def compare(x: Person, y: Person): Int =
      x.name.compareTo(y.name)
  }

  val sortedPeople = people.sorted // (personOrdering) <-- this is automatically passed by the compiler

  object PersonAltSyntaxt {
    given personOrdering: Ordering[Person] with {
      override def compare(x: Person, y: Person): Int =
        x.name.compareTo(y.name)
    }
  }

  // using clauses
  trait Combinator[A] {
    def combine(x: A, y: A): A
  }

  def combineAll[A](list: List[A])(using combinator: Combinator[A]): A =
    list.reduce(combinator.combine)

  /*
    combineAll(List(1,2,3)) // 6
    combineAll(people)
   */
  given intCombinator: Combinator[Int] with {
    override def combine(x: Int, y: Int): Int = x + y
  }

  val firstSum = combineAll(List(1, 2, 3))//(intCombinator) <-- passed automatically
  // al combineAllPeople = combineAll(people) // doesn't compile - no Combinator[Person] in scope

  def main(args: Array[String]): Unit = {}

}

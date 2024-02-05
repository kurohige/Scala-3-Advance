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

  // context bound
  def combineInGroupsOf3[A](list: List[A])(using Combinator[A]): List[A] = {
    list.grouped(3).map(group => combineAll(group)/*(combinator) passed by the compiler*/).toList
  }

  def combineInGroupsOf3V2[A: Combinator](list: List[A]): List[A] = {
    list.grouped(3).map(group => combineAll(group) /*(combinator) passed by the compiler*/).toList
  }

  // synthesize new given instance based on existing ones
  given listOrdering(using intOrdering: Ordering[Int]): Ordering[List[Int]] with {
    override def compare(x: List[Int], y: List[Int]): Int = {
      x.sum - y.sum
    }
  }

  val listOfList = List(List(1,2,3), List(4,5,6), List(7,8,9))
  val nestedListOrdered = listOfList.sorted // (listOrdering(intOrdering)) passed by the compiler

  // ... with generics
  given listOrderingBasedOnCombinator[A](using ord: Ordering[A])(using combinator: Combinator[A]): Ordering[List[A]] with {
    override def compare(x: List[A], y: List[A]) = {
      ord.compare(combineAll(x), combineAll(y))
    }
  }
  // pass a regular value instead of a given
  val myCombinator = new Combinator[Int] {
    override def combine(x: Int, y: Int): Int = x * y
  }
  val listProduct = combineAll(List(1,2,3))(using myCombinator)


  /*
   * Exercises:
   * 1 - create a given for ordering Option[A] if you can order A
   * 2 - create a summoning method that fetches the given value of your particular
   */

  given optionOrdering[A: Ordering]: Ordering[Option[A]] with {
    override def compare(x: Option[A], y: Option[A]): Int = (x, y) match {
      case (Some(a), Some(b)) => summon[Ordering[A]].compare(a, b)
      case (Some(_), None) => 1
      case (None, Some(_)) => -1
      case (None, None) => 0
    }
  }

  def fetchGivenValue[A](using theValue: A): A = theValue



  def main(args: Array[String]): Unit = {}

}

package com.rockthejvm.part4context

object OrganizingCAs {

  val aList = List(2,3,1,4)
  val anOrderedList = aList.sorted

  // compiler fetches givens/EMs for the type of the list
  // 1 - local scope
  given reverseOrdering : Ordering[Int] with
    override def compare(x: Int, y: Int) = y - x

  // 2 - imported scope
  case class Person(name: String, age: Int)
  val persons = List (
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 67)
  )

  object PersonGivens {
    given ageOrdering: Ordering[Person] with
      override def compare(x: Person, y: Person) = x.age - y.age
  }

  // a - import explicitly the givens
  // import PersonGivens.ageOrdering

  // b - import a given for a particular type
  //import PersonGivens.{given Ordering[Person]}

  // c - import all givens
  // import PersonGivens.given

  // warning: import PersonGivens.* does Not also import given instances!

  // 3 - companions of all types involved in method signature
  // def sorted[B >: A](using ord: Ordering[B]): List[A]

  object Person {
    given byNameOrdering: Ordering[Person] with
      override def compare(x: Person, y: Person) = x.name.compareTo(y.name)
  }

  val sortedPersons = persons.sorted



  def main(args: Array[String]): Unit = {

  }

}

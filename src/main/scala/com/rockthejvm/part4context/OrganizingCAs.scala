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

    extension (person: Person)
        def greets(other: String) = s"${person.name} says: Hi, $other!"
  }

  val sortedPersons = persons.sorted
  /*
    Good practice tips:
    1) When you have a "default" given (only ONE that makes sense) add it in the companion object of the type.
    2) when you have Many possible givens, but One that is dominant (used most), add that in the companion object.
    3) When you have Many possible givens, and no dominant one, add them in a separate object.
  */

  // some principles apply to extension methods as well

  /**
   * Exercises: Create given instances for:
   * - Ordering by total price, descending = 50% of code base
   * - Ordering by unit count, descending = 25% of code base
   * - Ordering by unit price, ascending = 25% of code base
   */
  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    given totalPriceOrdering: Ordering[Purchase] with
      override def compare(x: Purchase, y:Purchase) = {
        val totalX = x.nUnits * x.unitPrice
        val totalY = y.nUnits * y.unitPrice

        if(totalX == totalY) 0
        else if(totalX < totalY) -1
        else 1
      }
  }

  object UnitCountOrdering {
    given unitCountORdering: Ordering[Purchase] = Ordering.fromLessThan((x, y) => x.nUnits > y.nUnits)
  }

  object UnitPriceOrdering {
    given unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((x, y) => x.unitPrice < y.unitPrice)
  }

  def main(args: Array[String]): Unit = {
    import PersonGivens.* // includes extension methods
    println(Person("Steve", 30).greets("Daniel"))
  }

}

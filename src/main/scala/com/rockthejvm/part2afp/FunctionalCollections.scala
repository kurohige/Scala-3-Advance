package com.rockthejvm.part2afp

object FunctionalCollections {

  // sets are functions A => Boolean
  val aSet: Set[String] = Set("I", "love", "Scala")
  val setcontainsScala = aSet("Scala") // true

  // seq are partial functions [Int, A] => T
  val aSeq: Seq[Int] = Seq(1,2,3,4)
  val anElement = aSeq(2) // 3
  val aNonExistingElement = aSeq(100) // throws an exception OOBException

  // Map "extends" partial functions [A, B] => T
  val aPhonebook: Map[String, Int] = Map(
    ("Daniel", 1234),
    "Jane" -> 2345 // syntactic sugar for ("Jane", 2345)
  )
  val alicesPhone = aPhonebook("Alice") // throws an exception NoSuchElementException
  val danielPhone = aPhonebook("Daniel") // 1234




  def main(args: Array[String]): Unit = {

  }

}

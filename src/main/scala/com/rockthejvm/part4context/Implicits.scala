package com.rockthejvm.part4context

object Implicits {

  // the ability to pass arguments automatically(implicit parameters) by the compiler
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  def combineAll[A](list: List[A])(using semigroup: Semigroup[A]): A =
    list.reduce(semigroup.combine)

  given intSemigroup: Semigroup[Int] with
    override def combine(x: Int, y: Int) = x + y

  //val sumOf10 = combineAll((1 to 10).toList)

  // implicit conversions
  def combineAllImplicit[A](list: List[A])(implicit semigroup: Semigroup[A]): A =
    list.reduce(semigroup.combine)

  implicit val intSemigroupImplicit: Semigroup[Int] = new Semigroup[Int] {
    override def combine(x: Int, y: Int) = x + y
  }

  //val sumOf10Implicit = combineAllImplicit((1 to 10).toList)
  
  // extension methods
  extension (number: Int)
    def isEven: Boolean = number % 2 == 0
    
  val is23Even = 23.isEven
  
  // extension methods = implicit classes
  
  implicit class MyRichInteger(number: Int) {
    def isEven: Boolean = number % 2 == 0
  }
  
  val questionOfMyLife = 11.isEven // new myRichInteger(11).isEven
  
  // implicit classes are a shorthand for implicit conversions
  
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name"
  }
  
  implicit def stringToPerson(name: String): Person = Person(name)
  
  


  def main(args: Array[String]): Unit = {

  }

}

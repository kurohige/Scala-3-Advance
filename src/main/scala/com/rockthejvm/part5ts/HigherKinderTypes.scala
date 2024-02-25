package com.rockthejvm.part5ts
import scala.util.Try

object HigherKinderTypes {

  // are generic types where the type parameters are themselves generic
  class HigherKindedType[F[_]] // F is a type constructor
  class HigherKindedType2[F[_], G[
      _
  ], A] // F is a type constructor with 2 type parameters

  val hkExample = new HigherKindedType[List]
  val hkExample2 = new HigherKindedType2[Option, List, String]

  // why: abstract libraries, e.g. Cats, ScalaZ, Monix, ZIO, Akka, etc.
  // example: Functor

  val aList = List(1, 2, 3)
  val anOption = Option(2)
  val aTry = Try(43)

  val anIncrementedList = aList.map(_ + 1) // List(2, 3, 4)
  val anIncrementedOption = anOption.map(_ + 1) // Some(3)
  val anIncrementedTry = aTry.map(_ + 1) // Success(44)

  // "duplicated" APIs
  def do10xList(list: List[Int]): List[Int] = list.map(_ * 10)
  def do10xOption(option: Option[Int]): Option[Int] = option.map(_ * 10)
  def do10xTry(theTry: Try[Int]): Try[Int] = theTry.map(_ * 10)
  
  // DRY principle: Don't Repeat Yourself
  // step 1: TC definition
  trait Functor[F[_]] {
    def map[A, B](initialValue: F[A])(f: A => B): F[B]
    // map[A, B](lista: List[A])(f: A => B): List[B]
  }
  
  // step 2: TC instances
  given ListFunctor: Functor[List] with {
    override def map[A, B](initialValue: List[A])(f: A => B): List[B] = initialValue.map(f)
  }
  
  // step 3: "user-facing" API
  def do10x[F[_]](container: F[Int])(using functor: Functor[F]): F[Int] = functor.map(container)(_ * 10)
  
  // if you create TC instances for Option and Try, you can use do10x with those types as well
  
  // step 4: extension methods
  extension [F[_], A](container: F[A])(using functor: Functor[F])
    def map[B](f: A => B): F[B] = functor.map(container)(f)
    
  def do10x_v2[F[_]: Functor](container: F[Int]): F[Int] = container.map(_ * 10)
  
  
  def main(args: Array[String]): Unit = {
    println(do10x(List(1, 2, 3))) // List(10, 20, 30)
  }

}

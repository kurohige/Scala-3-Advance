package com.rockthejvm.part2afp

import scala.annotation.targetName

object Monads {

  def listStory(): Unit = {
    val aList = List(1, 2, 3)
    val listMultiplied = for {
      x <- List(1, 2, 3)
      y <- List(4, 5, 6)
    } yield x * y
    // for comprehensions are syntactic sugar for flatMap + map
    val listMultiplied_v2 =
      List(1, 2, 3).flatMap(x => List(4, 5, 6).map(y => x * y))

    val f = (x: Int) => List(x, x + 1)
    val g = (x: Int) => List(x, 2 * x)
    val pure = (x: Int) => List(x) // same as the list "constructor"

    // prop 1: left identity
    val leftIdentity =
      pure(42).flatMap(f) == f(
        42
      ) // for every x, for every f, pure(x).flatMap(f) == f(x)

    // prop 2: right identity
    val rightIdentity =
      aList.flatMap(
        pure
      ) == aList // for every x, pure(x).flatMap(pure) == pure(x)

    // prop 3: associativity
    /*
      [1,2,3].flatMap(x => [x, x + 1]) = [1,2,2,3,3,4]
      [1,2,3].flatMap(x => [x, 2 * x]) = [1,2,2,4,3,6]
      [1,2,3].flatMap(f).flatMap(g) = [1,2,2,4,3,6]
      [1,2,3].flatMap(x => f(x).flatMap(g)) = [1,2,2,4,3,6]
     */
    val associativity =
      aList.flatMap(f).flatMap(g) == aList.flatMap(x => f(x).flatMap(g))
  }

  def optionStory(): Unit = {
    val anOption = Option(42)
    val optionString = for {
      lang <- Option("Scala")
      ver <- Option("3")
    } yield s"$lang-$ver"

    val optionMultiplied_v2 =
      Option("Scala").flatMap(lang => Option("3").map(ver => s"$lang-$ver"))

    val f = (x: Int) => Option(x + 1)
    val g = (x: Int) => Option(2 * x)
    val pure = (x: Int) => Option(x)

    // prop 1: left identity
    val leftIdentity =
      pure(42).flatMap(f) == f(
        42
      ) // for every x, for every f, pure(x).flatMap(f) == f(x)

// prop 2: right identity
    val rightIdentity =
      anOption.flatMap(
        pure
      ) == anOption // for every x, pure(x).flatMap(pure) == pure(x)

    // prop 3: associativity
    /*
      [1,2,3].flatMap(x => [x, x + 1]) = [1,2,2,3,3,4]
      [1,2,3].flatMap(x => [x, 2 * x]) = [1,2,2,4,3,6]
      [1,2,3].flatMap(f).flatMap(g) = [1,2,2,4,3,6]
      [1,2,3].flatMap(x => f(x).flatMap(g)) = [1,2,2,4,3,6]
     */
    val associativity =
      anOption.flatMap(f).flatMap(g) == anOption.flatMap(x => f(x).flatMap(g))

  }

  // Monads = chain dependent computations
  case class PossiblyMonad[A](unsafeRun: () => A) {
    def map[B](f: A => B): PossiblyMonad[B] =
      PossiblyMonad(() => f(unsafeRun()))

    def flatMap[B](f: A => PossiblyMonad[B]): PossiblyMonad[B] =
      PossiblyMonad(() => f(unsafeRun()).unsafeRun())
  }

  object PossiblyMonad {
    @targetName("pure")
    def pure[A](value: A): PossiblyMonad[A] = PossiblyMonad(() => value)
  }

  def possiblyStory(): Unit = {
    val aMonad = PossiblyMonad.pure(42)
    val monadMultiplied = for {
      x <- PossiblyMonad.pure(1)
      y <- PossiblyMonad.pure(2)
    } yield x * y

    val monadMultiplied_v2 =
      PossiblyMonad
        .pure(1)
        .flatMap(x => PossiblyMonad.pure(2).map(y => x * y))

    val f = (x: Int) => PossiblyMonad.pure(x + 1)
    val g = (x: Int) => PossiblyMonad.pure(2 * x)
    val pure = (x: Int) => PossiblyMonad.pure(x)

    // prop 1: left identity
    val leftIdentity =
      pure(42).flatMap(f) == f(
        42
      ) // for every x, for every f, pure(x).flatMap(f) == f(x)

    // prop 2: right identity
    val rightIdentity =
      aMonad.flatMap(
        pure
      ) == aMonad // for every x, pure(x).flatMap(pure) == pure(x)

    // prop 3: associativity
    /*
      [1,2,3].flatMap(x => [x, x + 1]) = [1,2,2,3,3,4]
      [1,2,3].flatMap(x => [x, 2 * x]) = [1,2,2,4,3,6]
      [1,2,3].flatMap(f).flatMap(g) = [1,2,2,4,3,6]
      [1,2,3].flatMap(x => f(x).flatMap(g)) = [1,2,2,4,3,6]
     */
    val associativity =
      aMonad.flatMap(f).flatMap(g) == aMonad.flatMap(x => f(x).flatMap(g))
  }

  def main(args: Array[String]): Unit = {}
}

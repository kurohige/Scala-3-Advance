package com.rockthejvm.practice

import scala.annotation.tailrec

// write a lazily evaluated list, potentially infinite linked list
abstract class LzList[A] {
  def isEmpty: Boolean
  def head: A
  def tail: LzList[A]

  // utilities
  def #::(element: A): LzList[A] // prepend
  def ++(anotherList: LzList[A]): LzList[A] // concatenate two lists TODO warning

  // classics
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): LzList[B]
  def flatMap[B](f: A => LzList[B]): LzList[B]
  def filter(predicate: A => Boolean): LzList[A]
  def withFilter(predicate: A => Boolean): LzList[A] = filter(predicate)

  def take(n: Int): LzList[A] // takes the first n elements out of this list
  def takeAsList(n: Int): List[A] = take(n).toList
  def toList: List[A] = {
    @tailrec
    def toListAux(remaining: LzList[A], acc: List[A]): List[A] =
      if (remaining.isEmpty) acc.reverse
      else toListAux(remaining.tail, remaining.head :: acc)

    toListAux(this, List())
  }
}

case class LzEmpty[A]() extends LzList[A] {
  override def isEmpty: Boolean = true
  override def head: A = throw new NoSuchElementException
  override def tail: LzList[A] = throw new NoSuchElementException

  // utilities
  override def #::(element: A): LzList[A] = LzCons(element, this)
  override def ++(anotherList: LzList[A]): LzList[A] = anotherList // TODO warning

  override def foreach(f: A => Unit): Unit = ()
  override def map[B](f: A => B): LzList[B] = LzEmpty[B]()
  override def flatMap[B](f: A => LzList[B]): LzList[B] = LzEmpty[B]()
  override def filter(predicate: A => Boolean): LzList[A] = this

  override def take(n: Int): LzList[A] =
    if (n <= 0) LzEmpty[A]()
    else throw new NoSuchElementException("Cannot take elements from an empty list")
}

class LzCons[A](hd: => A, tl: => LzList[A]) extends LzList[A] {
  override def isEmpty: Boolean = false

  // use call by need
  override lazy val head: A = hd
  override lazy val tail: LzList[A] = tl

  def #::(element: A): LzList[A] = new LzCons(element, this)
  def ++(anotherList: LzList[A]): LzList[A] = new LzCons(head, tail ++ anotherList)

  def foreach(f: A => Unit): Unit = {
    def foreachTailrec(remaining: LzList[A]): Unit =
      if (!remaining.isEmpty) {
        f(remaining.head)
        foreachTailrec(remaining.tail)
      }

    foreachTailrec(this)
  }

  def map[B](f: A => B): LzList[B] = new LzCons(f(head), tail.map(f))
  def flatMap[B](f: A => LzList[B]): LzList[B] = f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): LzList[A] =
    if (predicate(hd)) LzCons(head, tail.filter(predicate))
    else tail.filter(predicate)

  override def take(n: Int): LzList[A] =
    if (n <= 0) LzEmpty[A]()
    else if (n == 1) LzCons(head, LzEmpty[A]() )
    else new LzCons(head, tail.take(n - 1))
}

object LzList {
  def empty[A]: LzList[A] = LzEmpty[A]()

  def generate[A](start: A)(generator: A => A): LzList[A] =
    LzCons(start, generate(generator(start))(generator))

  def from[A](list: List[A]): LzList[A] = list.foldLeft(LzList.empty) {
    (currentLzList, newElement) => new LzCons(newElement, currentLzList)
  }

}

def eratosthenes: LzList[Int] = {
  def isPrime(n: Int): Boolean = {
    if (n <= 1) false
    else if (n == 2) true
    else !(2 to Math.sqrt(n).toInt).exists(n % _ == 0)
  }

  def sieve(numbers: LzList[Int]): LzList[Int] = {
    if (numbers.isEmpty) numbers
    else if (!isPrime(numbers.head)) sieve(numbers.tail)
    else new LzCons(numbers.head, sieve(numbers.tail.filter(_ % numbers.head != 0)))
  }

    val naturalsFrom2 = LzList.generate(2)(_ + 1)
    sieve(naturalsFrom2)
}

object LzListPlayground{
  def main(args: Array[String]): Unit = {
    val naturals = LzList.generate(1)(_ + 1)
    println(naturals.head)
    println(naturals.tail.head)
    println(naturals.tail.tail.head)

    val first50k = naturals.take(50000)
    first50k.foreach(println)

    /**
     * Exercises:
     * 1. Lazy list of fibonacci numbers
     * 2. Lazy list of prime numbers
     *    - filter with isPrime
     *    - Eratosthenes' sieve
     */

    // 1
    def fibonacci(first: BigInt, second: BigInt): LzList[BigInt] =
      LzCons(first, fibonacci(second, first + second))

    println(fibonacci(1, 1).take(100).toList)




  }
}

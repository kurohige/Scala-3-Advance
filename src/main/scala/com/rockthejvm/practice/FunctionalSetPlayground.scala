package com.rockthejvm.practice

import scala.annotation.tailrec


abstract class FSet[A] extends (A => Boolean) {

  // main api
  def contains(elem: A): Boolean
  def apply(elem: A): Boolean = contains(elem)

  def +(elem: A): FSet[A]
  def ++(anotherSet: FSet[A]): FSet[A]

  // "classics"
  def map[B](f: A => B): FSet[B]
  def flatMap[B](f: A => FSet[B]): FSet[B]
  def filter(predicate: A => Boolean): FSet[A]
  def foreach(f: A => Unit): Unit

  // methods
  infix def -(elem: A): FSet[A]
  infix def --(anotherSet: FSet[A]): FSet[A]
  infix def &(anotherSet: FSet[A]): FSet[A]

  def unary_! : FSet[A] = new PBSet(x => !contains(x))
}

// example {x in N | x % 2 == 0} => {0,2,4,6,8,10...}
// property-based set
class PBSet[A](property: A => Boolean) extends FSet[A]{
  override def contains(elem: A): Boolean = property(elem)
  override def +(elem: A): FSet[A] = new PBSet[A](x => property(x) || x == elem)
  override def ++(anotherSet: FSet[A]): FSet[A] = new PBSet[A](x => property(x) || anotherSet(x))

  // "classics"
  override def map[B](f: A => B): FSet[B] = politelyFail
  override def flatMap[B](f: A => FSet[B]): FSet[B] = politelyFail
  override def filter(predicate: A => Boolean): FSet[A] = new PBSet[A](x => property(x) && predicate(x))
  override def foreach(f: A => Unit): Unit = politelyFail

  // methods
  override infix def -(elem: A): FSet[A] = filter(x => x != elem)
  override infix def --(anotherSet: FSet[A]): FSet[A] = filter(!anotherSet)
  override infix def &(anotherSet: FSet[A]): FSet[A] = filter(anotherSet)

  // "negation" == all the elements that DO NOT satisfy the property
  override def unary_! : FSet[A] = new PBSet[A](x => !property(x))
  // extra utilities(internal)
  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}

case class Empty[A]() extends FSet[A]{
  override def contains(elem: A): Boolean = false
  override def +(elem: A): FSet[A] = Cons(elem, this)
  override def ++(anotherSet: FSet[A]): FSet[A] = anotherSet

  override def map[B](f: A => B): FSet[B] = Empty[B]()
  override def flatMap[B](f: A => FSet[B]): FSet[B] = Empty[B]()
  override def filter(predicate: A => Boolean): FSet[A] = this
  override def foreach(f: A => Unit): Unit = ()

  override infix def -(elem: A): FSet[A] = this
  override infix def --(anotherSet: FSet[A]): FSet[A] = this
  override infix def &(anotherSet: FSet[A]): FSet[A] = this
}

case class Cons[A](head: A, tail: FSet[A]) extends FSet[A]{
  override def contains(elem: A): Boolean = {
    if (head == elem) true
    else tail.contains(elem)
  }

  override def +(elem: A): FSet[A] = Cons(elem, this)
  override def ++(anotherSet: FSet[A]): FSet[A] = tail ++ anotherSet + head

  // "classics"
  override def map[B](f: A => B): FSet[B] = tail.map(f) + f(head)
  override def flatMap[B](f: A => FSet[B]): FSet[B] = tail.flatMap(f) ++ f(head)
  override def filter(predicate: A => Boolean): FSet[A] = {
    val filteredTail = tail.filter(predicate)
    if predicate(head) then filteredTail + head
    else filteredTail
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  override infix def -(elem: A): FSet[A] = {
    if (head == elem) tail
    else tail - elem + head
  }
  override infix def --(anotherSet: FSet[A]): FSet[A] = filter(x=> !anotherSet(x))
  override infix def &(anotherSet: FSet[A]): FSet[A] = filter(anotherSet)

}

object FSet {
  def apply[A](values: A*): FSet[A] = {
    @tailrec
    def buildSet(valuesSeq: Seq[A], acc: FSet[A]): FSet[A] =
      if (valuesSeq.isEmpty) acc
      else buildSet(valuesSeq.tail, acc + valuesSeq.head)

    buildSet(values.toSeq, Empty[A]())
  }
}


object FunctionalSetPlayground {

  def main(args: Array[String]): Unit = {

    val first5 = FSet(1,2,3,4,5)
    val someNumbers = FSet(3,4,5,6,7,8,9,10)
    println(first5.contains(5))
    println(first5(6))
    println((first5 + 6).contains(6))
    println(first5.map(_ * 2).contains(10))

    println((first5 - 3).contains(3))
    println((first5 -- someNumbers).contains(4))
    println((first5 & someNumbers).contains(4))

    val naturals = new PBSet[Int](_ => true)
    println(naturals.contains(5237548)) // true
    println(!naturals.contains(0)) // false
    println((!naturals + 1 + 2 + 3).contains(3)) // true
    // println(!naturals.map(_ + 1)) // throws - map/flatMap/foreach will not work
  }
}

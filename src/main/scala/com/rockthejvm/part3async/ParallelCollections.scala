package com.rockthejvm.part3async

import scala.collection.parallel.*
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.immutable.ParVector

object ParallelCollections {

  val aList = (1 to 1000000).toList
  val anIncrementedList = aList.map(_ + 1) // 1,000,000 operations
  val parList: ParSeq[Int] = aList.par
  val aParallelizedIncrementedList =
    parList.map(_ + 1) // 1,000,000 operations in parallel
  /*
    Applicable for
    - Seq
    - Vector
    - Array
    - Maps
    - Sets

    use-case: faster processing
   */

  // parallel collection build explicitly
  val aParVector = ParVector[Int](1, 2, 3, 4, 5, 6)

  def measure[A](expression: => A): Long = {
    val time = System.currentTimeMillis()
    expression // forcing evaluation
    System.currentTimeMillis() - time
  }

  def compareListTransformation(): Unit = {
    val list = (1 to 1000000).toList
    println("List creation done")

    val serialTime = measure(list.map(_ + 1))
    println(s"Serial time: $serialTime ms")

    val parallelTime = measure(list.par.map(_ + 1))
    println(s"Parallel time: $parallelTime ms")
  }

  def demoUndefinedOrder(): Unit = {
    val aList = (1 to 1000).toList
    val reduction = aList.reduce(_ - _)
    // [1,2,3].reduce(_ - _) = 1 - 2 - 3 = -4
    // [1,2,3].reduce(_ - _) = 1 - (2 - 3) = 2

    val parallelReduction = aList.par.reduce(_ - _)
    // order of operation is undefined, returns different results

    println(s"sequential reduction: $reduction")
    println(s"parallel reduction: $parallelReduction")
  }

  // for associative ops, result is deterministic
  def demoDefinedOrder(): Unit = {
    val strings =
      "I love parallel collections but I must be careful".split(" ").toList
    val concatenation = strings.reduce(_ + " " + _)
    val parallelConcatenation = strings.par.reduce(_ + " " + _)

    println(s"sequential concatenation: $concatenation")
    println(s"parallel concatenation: $parallelConcatenation")
  }

  def demoRaceConditions(): Unit = {
    var sum = 0
    (1 to 1000).toList.foreach(elem => sum += elem)
    println(sum)
  }
  // WARNING: careful with doing parallel operations with imperative programming

  def main(args: Array[String]): Unit = {
    //compareListTransformation()
    //demoDefinedOrder()
    demoRaceConditions()
  }

}

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

  def main(args: Array[String]): Unit = {}

}

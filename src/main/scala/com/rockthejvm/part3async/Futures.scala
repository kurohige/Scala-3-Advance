package com.rockthejvm.part3async

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Futures {

  def calculateMeaningOfLife(): Int = {
    // simulate a long computation
    Thread.sleep(1000)
    42
  }

  // thread pool (scala-specific)
  val executor = Executors.newFixedThreadPool(4)
  given executionContext: ExecutionContext= ExecutionContext.fromExecutorService(executor)

  // a future = a computation which will complete at some point
  val aFuture: Future[Int] = Future.apply(calculateMeaningOfLife())// given executionContext will be passed here

  // Option[Try[Int]], because
  // - we don't know if we have a value
  // - if we do, that can be failed computation
  val futureInstantResult: Option[Try[Int]] = aFuture.value

  // callbacks
    aFuture.onComplete {
        case Success(meaningOfLife) => println(s"I've completed with the meaning of life: $meaningOfLife")
        case Failure(exception) => println(s"My async computation failed: $exception")
    }

  def main(args: Array[String]): Unit = {
    println(futureInstantResult)
    Thread.sleep(2000)
    executor.shutdown()
  }

}

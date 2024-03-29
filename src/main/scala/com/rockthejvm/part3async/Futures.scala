package com.rockthejvm.part3async

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration.*

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

  /*
    Functional composition on futures
   */
  case class Profile(id: String, name: String) {
    def sendMessage(anotherProfile: Profile, message: String): Unit = println(s"${this.name} sending message to: ${anotherProfile.name}: $message")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "rtjvm.id.1-daniel" -> "Daniel",
      "rtjvm.id.2-jane" -> "Jane",
      "rtjvm.id.3-mark" -> "Mark"
    )
    val friends = Map(
      "rtjvm.id.2-jane" -> "rtjvm.id.3-mark"
    )

    val random = new scala.util.Random()
    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bestFriendId = friends(profile.id)
      Profile(bestFriendId, names(bestFriendId))
    }
  }

  // problem: send a message to my best friend
  def sendMessageToBestFriend(profileId: String, message: String): Unit = {
    // step 1: fetch profile
    val profileFuture = SocialNetwork.fetchProfile(profileId)
    profileFuture.onComplete {
      case Success(profile) =>
        // step 2: fetch best friend
        val bestFriendFuture = SocialNetwork.fetchBestFriend(profile)
        bestFriendFuture.onComplete {
          case Success(bestFriend) =>
            // step 3: send a message
            profile.sendMessage(bestFriend, message)
          case Failure(exception) => println(s"Error fetching best friend: $exception")
        }
      case Failure(exception) => println(s"Error fetching profile: $exception")
    }
  }

  // on complete is a hassle.
  // solution: functional composition of futures
  def sendMessageToBestFriend_v2(profileId: String, message: String): Unit = {
    // step 1: fetch profile
    val profileFuture = SocialNetwork.fetchProfile(profileId)
    // step 2: fetch best friend
    profileFuture.flatMap {
      profile => SocialNetwork.fetchBestFriend(profile).map {
        bestFriend => profile.sendMessage(bestFriend, message)
      }
    }
  }

    // for-comprehensions

  def sendMessageToBestFriend_v3(profileId: String, message: String): Unit = {
    // step 1: fetch profile
    val profileFuture = SocialNetwork.fetchProfile(profileId)
    // step 2: fetch best friend
    for {
      profile <- profileFuture
      bestFriend <- SocialNetwork.fetchBestFriend(profile)
    } yield  profile.sendMessage(bestFriend, message)
  }

    // fallbacks
  val profileNoMatterWhat: Future[Profile] = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("rtjvm.id.0-dummy", "Forever alone")
  }

  val aFetchProfileNoMatteWhat: Future[Profile] = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("rtjvm.id.0-dummy")
  }
  val fallBackProfile: Future[Profile] = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("rtjvm.id.0-dummy"))

  val janeProfileFuture = SocialNetwork.fetchProfile("rtjvm.id.2-jane")
  val janeFuture: Future[String] = janeProfileFuture.map(janeProfile => janeProfile.name)
  val janesBestFriend: Future[Profile] = janeProfileFuture.flatMap(janeProfile => SocialNetwork.fetchBestFriend(janeProfile))
  val janesBestFriendRestricted: Future[Profile] = janesBestFriend.filter(profile => profile.name.startsWith("M"))

  /*
    Block for a future
   */

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    // "API's"
    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    // "External API"
    def purchase(username: String, item: String, merchantName: String, price: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture: Future[String] = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, price)
      } yield transaction.status

      // blocking call
      Await.result(transactionStatusFuture, 2.seconds) // throws TimeoutException if the future doesn't complete in 2sec
    }
  }

  /*
    Promises
   */

  def demoPromises(): Unit = {
    val promise = Promise[Int]() // "controller" over a future
    val futureInside: Future[Int] = promise.future

    // thread 1 - "consumer" : monitor the future for completion
    futureInside.onComplete {
      case Success(value) => println(s"[Consumer] I've just been completed with $value")
      case Failure(ex) => ex.printStackTrace()
    }

    // thread 2 - "producer" : fullfilling the promise
    val producerThread = new Thread(() => {
      println("[Producer] crunching numbers...")
      Thread.sleep(1000)
      // "fullfilling" the promise
      promise.success(42)
      println("done")
    })

    producerThread.start()
  }
  /*
    Exercises
    1) fulfill a future IMMEDIATELY with a value
    2) inSequence(fa, fb) in sequence: make sure the first future completes before returning the second future
    3) first(fa, fb) : returns a new future with the first value of the two futures
  */
  // 1
  def completeImmediately[T](value: T): Future[T] = Future(value) // async completion as soon as possible
  def completeImmediately_v2[T](value: T): Future[T] = Future.successful(value) // synchronous completion

  // 2
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = first.flatMap(_ => second)


  // 3
  def first[A](f1: Future[A], f2: Future[A]): Future[A] = {
    val promise = Promise[A]()
    f1.onComplete(result1 => promise.tryComplete(result1))
    f2.onComplete(result2 => promise.tryComplete(result2))

    promise.future
  }

  // 4
  def last[A](f1: Future[A], f2: Future[A]): Future[A] = {
    val bothPromise = Promise[A]()
    val lastPromise = Promise[A]()

    val checkAndComplete = (result: Try[A]) =>
      if (!bothPromise.tryComplete(result))
        lastPromise.complete(result)

    f1.onComplete(checkAndComplete)
    f2.onComplete(checkAndComplete)

    lastPromise.future
  }


  // 5
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action, condition)
      }

  def testRetries(): Unit = {
    val random = new scala.util.Random()
    val action = () => Future {
      Thread.sleep(100)
      val nextValue = random.nextInt(100)
      println(s"generated $nextValue")
      nextValue
    }

    val predicate = (x: Int) => x < 10

    retryUntil(action, predicate).foreach(result => println(s"settled at $result"))
    Thread.sleep(10000)
  }

  def main(args: Array[String]): Unit = {
//    sendMessageToBestFriend_v3("rtjvm.id.2-jane", "Hello, best friend!")
//    println("purchasing...")
//    BankingApp.purchase("Daniel-234", "shoes", "rtjvm-store", 3000)
//    println("purhcase finished")
    // demoPromises()
//    lazy val fast = Future {
//      Thread.sleep(100)
//      1
//    }
//    lazy val slow = Future {
//      Thread.sleep(200)
//      2
//    }
//    first(fast, slow).foreach(result => println(s"First: $result"))
//    last(fast, slow).foreach(result => println(s"Last: $result"))

    testRetries()
    Thread.sleep(2000)
    executor.shutdown()
  }

}

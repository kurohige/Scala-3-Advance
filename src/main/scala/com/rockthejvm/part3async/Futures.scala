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

  def main(args: Array[String]): Unit = {
    sendMessageToBestFriend_v3("rtjvm.id.2-jane", "Hello, best friend!")
    Thread.sleep(2000)
    executor.shutdown()
  }

}

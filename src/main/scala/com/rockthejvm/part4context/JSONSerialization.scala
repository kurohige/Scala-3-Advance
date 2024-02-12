package com.rockthejvm.part4context

import java.util.Date

object JSONSerialization {

  /*
    Users, posts, feeds
    Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
    1- intermediate data: numbers, strings, lists, dates, objects
    2 - type class to convert data to intermediate data
    3 - serialize to JSON
   */

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue])
      extends JSONValue {
    override def stringify = values
      .map { case (key, value) =>
        "\"" + key + "\":" + value.stringify
      }
      .mkString("{", ",", "}")
  }

  val data = JSONObject(
    Map(
      "user" -> JSONString("Daniel"),
      "posts" -> JSONArray(
        List(
          JSONString("Scala rocks!"),
          JSONNumber(42)
        )
      )
    )
  )

  // part 2 - type class pattern
  // 1 - TC definition
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // 2 - TC Instances for User, Post, Feed
  given stringCoverter: JSONConverter[String] with
    override def convert(value: String): JSONValue = JSONString(value)

  given intConverter: JSONConverter[Int] with
    override def convert(value: Int): JSONValue = JSONNumber(value)

  given dateConverter: JSONConverter[Date] with
    override def convert(value: Date): JSONValue = JSONString(value.toString)


  given userConverter: JSONConverter[User] with
    override def convert(user: User): JSONValue = JSONObject(
      Map(
        "name" -> stringCoverter.convert(user.name),
        "age" -> intConverter.convert(user.age),
        "email" -> stringCoverter.convert(user.email)
      )
    )

  given postConverter: JSONConverter[Post] with
    override def convert(post: Post): JSONValue = JSONObject(
      Map(
        "content" -> stringCoverter.convert(post.content),
        "createdAt" -> stringCoverter.convert(post.createdAt.toString)
      )
    )

  given feedConverter: JSONConverter[Feed] with {
    override def convert(feed: Feed): JSONValue = JSONObject(
      Map(
        "user" -> userConverter.convert(feed.user),
        "posts" -> JSONArray(feed.posts.map(postConverter.convert))
      )
    )
  }

  // 3 - user-facing API
  object JSONConverter{
    def convertToJson[T](value: T)(using converter: JSONConverter[T]): JSONValue = converter.convert(value)

    def apply[T](instance:JSONConverter[T]): JSONConverter[T] = instance
  }

  // example
  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@rockthejvm.com")
  val feed = Feed(john, List(
    Post("Hello, Scala!", now),
    Post("Look at this cute puppy!", now)
  ))

  // 4 - extension methods

  object JSONSyntax {
    extension [T](value: T) {
      def toIntermediate( using converter: JSONConverter[T]): JSONValue = converter.convert(value)

      def toJSON(using converter: JSONConverter[T]): String = toIntermediate.stringify
    }
  }

  def main(args: Array[String]): Unit = {
    import JSONSyntax.*
    println(john.toJSON)
    println(feed.toJSON)
  }

}

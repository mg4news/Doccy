package com.mg4news.matcha

import java.util.concurrent.TimeUnit

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.mongodb.scala._

object Helpers {

  implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] {
    override val converter: Document => String = doc => doc.toJson
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: C => String = doc => doc.toString
  }

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: C => String

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
    def headResult(): C = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))
    def printResults(initial: String = ""): Unit = {
      if (initial.length > 0) print(initial)
      results().foreach(res => println(converter(res)))
    }
    def printHeadResult(initial: String = ""): Unit = println(s"$initial${converter(headResult())}")
  }

  // Execute for a single result
  private val waitDuration = Duration(5, "seconds")
  implicit class SingleObservableExecutor[T](observable: SingleObservable[T]) {
    def result(): T = Await.result(observable.toFuture(), waitDuration)
  }

}


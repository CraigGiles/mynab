package com.gilesc
package endpoints

import com.gilesc.generator._

import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.Checkers
import java.nio.charset.StandardCharsets

class TransactionEndpointSpec extends FlatSpec
  with Matchers
  with Checkers
  with DateGenerator
  with CategoryGenerator
  with TransactionGenerator {

  import com.gilesc.endpoint.TransactionEndpoint
  import com.gilesc.mynab.PresentationData._

  behavior of "TransactionEndpoint - POST"
  it should "allow me to create a transaction" in {
    val data = arbitraryTransactionData.arbitrary.sample.get
    val input = Input.post(s"/${TransactionEndpoint.path}")
      .withBody[Application.Json](data, Some(StandardCharsets.UTF_8))

    val res = TransactionEndpoint.postTransaction(input)
    res.awaitOutputUnsafe().map(_.status) shouldBe(Some(Status.Created))
    res.awaitValueUnsafe().isDefined shouldBe(true)

    val Some(transaction) = res.awaitValueUnsafe()
    transaction.date shouldBe (data.date)
    transaction.payee shouldBe (data.payee)
    transaction.category.major shouldBe (data.category.major)
    transaction.category.minor shouldBe (data.category.minor)
    transaction.memo shouldBe (data.memo)
    transaction.withdrawal shouldBe (data.withdrawal)
    transaction.deposit shouldBe (data.deposit)

    // Todo.get(todo.id).isDefined === true
  }

  behavior of "TransactionEndpoint - GET"
  it should "return a list of transactions" in {
    val hello = Vector.empty[TransactionData]
    val result = TransactionEndpoint.getTransactions(Input.get(s"/${TransactionEndpoint.path}"))

    result.awaitValueUnsafe() shouldBe(Some(hello))
  }

}

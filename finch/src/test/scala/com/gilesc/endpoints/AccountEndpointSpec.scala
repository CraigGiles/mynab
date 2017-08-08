package com.gilesc
package endpoints

import com.twitter.finagle.http.Status
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import org.scalatest.{FlatSpec, Matchers}
import java.nio.charset.StandardCharsets

import com.gilesc.generator._

class AccountEndpointSpec extends FlatSpec
  with Matchers
  // with Checkers
  with DateGenerator
  with CategoryGenerator
  with AccountGenerator {

  import com.gilesc.mynab.PresentationData._
  import com.gilesc.endpoint.AccountEndpoint
  import com.gilesc.endpoint.AccountEndpoint._

  def createNewAccount(data: AccountResource): AccountData = {
    val input = Input.post(s"/${AccountEndpoint.accounts}")
      .withBody[Application.Json](data, Some(StandardCharsets.UTF_8))

    val res = AccountEndpoint.postAccount(input)
    res.awaitOutputUnsafe().map(_.status) shouldBe(Some(Status.Created))
    res.awaitValueUnsafe().isDefined shouldBe(true)

    val Some(account) = res.awaitValueUnsafe()
    account
  }

  behavior of "AccountEndpoint - POST"
  it should "allow me to create a new account with a given name" in {
    val ctx = genAccountResource.sample.get
    val account = createNewAccount(ctx)
    account.name shouldBe(ctx.name)
    account.transactions shouldBe(Vector.empty[TransactionData])
  }

  behavior of "AccountEndpoint - GET"
  it should "allow me to get an account by its id" in {
    val ctx = genAccountResource.sample.get
    val account = createNewAccount(ctx)
    val input = Input.get(s"/${AccountEndpoint.accounts}/${account.id}")
    val res = AccountEndpoint.getAccount(input)
    val Some(a) = res.awaitValueUnsafe()

    a.name shouldBe(ctx.name)
    a.transactions shouldBe(Vector.empty[TransactionData])
  }
}


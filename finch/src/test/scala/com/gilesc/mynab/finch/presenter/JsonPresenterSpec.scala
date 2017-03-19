package com.gilesc.mynab.finch.presenter

import java.time.LocalDate

import com.gilesc.mynab._

class JsonPresenterSpec extends TestCase
  with TestCaseHelpers
  with MockAccountCreation
  with MockTransactionCreation {

  val t = trans(1, LocalDate.parse("2017-03-19"), "Payee", "Major", "Minor", "Memo", 100, 0)
  val account = bankingWithId(0L, "hithere", Vector(t))
  val expected =
    """
      |{
      |  "id" : 0,
      |  "name" : "hithere",
      |  "type" : "Banking",
      |  "transactions" : [
      |    {
      |      "id" : 1,
      |      "date" : "2017-03-19",
      |      "payee" : "Payee",
      |      "category" : {
      |        "major" : "Major",
      |        "minor" : "Minor"
      |      },
      |      "memo" : "Memo",
      |      "withdrawal" : 100.0,
      |      "deposit" : 0.0,
      |      "cleared" : false
      |    }
      |  ]
      |}
    """.stripMargin

  "Json Presenter" should "convert an account object to proper JSON" in {
    CirceAccountPresenter.present(account).toString().trim should be(expected.trim)
  }
}

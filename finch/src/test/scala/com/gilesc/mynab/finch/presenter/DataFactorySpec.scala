package com.gilesc.mynab.finch.presenter

import java.time.LocalDate

import com.gilesc.mynab._
import com.gilesc.mynab.account.Banking

class DataFactorySpec extends TestCase
  with TestCaseHelpers
  with MockAccountCreation
  with MockTransactionCreation {

  val id = 0L
  val name = "hithere"
  val typ = Banking.toString

  val t = trans(1, LocalDate.parse("2017-03-19"), "Payee", "Major", "Minor", "Memo", 100, 0)
  val tr = Vector(PresentationData.transaction(t))
  val account = bankingWithId(id, name, Vector(t))

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

  "Data Factory" should "Convert correctly" in {
    PresentationData.account(account) should be(AccountData(id, name, typ, tr))
  }
}

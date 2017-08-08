package com.gilesc
package mynab
package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
// import de.heikoseeberger.akkahttpcirce.CirceSupport
// import me.archdev.restapi.http.SecurityDirectives
// import me.archdev.restapi.models.UserEntity
// import me.archdev.restapi.services.AuthService
import io.circe.generic.auto._
import io.circe.syntax._
import com.gilesc.mynab.account._
import de.heikoseeberger.akkahttpcirce._

object PresentationData {
  import com.gilesc.mynab.account._
  import com.gilesc.mynab.transaction._
  import com.gilesc.mynab.category._

  case class CategoryData(major: String, minor: String)
  case class TransactionData(id: String, date: String, payee: String,
    category: CategoryData, memo: String, withdrawal: Double, deposit: Double,
    cleared: Boolean)
  case class AccountData(id: String, userId: String, name: String, transactions: Vector[TransactionData])

  val fromCategory: Category => CategoryData = { (category: Category) =>
    CategoryData(
      category.major.value,
      category.minor.value)
  }

  val fromTransaction: Transaction =>  TransactionData = { (t: Transaction) =>
    TransactionData(
      t.id.toString,
      t.date.toString,
      t.payee.value,
      fromCategory(t.category),
      t.memo.value,
      t.withdrawal.value.toDouble,
      t.deposit.value.toDouble,
      t.cleared.value)
  }

  val fromAccount: Account => AccountData = { (account: Account) =>
    val transactions = account.transactions map fromTransaction
    AccountData(
      account.id.value.toString,
      account.userId.value.toString,
      account.name.value,
      transactions)
  }
}


object AccountRoute extends FailFastCirceSupport {
  val endpoint = "accounts"
  case class AccountResource(name: String)

  val postAccountsRoute = path(endpoint) {
    decodeRequest {
      entity(as[AccountResource]) { resource =>
        // TODO: This uuid should be the users uuid
        val uuid = java.util.UUID.randomUUID()
        val ctx = AccountContext(UserId(uuid), AccountName(resource.name))
        val data = Accounts.create(ctx)

        complete(StatusCodes.Created -> PresentationData.fromAccount(data).asJson)
      }
    }
  }

  val getAccountsRoute = path(endpoint / JavaUUID) { id =>
    get {
        val findby = FindBy.Id(AccountId(id))
        val account = Accounts.findById(findby)

        account match {
          case Some(a) =>
            complete(PresentationData.fromAccount(a).asJson)

          case None =>
            complete(StatusCodes.NotFound)
        }

    }
  }

  val routes = getAccountsRoute
}

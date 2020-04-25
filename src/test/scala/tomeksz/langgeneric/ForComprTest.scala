package tomeksz.langgeneric

import tomeksz.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class ForComprTest extends UnitSpec {

  def getPriceQuote(ticker: String): Future[BigDecimal] = Future {
    BigDecimal.valueOf(100)
  }

  def currencyCovert(amount: BigDecimal): Future[BigDecimal] = Future {
    amount * 100
  }

  it should "for comprehension" in {
    val price = for {
      quote <- getPriceQuote("ff")
      amountPLN <- currencyCovert(quote)
    } yield amountPLN

    price.onComplete {
        case Success(v) => println(v)
        case Failure(ex) => println("Failure: " + ex.getMessage)
    }
  }
}

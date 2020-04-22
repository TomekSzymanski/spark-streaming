package streaming

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.scalatest.BeforeAndAfter

abstract class IntegrationSpec extends UnitSpec with StreamingSuiteBase with BeforeAndAfter{
}

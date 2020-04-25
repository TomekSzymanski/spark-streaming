package tomeksz

import org.scalatest._
import org.scalatest.mockito.MockitoSugar

abstract class UnitSpec extends FlatSpec with Matchers with GivenWhenThen with MockitoSugar with Inside with BeforeAndAfterEach with BeforeAndAfterAll

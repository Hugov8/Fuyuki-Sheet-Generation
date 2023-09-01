package model


import org.scalatest.flatspec.AnyFlatSpec
import model.service.ScriptServiceImpl
import model.exception.ApplicationException

class ScriptServiceTest extends AnyFlatSpec {
    val mailTest: String = sys.env.get("TEST_MAIL") match {
        case Some(value) => value
        case None => throw new ApplicationException("Env variable not found : TEST_MAIL")
    }
    "War" should "be generated" in {
        val url: String = ScriptServiceImpl.generateWar("8090", mailTest)
        println(url)
        assert(!url.isEmpty())
    }

    "Long War" should "be generated" in {
        val url: String = ScriptServiceImpl.generateWar("107", mailTest)
        assert(!url.isEmpty)
    }
}
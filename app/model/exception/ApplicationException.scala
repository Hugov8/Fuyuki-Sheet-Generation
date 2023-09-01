package model.exception

class ApplicationException(val message: String) extends Throwable {
    override def getMessage(): String = message
}
class ParsingException(override val message: String) extends ApplicationException(message)
class ParsingAtlasException(override val message: String) extends ParsingException(message)
class ParsingAssemblerException(override val message: String) extends ParsingException(message)
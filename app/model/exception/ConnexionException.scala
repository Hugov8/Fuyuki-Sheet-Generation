package model.exception

class ConnexionException(val message: String) extends Throwable {
    override def getMessage(): String = message
}
class NotExistException(override val message: String) extends ConnexionException(message)
import model.service.ServiceLocator
import ModeBatch.{CREATION => CREATION}
import ModeBatch.{REFRESH => REFRESH}
import org.slf4j.LoggerFactory
import model.exception.ApplicationException

object ModeBatch extends Enumeration {
  type ModeBatch = Value

  val CREATION, REFRESH = Value
}

object App {
    val logger = LoggerFactory.getLogger(getClass)

    def main(args: Array[String]): Unit = {
        if(args.size < 3) {
            logger.warn(s"Utilisation des paramètres : <Mode CREATION ou REFRESH> <idWar récupérable sur Atlas> <Mail en cas de création, idSreadsheet depuis google sheet en cas de refresh")
            throw new ApplicationException(s"Nombre de parametre insuffisant : $args")
        }
        val mode : ModeBatch.ModeBatch = ModeBatch.withName(args(0))
        val idWar = args(1)
        mode match {
            case CREATION => {
                //TODO verifier mail
                val mail = args(2)
                ServiceLocator.scriptService.generateWar(idWar, mail)
            }
            case REFRESH => {
                val spreadSheetId = args(2)
                ServiceLocator.scriptService.updateWar(idWar, spreadSheetId)
            }
        }
    }
}
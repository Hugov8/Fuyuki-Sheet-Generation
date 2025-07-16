import model.service.ServiceLocator

object App {
    def main(args: Array[String]): Unit = {
        //TODO
        ServiceLocator.scriptService.generateWar(args.apply(0), args.apply(1))
    }
}
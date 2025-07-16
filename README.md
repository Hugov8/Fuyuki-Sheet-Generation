# Fuyuki-Generation-Sheet
Webapp de génération de sheet de traduction
 ## Développement
 Java 11 est recommandé
 
 Utilisation de sbt comme outil de CI. Pour lancer l'application en mode développement, lancer la commande :
 ```bash
 sbt run
 ```

 Pour lancer les tests unitaires : 
 ```bash
 sbt test
 ```

 ## Déploiement
 ### Déploiement direct
 Pour déployer l'application, générer une clé de production et stocker la dans application.conf. Pour ce faire, lancer la commande dans la console sbt :
 ```bash
 playUpdateSecret
 ```
 Build le projet en mode production se fait via la commande :
 ```bash
 sbt clean stage
 ```
 Lancer l'application
 ```bash
 target/universal/stage/bin/fuyuki-gen-sheet
 ```
 Plus d'info [ici](https://www.playframework.com/documentation/2.8.x/Deploying)

 ### Déploiement docker
 Pour construire l'image de l'application, lancer dans la console sbt :
 ```bash
 docker:publishLocal
 ```
 L'image docker sera généré. Pour lancer le container :
 ```bash
 docker run --rm -p 9000:9000 fuyuki-gen-sheet:1.0
 ```

 Pour lancer le docket en mode batch
  ```bash
 docker run --rm fuyuki-gen-sheet:1.0 <oarams>
 ```
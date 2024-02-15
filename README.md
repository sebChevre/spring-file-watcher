# File watcher
*spring application which checks a given directory and logs the changes in console*

## Run app
*pour démarrer l'application, la variable d'environnement **WATCH_FOLDER** doit être configurée et doit pointer sur un **répertoire existant***

## Exemples

### Java
> java -DWATCH_FOLDER=/my_folder/test -jar file-watcher.jar

### Docker
> docker run -e WATCH_FOLDER=/my_folder/test sebchevre/file-watcher:1.1.0
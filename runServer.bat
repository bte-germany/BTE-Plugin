copy "C:\Users\Tobias\Documents\Coding\BTE-Plugin\target\bteplugin-1.0-SNAPSHOT.jar" "C:\Users\Tobias\Documents\Coding\Spigot_1.12.2\plugins\bteplugin-1.0-SNAPSHOT.jar"
cd "C:\Users\Tobias\Documents\Coding\Spigot_1.12.2"
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar spigot-1.12.2.jar nogui
cd "C:\Users\Tobias\Documents\Coding\BTE-Plugin"
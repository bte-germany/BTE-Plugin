# BTE-Plugin

Ein Plugin für den BTE-Germany Server.

## Commands

| Command | Permission | Beschreibung |
|---|---|---|
| /fly | bte.fly | Schaltet  Fliegen wie im Creative-Mode an und aus.<br>Für Spieler mit der Permission `bte.fly.join` wird das Fliegen automatisch beim betreten des Servers aktiviert |
| /speed | bte.speed | Erlaubt es, den Lauf- und Fluggeschwindigkeit anzupassen |
| /build | bte.build | Wechselt zwischen den in der Konfiguration hinterlegten Gamemodes |
| /visit | - | Befehle für das Tour-System. Zum erstellen und verwalten von Touren wird die Permission `bte.visit.create` benötigt |

## Konfiguration

Für `/build` könne die Gamemodes eingestellt werden, zwischen denen gewechselt wird. Die entsprechenden Einstellungen können in der **config.yml** eingestellt werden. Gültige Werte sind:
* 0 => Survival
* 1 => Creative
* 2 => Adventure
* 3 => Spectator
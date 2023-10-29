### Motivations
J'ai fait cette repo pour fournir un outil permettant d'utiliser les mirroirs VRP Public sur linux, je n'ai pas de pc sous windows et je n'ai pas d'argent a depenser dans ça. Cet outil ne fait que télécharger, il doit etre utiliser en complement de sidenoder par exemple ou ADB.

Je ne suis aucunement affilier de près ou de loin au groupe VRPirates ni a leurs actions. 
#### Manual mode
##### Download Configuration
First download public config from vrpirates website

[https://wiki.vrpirates.club/downloads/vrp-public.json](https://wiki.vrpirates.club/downloads/vrp-public.json)

##### Decode
Decode password using base64 decoder [website](https://www.base64decode.org/)

You have now two things : 
- url : ex (https://skrazzle.glomtom.cyou/)
- password decoded ex (Z0w1OVZmZ1B4b0hS => gL59VfgPxoHR)

#### Speaking with vrpirates server
##### Setup vrpirates server in rclone
[![asciicast](https://asciinema.org/a/617808.svg)](https://asciinema.org/a/617808)
##### Download manifest using rclone
enter this command to download the manifest containing the list of all games
```sh
rclone copy --interactive vrpirates:/meta.7z ./ --progress 
```
Extract the meta.7z password is the "password decode" here gL59VfgPxoHR
##### Download Games
You have now the manifest containing all games name, to download a game : 
###### Get the full name
To get the full name of a game open VRP-GameList.txt
and copy the second column of the document for exemple
a want to download AimVR I search the line 

"AimVR;AimVR v15+0.1.15 -VRP;app.pinbit.aimvr;15;2023-07-27 10:59 UTC;169" 

and a copy only "AimVR v15+0.1.15 -VRP"

###### Get the encoded key
The path to download each game in VRP are encoded to get this encoded key, use this script in a online compiler like [https://www.jdoodle.com/compile-c-sharp-online/](https://www.jdoodle.com/compile-c-sharp-online/)
```c#
using System;
using System.Security.Cryptography;
using System.Text;

class Program
{
    static void Main() {
        string gameName = "The Climb 2 v974+2.2 -VRP"; //REPLACE ME WITH THE FULL NAME;
        string gameNameHash = string.Empty;
        using (MD5 md5 = MD5.Create())
                    {
                        byte[] bytes = Encoding.UTF8.GetBytes(gameName + "\n");
                        byte[] hash = md5.ComputeHash(bytes);
                        StringBuilder sb = new StringBuilder();
                        foreach (byte b in hash)
                        {
                            _ = sb.Append(b.ToString("x2"));
                        }

                        gameNameHash = sb.ToString();
        }

        Console.Write(gameNameHash);
    }
}
```

###### Download Game
You can now download the game, simply use this command
```sh
rclone copy vrpirates:/<REPLACE WITH ENCODED KEY>/ ./ --transfers 1 --multi-thread-streams 0 --progress --rc
```
Exemple
```sh
rclone copy vrpirates:/eaccf93009355a6c374682a637cb6cff/ ./ --transfers 1 --multi-thread-streams 0 --progress --rc
```
###### Extract the game
You have now a archive in multipart you can unzip the archive, the password is the "password decode"

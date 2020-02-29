# cck-rep
First bot project- FeelsGoodBot

# FeelsGoodBot
A bot created in Java, implementing JDA and the JRAW, and using MariaDB on my local PC (localhost) to manage the data.

Hey! So this bot was created by me to learn more about bot-making for Discord!

I am using https://github.com/DV8FromTheWorld/JDA/ and https://github.com/mattbdean/JRAW to make the things happen that happen here.
The function are as follows:
-You are able to moderate your discord server with this bot (though the extent of which are still relatively limited)

-You can add words to an SQL databank that you do not want your users to use.

-You can pull wallpapers from reddit.com/r/wallpapers
  
 ## Commands that EVERY User can use: 
  
* !counter -> `retrieves the top 5 user and their counts, of a word the have said from the SQL Databank, in the given server` 
  - for these counters, you have to program them in, in the future i might add a seperate command
                                        
* !scp -> `the definition of SCP from the Wiki page`

* !mycount -> `the amount of a particular word you have said`

* !pfp -> `the profile picture of a given user`

* !wallpaper **[0-30]** -> `gets the top wallpapers of today, the number specifies the ranking `
  - for example if you want the 3rd best you would use !wp 3

* !flushedcount  `the top 5 users who have used the :flushed: emoji the most (still have to fix bugs)`

### Commands that only MODERATORS can use

* !purge **[ID or Tag of User]** **[String of words to be deleted]**
  - Alternatively, leave this empty to purge a users messages completely in this channel

* !setpoints/sp **[ID or Tag]** **[0-99999999]**
  - Points, as of yet, have no use but I'm planning on implementing something for its use later

* !add **[ID of user or tag the user]** `adds user to database`

* !del **[Words, Links, Anything basically]** `adds that certain String into the database, and the bot deletes any messages containing it`


Something that I have not yet changed completely, is how the bot knows which person has certain privileges. Right now, it goes by colors
that i have set certain roles in my discord server to. I do not think that is a hard thing to fix though!

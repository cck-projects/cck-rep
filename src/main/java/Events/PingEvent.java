package Events;

import SQLDatabase.SQLFC;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class PingEvent extends ListenerAdapter {
    EmbedBuilder eb = new EmbedBuilder();

    SQLFC connection = new SQLFC();
    LinkGetter lg = new LinkGetter();
    int tryCatchAmount = 4;
    boolean whileLoop = true;

    public PingEvent() throws SQLException, IOException {
    }

    public boolean hasPriv(MessageReceivedEvent event) {
        int roleSize = event.getMember().getRoles().size();
        /*Check in the for () if the user has a role that you want to allow to use that specific command*/
        for (int i = 0; i < roleSize; i++) {
            if (event.getMember().getRoles().get(i).getIdLong() == 675134894665367565L || event.getMember().getRoles().get(i).getIdLong() == 675127266727952445L) { //rolecheck 2.0
                return true;
            }
        }
        return false;
    }

    public Long getGuildId(MessageReceivedEvent event) {
        return event.getGuild().getIdLong();
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (connection.getBannedWord(event, getGuildId(event))) {
            event.getMessage().delete().queue();
        }


        if (/*msg.getChannel().getId().compareTo("675143556876468225") == 0*/ true) {
            if (msg.getContentRaw().equals("!fgb")) {
                eb = new EmbedBuilder();

                eb.setTitle("**List of commands:**");
                eb.addField("Commands that **EVERY** User can use:", "!counter !scp !mycount !pfp !wp [0-30]", true);
                eb.addField("Commands that only **MODERATORS** can use:", "NEW------> **!purge [ID or Tag of User] [String of words to be deleted(Alternatively, leave this empty to purge a users messages completely in this channel!)]**\n!add [ID of user or tag the user] -adds user to database\n" +
                        "!setpoints/sp [ID or Tag] [0-99999999]", false).setColor(Color.yellow);
                eb.setAuthor("***NEW COMMAND*** !wallpaper [0-30] // TOP x post of r/wallpapers of today!");


                MessageChannel channel = msg.getChannel();
                channel.sendMessage(eb.build()).queue();
            }

            if (args[0].equals("!counter")) {
                MessageChannel channel = event.getChannel();
                try {
                    eb = new EmbedBuilder();
                    eb.setTitle("**Top 5 Weirdcounter leaders**");
                    eb.setColor(Color.RED);
                    eb.setDescription(connection.getAllUserWCount(getGuildId(event)));
                    channel.sendMessage(eb.build()).queue();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (args[0].equals("!flushedcount") || args[0].equals("!fcount") || args[0].equals("!fc")) {
                MessageChannel channel = event.getChannel();
                try {
                    eb = new EmbedBuilder();
                    eb.setTitle("**Top 5 most flushed users**");
                    eb.setColor(Color.RED);
                    eb.setDescription(connection.getFlushedCount(getGuildId(event)));
                    channel.sendMessage(eb.build()).queue();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (args[0].equals("!scp")) {
                MessageChannel channel = msg.getChannel();
                channel.sendMessage("The SCP Foundation is a fictional organization " +
                        "documented by the web-based collaborative-fiction project of the same name. " +
                        "Within the website's fictional setting, the SCP Foundation is responsible for " +
                        "locating and containing individuals, entities, locations, and objects that violate " +
                        "natural law (referred to as SCPs). The real-world website is community-based and " +
                        "includes elements of many genres such as horror, science fiction, and urban fantasy.\n" +
                        "\n").queue();
            }

            if (args[0].equals("!mycount")) {
                MessageChannel channel = event.getChannel();
                Long id = Long.parseLong(msg.getAuthor().getId());
                eb = new EmbedBuilder();
                eb.setColor(Color.RED);
                eb.setTitle("*Your ID and your count*");

                try {
                    eb = new EmbedBuilder();
                    eb.setDescription(connection.getOneUser(id, getGuildId(event)));
                    channel.sendMessage(eb.build()).queue();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (args[0].equalsIgnoreCase("!setpoints") || args[0].equalsIgnoreCase("!sp") || args[0].equalsIgnoreCase("!spoints")) {
                if (hasPriv(event)) {
                    int size = event.getMessage().getMentionedMembers().size();
                    try {
                        Long number = Long.parseLong(args[2]);
                        Long myID;
                        if (number > 99999999L) {
                            event.getChannel().sendMessage("Okay you have to calm down buddy that number is too large!\n" +
                                    "**BUT** you can set the value anywhere from 0-99999999\nTry again!").queue();
                        } else {
                            if (size > 0) {
                                myID = event.getMessage().getMentionedMembers().get(0).getIdLong();
                            } else {
                                myID = Long.parseLong(args[1]);
                            }


                            if (connection.getUser(myID, getGuildId(event))) {
                                connection.setPoints(number, myID, getGuildId(event));
                            } else {
                                connection.addUserDatabase(myID, getGuildId(event));
                                connection.setPoints(number, myID, getGuildId(event));
                            }
                            event.getChannel().sendMessage("Done! -> Points set to: " + number + "!").queue();
                        }

                    } catch (Exception e) {
                        event.getChannel().sendMessage("One of the following issues occurred: \n-One of the parameters is not a Number\n" +
                                "*-Worst case is that something with the database happened," +
                                "\n" +
                                "**AS A REMINDER:** !sp [ID of User] [amount of Points (0-99999999)]").queue();
                    }
                } else {
                    event.getChannel().sendMessage("You do not have the rights to call this function!").queue();
                }
            }


            if (args[0].equalsIgnoreCase("!add")) {
                if (hasPriv(event)) {
                    try {
                        int size = event.getMessage().getMentionedMembers().size();
                        if (size > 0) {
                            Long id = event.getMessage().getMentionedMembers().get(0).getIdLong();
                            event.getChannel().sendMessage(connection.addUser(id, getGuildId(event))).queue();
                        } else {
                            Long myID = Long.parseLong(args[1]);
                            event.getChannel().sendMessage(connection.addUser(myID, getGuildId(event))).queue();
                        }
                    } catch (Exception e) {
                        event.getChannel().sendMessage("Either the user is already in the database, or you entered something wrong!\nRemember-> !add [ID of user **OR** @ the User]").queue();
                    }
                } else {
                    event.getChannel().sendMessage("You have no right to do this! Contact owner, or a specific role!").queue();
                }

            }
        }

        if (args[0].equals("!pfp")) {
            if (event.getMessage().getMentionedMembers().size() > 0) {
                eb = new EmbedBuilder();
                eb.setImage(event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl() + "?size=1024");
                eb.setDescription("Profile picture of " + event.getMessage().getMentionedMembers().get(0).getAsMention());
                event.getMessage().getChannel().sendMessage(eb.build()).queue();
            } else {
                event.getMessage().getChannel().sendMessage("You have to tag a user!").queue();
            }
        }

        /*--------------------------------Reddit stuff--------------------------------------------*/
        if (args[0].equals("!wp")) {

            try {
                int postNr = Integer.parseInt(args[1]);
                whileLoop = true;
                tryCatchAmount = 3;       //amount of loops desired
                while (whileLoop) {     //loops until either it went through the try catch 3 times OR successfully gets the Links and sends it
                    try {
                        msg.getChannel().sendMessage(lg.getWallpaper(postNr)).queue();
                        whileLoop = false;
                    } catch (Exception e) {
                        tryCatchAmount--;
                        if (tryCatchAmount == 0) {
                            whileLoop = false;
                        }
                    }
                }

            } catch (Exception e) {
                msg.getChannel().sendMessage("Second param must be a value between 0-30").queue();
            }
        }

        /*--------------------------------------------------------------------------- Reactions ----------------------------------------------------------------------*/

        if (event.getMessage().getContentRaw().toLowerCase().contains("lol")) {
            event.getMessage().addReaction("\uD83E\uDD23").queue();
        }

            /*
       if(event.getMessage().getMember().getIdLong()==383414627200925696L){
            event.getMessage().addReaction("\uD83C\uDDF1").queue();
            event.getMessage().addReaction("\uD83C\uDDF4").queue();
            event.getMessage().addReaction("\uD83C\uDDF8").queue();
            event.getMessage().addReaction("\uD83C\uDDEA").queue();
            event.getMessage().addReaction("\uD83C\uDDF7").queue();

        }
            */


        /*-------------------------------------------------------------------------- Deletetions --------------------------------------------------------------------*/
        if (hasPriv(event) || event.getMessage().getMember().isOwner()) {

            /*Adds a String that the bot should delete to your database (Links work too)*/
            if (args[0].toLowerCase().equals("!del")) {
                //                                                                                                                     if (hasPriv(event)) {
                if (args[1].toCharArray().length > 4) {
                    if (event.getMessage().getMentionedMembers().size() > 0) {
                        event.getMessage().getChannel().sendMessage("No tags of users allowed!").queue();
                    } else {
                        if (!(connection.isNotAllowed(args[1].toLowerCase(), getGuildId(event)))) {
                            try {
                                event.getMessage().getChannel().sendMessage(connection.addDelCon(args[1].toLowerCase(), getGuildId(event))).queue();
                                event.getMessage().delete().queue();

                            } catch (SQLException e) {
                                event.getMessage().getChannel().sendMessage("Phrase already in database!").queue();
                            }
                        }
                    }
                }
                //                                                                                                                                       }
            }


            if (args[0].equals("!wipedb")) {
                if (msg.getMember().isOwner()) {
                    MessageChannel channel = msg.getChannel();
                    try {
                        channel.sendMessage(connection.wipeDB(getGuildId(event))).queue();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    MessageChannel channel = msg.getChannel();
                    channel.sendMessage("Only the Owner can Wipe the DB!").queue();
                }
            }


            if (args[0].equals("!purge")) {
                try {

                    Thread object = new Thread(new PurgeClass(event, args));
                    object.start();

                } catch (Exception e) {
                    msg.getChannel().sendMessage("Error occured!").queue();
                }

            }
        }

        /*----------------------------------------------------------------------- Counter stuff --------------------------------------------------------------------*/


        if (event.getMessage().getContentRaw().toLowerCase().contains("flushed") || event.getMessage().getContentRaw().toLowerCase().contains("\uD83D\uDE33")) {
            try {
                connection.incCounter(msg.getAuthor().getIdLong(), "flushed", getGuildId(event));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}








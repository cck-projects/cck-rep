package Events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;

public class PurgeClass extends Thread {
    MessageReceivedEvent event;
    String[] args;

    public PurgeClass(MessageReceivedEvent myEvent, String[] args2) {
        this.event = myEvent;
        this.args = args2;

    }

    public void run() {
        MessagePaginationAction history = event.getChannel().getIterableHistory();
        int amountOfMention = event.getMessage().getMentionedMembers().size();
        Long targetID = 00000000L;


        if (amountOfMention > 0) {
            targetID = event.getMessage().getMentionedMembers().get(0).getIdLong();
        } else {
            try {
                targetID = Long.parseLong(args[1]);
            } catch (Exception e) {
                event.getMessage().getChannel().sendMessage("Either use an valid ID of a User, or Tag them!").queue();
            }

        }
        Long finalTargetID = targetID;
        if(args.length>=3){
            history.forEach((message ->
            {
                if (message.getContentRaw().contains(args[2]) && message.getAuthor().getIdLong() == finalTargetID) {
                    message.delete().queue();
                }
            }));
            event.getChannel().sendMessage("Done! The memories of the user saying these words have been erased... <@" + event.getMessage().getAuthor().getIdLong() + ">").queue();
        }else {
            history.forEach((message ->
            {
                if (message.getAuthor().getIdLong() == finalTargetID) {
                    message.delete().queue();
                }
            }));
            event.getChannel().sendMessage("Done! The memories of this User being in this Channel have been erased... <@" + event.getMessage().getAuthor().getIdLong() + ">").queue();
        }


    }

}


package Events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserJoinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Guild guild = e.getGuild();
        guild.addRoleToMember(e.getMember(), guild.getRoleById(675126855065665540L)).queue();


    }
}

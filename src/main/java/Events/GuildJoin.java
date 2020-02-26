package Events;

import SQLDatabase.SQLFC;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class GuildJoin extends ListenerAdapter {
    SQLFC connection = new SQLFC();


    public GuildJoin() throws SQLException {
    }

    @Override
    public void onGuildJoin(GuildJoinEvent gji){
        connection.createNewDatabase(gji.getGuild().getIdLong());
    }

}

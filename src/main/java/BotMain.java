import Events.GuildJoin;
import Events.PingEvent;
import Events.UserJoinEvent;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class BotMain {

    public static void main(String[] args) throws Exception {
        try {


            InputStream input = new FileInputStream("C:\\Users\\IRB\\IdeaProjects\\FeelsGoodBot\\src\\main\\java\\config.properties");
                Properties prop = new Properties();
                prop.load(input);

            JDA jda = new JDABuilder(AccountType.BOT).setToken(prop.getProperty("bot.token")).addEventListeners(new PingEvent(), new UserJoinEvent(), new GuildJoin()).build();

            jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(Activity.listening("!fgb for help"));
            System.out.println("Finished Building JDA!");
        } catch (LoginException e) {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }


    }

}

package Events;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import org.jetbrains.annotations.NotNull;

import javax.xml.soap.SOAPPart;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class LinkGetter {
    Credentials c;
    UserAgent ua;
    NetworkAdapter adapter;

    RedditClient reddit;
    DefaultPaginator<Submission> allsubmissions;

    public LinkGetter() throws IOException {
        InputStream input = new FileInputStream("C:\\Users\\IRB\\IdeaProjects\\FeelsGoodBot\\src\\main\\java\\Events\\config.properties");
        Properties prop = new Properties();
        prop.load(input);

        c = Credentials.script(prop.getProperty("c.username"), prop.getProperty("c.password"), prop.getProperty("c.clientid"), prop.getProperty("c.secret"));
        ua = new UserAgent(prop.getProperty("u.platform"), prop.getProperty("u.appid"), prop.getProperty("u.version"), prop.getProperty("u.username"));


        adapter = new OkHttpNetworkAdapter(ua);
        reddit = OAuthHelper.automatic(adapter, c);
        allsubmissions = reddit.subreddit("Wallpapers").posts().sorting(SubredditSort.TOP).timePeriod(TimePeriod.DAY).limit(30).build();
    }

    public String getWallpaper(int i) {
        String url = "https://www.reddit.com" + allsubmissions.next().get(i).getPermalink();
        return allsubmissions.next().get(i).getUrl();

        //System.out.println(url);
        //return url;
    }


}
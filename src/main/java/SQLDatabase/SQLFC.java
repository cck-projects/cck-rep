package SQLDatabase;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.mariadb.jdbc.internal.com.read.resultset.SelectResultSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.*;


public class SQLFC {
    //create connection for a server installed in localhost, with a user "root" with your password


    Connection conn;

    // create a Statement
    Statement stmt;

    public SQLFC() throws SQLException {
        try (InputStream input = new FileInputStream("C:\\Users\\IRB\\IdeaProjects\\FeelsGoodBot\\src\\main\\java\\SQLDatabase\\config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", prop.getProperty("db.user"), prop.getProperty("db.password"));
            stmt=conn.createStatement();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
            // https://mkyong.com/java/java-properties-file-examples/ ---> getting config information

    }

    public String getAllUserWCount(Long guildID) throws SQLException {
        ResultSet rs = stmt.executeQuery("select * from d"+guildID+".weirdcounter ORDER BY d"+guildID+".weirdcounter.counter DESC");
        StringBuilder output = new StringBuilder();
        int i = 0;
        while (rs.next()) {
            if (i == 5) {
                return output.toString();
            }
            output.append("User: <@" + rs.getLong("id") + "> Counter: " + rs.getInt("counter") + " \n");
            i++;
        }
        String output2 = output.toString();
        return output2;
    }

    public String getFlushedCount(Long guildID) throws SQLException {
        ResultSet rs = stmt.executeQuery("select * from d"+guildID+".weirdcounter ORDER BY d"+guildID+".weirdcounter.flushed DESC");
        StringBuilder output = new StringBuilder();
        int i = 0;
        while (rs.next()) {
            if (i == 5) {
                String output2 = output.toString();
                return output2;
            }
            output.append("User: <@" + rs.getLong("id") + "> Counter: " + rs.getInt("flushed") + " \n");
            i++;
        }
        String output2 = output.toString();
        return output2;
    }


    public String addUserDatabase(Long usrId, Long guildID) throws SQLException {
        stmt.executeQuery("insert into d"+guildID+".weirdcounter (d"+guildID+".weirdcounter.id, d"+guildID+".weirdcounter.counter) values (" + usrId + ",0)");
        return "Did not have you in my database but now i do <:pepoevil:677132019720912908>";
    }


    public Boolean getUser(Long usrId, Long guildID) throws SQLException {
        ResultSet rs = stmt.executeQuery("select * from d"+guildID+".weirdcounter where d"+guildID+".weirdcounter.id=" + usrId);
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }


    public String getOneUser(Long usrId, Long guildID) throws SQLException {
        ResultSet rs = stmt.executeQuery("select d"+guildID+".weirdcounter.counter, d"+guildID+".weirdcounter.id from d"+guildID+".weirdcounter where d"+guildID+".weirdcounter.id=" + usrId);
        if (rs.next()) {
            return "-ID: " + rs.getLong("id") + "\n-Weirdcount: " + rs.getInt("counter");

        } else {
            return addUserDatabase(usrId, guildID);
        }
    }

    public String incCounter(Long usrId, String column, Long guildID) throws SQLException {
        try {
            stmt.executeQuery("Update d"+guildID+".weirdcounter SET " + column + "=" + column + "+1 WHERE d"+guildID+".weirdcounter.id=" + usrId);
            return null;
        } catch (Exception e) {

            String myRet = addUserDatabase(usrId,guildID);
            stmt.executeQuery("Update d"+guildID+".weirdcounter SET " + column + "=" + column + "+1 WHERE d"+guildID+".weirdcounter.id=" + usrId);
            return myRet;
        }
    }

    public String wipeDB(Long guildID) throws SQLException {
        stmt.executeQuery("DELETE FROM d"+guildID+".weirdcounter");
        return "...the deed has been done <:thisdog:676437895418216448>";
    }

    public String setPoints(Long value, Long usrId,Long guildID) throws SQLException {
        stmt.executeQuery("UPDATE d"+guildID+".weirdcounter SET d"+guildID+".weirdcounter.points=" + value + " WHERE d"+guildID+".weirdcounter.id=" + usrId);
        return "Points have been set!";

    }

    public String addUser(Long usrId,Long guildID) throws SQLException {
        stmt.executeQuery("INSERT INTO d"+guildID+".weirdcounter(d"+guildID+".weirdcounter.id) VALUES(" + usrId + ")");
        return "COMPLETE! User added to Database <:pepoevil:677132019720912908>";
    }

    public String addDelCon(String del,Long guildID) throws SQLException {
        stmt.executeQuery("INSERT INTO d"+guildID+".deletiontable(d"+guildID+".deletiontable.phrase) VALUES (' " + del + " ')");
        return "Added to deletion table!";
    }

    public boolean isNotAllowed(String check,Long guildID) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM d"+guildID+".deletiontable WHERE d"+guildID+".deletiontable.phrase='" + check + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (((SelectResultSet) rs).getDataSize() > 0);
    }

    public int allBannedWords(Long guildID) {
        try {
            ResultSet rs = stmt.executeQuery("select * from d"+guildID+".deletiontable");
            return ((SelectResultSet) rs).getDataSize();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Boolean getBannedWord(MessageReceivedEvent event, Long guildID) {
        try {


            ResultSet rs = stmt.executeQuery("SELECT * FROM d"+guildID+".deletiontable");

            while(rs.next()) {
                if (event.getMessage().getContentRaw().toLowerCase().contains(rs.getString("phrase").substring(1,rs.getString("phrase").length()-1))) {
                    return true;
                }
            }
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void createNewDatabase(Long guildID){
        try {
            stmt.executeQuery("CREATE DATABASE d"+guildID);
            stmt.executeQuery("CREATE TABLE d"+guildID+".weirdcounter(id BIGINT not null primary key , counter int default 0 null, points bigint default 0 not null)" );
            stmt.executeQuery("CREATE TABLE d"+guildID+".deletiontable(phrase varchar(255) default 'nothing' null, constraint deletiontable_phrase_uindex unique (phrase))");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




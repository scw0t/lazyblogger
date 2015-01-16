package lazyblogger.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dbController {

    private Connection conn;
    private Statement stat;
    private final String DB_NAME = "blogrecs";

    public dbController() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect(DB_NAME);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect(String dbName) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        stat = conn.createStatement();
    }

    public void createTableIfNotExists() {
        try {
            stat.executeUpdate("CREATE TABLE " + DB_NAME
                    + " (indx, "
                    + "artist, "
                    + "album, "
                    + "year, "
                    + "label, "
                    + "catNumber, "
                    + "genre, "
                    + "country, "
                    + "discogsLink, "
                    + "mbLink, "
                    + "rymLink, "
                    + "youtubeLink, "
                    + "dl1Link, "
                    + "dl2Link, "
                    + "txtLink, "
                    + "thumbLink, "
                    + "postBody);");
        } catch (SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dropTable(){
        try {
            stat.executeUpdate("DROP TABLE IF EXISTS " + DB_NAME + ";");
        } catch (SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addRecord(String artist,
                        String album,
                        String year,
                        String label,
                        String catNumber,
                        String genre,
                        String country,
                        String discogsLink,
                        String mbLink,
                        String rymLink,
                        String youtubeLink,
                        String dl1Link,
                        String dl2Link,
                        String txtLink,
                        String thumbLink,
                        String postBody) {
        try {
            int index = getNumberOfRows();

            PreparedStatement prep = conn.prepareStatement(
                    "insert into " + DB_NAME + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            
            prep.setString(1, String.valueOf(index));
            prep.setString(2, artist);
            prep.setString(3, album);
            prep.setString(4, year);
            prep.setString(5, label);
            prep.setString(6, catNumber);
            prep.setString(7, genre);
            prep.setString(8, country);
            prep.setString(9, discogsLink);
            prep.setString(10, mbLink);
            prep.setString(11, rymLink);
            prep.setString(12, youtubeLink);
            prep.setString(13, dl1Link);
            prep.setString(14, dl2Link);
            prep.setString(15, txtLink);
            prep.setString(16, thumbLink);
            prep.setString(17, postBody);
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
        } catch (SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void outputLastRecord(){
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM " + DB_NAME + ";");
            while (rs.next()) {
                System.out.println("index = " + rs.getString("indx"));
                System.out.println("album = " + rs.getString("album"));
                System.out.println("year = " + rs.getString("year"));
                System.out.println("label = " + rs.getString("label"));
                System.out.println("catNumber = " + rs.getString("catNumber"));
                System.out.println("genre = " + rs.getString("genre"));
                System.out.println("country = " + rs.getString("country"));
                System.out.println("discogsLink = " + rs.getString("discogsLink"));
                System.out.println("mbLink = " + rs.getString("mbLink"));
                System.out.println("rymLink = " + rs.getString("rymLink"));
                System.out.println("youtubeLink = " + rs.getString("youtubeLink"));
                System.out.println("dl1Link = " + rs.getString("dl1Link"));
                System.out.println("dl2Link = " + rs.getString("dl2Link"));
                System.out.println("txtLink = " + rs.getString("txtLink"));
                System.out.println("thumbLink = " + rs.getString("thumbLink"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int getNumberOfRows(){
        int count = 0;
        try {
            ResultSet rs = stat.executeQuery("SELECT Count(*) AS cnt FROM " + DB_NAME + ";");
            while (rs.next()) {
                count = Integer.valueOf(rs.getString("cnt"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(dbController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ++count;
    }
}

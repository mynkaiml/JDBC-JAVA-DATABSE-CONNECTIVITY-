import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    private static final String URL = //YOUR JDBC URL HERE;
    private static final String USER = //YOUR USERNAME HERE;
    private static final String PASS = //YOUR MYSQL PASSWORD;

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

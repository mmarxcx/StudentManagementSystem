import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.util.Properties;

public class DBConnection {
    public static Connection connect() {
        try {
            // 1. Create a tool to read the file
            Properties props = new Properties();

            // 2. Open the .env file
            FileInputStream fis = new FileInputStream(".env");
            props.load(fis);
            fis.close();

            // 3. Extract the secret variables from the file
            String dbUrl = props.getProperty("DB_URL");
            String dbUser = props.getProperty("DB_USER");
            String dbPassword = props.getProperty("DB_PASSWORD");

            // 4. Connect to PostgreSQL
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("✅ SUCCESSFULLY CONNECTED USING THE .ENV FILE!");

            return conn;

        } catch (Exception e) {
            System.out.println("❌ Connection Failed! Make sure your .env file is in the right folder.");
            e.printStackTrace();
            return null;
        }
    }
}
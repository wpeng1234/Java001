import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddTable {
    public static void main(String[] args) throws Exception {
        SimpleDataSource.init("database.properties");
        Connection connection = SimpleDataSource.getConnection();
        PreparedStatement statement2 = connection.prepareStatement("CREATE TABLE Orders (OrderID VARCHAR(50),BaristaID VARCHAR(50),OrderItems VARCHAR(50),OrderPrice VARCHAR(50),Time VARCHAR(50))");
        statement2.execute();
    }
 }


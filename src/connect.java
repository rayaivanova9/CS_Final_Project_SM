import org.mindrot.jbcrypt.BCrypt;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class connect {
    private static final String URL = "jdbc:mysql://localhost:3306/school";
    private static final String USER = "root";
    private static final String PASSWORD = "0000";

    public static ArrayList<String[]> executeTable(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add colums automatically
            for (int i = 1; i <= columnCount; i++) {
                //formName.model.addColumn(metaData.getColumnName(i)); // model from form1.java
            }

            // to add rows.
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getString(i + 1);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return results;
    }

    public static boolean executeValidation (String email, String password, int role) {
        String tableName;
        //email = "john.doe@example.com";
        if (role == 1) {
            tableName = "students";
        } else if (role == 2) {
            tableName = "teachers";
        } else {
            return false;
        }

        String query = "SELECT password FROM " + tableName + " WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Query being executed: " + query);

            if (rs.next()) {
                //String hashedPassword = rs.getString("password");
                System.out.println(password);
                //String testPassword = "student123";
                //String correctHash = "$2a$10$Zz96Kwq6X8Z6VWWuMc1z0.8B9Y/7hBNepzxl9ZBx9G.CXcpLMXgQe";
                //System.out.println(BCrypt.checkpw(testPassword, correctHash));
                //return BCrypt.checkpw(password, hashedPassword); // Compare using BCrypt
                return password.equals(rs.getString("password"));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return false;
    }
}

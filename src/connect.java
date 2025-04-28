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

    public static ArrayList<String[]> executeTable(String query, String email, int role, int buttonIndex) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add columns automatically
            for (int i = 1; i <= columnCount; i++) {
                if (role == 1) {
                    students.model.addColumn(metaData.getColumnName(i));
                }
                else if (role == 2) {
                    switch(buttonIndex) {
                        case 1:
                            courses.model.addColumn(metaData.getColumnName(i));
                            break;
                        case 2:
                            grades.model.addColumn(metaData.getColumnName(i));
                            break;
                    }
                }
            }

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

    public static ArrayList<String[]> executeQuery(String query, int buttonIndex) {
        ArrayList<String[]> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // to add columns automatically
            for (int i = 1; i <= columnCount; i++) {
                switch(buttonIndex) {
                    case 3:
                        teachers.model.addColumn(metaData.getColumnName(i));
                        break;
                    case 4:
                        studentsForm.model.addColumn(metaData.getColumnName(i));
                        break;
                }
            }

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

    public static int getId (String email) {
        String queryGetId = "SELECT teacher_id FROM teachers WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(queryGetId)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return 0;
    }

    public static String getEmail (int id) {
        String queryGetId = "SELECT email FROM students WHERE student_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(queryGetId)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
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

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                return BCrypt.checkpw(password, hashedPassword); // Compare using BCrypt
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return false;
    }

    public static double executeCoursesCount(String query, String email) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return 0;
    }

    public static String loadText(String email) {
        String query = "SELECT content FROM notes WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("content");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveText(String content, String email) {
        String query = "REPLACE INTO notes (email, content) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRow(int selectedId, String tableName, String columnName) {
        String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, selectedId);
            int affectedRows = ps.executeUpdate(); // Execute delete operation


            if (affectedRows > 0) {
                System.out.println("Row with ID " + selectedId + " deleted successfully.");
            } else {
                System.out.println("No row found with ID " + selectedId);
            }
        }
        catch (SQLException e) {
            // If there's an exception (e.g., a connection issue or query issue), print the error message.
            e.printStackTrace();
        }
    }

    public static void addRowCourses(String newName, String tableName, String email, int id, String query) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, newName);
                pstmt.setInt(2, id);
                pstmt.setString(3, email);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new row was inserted successfully into " + tableName + "!");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addRowGrades(int studentId, String tableName, String email, int courseId, double grade, String query) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.setDouble(3, grade);
            pstmt.setString(4, email);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new row was inserted successfully into " + tableName + "!");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGrade(int studentId, int courseId, double grade, String query) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, grade);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, courseId);

            int affectedRows = pstmt.executeUpdate(); // Execute delete operation


            if (affectedRows > 0) {
                System.out.println("The grade was updated.");
            } else {
                System.out.println("Something went wrong.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDatabase(String email, String[] columns, String[] newValues, String tableName) {
        if (columns.length != newValues.length) {
            System.out.println("Error: Column count does not match value count.");
            return;
        }

        // Construct the SQL query dynamically based on the number of columns to update
        StringBuilder queryBuilder = new StringBuilder("UPDATE school.");
        queryBuilder.append(tableName).append(" SET ");
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(" WHERE email = ?");

        String query = queryBuilder.toString(); // Convert StringBuilder to a SQL string

        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Start transaction

            pstmt = connection.prepareStatement(query);

            // Set the new values dynamically
            for (int i = 0; i < newValues.length; i++) {
                pstmt.setString(i + 1, newValues[i]);
            }
            pstmt.setString(newValues.length + 1, email); // Set the WHERE condition


            int rowsAffected = pstmt.executeUpdate();


            if (rowsAffected > 0) {
                connection.commit(); // Commit only if update was successful
                System.out.println("Update committed successfully for user: " + email);
            } else {
                connection.rollback(); // Rollback if no rows were affected
                System.out.println("Update failed. Transaction rolled back.");
            }


        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback on error
                }
                System.out.println("SQL Error: " + ex.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void updateTeacherImageByEmail(String email, byte[] imageData) {
        String query = "UPDATE teachers SET photo = ? WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setBytes(1, imageData); // Set image bytes
            pstmt.setString(2, email);    // Set teacher's email

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Teacher's image updated successfully.");
            } else {
                System.out.println("Failed to update teacher's image.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ImageIcon loadTeacherImage(String email) {
        String query = "SELECT photo FROM teachers WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); // step 1: establish connection
             PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    byte[] imageData = rs.getBytes("photo");
                    if (imageData != null) {
                        ImageIcon imageIcon = new ImageIcon(imageData);
                        return (new ImageIcon(imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

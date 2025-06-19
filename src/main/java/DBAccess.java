import java.sql.*;

public class DBAccess {

    public static class DatabaseConnection {

        private DatabaseConnection() {}

        public static Connection getConnection() throws SQLException {
            String url = System.getenv("DB_URL"); //"jdbc:mysql://localhost:3306/yourdb";
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new SQLException("Database environment variables not set properly.");
            }

            return DriverManager.getConnection(url, user, password);
        }
    }

    public void insertResident(int id, String name, int age, String room) {
        String sql = "INSERT INTO residents (id, name, age, room_number) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, age);
            stmt.setString(4, room);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getResident(int id) {
        String sql = "SELECT * FROM residents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Room: " + rs.getString("room_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateResidentRoom(int id, String newRoom) {
        String sql = "UPDATE residents SET room_number = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newRoom);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteResident(int id) {
        String sql = "DELETE FROM residents WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

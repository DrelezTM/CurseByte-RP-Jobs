package org.cursebyte.module.jobs;

import com.cursebyte.plugin.database.DatabaseManager;

import java.sql.*;
import java.util.UUID;

public class JobsRepository {
    public static void init() {
        createTable();
    }

    private static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS jobs (
                    uuid TEXT PRIMARY KEY,
                    job TEXT
                );
                """;

        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPlayer(UUID uuid){
        String sql = "INSERT OR IGNORE INTO jobs(uuid, job) VALUES(?, ?)";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, "UNEMPLOYMENT" );
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getJobs(UUID uuid){
        String sql = "SELECT job FROM jobs WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("job");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "UNEMPLOYED";
    }

    public static void changeJob(UUID uuid, String job){
        String sql = "UPDATE jobs SET job = ? WHERE uuid = ?";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, job);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

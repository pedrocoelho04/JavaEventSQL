import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.SQLiteConnection;

public class TexteConnection {
    public static void main(String[] args) {
        try {
            String sql = "SELECT * FROM participante";
            SQLiteConnection sqlConn = new SQLiteConnection();
            Connection conn = sqlConn.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("nome"));
            }
            rs.close();
            stm.close();
            sqlConn.close(conn);
        } catch (SQLException e) {
            System.err.println("Erro ao executar SELECT: " + e.getMessage());
        }
    }
}

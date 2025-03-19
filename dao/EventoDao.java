package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import table.Evento;
import util.SQLiteConnection;

public class EventoDao {
    private SQLiteConnection sqlConn;

    public EventoDao() {
        this.sqlConn = new SQLiteConnection();
    }

    public List<Evento> listarTodos() {
        try {
            List<Evento> lista = new ArrayList<Evento>();
            String sql = "SELECT * FROM evento";
            Connection conn = this.sqlConn.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString("titulo"),
                        rs.getString("local"));
                lista.add(evento);
            }
            rs.close();
            stm.close();
            this.sqlConn.close(conn);
            return lista;
        } catch (SQLException e) {
            System.err.println(
                    "Erro no método listarTodos() da classe EventoDao ao executar SELECT: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<Evento>();
        }
    }

    public List<Evento> listarPorParametro(String titulo, String local) {
        try {
            List<Evento> lista = new ArrayList<Evento>();
            String sql = "SELECT * FROM evento";
            String sqlWhere = "";
            if (((titulo != null) && (!titulo.isEmpty())) || ((local != null) && (!local.isEmpty()))) {
                sqlWhere = " WHERE";
                if ((titulo != null) && (!titulo.isEmpty()))
                    sqlWhere += " titulo LIKE ?";
                if ((local != null) && (!local.isEmpty())) {
                    if (sqlWhere.equals(""))
                        sqlWhere += " email = ?";
                    else
                        sqlWhere += " AND email = ?";
                }
            }
            sql += sqlWhere;
            Connection conn = this.sqlConn.connect();
            PreparedStatement pstm = conn.prepareStatement(sql);
            if ((titulo != null) && (!titulo.isEmpty()) && (local != null) && (!local.isEmpty())) {
                pstm.setString(1, titulo);
                pstm.setString(2, local);
            } else if ((titulo != null) && (!titulo.isEmpty()))
                pstm.setString(1, titulo);
            else if ((titulo != null) && (!titulo.isEmpty()))
                pstm.setString(1, local);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("id"), rs.getString("titulo"),
                        rs.getString("local"));
                lista.add(evento);
            }
            rs.close();
            pstm.close();
            this.sqlConn.close(conn);
            return lista;
        } catch (SQLException e) {
            System.err.println(
                    "Erro no método listarPorParametro(String titulo, String local) da classe EventoDao ao executar SELECT: "
                            + e.getMessage());
            return new ArrayList<Evento>();
        }
    }

    public Evento buscarPorId(Integer id) {
        try {
            Evento evento = new Evento();
            String sql = "SELECT * FROM evento where id = ?";
            Connection conn = this.sqlConn.connect();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                evento = new Evento(rs.getInt("id"), rs.getString("titulo"),
                rs.getString("local"));
            rs.close();
            pstm.close();
            this.sqlConn.close(conn);
            return evento;
        } catch (SQLException e) {
            System.err.println("Erro no método buscarPorId(Integer id) da classe EventoDao ao executar SELECT: "
                    + e.getMessage());
            e.printStackTrace();
            return new Evento();
        }
    }

    public Evento buscarPorTitulo(String titulo) {
        try {
            Evento evento = new Evento();
            String sql = "SELECT * FROM evento where titulo = ?";
            Connection conn = this.sqlConn.connect();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, titulo);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                evento = new Evento(rs.getInt("id"), rs.getString("titulo"),
                rs.getString("local"));
            rs.close();
            pstm.close();
            this.sqlConn.close(conn);
            return evento;
        } catch (SQLException e) {
            System.err.println(
                    "Erro no método buscarPorTitulo(String titulo) da classe EventoDao ao executar SELECT: "
                            + e.getMessage());
            e.printStackTrace();
            return new Evento();
        }
    }

    public Evento buscarPorLocal(String local) {
        try {
            Evento evento = new Evento();
            String sql = "SELECT * FROM evento where local = ?";
            Connection conn = this.sqlConn.connect();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, local);
            ResultSet rs = pstm.executeQuery();
            if (rs.next())
                evento = new Evento(rs.getInt("id"), rs.getString("titulo"),
                rs.getString("local"));
            rs.close();
            pstm.close();
            this.sqlConn.close(conn);
            return evento;
        } catch (SQLException e) {
            System.err.println(
                    "Erro no método buscarPorLocal(String local) da classe EventoDao ao executar SELECT: "
                            + e.getMessage());
            e.printStackTrace();
            return new Evento();
        }
    }

    public String inserir(String titulo, String local) {
        try {
            Integer id = this.getNewId();
            String sql = "INSERT INTO evento(id, titulo, local) VALUES(?, ?, ?)";
            Connection conn = this.sqlConn.connect();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            pstm.setString(2, titulo);
            pstm.setString(3, local);
            System.out.println("Resposta: " + pstm.executeUpdate());
            pstm.close();
            this.sqlConn.close(conn);
            return "sucesso";
        } catch (Exception e) {
            System.err.println(
                    "Erro no método inserir(String titulo, String local) da classe EventoDao ao executar SELECT: "
                            + e.getMessage());
            e.printStackTrace();
            return "erro";
        }
    }

    private Integer getNewId() {
        try {
            Integer id = 1;
            String sql = "SELECT MAX(id) AS max_id FROM evento";
            Connection conn = this.sqlConn.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next())
                id = rs.getInt("max_id") + 1;
            rs.close();
            stm.close();
            this.sqlConn.close(conn);
            return id;
        } catch (Exception e) {
            System.err.println(
                    "Erro no método getNewId() da classe EventoDao ao executar SELECT: "
                            + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}

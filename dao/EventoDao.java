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
        Evento evento = new Evento(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("local"),
            rs.getString("data"),
            rs.getString("detalhes"));
        lista.add(evento);
      }
      rs.close();
      stm.close();
      this.sqlConn.close(conn);
      return lista;
    } catch (SQLException e) {
      System.err.println("Erro no método listarTodos(): " + e.getMessage());
      e.printStackTrace();
      return new ArrayList<Evento>();
    }
  }

  public List<Evento> listarPorParametro(String titulo, String local) {
    try {
      List<Evento> lista = new ArrayList<Evento>();
      String sql = "SELECT * FROM evento";
      String sqlWhere = "";
      boolean hasTitulo = titulo != null && !titulo.isEmpty();
      boolean hasLocal = local != null && !local.isEmpty();

      if (hasTitulo || hasLocal) {
        sqlWhere = " WHERE";
        if (hasTitulo)
          sqlWhere += " titulo LIKE ?";
        if (hasLocal)
          sqlWhere += (hasTitulo ? " AND" : "") + " local = ?";
      }

      sql += sqlWhere;
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);

      int index = 1;
      if (hasTitulo)
        pstm.setString(index++, "%" + titulo + "%");
      if (hasLocal)
        pstm.setString(index++, local);

      ResultSet rs = pstm.executeQuery();
      while (rs.next()) {
        Evento evento = new Evento(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("local"),
            rs.getString("data"),
            rs.getString("detalhes"));
        lista.add(evento);
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return lista;
    } catch (SQLException e) {
      System.err.println("Erro no método listarPorParametro(): " + e.getMessage());
      return new ArrayList<Evento>();
    }
  }

  public Evento buscarPorId(Integer id) {
    try {
      Evento evento = new Evento();
      String sql = "SELECT * FROM evento WHERE id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      ResultSet rs = pstm.executeQuery();
      if (rs.next()) {
        evento = new Evento(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("local"),
            rs.getString("data"),
            rs.getString("detalhes"));
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return evento;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorId(): " + e.getMessage());
      e.printStackTrace();
      return new Evento();
    }
  }

  public Evento buscarPorTitulo(String titulo) {
    try {
      Evento evento = new Evento();
      String sql = "SELECT * FROM evento WHERE titulo = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setString(1, titulo);
      ResultSet rs = pstm.executeQuery();
      if (rs.next()) {
        evento = new Evento(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("local"),
            rs.getString("data"),
            rs.getString("detalhes"));
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return evento;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorTitulo(): " + e.getMessage());
      e.printStackTrace();
      return new Evento();
    }
  }

  public Evento buscarPorLocal(String local) {
    try {
      Evento evento = new Evento();
      String sql = "SELECT * FROM evento WHERE local = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setString(1, local);
      ResultSet rs = pstm.executeQuery();
      if (rs.next()) {
        evento = new Evento(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("local"),
            rs.getString("data"),
            rs.getString("detalhes"));
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return evento;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorLocal(): " + e.getMessage());
      e.printStackTrace();
      return new Evento();
    }
  }

  public String inserir(String titulo, String local, String data, String detalhes) {
    try {
      Integer id = this.getNewId();
      String sql = "INSERT INTO evento(id, titulo, local, data, detalhes) VALUES(?, ?, ?, ?, ?)";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      pstm.setString(2, titulo);
      pstm.setString(3, local);
      pstm.setString(4, data);
      pstm.setString(5, detalhes);
      System.out.println("Resposta: " + pstm.executeUpdate());
      pstm.close();
      this.sqlConn.close(conn);
      return "sucesso";
    } catch (Exception e) {
      System.err.println("Erro no método inserir(): " + e.getMessage());
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
      System.err.println("Erro no método getNewId(): " + e.getMessage());
      e.printStackTrace();
      return -1;
    }
  }

  public String excluirPorId(int id) {
    try {
      String sql = "DELETE FROM evento WHERE id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      System.out.println("Resposta: " + pstm.executeUpdate());
      pstm.close();
      this.sqlConn.close(conn);
      return "sucesso";
    } catch (Exception e) {
      System.err.println("Erro no método excluirPorId(): " + e.getMessage());
      e.printStackTrace();
      return "erro";
    }
  }

  public String atualizar(Evento evento) {
    try {
      String sql = "UPDATE evento SET titulo = ?, local = ?, data = ?, detalhes = ? WHERE id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);

      pstm.setString(1, evento.getTitulo());
      pstm.setString(2, evento.getLocal());
      pstm.setString(3, evento.getData());
      pstm.setString(4, evento.getDetalhes());
      pstm.setInt(5, evento.getId());

      int resultado = pstm.executeUpdate();
      pstm.close();
      this.sqlConn.close(conn);

      if (resultado > 0) {
        return "sucesso";
      } else {
        return "erro";
      }
    } catch (SQLException e) {
      System.err.println("Erro ao atualizar evento: " + e.getMessage());
      return "erro";
    }
  }
}

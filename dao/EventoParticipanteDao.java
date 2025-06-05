package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import table.EventoParticipante;
import util.SQLiteConnection;

public class EventoParticipanteDao {
  private SQLiteConnection sqlConn;

  public EventoParticipanteDao() {
    this.sqlConn = new SQLiteConnection();
  }

  public List<EventoParticipante> listarTodos() {
    try {
      List<EventoParticipante> lista = new ArrayList<EventoParticipante>();
      String sql = "SELECT * FROM evento_participante";
      Connection conn = this.sqlConn.connect();
      Statement stm = conn.createStatement();
      ResultSet rs = stm.executeQuery(sql);
      while (rs.next()) {
        EventoParticipante eventoParticipante = new EventoParticipante(rs.getInt("id"), rs.getInt("id_evento"), rs.getInt("id_participante"));
        lista.add(eventoParticipante);
      }
      rs.close();
      stm.close();
      this.sqlConn.close(conn);
      return lista;
    } catch (SQLException e) {
      System.err.println(
          "Erro no método listarTodos() da classe EventoParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return new ArrayList<EventoParticipante>();
    }
  }

  public List<EventoParticipante> listarPorParametro(Integer id_evento, Integer id_participante) {
    try {
      List<EventoParticipante> lista = new ArrayList<EventoParticipante>();
      String sql = "SELECT * FROM evento_participante";
      String sqlWhere = "";
      if (((id_evento != null) && (id_evento > 0)) || ((id_participante != null) && (id_participante > 0))) {
        sqlWhere = " WHERE";
        if ((id_evento != null) && (id_evento > 0))
          sqlWhere += " id_evento LIKE ?";
        if ((id_participante != null) && (id_participante > 0)) {
          if (sqlWhere.equals(""))
            sqlWhere += " id_participante = ?";
          else
            sqlWhere += " AND id_participante = ?";
        }
      }
      sql += sqlWhere;
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      if ((id_evento != null) && (id_evento > 0) && (id_participante != null) && (id_participante > 0)) {
        pstm.setInt(1, id_evento);
        pstm.setInt(2, id_participante);
      } else if ((id_evento != null) && (id_evento > 0))
        pstm.setInt(1, id_evento);
      else if ((id_evento != null) && (id_evento > 0))
        pstm.setInt(1, id_participante);
      ResultSet rs = pstm.executeQuery();
      while (rs.next()) {
        EventoParticipante eventoParticipante = new EventoParticipante(rs.getInt("id"), rs.getInt("id_evento"), rs.getInt("id_participante"));
        lista.add(eventoParticipante);
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return lista;
    } catch (SQLException e) {
      System.err.println(
          "Erro no método listarPorParametro(Integer id_evento, Integer id_participante) da classe EventoParticipanteDao ao executar SELECT: "
              + e.getMessage());
      return new ArrayList<EventoParticipante>();
    }
  }

  public EventoParticipante buscarPorId(Integer id) {
    try {
      EventoParticipante eventoParticipante = new EventoParticipante();
      String sql = "SELECT * FROM evento_participante where id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      ResultSet rs = pstm.executeQuery();
      if (rs.next())
        eventoParticipante = new EventoParticipante(rs.getInt("id"), rs.getInt("id_evento"), rs.getInt("id_participante"));
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return eventoParticipante;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorId(Integer id) da classe EventoParticipanteDao ao executar SELECT: "
          + e.getMessage());
      e.printStackTrace();
      return new EventoParticipante();
    }
  }

  public EventoParticipante buscarPorIdEvento(Integer id_evento) {
    try {
      EventoParticipante eventoParticipante = new EventoParticipante();
      String sql = "SELECT * FROM evento_participante where id_evento = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id_evento);
      ResultSet rs = pstm.executeQuery();
      if (rs.next())
        eventoParticipante = new EventoParticipante(rs.getInt("id"), rs.getInt("id_evento"), rs.getInt("id_participante"));
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return eventoParticipante;
    } catch (SQLException e) {
      System.err.println(
          "Erro no método buscarPorIdEvento(Integer id_evento) da classe EventoParticipanteDao ao executar SELECT: "
              + e.getMessage());
      e.printStackTrace();
      return new EventoParticipante();
    }
  }

  public EventoParticipante buscarPorIdParticipante(Integer id_participante) {
    try {
      EventoParticipante eventoParticipante = new EventoParticipante();
      String sql = "SELECT * FROM evento_participante where id_participante = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id_participante);
      ResultSet rs = pstm.executeQuery();
      if (rs.next())
        eventoParticipante = new EventoParticipante(rs.getInt("id"), rs.getInt("id_evento"), rs.getInt("id_participante"));
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return eventoParticipante;
    } catch (SQLException e) {
      System.err.println(
          "Erro no método buscarPorIdParticipante(Integer id_participante) da classe EventoParticipanteDao ao executar SELECT: "
              + e.getMessage());
      e.printStackTrace();
      return new EventoParticipante();
    }
  }

  public String inserir(Integer id_evento, Integer id_participante) {
    try {
      Integer id = this.getNewId();
      String sql = "INSERT INTO evento_participante(id, id_evento, id_participante) VALUES(?, ?, ?)";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      pstm.setInt(2, id_evento);
      pstm.setInt(3, id_participante);
      System.out.println("Resposta: " + pstm.executeUpdate());
      pstm.close();
      this.sqlConn.close(conn);
      return "sucesso";
    } catch (Exception e) {
      System.err.println("Erro no método inserir(Integer id_evento, Integer id_participante) da classe EventoParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return "erro";
    }
  }

  private Integer getNewId() {
    try {
      Integer id = 1;
      String sql = "SELECT MAX(id) AS max_id FROM evento_participante";
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
      System.err.println("Erro no método getNewId() da classe EventoParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return -1;
    }
  }
  public String excluirPorId(int id){
    try {
      String sql = "DELETE FROM evento_participante WHERE id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      System.out.println("Resposta: " + pstm.executeUpdate());
      pstm.close();
      this.sqlConn.close(conn);
      return "sucesso";
    } catch (Exception e) {
      System.err.println("Erro no método excluirPorId(int id) da classe EventoParticipanteDAO ao executar DELETE: " + e.getMessage());
      e.printStackTrace();
      return "erro";
    }
  }
}
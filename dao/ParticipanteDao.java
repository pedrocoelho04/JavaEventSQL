package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import table.Participante;
import util.SQLiteConnection;

public class ParticipanteDao {
  private SQLiteConnection sqlConn;

  public ParticipanteDao() {
    this.sqlConn = new SQLiteConnection();
  }

  public List<Participante> listarTodos() {
    try {
      List<Participante> lista = new ArrayList<Participante>();
      String sql = "SELECT * FROM participante";
      Connection conn = this.sqlConn.connect();
      Statement stm = conn.createStatement();
      ResultSet rs = stm.executeQuery(sql);
      while (rs.next()) {
        Participante participante = new Participante(
          rs.getInt("id"),
          rs.getString("nome"),
          rs.getString("sexo"),
          rs.getString("email"),
          rs.getString("celular"),
          rs.getString("ePalestrante"),
          rs.getString("curriculo"),
          rs.getString("areaAtuacao"),
          rs.getString("evento")
        );
        lista.add(participante);
      }
      rs.close();
      stm.close();
      this.sqlConn.close(conn);
      return lista;
    } catch (SQLException e) {
      System.err.println("Erro no método listarTodos() da classe ParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return new ArrayList<Participante>();
    }
  }

  public List<Participante> listarPorParametro(String nome, String sexo) {
    try {
      List<Participante> lista = new ArrayList<Participante>();
      String sql = "SELECT * FROM participante";
      String sqlWhere = "";
      if (((nome != null) && (!nome.isEmpty())) || ((sexo != null) && (!sexo.isEmpty()))) {
        sqlWhere = " WHERE";
        if ((nome != null) && (!nome.isEmpty()))
          sqlWhere += " nome LIKE ?";
        if ((sexo != null) && (!sexo.isEmpty())) {
          if (!sqlWhere.equals(" WHERE"))
            sqlWhere += " AND sexo LIKE ?";
          else
            sqlWhere += " sexo LIKE ?";
        }
      }
      sql += sqlWhere;
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      if ((nome != null) && (!nome.isEmpty()) && (sexo != null) && (!sexo.isEmpty())) {
        pstm.setString(1, "%" + nome + "%");
        pstm.setString(2, "%" + sexo + "%");
      } else if ((nome != null) && (!nome.isEmpty()))
        pstm.setString(1, "%" + nome + "%");
      else if ((sexo != null) && (!sexo.isEmpty()))
        pstm.setString(1, "%" + sexo + "%");
      ResultSet rs = pstm.executeQuery();
      while (rs.next()) {
        Participante participante = new Participante(
          rs.getInt("id"),
          rs.getString("nome"),
          rs.getString("sexo"),
          rs.getString("email"),
          rs.getString("celular"),
          rs.getString("ePalestrante"),
          rs.getString("curriculo"),
          rs.getString("areaAtuacao"),
          rs.getString("evento")
        );
        lista.add(participante);
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return lista;
    } catch (SQLException e) {
      System.err.println("Erro no método listarPorParametro(String nome, String sexo) da classe ParticipanteDao ao executar SELECT: " + e.getMessage());
      return new ArrayList<Participante>();
    }
  }

  public Participante buscarPorId(Integer id) {
    try {
      Participante participante = new Participante();
      String sql = "SELECT * FROM participante where id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      ResultSet rs = pstm.executeQuery();
      if (rs.next()) {
        participante = new Participante(
          rs.getInt("id"),
          rs.getString("nome"),
          rs.getString("sexo"),
          rs.getString("email"),
          rs.getString("celular"),
          rs.getString("ePalestrante"),
          rs.getString("curriculo"),
          rs.getString("areaAtuacao"),
          rs.getString("evento")
        );
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return participante;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorId(Integer id) da classe ParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return new Participante();
    }
  }

  public Participante buscarPorEmail(String email) {
    try {
      Participante participante = new Participante();
      String sql = "SELECT * FROM participante where email = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setString(1, email);
      ResultSet rs = pstm.executeQuery();
      if (rs.next()) {
        participante = new Participante(
          rs.getInt("id"),
          rs.getString("nome"),
          rs.getString("sexo"),
          rs.getString("email"),
          rs.getString("celular"),
          rs.getString("ePalestrante"),
          rs.getString("curriculo"),
          rs.getString("areaAtuacao"),
          rs.getString("evento")
        );
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return participante;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorEmail(String email) da classe ParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return new Participante();
    }
  }

  public Participante buscarPorCelular(String celular) {
    try {
      Participante participante = new Participante();
      String sql = "SELECT * FROM participante where celular = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setString(1, celular);
      ResultSet rs = pstm.executeQuery();
      if (rs.next()) {
        participante = new Participante(
          rs.getInt("id"),
          rs.getString("nome"),
          rs.getString("sexo"),
          rs.getString("email"),
          rs.getString("celular"),
          rs.getString("ePalestrante"),
          rs.getString("curriculo"),
          rs.getString("areaAtuacao"),
          rs.getString("evento")
        );
      }
      rs.close();
      pstm.close();
      this.sqlConn.close(conn);
      return participante;
    } catch (SQLException e) {
      System.err.println("Erro no método buscarPorCelular(String celular) da classe ParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return new Participante();
    }
  }

  public String inserir(String nome, String sexo, String email, String celular, String ePalestrante, String curriculo, String areaAtuacao, String evento) {
    try {
      Integer id = this.getNewId();
      String sql = "INSERT INTO participante(id, nome, sexo, email, celular, ePalestrante, curriculo, areaAtuacao, evento) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      pstm.setString(2, nome);
      pstm.setString(3, sexo);
      pstm.setString(4, email);
      pstm.setString(5, celular);
      pstm.setString(6, ePalestrante);
      pstm.setString(7, curriculo);
      pstm.setString(8, areaAtuacao);
      pstm.setString(9, evento);
      System.out.println("Resposta: " + pstm.executeUpdate());
      pstm.close();
      this.sqlConn.close(conn);
      return "sucesso";
    } catch (Exception e) {
      System.err.println("Erro no método inserir(...) da classe ParticipanteDao ao executar INSERT: " + e.getMessage());
      e.printStackTrace();
      return "erro";
    }
  }

  private Integer getNewId() {
    try {
      Integer id = 1;
      String sql = "SELECT MAX(id) AS max_id FROM participante";
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
      System.err.println("Erro no método getNewId() da classe ParticipanteDao ao executar SELECT: " + e.getMessage());
      e.printStackTrace();
      return -1;
    }
  }

  public String excluirPorId(int id) {
    try {
      String sql = "DELETE FROM participante WHERE id = ?";
      Connection conn = this.sqlConn.connect();
      PreparedStatement pstm = conn.prepareStatement(sql);
      pstm.setInt(1, id);
      System.out.println("Resposta: " + pstm.executeUpdate());
      pstm.close();
      this.sqlConn.close(conn);
      return "sucesso";
    } catch (Exception e) {
      System.err.println("Erro no método excluirPorId(int id) da classe ParticipanteDao ao executar DELETE: " + e.getMessage());
      e.printStackTrace();
      return "erro";
    }
  }

  public String atualizar(Participante participante) {
    try {
        String sql = "UPDATE participante SET nome = ?, sexo = ?, email = ?, celular = ?, ePalestrante = ?, curriculo = ?, areaAtuacao = ?, evento = ? WHERE id = ?";
        Connection conn = this.sqlConn.connect();
        PreparedStatement pstm = conn.prepareStatement(sql);
        
        // Setando os valores do participante nos parâmetros da query
        pstm.setString(1, participante.getNome());
        pstm.setString(2, participante.getSexo());
        pstm.setString(3, participante.getEmail());
        pstm.setString(4, participante.getCelular());
        pstm.setString(5, participante.getePalestrante());
        pstm.setString(6, participante.getCurriculo());
        pstm.setString(7, participante.getAreaAtuacao());
        pstm.setString(8, participante.getEvento());
        pstm.setInt(9, participante.getId());
        
        // Executa o update e retorna a resposta
        int resultado = pstm.executeUpdate();
        
        pstm.close();
        this.sqlConn.close(conn);
        
        if (resultado > 0) {
            return "sucesso";
        } else {
            return "erro";
        }
    } catch (SQLException e) {
        System.err.println("Erro no método atualizar() da classe ParticipanteDao ao executar UPDATE: " + e.getMessage());
        e.printStackTrace();
        return "erro";
    }
}
}
package service;

import java.util.List;

import dao.ParticipanteDao;
import table.Participante;

public class ParticipanteService {
  private ParticipanteDao dao;

  public ParticipanteService() {
    this.dao = new ParticipanteDao();
  }

  public List<Participante> listarTodos() {
    return this.dao.listarTodos();
  }

  public List<Participante> listarPorParamentro(String nome, String sexo) {
    return this.dao.listarPorParametro(nome, sexo);
  }

  public Participante buscarPorId(Integer id) {
    return this.dao.buscarPorId(id);
  }

  public Participante buscarPorEmail(String email) {
    return this.dao.buscarPorEmail(email);
  }

  public Participante buscarPorCelular(String celular) {
    return this.dao.buscarPorCelular(celular);
  }

  public String inserir(String nome, String sexo, String email, String celular, String ePalestrante,
                        String curriculo, String areaAtuacao, String evento) {
    return this.dao.inserir(nome, sexo, email, celular, ePalestrante, curriculo, areaAtuacao, evento);
  }

  public String excluirPorId(int id) {
    return this.dao.excluirPorId(id);
  }
}

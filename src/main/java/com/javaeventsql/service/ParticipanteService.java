package com.javaeventsql.service;

import java.util.List;

import com.javaeventsql.dao.ParticipanteDao;
import com.javaeventsql.table.Participante;

public class ParticipanteService {
  private ParticipanteDao dao;

  public ParticipanteService(ParticipanteDao dao) {
    this.dao = dao;
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

  public String inserir(String nome, String sexo, String email, String celular, String cpf, String ePalestrante, String curriculo, String areaAtuacao) {
    return this.dao.inserir(nome, sexo, email, celular, cpf, ePalestrante, curriculo, areaAtuacao);
  }

  public String atualizarPorId(int id, String nome, String sexo, String email, String cpf, String celular, String ePalestrante,
  String curriculo, String areaAtuacao) {
    return this.dao.atualizarPorId(id, nome, sexo, email, cpf, celular, ePalestrante, curriculo, areaAtuacao);
  }

  public String excluirPorId(int id) {
    return this.dao.excluirPorId(id);
  }
}

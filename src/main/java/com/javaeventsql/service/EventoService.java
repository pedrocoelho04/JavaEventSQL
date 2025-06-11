package com.javaeventsql.service;

import java.util.List;

import com.javaeventsql.dao.EventoDao;
import com.javaeventsql.table.Evento;

public class EventoService {
  private EventoDao dao;

  public EventoService(EventoDao dao) {
    this.dao = dao;
  }

  public List<Evento> listarTodos() {
    return this.dao.listarTodos();
  }

  public List<Evento> listarPorParamentro(String titulo, String local) {
    return this.dao.listarPorParametro(titulo, local);
  }

  public Evento buscarPorId(Integer id) {
    return this.dao.buscarPorId(id);
  }

  public Evento buscarPorLocal(String local) {
    return this.dao.buscarPorLocal(local);
  }

  public Evento buscarPorTitulo(String titulo) {
    return this.dao.buscarPorTitulo(titulo);
  }

  public String inserir(String titulo, String local, String data, String detalhes) {
    return this.dao.inserir(titulo, local, data, detalhes);
  }

  public String atualizar(Evento evento){
    return this.dao.atualizar(evento);
  }

  public String excluirPorId(int id) {
    return this.dao.excluirPorId(id);
  }
}
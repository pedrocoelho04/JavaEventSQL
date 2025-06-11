package com.javaeventsql.service;

import java.util.List;

import com.javaeventsql.dao.EventoParticipanteDao;
import com.javaeventsql.table.EventoParticipante;

public class EventoParticipanteService {
  private EventoParticipanteDao dao;

  public EventoParticipanteService(EventoParticipanteDao dao) {
    this.dao = dao;
  }

  public List<EventoParticipante> listarTodos() {
    return this.dao.listarTodos();
  }

  public List<EventoParticipante> listarPorParamentro(Integer id_evento, Integer id_participante) {
    return this.dao.listarPorParametro(id_evento, id_participante);
  }

  public EventoParticipante buscarPorId(Integer id) {
    return this.dao.buscarPorId(id);
  }

  public EventoParticipante buscarPorIdEvento(Integer id_evento) {
    return this.dao.buscarPorIdEvento(id_evento);
  }

  public EventoParticipante buscarPorIdParticipante(Integer id_participante) {
    return this.dao.buscarPorIdParticipante(id_participante);
  }

  public String inserir(Integer id_evento, Integer id_participante) {
    return this.dao.inserir(id_evento, id_participante);
  }
}

package service;

import java.util.List;

import dao.EventoDao;
import table.Evento;

public class EventoService {
  private EventoDao dao;

  public EventoService() {
    this.dao = new EventoDao();
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

  public String inserir(String titulo, String local) {
    return this.dao.inserir(titulo, local);
  }

  public String excluirPorId(int id) {
    return this.dao.excluirPorId(id);
  }
}

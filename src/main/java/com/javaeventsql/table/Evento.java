package com.javaeventsql.table;

public class Evento {
  private Integer id;
  private String titulo;
  private String local;
  private String data;
  private String detalhes;

  public Evento() {
  }

  public Evento(Integer id, String titulo, String local, String data, String detalhes) {
    this.id = id;
    this.titulo = titulo;
    this.local = local;
    this.data = data;
    this.detalhes = detalhes;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getDetalhes() {
    return detalhes;
  }

  public void setDetalhes(String detalhes) {
    this.detalhes = detalhes;
  }
}

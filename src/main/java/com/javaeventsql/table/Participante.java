package com.javaeventsql.table;

public class Participante {
  private Integer id;
  private String nome;
  private String sexo;
  private String email;
  private String celular;
  private String ePalestrante;
  private String curriculo;
  private String areaAtuacao;
  private String cpf;
  private String eventosInscritos;

  public Participante() {
  }

  public Participante(Integer id, String nome, String sexo, String email, String celular, String ePalestrante, String curriculo, String areaAtuacao, String cpf) {
    this.id = id;
    this.nome = nome;
    this.sexo = sexo;
    this.email = email;
    this.celular = celular;
    this.ePalestrante = ePalestrante;
    this.curriculo = curriculo;
    this.areaAtuacao = areaAtuacao;
    this.cpf = cpf;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getSexo() {
    return sexo;
  }

  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public String getePalestrante() {
    return ePalestrante;
  }

  public void setePalestrante(String ePalestrante) {
    this.ePalestrante = ePalestrante;
  }

  public String getCurriculo() {
    return curriculo;
  }

  public void setCurriculo(String curriculo) {
    this.curriculo = curriculo;
  }

  public String getAreaAtuacao() {
    return areaAtuacao;
  }

  public void setAreaAtuacao(String areaAtuacao) {
    this.areaAtuacao = areaAtuacao;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
      this.cpf = cpf;
  }

  public String getEventosInscritos() {
    return eventosInscritos;
  }

  public void setEventosInscritos(String eventosInscritos) {
    this.eventosInscritos = eventosInscritos;
  }
}

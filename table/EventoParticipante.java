package table;

public class EventoParticipante {
  private Integer id;
  private Integer id_evento;
  private Integer id_participante;

  public EventoParticipante() {
  }

  public EventoParticipante(Integer id, Integer id_evento, Integer id_participante) {
    this.id = id;
    this.id_evento = id_evento;
    this.id_participante = id_participante;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getEventId() {
    return id_evento;
  }

  public void setEventId(Integer id_evento) {
    this.id_evento = id_evento;
  }

  public Integer getParticipanteId() {
    return id_participante;
  }

  public void setParticipanteId(Integer id_participante) {
    this.id_participante = id_participante;
  }
}

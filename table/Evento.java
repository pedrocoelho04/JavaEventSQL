package table;

public class Evento {
    private Integer id;
    private String titulo;
    private String local;

    public Evento() {
    }

    public Evento(Integer id, String titulo, String local) {
        this.id = id;
        this.titulo = titulo;
        this.local = local;
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

}

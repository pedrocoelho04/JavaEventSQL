// import java.util.List;

// import service.EventoService;
// import service.ParticipanteService;
// import table.Evento;
// import table.Participante;

// public class StartApp {
//   public static void main(String[] args) {
//     ParticipanteService ps = new ParticipanteService();
//     List<Participante> listaParticipantes = ps.listarTodos();
//     for (Participante item : listaParticipantes) {
//       System.out.println("Nome: " + item.getNome() + "    E-mail: " + item.getEmail());
//     }
//     listaParticipantes = ps.listarPorParamentro("Vito", null);
//     for (Participante item : listaParticipantes) {
//       System.out.println("Nome: " + item.getNome() + "    Celular: " + item.getCelular());
//     }
//     Participante p = new Participante();
//     p = ps.buscarPorId(1);
//     System.out.println("Id: " + p.getId() + "   Nome: " + p.getNome());

//     EventoService es = new EventoService();
//     List<Evento> listaEventos = es.listarTodos();

//     // System.out.println(ps.inserir("Ana", "F", "ana@gmail.com", "(27) 99734
//     // 9870"));
//   }
// }

import screens.HomeFrame;

public class StartApp {
  public static void main(String[] args) {
    new HomeFrame();
  }
}
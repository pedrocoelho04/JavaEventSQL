import java.util.List;

import service.ParticipanteService;
import table.Participante;

public class StartApp {
    public static void main(String[] args) {
        ParticipanteService ps = new ParticipanteService();
        List<Participante> lista = ps.listarTodos();
        for (Participante item : lista) {
            System.out.println("Nome: " + item.getNome() + "    E-mail: " + item.getEmail());
        }
        lista = ps.listarPorParamentro("Vito", null);
        for (Participante item : lista) {
            System.out.println("Nome: " + item.getNome() + "    Celular: " + item.getCelular());
        }
        Participante p = new Participante();
        p = ps.buscarPorId(1);
        System.out.println("Id: " + p.getId() + "   Nome: " + p.getNome());

        // System.out.println(ps.inserir("Ana", "F", "ana@gmail.com", "(27) 99734
        // 9870"));
    }
}

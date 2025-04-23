package screens;

import dao.EventoDao;
import table.Evento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ConsultaEventosFrame extends JFrame {
  private JTable tabela;
  private EventoDao dao = new EventoDao();
  private List<Evento> eventos;

  public ConsultaEventosFrame() {
    setTitle("Consulta de Eventos");
    setSize(700, 400);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    atualizarTabela();

    JButton btnExcluir = new JButton("Excluir Evento Selecionado");
    btnExcluir.addActionListener(e -> excluirEvento());

    add(new JScrollPane(tabela), BorderLayout.CENTER);
    add(btnExcluir, BorderLayout.SOUTH);

    setVisible(true);
  }

  private void atualizarTabela() {
    eventos = dao.listarTodos();
    String[] colunas = { "ID", "Título", "Local" };
    String[][] dados = new String[eventos.size()][3];

    for (int i = 0; i < eventos.size(); i++) {
      Evento e = eventos.get(i);
      dados[i][0] = String.valueOf(e.getId());
      dados[i][1] = e.getTitulo();
      dados[i][2] = e.getLocal();
    }

    tabela = new JTable(dados, colunas);
  }

  private void excluirEvento() {
    int linha = tabela.getSelectedRow();
    if (linha >= 0) {
      int confirm = JOptionPane.showConfirmDialog(this,
          "Tem certeza que deseja excluir o evento selecionado?",
          "Confirmação de Exclusão",
          JOptionPane.YES_NO_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        int id = Integer.parseInt((String) tabela.getValueAt(linha, 0));
        String resultado = dao.excluirPorId(id);

        if (resultado.equals("sucesso")) {
          JOptionPane.showMessageDialog(this, "Evento excluído com sucesso.");
          dispose(); // fecha a janela atual
          new ConsultaEventosFrame(); // reabre para atualizar
        } else {
          JOptionPane.showMessageDialog(this, "Erro ao excluir o evento.");
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um evento para excluir.");
    }
  }
}
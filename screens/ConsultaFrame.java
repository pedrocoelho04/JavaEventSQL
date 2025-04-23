package screens;

import dao.ParticipanteDao;
import table.Participante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ConsultaFrame extends JFrame {
  private JTable tabela;
  private ParticipanteDao dao = new ParticipanteDao();
  private List<Participante> participantes;

  public ConsultaFrame() {
    setTitle("Consulta de Participantes");
    setSize(700, 400);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    atualizarTabela();

    JButton btnExcluir = new JButton("Excluir Participante Selecionado");
    btnExcluir.addActionListener(e -> excluirParticipante());

    add(new JScrollPane(tabela), BorderLayout.CENTER);
    add(btnExcluir, BorderLayout.SOUTH);

    setVisible(true);
  }

  private void atualizarTabela() {
    participantes = dao.listarTodos();
    String[] colunas = { "ID", "Nome", "Sexo", "Email", "Celular", "É Palestrante" };
    String[][] dados = new String[participantes.size()][6];

    for (int i = 0; i < participantes.size(); i++) {
      Participante p = participantes.get(i);
      dados[i][0] = String.valueOf(p.getId());
      dados[i][1] = p.getNome();
      dados[i][2] = p.getSexo();
      dados[i][3] = p.getEmail();
      dados[i][4] = p.getCelular();
      dados[i][5] = p.getePalestrante();
    }

    tabela = new JTable(dados, colunas);
  }

  private void excluirParticipante() {
    int linha = tabela.getSelectedRow();
    if (linha >= 0) {
      int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o participante?",
          "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        int id = Integer.parseInt((String) tabela.getValueAt(linha, 0));
        dao.excluirPorId(id);
        JOptionPane.showMessageDialog(this, "Participante excluído!");
        dispose();
        new ConsultaFrame();
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um participante para excluir.");
    }
  }
}
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
    setSize(800, 400);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    atualizarTabela();

    JButton btnExcluir = new JButton("Excluir Evento Selecionado");
    btnExcluir.addActionListener(e -> excluirEvento());

    JButton btnSalvarAlteracoes = new JButton("Salvar Alterações");
    btnSalvarAlteracoes.addActionListener(e -> salvarAlteracoes());

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(btnExcluir);
    buttonPanel.add(btnSalvarAlteracoes);

    add(new JScrollPane(tabela), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    setVisible(true);
  }

  private void atualizarTabela() {
    eventos = dao.listarTodos();
    String[] colunas = { "ID", "Título", "Local", "Data", "Detalhes" };
    String[][] dados = new String[eventos.size()][5];

    for (int i = 0; i < eventos.size(); i++) {
      Evento e = eventos.get(i);
      dados[i][0] = String.valueOf(e.getId());
      dados[i][1] = e.getTitulo();
      dados[i][2] = e.getLocal();
      dados[i][3] = e.getData();
      dados[i][4] = e.getDetalhes();
    }

    tabela = new JTable(dados, colunas);
    tabela.setCellSelectionEnabled(true); // Permitir a edição de células
    tabela.setFillsViewportHeight(true); // Ajuste da tabela

    // Escutador de eventos para detectar quando o usuário edita as células
    tabela.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        // Salvar alterações quando a tecla Enter for pressionada
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          salvarAlteracoes();
        }
      }
    });
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
          dispose();
          new ConsultaEventosFrame();
        } else {
          JOptionPane.showMessageDialog(this, "Erro ao excluir o evento.");
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um evento para excluir.");
    }
  }

  private void salvarAlteracoes() {
    int linha = tabela.getSelectedRow();
    if (linha >= 0) {
      try {
        int id = Integer.parseInt((String) tabela.getValueAt(linha, 0));
        String titulo = (String) tabela.getValueAt(linha, 1);
        String local = (String) tabela.getValueAt(linha, 2);
        String data = (String) tabela.getValueAt(linha, 3);
        String detalhes = (String) tabela.getValueAt(linha, 4);

        // Cria o evento com os novos dados
        Evento evento = new Evento(id, titulo, local, data, detalhes);

        // Atualiza o evento no banco de dados
        String resultado = dao.atualizar(evento);

        if (resultado.equals("sucesso")) {
          JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso.");
          atualizarTabela(); // Atualiza a tabela com os novos dados
        } else {
          JOptionPane.showMessageDialog(this, "Erro ao atualizar o evento.");
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.");
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um evento para editar.");
    }
  }
}

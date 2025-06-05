package screens;

import dao.ParticipanteDao;
import table.Participante;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsultaParticipanteFrame extends JFrame {
  private JTable tabela;
  private ParticipanteDao dao = new ParticipanteDao();
  private List<Participante> participantes;

  public ConsultaParticipanteFrame() {
    setTitle("Consulta de Participantes/Palestrantes");
    setSize(700, 400);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    atualizarTabela();

    // Botão para excluir o participante selecionado
    JButton btnExcluir = new JButton("Excluir Participante Selecionado");
    btnExcluir.addActionListener(e -> excluirParticipante());

    // Botão para editar o participante selecionado
    JButton btnEditar = new JButton("Editar Participante Selecionado");
    btnEditar.addActionListener(e -> editarParticipante());

    // Painel com a tabela e os botões
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JScrollPane(tabela), BorderLayout.CENTER);

    // Painel com os botões (Excluir e Editar)
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(btnExcluir);
    buttonPanel.add(btnEditar);

    panel.add(buttonPanel, BorderLayout.SOUTH);

    add(panel, BorderLayout.CENTER);

    setVisible(true);
  }

  private void atualizarTabela() {
    participantes = dao.listarTodos();
    String[] colunas = { "ID", "Nome", "Sexo", "Email", "Celular", "É Palestrante", "Currículo", "Área de Atuação" };
    String[][] dados = new String[participantes.size()][9];

    for (int i = 0; i < participantes.size(); i++) {
      Participante p = participantes.get(i);
      dados[i][0] = String.valueOf(p.getId());
      dados[i][1] = p.getNome();
      dados[i][2] = p.getSexo();
      dados[i][3] = p.getEmail();
      dados[i][4] = p.getCelular();
      dados[i][5] = p.getePalestrante();
      dados[i][6] = p.getCurriculo() != null ? p.getCurriculo() : "N/A";
      dados[i][7] = p.getAreaAtuacao() != null ? p.getAreaAtuacao() : "N/A";
    }

    tabela = new JTable(dados, colunas);
    tabela.setAutoCreateRowSorter(true); // Habilitar ordenação de colunas
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
        new ConsultaParticipanteFrame();
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um participante para excluir.");
    }
  }

  private void editarParticipante() {
    int linha = tabela.getSelectedRow();
    if (linha >= 0) {
      int id = Integer.parseInt((String) tabela.getValueAt(linha, 0));
      Participante p = dao.buscarPorId(id);

      // Criando a janela de edição
      JFrame editarFrame = new JFrame("Editar Participante");
      editarFrame.setSize(400, 400);
      editarFrame.setLocationRelativeTo(null);

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(9, 2));

      JTextField nomeField = new JTextField(p.getNome());
      JTextField sexoField = new JTextField(p.getSexo());
      JTextField emailField = new JTextField(p.getEmail());
      JTextField celularField = new JTextField(p.getCelular());
      JTextArea curriculoArea = new JTextArea(p.getCurriculo());
      JTextArea areaAtuacaoArea = new JTextArea(p.getAreaAtuacao());
      JCheckBox ePalestranteBox = new JCheckBox("É Palestrante?", p.getePalestrante().equals("S"));

      JButton salvarButton = new JButton("Salvar");
      salvarButton.addActionListener(e -> {
        String nome = nomeField.getText().trim();
        String sexo = sexoField.getText().trim().toUpperCase();
        String email = emailField.getText().trim();
        String celular = celularField.getText().trim();
        String ePalestrante = ePalestranteBox.isSelected() ? "S" : "N";
        String curriculo = curriculoArea.getText().trim();
        String areaAtuacao = areaAtuacaoArea.getText().trim();

        if (nome.isEmpty() || sexo.isEmpty() || email.isEmpty() || celular.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
          return;
        }

        if (!sexo.equals("F") && !sexo.equals("M")) {
          JOptionPane.showMessageDialog(null, "O campo 'Sexo' deve ser 'F' para feminino ou 'M' para masculino.");
          return;
        }

        // Atualizando o participante
        p.setNome(nome);
        p.setSexo(sexo);
        p.setEmail(email);
        p.setCelular(celular);
        p.setePalestrante(ePalestrante);
        p.setCurriculo(curriculo);
        p.setAreaAtuacao(areaAtuacao);

        dao.atualizarPorId(id, nome, sexo, email, celular, ePalestrante, curriculo, areaAtuacao); // Atualizando no banco de dados
        JOptionPane.showMessageDialog(editarFrame, "Participante atualizado com sucesso!");
        editarFrame.dispose();
        dispose();
        new ConsultaParticipanteFrame(); // Atualiza a lista de participantes
      });

      panel.add(new JLabel("Nome:"));
      panel.add(nomeField);
      panel.add(new JLabel("Sexo:"));
      panel.add(sexoField);
      panel.add(new JLabel("E-mail:"));
      panel.add(emailField);
      panel.add(new JLabel("Celular:"));
      panel.add(celularField);
      panel.add(new JLabel("Currículo:"));
      panel.add(new JScrollPane(curriculoArea));
      panel.add(new JLabel("Área de Atuação:"));
      panel.add(new JScrollPane(areaAtuacaoArea));
      panel.add(ePalestranteBox);
      panel.add(new JLabel(""));
      panel.add(salvarButton);

      editarFrame.add(panel);
      editarFrame.setVisible(true);
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um participante para editar.");
    }
  }
}
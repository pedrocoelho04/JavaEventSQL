package screens;

import dao.ParticipanteDao;
import table.Participante;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.util.List;

public class ConsultaParticipanteFrame extends JFrame {
  private JTable tabela;
  private ParticipanteDao dao = new ParticipanteDao();
  private List<Participante> participantes;

  private String formatarCpfParaExibicao(String cpf) {
    if (cpf == null || cpf.length() != 11) {
      return "N/A";
    }
    return cpf.substring(0, 3) + ".***.***-" + cpf.substring(9, 11);
  }

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
    String[] colunas = { "ID", "Nome", "Sexo", "Email", "CPF", "Celular", "É Palestrante", "Currículo",
        "Área de Atuação", "Eventos Inscritos" };
    String[][] dados = new String[participantes.size()][10];

    for (int i = 0; i < participantes.size(); i++) {
      Participante p = participantes.get(i);
      dados[i][0] = String.valueOf(p.getId());
      dados[i][1] = p.getNome();
      dados[i][2] = p.getSexo();
      dados[i][3] = p.getEmail();
      dados[i][4] = formatarCpfParaExibicao(p.getCpf());
      dados[i][5] = p.getCelular();
      dados[i][6] = "S".equals(p.getePalestrante()) ? "Sim" : "Não";
      dados[i][7] = p.getCurriculo() != null ? p.getCurriculo() : "N/A";
      dados[i][8] = p.getAreaAtuacao() != null ? p.getAreaAtuacao() : "N/A";
      dados[i][9] = p.getEventosInscritos() != null ? p.getEventosInscritos() : "Nenhum";
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
    if (linha < 0) {
      JOptionPane.showMessageDialog(this, "Selecione um participante na tabela para editar.",
          "Nenhum Participante Selecionado", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Busca o participante selecionado pelo ID na tabela
    int id = Integer.parseInt((String) tabela.getValueAt(linha, 0));
    Participante p = dao.buscarPorId(id);

    if (p == null || p.getId() == null) {
      JOptionPane.showMessageDialog(this, "Não foi possível encontrar os dados do participante selecionado.", "Erro",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    // --- Criação da Janela de Edição ---
    JFrame editarFrame = new JFrame("Editar Participante");
    editarFrame.setSize(450, 500); // Aumentei um pouco a altura para acomodar melhor os campos
    editarFrame.setLocationRelativeTo(this);
    editarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST; // Alinhar componentes à esquerda

    // --- Campos do Formulário ---
    JTextField nomeField = new JTextField(p.getNome());
    // Removido: JTextField sexoField = new JTextField(p.getSexo());
    JTextField emailField = new JTextField(p.getEmail());

    // --- Campo de Sexo com Radio Buttons ---
    JRadioButton femininoRadioButton = new JRadioButton("Feminino");
    JRadioButton masculinoRadioButton = new JRadioButton("Masculino");
    ButtonGroup sexoButtonGroup = new ButtonGroup();
    sexoButtonGroup.add(femininoRadioButton);
    sexoButtonGroup.add(masculinoRadioButton);

    // Pré-seleciona o sexo do participante
    if ("F".equals(p.getSexo())) {
      femininoRadioButton.setSelected(true);
    } else if ("M".equals(p.getSexo())) {
      masculinoRadioButton.setSelected(true);
    }

    JPanel sexoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sexoPanel.add(femininoRadioButton);
    sexoPanel.add(masculinoRadioButton);

    // Campo de Celular com Máscara
    JFormattedTextField celularField = null;
    try {
      MaskFormatter celularFormatter = new MaskFormatter("(##) #####-####");
      celularFormatter.setValueContainsLiteralCharacters(false);
      celularField = new JFormattedTextField(celularFormatter);
      celularField.setValue(p.getCelular()); // Carrega o celular
    } catch (java.text.ParseException e) {
      celularField = new JFormattedTextField(); // Fallback
    }

    // CAMPO DE CPF COM MÁSCARA
    JFormattedTextField cpfField = null;
    try {
      MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
      cpfFormatter.setValueContainsLiteralCharacters(false);
      cpfField = new JFormattedTextField(cpfFormatter);
      cpfField.setValue(p.getCpf()); // Carrega o CPF existente
    } catch (java.text.ParseException e) {
      cpfField = new JFormattedTextField(); // Fallback
    }

    JCheckBox ePalestranteBox = new JCheckBox("É Palestrante?", "S".equals(p.getePalestrante()));
    JTextArea curriculoArea = new JTextArea(p.getCurriculo(), 3, 20);
    JTextArea areaAtuacaoArea = new JTextArea(p.getAreaAtuacao(), 3, 20);

    // --- Botão Salvar ---
    JButton salvarButton = new JButton("Salvar Alterações");
    JFormattedTextField finalCpfField = cpfField; // Variável final para usar no lambda
    JFormattedTextField finalCelularField = celularField;
    salvarButton.addActionListener(e -> {
      // Obter dados dos campos
      String nome = nomeField.getText().trim();
      String email = emailField.getText().trim();
      String cpf = (String) finalCpfField.getValue();
      String celular = (String) finalCelularField.getValue();

      // Obter sexo dos Radio Buttons
      String sexo = "";
      if (femininoRadioButton.isSelected()) {
        sexo = "F";
      } else if (masculinoRadioButton.isSelected()) {
        sexo = "M";
      }

      String ePalestrante = ePalestranteBox.isSelected() ? "S" : "N";
      String curriculo = curriculoArea.getText().trim();
      String areaAtuacao = areaAtuacaoArea.getText().trim();

      // Validações
      if (nome.isEmpty() || sexo.isEmpty() || email.isEmpty() || celular == null || cpf == null) {
        JOptionPane.showMessageDialog(editarFrame, "Preencha todos os campos obrigatórios.");
        return;
      }

      // VALIDAÇÃO DO CPF
      if (!isCPFValido(cpf)) {
        JOptionPane.showMessageDialog(editarFrame, "O CPF inserido é inválido!", "CPF Inválido",
            JOptionPane.ERROR_MESSAGE);
        finalCpfField.requestFocus();
        return;
      }

      // Atualizando no banco de dados
      String resultado = dao.atualizarPorId(id, nome, sexo, email, cpf, celular, ePalestrante, curriculo, areaAtuacao);

      if ("sucesso".equalsIgnoreCase(resultado)) {
        JOptionPane.showMessageDialog(editarFrame, "Participante atualizado com sucesso!");
        editarFrame.dispose(); // Fecha a janela de edição
        dispose(); // Fecha a janela de consulta
        new ConsultaParticipanteFrame(); // Recria a janela de consulta para atualizar a tabela
      } else {
        JOptionPane.showMessageDialog(editarFrame, "Erro ao atualizar participante: " + resultado, "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    // --- Montagem do Painel ---
    int linhaGbc = 0;
    gbc.weightx = 0.1; // Peso para o label
    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("Nome:"), gbc);

    gbc.weightx = 0.9; // Peso para o campo
    gbc.gridx = 1;
    panel.add(nomeField, gbc);
    linhaGbc++;

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("CPF:"), gbc);
    gbc.gridx = 1;
    panel.add(cpfField, gbc);
    linhaGbc++;

    // Adicionando o painel de sexo
    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("Sexo:"), gbc);
    gbc.gridx = 1;
    panel.add(sexoPanel, gbc);
    linhaGbc++;

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("E-mail:"), gbc);
    gbc.gridx = 1;
    panel.add(emailField, gbc);
    linhaGbc++;

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("Celular:"), gbc);
    gbc.gridx = 1;
    panel.add(celularField, gbc);
    linhaGbc++;

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST; // Alinhar checkbox à esquerda
    panel.add(ePalestranteBox, gbc);
    linhaGbc++;
    gbc.gridwidth = 1; // Reset gridwidth

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("Currículo:"), gbc);
    gbc.gridx = 1;
    panel.add(new JScrollPane(curriculoArea), gbc);
    linhaGbc++;

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    panel.add(new JLabel("Área de Atuação:"), gbc);
    gbc.gridx = 1;
    panel.add(new JScrollPane(areaAtuacaoArea), gbc);
    linhaGbc++;

    // Adiciona um "espaçador" para empurrar o botão para baixo
    gbc.gridy = linhaGbc++;
    gbc.weighty = 1.0;
    panel.add(new JLabel(""), gbc);
    gbc.weighty = 0.0;

    gbc.gridx = 0;
    gbc.gridy = linhaGbc;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE; // Para não esticar o botão
    gbc.anchor = GridBagConstraints.CENTER; // Centraliza o botão
    panel.add(salvarButton, gbc);

    editarFrame.add(new JScrollPane(panel)); // Adiciona um scrollpane para caso a janela fique pequena
    editarFrame.setVisible(true);
  }

  public static boolean isCPFValido(String cpf) {
    // Remove caracteres não numéricos
    cpf = cpf.replaceAll("[^0-9]", "");

    // 1. Verifica se o CPF tem 11 dígitos
    if (cpf.length() != 11) {
      return false;
    }

    // 2. Verifica se todos os dígitos são iguais (ex: 111.111.111-11), o que é
    // inválido
    if (cpf.matches("(\\d)\\1{10}")) {
      return false;
    }

    try {
      // --- Cálculo do 1º Dígito Verificador ---
      int soma = 0;
      for (int i = 0; i < 9; i++) {
        soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (10 - i);
      }

      int primeiroDigito = 11 - (soma % 11);
      if (primeiroDigito >= 10) {
        primeiroDigito = 0;
      }

      // Verifica se o 1º dígito calculado é igual ao 10º dígito do CPF
      if (primeiroDigito != Integer.parseInt(String.valueOf(cpf.charAt(9)))) {
        return false;
      }

      // --- Cálculo do 2º Dígito Verificador ---
      soma = 0;
      for (int i = 0; i < 10; i++) {
        soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (11 - i);
      }

      int segundoDigito = 11 - (soma % 11);
      if (segundoDigito >= 10) {
        segundoDigito = 0;
      }

      // Verifica se o 2º dígito calculado é igual ao 11º dígito do CPF
      if (segundoDigito != Integer.parseInt(String.valueOf(cpf.charAt(10)))) {
        return false;
      }

    } catch (NumberFormatException e) {
      // Se ocorrer um erro na conversão, o CPF é inválido
      return false;
    }

    // Se passou por todas as verificações, o CPF é válido
    return true;
  }
}
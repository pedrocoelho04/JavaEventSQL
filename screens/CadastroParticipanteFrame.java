package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import service.ParticipanteService;

public class CadastroParticipanteFrame extends JFrame {
  private JTable tabela;
  public CadastroParticipanteFrame() {
    setTitle("Cadastro de Participante/Palestrante");
    setSize(400, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(10, 2, 10, 10)); // Organizando a grade para mais campos

    JLabel nomeLabel = new JLabel("Nome:");
    JTextField nomeField = new JTextField();

    JLabel sexoLabel = new JLabel("Sexo (F/M):");
    JTextField sexoField = new JTextField();

    JLabel emailLabel = new JLabel("E-mail:");
    JTextField emailField = new JTextField();

    JLabel celularLabel = new JLabel("Celular:");
    JTextField celularField = new JTextField();

    JCheckBox ePalestranteBox = new JCheckBox("É Palestrante?");

    add(new JScrollPane(tabela));

    // Campos adicionais
    JLabel curriculoLabel = new JLabel("Currículo:");
    JTextArea curriculoArea = new JTextArea();
    curriculoArea.setEnabled(false); // Inicia desabilitado
    JScrollPane curriculoScroll = new JScrollPane(curriculoArea); // Scroll para o campo de texto

    JLabel areaAtuacaoLabel = new JLabel("Área de Atuação:");
    JTextField areaAtuacaoField = new JTextField();
    areaAtuacaoField.setEnabled(false); // Inicia desabilitado

    // Mostrar/Esconder os campos adicionais quando o checkbox for selecionado
    ePalestranteBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean isPalestrante = ePalestranteBox.isSelected();
        curriculoArea.setEnabled(isPalestrante);
        areaAtuacaoField.setEnabled(isPalestrante);
      }
    });

    JButton salvarButton = new JButton("Salvar");
    salvarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String nome = nomeField.getText().trim();
        String sexo = sexoField.getText().trim().toUpperCase();
        String email = emailField.getText().trim();
        String celular = celularField.getText().trim();
        String ePalestrante = ePalestranteBox.isSelected() ? "S" : "N";
        String curriculo = curriculoArea.getText().trim();
        String areaAtuacao = areaAtuacaoField.getText().trim();

        if (nome.isEmpty() || sexo.isEmpty() || email.isEmpty() || celular.isEmpty() || ePalestrante.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
          return;
        }

        if (!sexo.equals("F") && !sexo.equals("M")) {
          JOptionPane.showMessageDialog(null, "O campo 'Sexo' deve ser 'F' para feminino ou 'M' para masculino.");
          return;
        }

        // Verifica se o participante é palestrante e valida os campos adicionais
        if (ePalestrante.equals("S") && (curriculo.isEmpty() || areaAtuacao.isEmpty())) {
          JOptionPane.showMessageDialog(null,
              "Preencha os campos 'Currículo', 'Área de Atuação' para palestrantes.");
          return;
        }

        ParticipanteService ps = new ParticipanteService();
        String resultado = ps.inserir(nome, sexo, email, celular, ePalestrante, curriculo, areaAtuacao);

        if ("sucesso".equalsIgnoreCase(resultado)) {
          JOptionPane.showMessageDialog(null, "Participante cadastrado com sucesso!");
          dispose();
        } else {
          JOptionPane.showMessageDialog(null, "Erro ao cadastrar participante:\n" + resultado);
        }
      }
    });

    // Adicionando os componentes ao painel
    panel.add(nomeLabel);
    panel.add(nomeField);

    panel.add(sexoLabel);
    panel.add(sexoField);

    panel.add(emailLabel);
    panel.add(emailField);

    panel.add(celularLabel);
    panel.add(celularField);

    panel.add(new JLabel("")); // Espaço vazio
    panel.add(ePalestranteBox);

    panel.add(curriculoLabel);
    panel.add(curriculoScroll);

    panel.add(areaAtuacaoLabel);
    panel.add(areaAtuacaoField);

    panel.add(new JLabel("")); // Espaço vazio
    panel.add(salvarButton);

    add(panel);
    setVisible(true);
  }  
}
package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import service.ParticipanteService;

public class CadastroFrame extends JFrame {
  public CadastroFrame() {
    setTitle("Cadastro de Participante");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(6, 2));

    JLabel nomeLabel = new JLabel("Nome:");
    JTextField nomeField = new JTextField();

    JLabel sexoLabel = new JLabel("Sexo (F/M):");
    JTextField sexoField = new JTextField();

    JLabel emailLabel = new JLabel("E-mail:");
    JTextField emailField = new JTextField();

    JLabel celularLabel = new JLabel("Celular:");
    JTextField celularField = new JTextField();

    JCheckBox ePalestranteBox = new JCheckBox("É Palestrante?");

    JButton salvarButton = new JButton("Salvar");
    salvarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String nome = nomeField.getText().trim();
        String sexo = sexoField.getText().trim().toUpperCase();
        String email = emailField.getText().trim();
        String celular = celularField.getText().trim();
        String ePalestrante = ePalestranteBox.isSelected() ? "S" : "N";

        if (nome.isEmpty() || sexo.isEmpty() || email.isEmpty() || celular.isEmpty() || ePalestrante.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
          return;
        }

        if (!sexo.equals("F") && !sexo.equals("M")) {
          JOptionPane.showMessageDialog(null, "O campo 'Sexo' deve ser 'F' para feminino ou 'M' para masculino.");
          return;
        }

        ParticipanteService ps = new ParticipanteService();
        String resultado = ps.inserir(nome, sexo, email, celular, ePalestrante);

        if ("sucesso".equalsIgnoreCase(resultado)) {
          JOptionPane.showMessageDialog(null, "Participante cadastrado com sucesso!");
          dispose();
        } else
          JOptionPane.showMessageDialog(null, "Erro ao cadastrar participante:\n" + resultado);
      }
    });

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
    panel.add(new JLabel("")); // Espaço vazio
    panel.add(salvarButton);

    add(panel);
    setVisible(true);
  }
}
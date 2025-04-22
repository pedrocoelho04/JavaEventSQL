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
    panel.setLayout(new GridLayout(5, 2));

    JLabel nomeLabel = new JLabel("Nome:");
    JTextField nomeField = new JTextField();

    JLabel emailLabel = new JLabel("E-mail:");
    JTextField emailField = new JTextField();

    JLabel celularLabel = new JLabel("Celular:");
    JTextField celularField = new JTextField();

    //O checkbox do ePalestrante

    JButton salvarButton = new JButton("Salvar");
    salvarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String celular = celularField.getText();
        //String ePalestrante = ePalestranteField.getText();

        ParticipanteService ps = new ParticipanteService();
        // ADICIONAR O CAMPO EPALESTRANTE no ps.inserir(nome, email, celular, ePalestrante), ELE DEVE SOMENTE RETORNAR 'S' SE ESTIVER MARCADO E 'N' PARA CASO NÃO ESTEJA MARCADO. 
        //boolean sucesso = ps.inserir(nome, email, celular);

        /*
        if (sucesso) {
          JOptionPane.showMessageDialog(null, "Participante cadastrado com sucesso!");
          dispose();
        } else {
          JOptionPane.showMessageDialog(null, "Erro ao cadastrar participante.");
        }
        */
      }
    });

    panel.add(nomeLabel);
    panel.add(nomeField);
    panel.add(emailLabel);
    panel.add(emailField);
    panel.add(celularLabel);
    panel.add(celularField);
    panel.add(new JLabel());
    panel.add(salvarButton);

    add(panel);
    setVisible(true);
  }
}
package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import service.EventoService;

public class CadastroEventoFrame extends JFrame {
  private JTextField tituloField;
  private JTextField localField;
  private EventoService eventoService;

  public CadastroEventoFrame() {
    setTitle("Cadastrar Novo Evento");
    setSize(400, 200);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(4, 2, 10, 10));

    eventoService = new EventoService();

    add(new JLabel("Título do Evento:"));
    tituloField = new JTextField();
    add(tituloField);

    add(new JLabel("Local do Evento:"));
    localField = new JTextField();
    add(localField);

    JButton btnCadastrar = new JButton("Cadastrar");
    btnCadastrar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String titulo = tituloField.getText().trim();
        String local = localField.getText().trim();

        if (titulo.isEmpty() || local.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
          return;
        }

        String resultado = eventoService.inserir(titulo, local);
        if (resultado.equals("sucesso") || resultado.equalsIgnoreCase("ok")) {
          JOptionPane.showMessageDialog(null, "Evento cadastrado com sucesso!");
          tituloField.setText("");
          localField.setText("");
        } else {
          JOptionPane.showMessageDialog(null, "Erro ao cadastrar evento.");
        }
      }
    });

    add(new JLabel()); // Espaço vazio
    add(btnCadastrar);

    setVisible(true);
  }
}
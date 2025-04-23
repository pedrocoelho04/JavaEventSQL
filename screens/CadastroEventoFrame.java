package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import service.EventoService;

public class CadastroEventoFrame extends JFrame {
  private JTextField tituloField;
  private JTextField localField;
  private JTextField dataField;
  private JTextField detalhesField;
  private EventoService eventoService;

  public CadastroEventoFrame() {
    setTitle("Cadastrar Novo Evento");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(6, 2, 10, 10));

    eventoService = new EventoService();

    // Título
    add(new JLabel("Título do Evento:"));
    tituloField = new JTextField();
    add(tituloField);

    // Local
    add(new JLabel("Local do Evento:"));
    localField = new JTextField();
    add(localField);

    // Data
    add(new JLabel("Data do Evento:"));
    dataField = new JTextField();
    add(dataField);

    // Detalhes
    add(new JLabel("Detalhes do Evento:"));
    detalhesField = new JTextField();
    add(detalhesField);

    // Botão Cadastrar
    JButton btnCadastrar = new JButton("Cadastrar");
    btnCadastrar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String titulo = tituloField.getText().trim();
        String local = localField.getText().trim();
        String data = dataField.getText().trim();
        String detalhes = detalhesField.getText().trim();

        if (titulo.isEmpty() || local.isEmpty() || data.isEmpty() || detalhes.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
          return;
        }

        String resultado = eventoService.inserir(titulo, local, data, detalhes);
        if (resultado.equals("sucesso") || resultado.equalsIgnoreCase("ok")) {
          JOptionPane.showMessageDialog(null, "Evento cadastrado com sucesso!");
          tituloField.setText("");
          localField.setText("");
          dataField.setText("");
          detalhesField.setText("");
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

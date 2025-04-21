package screens;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends JFrame {
  public HomeFrame() {
    setTitle("Sistema de Gerenciamento de Eventos");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(3, 1, 10, 10));

    JButton consultaParticipantesButton = new JButton("Consultar Participantes");
    consultaParticipantesButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new ConsultaFrame();
      }
    });

    JButton cadastroParticipantesButton = new JButton("Cadastrar Participante");
    cadastroParticipantesButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new CadastroFrame();
      }
    });

    JButton consultaEventosButton = new JButton("Consultar Eventos");
    consultaEventosButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new ConsultaEventosFrame();
      }
    });

    mainPanel.add(consultaParticipantesButton);
    mainPanel.add(cadastroParticipantesButton);
    mainPanel.add(consultaEventosButton);

    add(mainPanel);
    setVisible(true);
  }

  public static void main(String[] args) {
    new HomeFrame();
  }
}
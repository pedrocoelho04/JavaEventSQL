package screens;

import javax.swing.*;
import java.awt.*;

public class HomeFrame extends JFrame {
  public HomeFrame() {
    setTitle("Sistema de Gerenciamento de Eventos");
    setSize(500, 350);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));

    JLabel titleLabel = new JLabel("Painel Principal", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
    add(titleLabel, BorderLayout.NORTH);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(2, 2, 15, 15));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    JButton btnConsultaParticipantes = new JButton("Consultar Participantes");
    btnConsultaParticipantes.addActionListener(e -> new ConsultaFrame());

    JButton btnCadastroParticipantes = new JButton("Cadastrar Participante");
    btnCadastroParticipantes.addActionListener(e -> new CadastroFrame());

    JButton btnConsultaEventos = new JButton("Consultar Eventos");
    btnConsultaEventos.addActionListener(e -> new ConsultaEventosFrame());

    JButton btnCadastroEventos = new JButton("Cadastrar Evento");
    btnCadastroEventos.addActionListener(e -> new CadastroEventoFrame());

    mainPanel.add(btnConsultaParticipantes);
    mainPanel.add(btnCadastroParticipantes);
    mainPanel.add(btnConsultaEventos);
    mainPanel.add(btnCadastroEventos);

    add(mainPanel, BorderLayout.CENTER);

    JLabel footer = new JLabel("Sistema de Eventos", SwingConstants.CENTER);
    footer.setFont(new Font("Arial", Font.PLAIN, 12));
    add(footer, BorderLayout.SOUTH);

    setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new HomeFrame());
  }
}
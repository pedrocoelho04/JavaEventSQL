package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeFrame extends JFrame {

  // Definição de Cores (exemplo, sinta-se à vontade para ajustar)
  private static final Color COR_FUNDO_PRINCIPAL = new Color(238, 238, 238); // Um cinza bem claro
  private static final Color COR_PAINEL_BOTOES = new Color(250, 250, 250); // Quase branco
  private static final Color COR_TITULO = new Color(60, 70, 90); // Um azul escuro/cinza
  private static final Color COR_BOTAO_FUNDO = new Color(122, 122, 122); // Azul Microsoft
  private static final Color COR_BOTAO_FUNDO_HOVER = new Color(90, 90, 90); // Azul um pouco mais escuro
  private static final Color COR_BOTAO_TEXTO = Color.BLACK;
  private static final Color COR_RODAPE = new Color(100, 100, 100); // Cinza para o rodapé

  // Definição de Fontes
  private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 28);
  private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.PLAIN, 15);
  private static final Font FONTE_RODAPE = new Font("Segoe UI", Font.ITALIC, 12);

  public HomeFrame() {
    try {
      // Aplica o Look and Feel Nimbus para uma aparência mais moderna
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // Se o Nimbus não estiver disponível, usa o padrão do sistema
      System.err.println("Nimbus L&F não encontrado, usando o padrão.");
    }

    setTitle("Sistema de Gerenciamento de Eventos");
    setSize(700, 450); // Aumentei um pouco para melhor acomodação
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10)); // Espaçamento entre as regiões do BorderLayout
    getContentPane().setBackground(COR_FUNDO_PRINCIPAL); // Cor de fundo para o frame

    // --- TÍTULO ---
    JLabel titleLabel = new JLabel("Painel de Controle de Eventos", SwingConstants.CENTER);
    titleLabel.setFont(FONTE_TITULO);
    titleLabel.setForeground(COR_TITULO);
    // Adiciona um preenchimento (padding) acima e abaixo do título
    titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
    add(titleLabel, BorderLayout.NORTH);

    // --- PAINEL DE BOTÕES ---
    JPanel buttonPanel = new JPanel();
    // Usando GridBagLayout para mais controle, mas GridLayout também funciona
    // GridLayout(linhas, colunas, gapHorizontal, gapVertical)
    buttonPanel.setLayout(new GridLayout(2, 2, 20, 20));
    buttonPanel.setBackground(COR_PAINEL_BOTOES);
    // Adiciona um preenchimento (padding) ao redor do painel de botões
    buttonPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(20, 40, 20, 40), // Margem externa
        BorderFactory.createEmptyBorder(20, 20, 20, 20) // Margem interna
    ));

    // Criando os botões com o novo estilo
    JButton btnConsultaParticipantes = createStyledButton("Consultar Participantes/Palestrantes");
    btnConsultaParticipantes.addActionListener(e -> {
      // new ConsultaParticipanteFrame().setVisible(true); // Se o construtor não
      // tornar visível
      new ConsultaParticipanteFrame();
    });

    JButton btnCadastroParticipantes = createStyledButton("Cadastrar Participante/Palestrante");
    btnCadastroParticipantes.addActionListener(e -> {
      new CadastroParticipanteFrame();
    });

    JButton btnConsultaEventos = createStyledButton("Consultar Eventos");
    btnConsultaEventos.addActionListener(e -> {
      new ConsultaEventosFrame();
    });

    JButton btnCadastroEventos = createStyledButton("Cadastrar Evento");
    btnCadastroEventos.addActionListener(e -> {
      new CadastroEventoFrame(null);
    });

    buttonPanel.add(btnConsultaParticipantes);
    buttonPanel.add(btnCadastroParticipantes);
    buttonPanel.add(btnConsultaEventos);
    buttonPanel.add(btnCadastroEventos);

    add(buttonPanel, BorderLayout.CENTER);

    // --- RODAPÉ ---
    JLabel footerLabel = new JLabel("© 2025 Sistema de Eventos", SwingConstants.CENTER);
    footerLabel.setFont(FONTE_RODAPE);
    footerLabel.setForeground(COR_RODAPE);
    footerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    add(footerLabel, BorderLayout.SOUTH);

    setVisible(true);
  }

  // Método utilitário para criar e estilizar botões
  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(FONTE_BOTAO);
    button.setBackground(COR_BOTAO_FUNDO);
    button.setForeground(COR_BOTAO_TEXTO);
    button.setFocusPainted(false); // Remove a borda de foco pintada
    button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor para "mãozinha"

    // Adiciona um leve preenchimento interno ao botão
    button.setMargin(new Insets(10, 15, 10, 15));

    // Efeito de hover simples
    button.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        button.setBackground(COR_BOTAO_FUNDO_HOVER);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        button.setBackground(COR_BOTAO_FUNDO);
      }
    });
    return button;
  }
}
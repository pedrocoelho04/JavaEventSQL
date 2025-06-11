package com.javaeventsql.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.javaeventsql.table.Evento;
import com.javaeventsql.dao.EventoDao;
import com.javaeventsql.service.EventoService;

public class CadastroEventoFrame extends JFrame {
  

  // --- Constantes de Estilo (Replicadas para exemplo, idealmente em uma classe
  // de utilidades) ---
  private static final Color COR_FUNDO_PAINEL = new Color(250, 250, 250); // Usado para o painel principal
  private static final Color COR_BOTAO_FUNDO = new Color(0, 120, 215);
  private static final Color COR_BOTAO_FUNDO_HOVER = new Color(0, 100, 195);
  private static final Color COR_BOTAO_TEXTO = Color.WHITE;
  private static final Font FONTE_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
  private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);
  private static final Font FONTE_TEXTFIELD = new Font("Segoe UI", Font.PLAIN, 14);
  // --- Fim das Constantes de Estilo ---

  private JTextField tituloField;
  private JTextField localField;
  private JFormattedTextField dataField;
  private JTextField detalhesField; // Poderia ser JTextArea se detalhes forem longos
  private JButton btnAcao;

  private EventoService eventoService;
  private EventoDao eventoDao;
  private Evento eventoParaEditar;
  private ConsultaEventosFrame consultaFramePai;

  public CadastroEventoFrame(ConsultaEventosFrame consultaFramePai) {
    this(null, consultaFramePai);
  }

  public CadastroEventoFrame(Evento evento, ConsultaEventosFrame consultaFramePai) {
    // Aplicar Look and Feel Nimbus (Idealmente feito uma vez no main da aplicação)
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      System.err.println("Nimbus L&F não encontrado, usando o padrão.");
    }

    this.eventoParaEditar = evento;
    this.eventoService = new EventoService(eventoDao);
    this.consultaFramePai = consultaFramePai;

    if (eventoParaEditar != null) {
      setTitle("Editar Evento");
    } else {
      setTitle("Cadastrar Novo Evento");
    }

    // setSize(450, 280); // pack() cuidará disso
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    // setLocationRelativeTo(null); // Chamado após pack()

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(COR_FUNDO_PAINEL); // Aplicar cor de fundo ao painel
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8); // Aumentar um pouco o espaçamento
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    int linha = 0;

    // Título
    addFormField(panel, gbc, "Título do Evento:", tituloField = new JTextField(25), linha++);

    // Local
    addFormField(panel, gbc, "Local do Evento:", localField = new JTextField(25), linha++);

    // Data
    try {
      MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
      dateFormatter.setPlaceholderCharacter('_');
      dataField = new JFormattedTextField(dateFormatter);
      dataField.setColumns(12); // Ajuste leve
    } catch (ParseException e) {
      System.err.println("Erro máscara de data: " + e.getMessage());
      dataField = new JFormattedTextField();
      dataField.setToolTipText("Use DD/MM/AAAA");
    }
    addFormField(panel, gbc, "Data (DD/MM/AAAA):", dataField, linha++);

    // Detalhes
    // Se detalhes puderem ser longos, considere JTextArea com JScrollPane
    // JTextArea detalhesArea = new JTextArea(4, 25);
    // JScrollPane detalhesScroll = new JScrollPane(detalhesArea);
    // addFormField(panel, gbc, "Detalhes:", detalhesScroll, linha++);
    addFormField(panel, gbc, "Detalhes do Evento:", detalhesField = new JTextField(25), linha++);

    // Botão Ação (Cadastrar ou Atualizar)
    String textoBotao = (eventoParaEditar != null) ? "Atualizar Evento" : "Cadastrar Evento";
    btnAcao = createStyledButton(textoBotao); // Usar o helper para estilizar
    btnAcao.addActionListener(e -> executarAcaoPrincipal());

    gbc.gridx = 0; // Para o botão ocupar ambas as colunas ou alinhar à direita
    gbc.gridy = linha;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER; // Centralizar o botão
    gbc.fill = GridBagConstraints.NONE; // Para não esticar o botão
    gbc.insets = new Insets(15, 8, 8, 8); // Mais espaço acima do botão
    panel.add(btnAcao, gbc);

    // Adicionando painel ao Frame
    // getContentPane().setBackground(new Color(220,220,220)); // Cinza claro para
    // fundo do frame
    add(panel, BorderLayout.CENTER);
    // Adiciona um pouco de padding ao redor do painel principal (formPanel)
    // O panel já tem seu próprio border/padding de gbc.insets
    // Para padding geral do frame:
    ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    if (eventoParaEditar != null) {
      preencherCamposParaEdicao();
    }

    pack(); // Ajusta o frame ao conteúdo
    setMinimumSize(getSize()); // Garante que o frame não encolha demais
    setLocationRelativeTo(null); // Centraliza após o pack
    setVisible(true);
  }

  // Método auxiliar para adicionar campos ao formulário GridBagLayout
  // (Replicado de CadastroParticipanteFrame, idealmente em classe utilitária)
  private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
    JLabel label = new JLabel(labelText);
    label.setFont(FONTE_LABEL);
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.3; // Proporção do label
    gbc.anchor = GridBagConstraints.LINE_START; // Alinha labels à esquerda
    panel.add(label, gbc);

    if (component instanceof JTextField || component instanceof JFormattedTextField || component instanceof JComboBox) {
      component.setFont(FONTE_TEXTFIELD);
    }
    gbc.gridx = 1;
    gbc.weightx = 0.7; // Proporção do campo
    gbc.anchor = GridBagConstraints.LINE_END; // Alinha campos à direita
    panel.add(component, gbc);
  }

  private void preencherCamposParaEdicao() {
    tituloField.setText(eventoParaEditar.getTitulo());
    localField.setText(eventoParaEditar.getLocal());
    limparEValidarDataField(eventoParaEditar.getData());
    detalhesField.setText(eventoParaEditar.getDetalhes());
  }

  private void executarAcaoPrincipal() {
    String titulo = tituloField.getText().trim();
    String local = localField.getText().trim();
    String dataStr = dataField.getText().trim();
    String detalhes = detalhesField.getText().trim();

    if (titulo.isEmpty() || local.isEmpty() || detalhes.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Preencha todos os campos (Título, Local, Detalhes).", "Campos Vazios",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (dataStr.contains("_") || dataStr.equals("//")) { // Checa placeholder ou campo vazio pela máscara
      JOptionPane.showMessageDialog(this, "A data está incompleta ou vazia. Use o formato DD/MM/AAAA.", "Data Inválida",
          JOptionPane.ERROR_MESSAGE);
      dataField.requestFocus();
      return;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setLenient(false);
    try {
      sdf.parse(dataStr);
    } catch (ParseException ex) {
      JOptionPane.showMessageDialog(this, "Data inválida. Use o formato DD/MM/AAAA.", "Data Inválida",
          JOptionPane.ERROR_MESSAGE);
      limparEValidarDataField(null);
      dataField.requestFocus();
      return;
    }

    String resultado;
    if (eventoParaEditar != null) {
      Evento eventoAtualizado = new Evento(
          eventoParaEditar.getId(), titulo, local, dataStr, detalhes);
      resultado = eventoService.atualizar(eventoAtualizado);
      if ("sucesso".equalsIgnoreCase(resultado)) {
        JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!", "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
        if (consultaFramePai != null) {
          consultaFramePai.atualizarTabelaView();
        }
        dispose();
      } else {
        JOptionPane.showMessageDialog(this, "Erro ao atualizar evento: " + resultado, "Erro na Atualização",
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      resultado = eventoService.inserir(titulo, local, dataStr, detalhes);
      if ("sucesso".equalsIgnoreCase(resultado) || "ok".equalsIgnoreCase(resultado)) {
        JOptionPane.showMessageDialog(this, "Evento cadastrado com sucesso!", "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
        tituloField.setText("");
        localField.setText("");
        limparEValidarDataField(null);
        detalhesField.setText("");
        if (consultaFramePai != null) {
          consultaFramePai.atualizarTabelaView();
        }
      } else {
        JOptionPane.showMessageDialog(this, "Erro ao cadastrar evento: " + resultado, "Erro no Cadastro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void limparEValidarDataField(String dataStr) {
    if (dataStr == null || dataStr.trim().isEmpty() || dataStr.equals("//")) { // Adicionado "//" para caso de campo
                                                                               // vazio com máscara
      dataField.setValue(null); // Limpa o valor formatado
      dataField.setText(""); // Garante que o texto visual também seja limpo
      return;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setLenient(false);
    try {
      if (!dataStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
        throw new ParseException("Formato de data inválido, esperado DD/MM/AAAA", 0);
      }
      Date data = sdf.parse(dataStr);
      dataField.setText(sdf.format(data)); // Define o texto formatado
    } catch (ParseException e) {
      System.err.println("Data inválida para campo: '" + dataStr + "'. Campo limpo. Erro: " + e.getMessage());
      dataField.setValue(null);
      dataField.setText("");
    }
  }

  // Método auxiliar para criar botões estilizados (replicado)
  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(FONTE_BOTAO);
    button.setBackground(COR_BOTAO_FUNDO);
    button.setForeground(COR_BOTAO_TEXTO);
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setMargin(new Insets(8, 15, 8, 15));

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
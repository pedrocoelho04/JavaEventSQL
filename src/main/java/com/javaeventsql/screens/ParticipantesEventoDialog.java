package com.javaeventsql.screens;

import com.javaeventsql.dao.EventoParticipanteDao;
import com.javaeventsql.dao.ParticipanteDao;
import com.javaeventsql.table.EventoParticipante;
import com.javaeventsql.table.Participante;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipantesEventoDialog extends JDialog {

  private int eventoId;
  // private String eventoTitulo; // Não usado diretamente após o título da janela

  private JTable tabelaInscritos;
  private DefaultTableModel modelInscritos;
  private JTable tabelaOutros;
  private DefaultTableModel modelOutros;

  private JComboBox<String> comboBoxCriterioBusca; // Novo ComboBox
  private JTextField campoValorBusca; // Renomeado de campoBuscaOutros para clareza
  private JButton botaoBuscarOutros;
  private JButton botaoAdicionarParticipante;
  private JButton botaoRemoverParticipante;

  private ParticipanteDao participanteDao;
  private EventoParticipanteDao eventoParticipanteDao;

  private List<Participante> todosParticipantesCache;

  public ParticipantesEventoDialog(Frame owner, int eventoId, String eventoTitulo) {
    super(owner, "Participantes do Evento: " + eventoTitulo, true);
    this.eventoId = eventoId;
    // this.eventoTitulo = eventoTitulo; // Armazenado se necessário para outros
    // usos
    this.participanteDao = new ParticipanteDao();
    this.eventoParticipanteDao = new EventoParticipanteDao();

    setSize(950, 650); // Aumentei um pouco para comportar os novos campos de busca
    setLocationRelativeTo(owner);
    setLayout(new BorderLayout(10, 10));

    initComponents();
    carregarTodosParticipantesCache();
    carregarParticipantesInscritos();
    // Carrega inicialmente com critério "Todos" (ou "Nome" com valor vazio)
    carregarOutrosParticipantes((String) comboBoxCriterioBusca.getSelectedItem(), campoValorBusca.getText());

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }

  private void initComponents() {
    // --- Painel Superior: Participantes Inscritos ---
    JPanel painelInscritos = new JPanel(new BorderLayout(5, 5));
    painelInscritos.setBorder(new TitledBorder("Participantes Inscritos no Evento"));

    String[] colunasInscritos = { "ID", "Nome", "Email", "Celular", "É Palestrante" };
    modelInscritos = new DefaultTableModel(colunasInscritos, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tabelaInscritos = new JTable(modelInscritos);
    tabelaInscritos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    painelInscritos.add(new JScrollPane(tabelaInscritos), BorderLayout.CENTER);

    botaoRemoverParticipante = new JButton("Remover Selecionado do Evento");
    botaoRemoverParticipante.addActionListener(e -> removerParticipanteDoEvento());
    JPanel painelAcaoRemover = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    painelAcaoRemover.add(botaoRemoverParticipante);
    painelInscritos.add(painelAcaoRemover, BorderLayout.SOUTH);

    // --- Painel Inferior: Outros Participantes e Busca ---
    JPanel painelOutros = new JPanel(new BorderLayout(5, 5));
    painelOutros.setBorder(new TitledBorder("Adicionar Outros Participantes"));

    // Sub-Painel de Busca
    JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Adicionado espaçamento
    painelBusca.add(new JLabel("Buscar por:"));

    String[] criterios = { "Todos", "ID", "Nome", "Email", "Celular", "É Palestrante" };
    comboBoxCriterioBusca = new JComboBox<>(criterios);
    painelBusca.add(comboBoxCriterioBusca);

    painelBusca.add(new JLabel("Valor:"));
    campoValorBusca = new JTextField(20);
    // Adiciona listener para buscar ao pressionar Enter no campo de texto
    campoValorBusca.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          botaoBuscarOutros.doClick();
        }
      }
    });
    painelBusca.add(campoValorBusca);

    botaoBuscarOutros = new JButton("Buscar");
    botaoBuscarOutros.addActionListener(e -> {
      String criterio = (String) comboBoxCriterioBusca.getSelectedItem();
      String valor = campoValorBusca.getText();
      carregarOutrosParticipantes(criterio, valor);
    });
    painelBusca.add(botaoBuscarOutros);
    painelOutros.add(painelBusca, BorderLayout.NORTH);

    String[] colunasOutros = { "ID", "Nome", "Email", "Celular", "É Palestrante" };
    modelOutros = new DefaultTableModel(colunasOutros, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tabelaOutros = new JTable(modelOutros);
    tabelaOutros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    painelOutros.add(new JScrollPane(tabelaOutros), BorderLayout.CENTER);

    botaoAdicionarParticipante = new JButton("Adicionar Selecionado ao Evento");
    botaoAdicionarParticipante.addActionListener(e -> adicionarParticipanteAoEvento());
    JPanel painelAcaoAdicionar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    painelAcaoAdicionar.add(botaoAdicionarParticipante);
    painelOutros.add(painelAcaoAdicionar, BorderLayout.SOUTH);

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelInscritos, painelOutros);
    splitPane.setResizeWeight(0.45); // Ajuste para dar um pouco mais de espaço para "Outros" inicialmente
    add(splitPane, BorderLayout.CENTER);

    JButton btnFechar = new JButton("Fechar");
    btnFechar.addActionListener(e -> dispose());
    JPanel painelBotoesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    painelBotoesInferior.add(btnFechar);
    add(painelBotoesInferior, BorderLayout.SOUTH);
  }

  private void carregarTodosParticipantesCache() {
    todosParticipantesCache = participanteDao.listarTodos();
    if (todosParticipantesCache == null) {
      todosParticipantesCache = new ArrayList<>();
      JOptionPane.showMessageDialog(this, "Não foi possível carregar a lista de todos os participantes.",
          "Erro de Dados", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void carregarParticipantesInscritos() {
    modelInscritos.setRowCount(0);
    List<EventoParticipante> inscricoes = eventoParticipanteDao.listarPorParametro(this.eventoId, null);
    if (inscricoes != null) {
      for (EventoParticipante ep : inscricoes) {
        Participante p = todosParticipantesCache.stream()
            .filter(participante -> participante.getId().equals(ep.getParticipanteId()))
            .findFirst()
            .orElse(null); // Busca no cache primeiro
        if (p == null) { // Se não estiver no cache (improvável se cache está atualizado), busca no DAO
          p = participanteDao.buscarPorId(ep.getParticipanteId());
        }

        if (p != null && p.getId() != null) {
          modelInscritos.addRow(new Object[] {
              p.getId(), p.getNome(), p.getEmail(), p.getCelular(), "S".equals(p.getePalestrante()) ? "Sim" : "Não"
          });
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Não foi possível carregar os participantes inscritos.", "Erro de Dados",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void carregarOutrosParticipantes(String criterioBusca, String valorBuscaStr) {
    modelOutros.setRowCount(0);

    List<Integer> idsInscritos = new ArrayList<>();
    for (int i = 0; i < modelInscritos.getRowCount(); i++) {
      idsInscritos.add((Integer) modelInscritos.getValueAt(i, 0));
    }
    // Alternativamente, buscar novamente do DAO se preferir garantir dados mais
    // recentes
    // List<EventoParticipante> inscricoes =
    // eventoParticipanteDao.listarPorParametro(this.eventoId, null);
    // if (inscricoes != null) {
    // for (EventoParticipante ep : inscricoes) {
    // idsInscritos.add(ep.getId_participante());
    // }
    // }

    List<Participante> participantesParaConsiderar = new ArrayList<>(todosParticipantesCache);
    List<Participante> resultadoFiltragem = new ArrayList<>();

    final String valorBuscaFinal = (valorBuscaStr == null) ? "" : valorBuscaStr.trim();

    if ("Todos".equals(criterioBusca) || valorBuscaFinal.isEmpty() && !"ID".equals(criterioBusca)) {
      // Se for "Todos", ou se o valor de busca estiver vazio (exceto para ID, onde
      // vazio não faz sentido)
      resultadoFiltragem.addAll(participantesParaConsiderar);
    } else {
      switch (criterioBusca) {
        case "ID":
          if (valorBuscaFinal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ID para buscar.", "ID Vazio",
                JOptionPane.WARNING_MESSAGE);
            resultadoFiltragem.addAll(participantesParaConsiderar); // Ou não mostrar nada: resultadoFiltragem.clear();
            break;
          }
          try {
            Integer idBuscado = Integer.parseInt(valorBuscaFinal);
            resultadoFiltragem = participantesParaConsiderar.stream()
                .filter(p -> p.getId() != null && p.getId().equals(idBuscado))
                .collect(Collectors.toList());
          } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido. Deve ser um número.", "Erro de Formato",
                JOptionPane.ERROR_MESSAGE);
            resultadoFiltragem.addAll(participantesParaConsiderar); // Fallback: mostra todos os não inscritos
          }
          break;
        case "Nome":
          resultadoFiltragem = participantesParaConsiderar.stream()
              .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(valorBuscaFinal.toLowerCase()))
              .collect(Collectors.toList());
          break;
        case "Email":
          resultadoFiltragem = participantesParaConsiderar.stream()
              .filter(p -> p.getEmail() != null && p.getEmail().toLowerCase().contains(valorBuscaFinal.toLowerCase()))
              .collect(Collectors.toList());
          break;
        case "Celular":
          resultadoFiltragem = participantesParaConsiderar.stream()
              .filter(p -> p.getCelular() != null && p.getCelular().contains(valorBuscaFinal))
              .collect(Collectors.toList());
          break;
        default: // Caso "Todos" já tratado, mas como segurança
          resultadoFiltragem.addAll(participantesParaConsiderar);
          break;
      }
    }

    // Adiciona à tabela 'modelOutros' apenas os que estão em 'resultadoFiltragem' E
    // não estão em 'idsInscritos'
    if (resultadoFiltragem != null) {
      for (Participante p : resultadoFiltragem) {
        if (p.getId() != null && !idsInscritos.contains(p.getId())) {
          modelOutros.addRow(new Object[] {
              p.getId(), p.getNome(), p.getEmail(), p.getCelular(), "S".equals(p.getePalestrante()) ? "Sim" : "Não"
          });
        }
      }
    } else {
      // Isso não deve acontecer se resultadoFiltragem for inicializado
      JOptionPane.showMessageDialog(this, "Erro ao filtrar participantes.", "Erro de Dados", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void adicionarParticipanteAoEvento() {
    int linhaSelecionada = tabelaOutros.getSelectedRow();
    if (linhaSelecionada >= 0) {
      try {
        int idParticipante = (Integer) modelOutros.getValueAt(linhaSelecionada, 0);

        boolean jaInscrito = false;
        for (int i = 0; i < modelInscritos.getRowCount(); i++) {
          if (idParticipante == (Integer) modelInscritos.getValueAt(i, 0)) {
            jaInscrito = true;
            break;
          }
        }
        // List<EventoParticipante> inscricoes =
        // eventoParticipanteDao.listarPorParametro(this.eventoId, idParticipante);
        // if (inscricoes != null && !inscricoes.isEmpty()){
        // jaInscrito = true;
        // }

        if (jaInscrito) {
          JOptionPane.showMessageDialog(this, "Este participante já está inscrito neste evento.", "Atenção",
              JOptionPane.WARNING_MESSAGE);
          return;
        }

        String resultado = eventoParticipanteDao.inserir(this.eventoId, idParticipante);
        if ("sucesso".equals(resultado)) {
          JOptionPane.showMessageDialog(this, "Participante adicionado ao evento com sucesso!");
          // Atualizar cache ou refazer chamadas DAO seria mais robusto se o cache não for
          // alterado aqui
          carregarParticipantesInscritos();
          carregarOutrosParticipantes((String) comboBoxCriterioBusca.getSelectedItem(), campoValorBusca.getText());
        } else {
          JOptionPane.showMessageDialog(this, "Erro ao adicionar participante: " + resultado, "Erro",
              JOptionPane.ERROR_MESSAGE);
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao processar adição: " + e.getMessage(), "Erro",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um participante na tabela 'Outros Participantes' para adicionar.",
          "Nenhum Participante Selecionado", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void removerParticipanteDoEvento() {
    int linhaSelecionada = tabelaInscritos.getSelectedRow();
    if (linhaSelecionada >= 0) {
      try {
        int idParticipante = (Integer) modelInscritos.getValueAt(linhaSelecionada, 0);

        List<EventoParticipante> relacoes = eventoParticipanteDao.listarPorParametro(this.eventoId, idParticipante);
        if (relacoes != null && !relacoes.isEmpty()) {
          int idRelacao = relacoes.get(0).getId();
          String resultado = eventoParticipanteDao.excluirPorId(idRelacao);

          if ("sucesso".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Participante removido do evento com sucesso!");
            carregarParticipantesInscritos();
            carregarOutrosParticipantes((String) comboBoxCriterioBusca.getSelectedItem(), campoValorBusca.getText());
          } else {
            JOptionPane.showMessageDialog(this, "Erro ao remover participante: " + resultado, "Erro",
                JOptionPane.ERROR_MESSAGE);
          }
        } else {
          JOptionPane.showMessageDialog(this, "Não foi possível encontrar a inscrição do participante para remover.",
              "Erro de Dados", JOptionPane.ERROR_MESSAGE);
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao processar remoção: " + e.getMessage(), "Erro",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
      }
    } else {
      JOptionPane.showMessageDialog(this, "Selecione um participante na tabela 'Participantes Inscritos' para remover.",
          "Nenhum Participante Selecionado", JOptionPane.INFORMATION_MESSAGE);
    }
  }
}
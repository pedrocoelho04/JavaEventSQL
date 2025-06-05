package screens;

import dao.EventoDao;
import table.Evento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ConsultaEventosFrame extends JFrame {
    private JTable tabela;
    private EventoDao dao = new EventoDao();
    private List<Evento> eventos; // Mantém a lista de eventos para fácil acesso

    public ConsultaEventosFrame() {
        setTitle("Consulta de Eventos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        atualizarTabelaView();

        JButton btnExcluir = new JButton("Excluir Evento");
        btnExcluir.addActionListener(e -> excluirEvento());

        JButton btnEditar = new JButton("Editar Evento");
        btnEditar.addActionListener(e -> abrirJanelaEdicao());

        JButton btnVerParticipantes = new JButton("Ver Participantes");
        btnVerParticipantes.addActionListener(e -> abrirJanelaParticipantes());
        
        // O botão "Novo Evento" foi REMOVIDO daqui

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        // O botão "Novo Evento" foi REMOVIDO do painel
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnVerParticipantes);

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setVisible(true);
    }

    public void atualizarTabelaView() {
        eventos = dao.listarTodos();
        String[] colunas = { "ID", "Título", "Local", "Data", "Detalhes" };
        Object[][] dados = new Object[eventos.size()][5];

        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            dados[i][0] = e.getId();
            dados[i][1] = e.getTitulo();
            dados[i][2] = e.getLocal();
            dados[i][3] = e.getData();
            dados[i][4] = e.getDetalhes();
        }

        if (tabela == null) {
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(dados, colunas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };
            tabela = new JTable(model);
            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tabela.setFillsViewportHeight(true);
            
            tabela.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2 && tabela.getSelectedRow() != -1) {
                        abrirJanelaEdicao();
                    }
                }
            });

        } else {
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tabela.getModel();
            model.setDataVector(dados, colunas);
        }

        Component centerComponent = ((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent instanceof JScrollPane) {
            ((JScrollPane) centerComponent).setViewportView(tabela);
        }
    }
    
    // O método abrirJanelaCadastro() foi REMOVIDO

    private void abrirJanelaEdicao() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Validação para garantir que o índice da linha é válido no modelo da tabela
            int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
            if (modelRow < eventos.size()) { // Verifica se o índice está dentro dos limites da lista 'eventos'
                Evento eventoSelecionado = eventos.get(modelRow);
                new CadastroEventoFrame(eventoSelecionado, this);
            } else {
                System.err.println("Erro: Índice de linha selecionada fora dos limites da lista de eventos após conversão.");
                JOptionPane.showMessageDialog(this, "Erro ao selecionar o evento. Tente novamente.", "Erro de Seleção", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um evento na tabela para editar.", "Nenhum Evento Selecionado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void excluirEvento() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o evento selecionado?",
                    "Confirmação de Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                 // Validação para garantir que o índice da linha é válido no modelo da tabela
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                if (modelRow < eventos.size()) {
                    Evento eventoParaExcluir = eventos.get(modelRow);
                    int id = eventoParaExcluir.getId();
                    String resultado = dao.excluirPorId(id);

                    if ("sucesso".equalsIgnoreCase(resultado)) {
                        JOptionPane.showMessageDialog(this, "Evento excluído com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        atualizarTabelaView();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir o evento: " + resultado, "Erro na Exclusão", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.err.println("Erro: Índice de linha selecionada fora dos limites da lista de eventos para exclusão.");
                    JOptionPane.showMessageDialog(this, "Erro ao selecionar o evento para excluir. Tente novamente.", "Erro de Seleção", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para excluir.", "Nenhum Evento Selecionado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirJanelaParticipantes() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            try {
                 // Validação para garantir que o índice da linha é válido no modelo da tabela
                int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
                if (modelRow < eventos.size()) {
                    Evento eventoSelecionado = eventos.get(modelRow);
                    int idEvento = eventoSelecionado.getId();
                    String tituloEvento = eventoSelecionado.getTitulo();

                    ParticipantesEventoDialog dialogo = new ParticipantesEventoDialog(this, idEvento, tituloEvento);
                    dialogo.setVisible(true);
                } else {
                     System.err.println("Erro: Índice de linha selecionada fora dos limites da lista de eventos para ver participantes.");
                     JOptionPane.showMessageDialog(this, "Erro ao selecionar o evento. Tente novamente.", "Erro de Seleção", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IndexOutOfBoundsException e) { // Segurança adicional, embora a checagem acima deva cobrir
                 JOptionPane.showMessageDialog(this, "Erro ao obter dados do evento selecionado.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um evento na tabela.", "Nenhum Evento Selecionado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
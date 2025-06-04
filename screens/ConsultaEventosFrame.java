package screens;

import dao.EventoDao;
import table.Evento;
// Removidos imports não utilizados diretamente nesta classe para esta funcionalidade
// import dao.ParticipanteDao;
// import table.Participante;
// import dao.EventoParticipanteDao;
// import table.EventoParticipante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ConsultaEventosFrame extends JFrame {
    private JTable tabela;
    private EventoDao dao = new EventoDao();
    private List<Evento> eventos;

    public ConsultaEventosFrame() {
        setTitle("Consulta de Eventos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Melhor que EXIT_ON_CLOSE se houver outras janelas
        setLayout(new BorderLayout());

        atualizarTabela(); // Inicializa a tabela

        JButton btnExcluir = new JButton("Excluir Evento Selecionado");
        btnExcluir.addActionListener(e -> excluirEvento());

        JButton btnSalvarAlteracoes = new JButton("Salvar Alterações");
        btnSalvarAlteracoes.addActionListener(e -> salvarAlteracoes());

        JButton btnVerParticipantes = new JButton("Ver Participantes do Evento");
        btnVerParticipantes.addActionListener(e -> abrirJanelaParticipantes());

        JPanel buttonPanel = new JPanel(); // FlowLayout por padrão
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnSalvarAlteracoes);
        buttonPanel.add(btnVerParticipantes); // Adiciona o novo botão

        // Garante que a tabela seja adicionada ao JScrollPane após ser inicializada
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void atualizarTabela() {
        eventos = dao.listarTodos();
        String[] colunas = { "ID", "Título", "Local", "Data", "Detalhes" };
        String[][] dados = new String[eventos.size()][5];

        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            dados[i][0] = String.valueOf(e.getId());
            dados[i][1] = e.getTitulo();
            dados[i][2] = e.getLocal();
            dados[i][3] = e.getData();
            dados[i][4] = e.getDetalhes();
        }

        if (tabela == null) { // Cria a tabela apenas uma vez
            tabela = new JTable(dados, colunas);
            tabela.setCellSelectionEnabled(true);
            tabela.setFillsViewportHeight(true);
            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Importante para pegar um único evento

            tabela.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        // Verificar se uma célula está em edição antes de salvar
                        if (tabela.isEditing()) {
                            tabela.getCellEditor().stopCellEditing();
                        }
                        salvarAlteracoes();
                    }
                }
            });
        } else { // Apenas atualiza o modelo da tabela se ela já existir
            ((javax.swing.table.DefaultTableModel) tabela.getModel()).setDataVector(dados, colunas);
        }

        // Se o JScrollPane já existe, apenas atualiza a view.
        // Se não, ele será criado e adicionado no construtor.
        // Esta parte é importante se atualizarTabela for chamada após a construção inicial.
        Component centerComponent = ((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent instanceof JScrollPane) {
            ((JScrollPane) centerComponent).setViewportView(tabela);
        }
    }


    private void excluirEvento() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o evento selecionado?",
                    "Confirmação de Exclusão",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt((String) tabela.getValueAt(linha, 0));
                String resultado = dao.excluirPorId(id);

                if (resultado.equals("sucesso")) {
                    JOptionPane.showMessageDialog(this, "Evento excluído com sucesso.");
                    // Não recriar a frame, apenas atualizar a tabela
                    atualizarTabelaView();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o evento: " + resultado);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um evento para excluir.");
        }
    }

    private void salvarAlteracoes() {
        int linha = tabela.getSelectedRow();
        // Se uma célula estiver sendo editada, tenta parar a edição para pegar o valor atualizado
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }

        if (linha >= 0) {
            try {
                // Re-ler os valores da tabela, pois podem ter sido editados
                int id = Integer.parseInt(tabela.getModel().getValueAt(linha, 0).toString());
                String titulo = tabela.getModel().getValueAt(linha, 1).toString();
                String local = tabela.getModel().getValueAt(linha, 2).toString();
                String data = tabela.getModel().getValueAt(linha, 3).toString();
                String detalhes = tabela.getModel().getValueAt(linha, 4).toString();


                Evento evento = new Evento(id, titulo, local, data, detalhes);
                String resultado = dao.atualizar(evento);

                if (resultado.equals("sucesso")) {
                    JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso.");
                    eventos.set(linha, evento); // Atualiza a lista local também
                    // A tabela já reflete a edição, mas uma recarga pode ser necessária
                    // se a ordem ou o número de itens mudar. Neste caso, não.
                    // ((javax.swing.table.DefaultTableModel) tabela.getModel()).fireTableRowsUpdated(linha, linha);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar o evento: " + resultado);
                    atualizarTabelaView(); // Reverter para os dados do banco em caso de erro
                }
            } catch (NumberFormatException nfe) {
                 JOptionPane.showMessageDialog(this, "Erro ao salvar alterações: ID inválido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar alterações: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum evento selecionado para salvar ou linha inválida.");
        }
    }

    // Método auxiliar para atualizar a tabela e o JScrollPane
    private void atualizarTabelaView() {
        eventos = dao.listarTodos();
        String[] colunas = { "ID", "Título", "Local", "Data", "Detalhes" };
        String[][] dados = new String[eventos.size()][5];

        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            dados[i][0] = String.valueOf(e.getId());
            dados[i][1] = e.getTitulo();
            dados[i][2] = e.getLocal();
            dados[i][3] = e.getData();
            dados[i][4] = e.getDetalhes();
        }
        // Assumindo que a tabela usa DefaultTableModel ou você precisará adaptar
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tabela.getModel();
        model.setDataVector(dados, colunas);
    }


    private void abrirJanelaParticipantes() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            try {
                // Obter o ID do evento da linha selecionada
                // Certifique-se de que a coluna 0 (ID) é convertida corretamente
                int idEvento = Integer.parseInt(tabela.getValueAt(linhaSelecionada, 0).toString());
                String tituloEvento = tabela.getValueAt(linhaSelecionada, 1).toString();

                // Criar e exibir a nova janela de diálogo
                // Supondo que ParticipantesEventoDialog existe no mesmo pacote 'screens'
                ParticipantesEventoDialog dialogo = new ParticipantesEventoDialog(this, idEvento, tituloEvento);
                dialogo.setVisible(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID do evento inválido na linha selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um evento na tabela.", "Nenhum Evento Selecionado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
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
        setLayout(new BorderLayout());

        atualizarTabelaView(); // Renomeado para consistência e para inicializar

        JButton btnExcluir = new JButton("Excluir Evento");
        btnExcluir.addActionListener(e -> excluirEvento());

        JButton btnEditar = new JButton("Editar Evento"); // NOVO BOTÃO
        btnEditar.addActionListener(e -> abrirJanelaEdicao());

        // O botão "Salvar Alterações" (para edição na tabela) pode ser mantido ou removido
        // se a edição for exclusivamente pela nova tela. Vou mantê-lo por enquanto.
        JButton btnSalvarAlteracoesTabela = new JButton("Salvar Edição na Tabela");
        btnSalvarAlteracoesTabela.addActionListener(e -> salvarAlteracoesNaTabela());

        JButton btnVerParticipantes = new JButton("Ver Participantes");
        btnVerParticipantes.addActionListener(e -> abrirJanelaParticipantes());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnEditar); // Adiciona o botão de editar
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnSalvarAlteracoesTabela);
        buttonPanel.add(btnVerParticipantes);

        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Renomeei 'atualizarTabela' para 'atualizarTabelaView' para clareza
    // e para diferenciá-lo do método de DAO, caso houvesse.
    // Este método agora também lida com a criação inicial da tabela.
    public void atualizarTabelaView() {
        eventos = dao.listarTodos(); // Atualiza a lista de eventos local
        String[] colunas = { "ID", "Título", "Local", "Data", "Detalhes" };
        Object[][] dados = new Object[eventos.size()][5]; // Usar Object[][] para JTable

        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            dados[i][0] = e.getId(); // ID como Integer é melhor para JTable
            dados[i][1] = e.getTitulo();
            dados[i][2] = e.getLocal();
            dados[i][3] = e.getData();
            dados[i][4] = e.getDetalhes();
        }

        if (tabela == null) {
            // Criar um DefaultTableModel para que a tabela não seja editável por padrão
            // A menos que você queira permitir edição direta E por formulário
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(dados, colunas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Permite edição apenas se você quiser manter a funcionalidade de edição na tabela.
                    // Se a edição for exclusivamente pelo formulário, retorne false.
                    // Por agora, vamos manter a edição na tabela conforme o código original.
                    return true; // ou return false; para desabilitar edição na tabela
                }
            };
            tabela = new JTable(model);
            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tabela.setFillsViewportHeight(true);
            // Removido setCellSelectionEnabled(true) pois o DefaultTableModel já lida com isso
            // e o isCellEditable controla a edição.

            tabela.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (tabela.isEditing()) {
                            tabela.getCellEditor().stopCellEditing();
                        }
                        // Se a edição for só por formulário, este listener pode ser removido ou adaptado
                        salvarAlteracoesNaTabela();
                    }
                }
            });
             // Adicionar listener para duplo clique para editar (opcional)
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
        } else if (tabela != null) { // Caso inicial onde o JScrollPane pode não estar no layout ainda
            // Esta lógica pode ser simplificada se o JScrollPane for criado uma vez no construtor
        }
    }

    private void abrirJanelaEdicao() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Obter o objeto Evento da lista 'eventos' usando o índice da linha
            // Isso é mais seguro do que reconstruir o objeto a partir dos valores da tabela,
            // especialmente se o ID não for estritamente numérico ou se houver conversões.
            Evento eventoSelecionado = eventos.get(tabela.convertRowIndexToModel(linhaSelecionada)); // Usa a lista local
            
            // Passa 'this' para que CadastroEventoFrame possa chamar atualizarTabelaView()
            new CadastroEventoFrame(eventoSelecionado, this);
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
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Obter o ID do objeto Evento, não da String na tabela diretamente, para mais segurança
                Evento eventoParaExcluir = eventos.get(tabela.convertRowIndexToModel(linhaSelecionada));
                int id = eventoParaExcluir.getId();
                String resultado = dao.excluirPorId(id);

                if (resultado.equals("sucesso")) {
                    JOptionPane.showMessageDialog(this, "Evento excluído com sucesso.");
                    atualizarTabelaView();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o evento: " + resultado);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um evento para excluir.");
        }
    }

    // Renomeado para clareza, já que agora existe uma tela de edição dedicada
    private void salvarAlteracoesNaTabela() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }

        if (linhaSelecionada >= 0) {
            try {
                // É importante obter os dados do MODELO da tabela, não da lista 'eventos' diretamente aqui,
                // pois o usuário pode ter editado na célula.
                int id = (Integer) tabela.getModel().getValueAt(tabela.convertRowIndexToModel(linhaSelecionada), 0); // Assumindo que ID é Integer
                String titulo = tabela.getModel().getValueAt(tabela.convertRowIndexToModel(linhaSelecionada), 1).toString();
                String local = tabela.getModel().getValueAt(tabela.convertRowIndexToModel(linhaSelecionada), 2).toString();
                String data = tabela.getModel().getValueAt(tabela.convertRowIndexToModel(linhaSelecionada), 3).toString();
                String detalhes = tabela.getModel().getValueAt(tabela.convertRowIndexToModel(linhaSelecionada), 4).toString();

                Evento evento = new Evento(id, titulo, local, data, detalhes);
                String resultado = dao.atualizar(evento);

                if (resultado.equals("sucesso")) {
                    JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso (via tabela).");
                    // Atualizar a lista 'eventos' local para manter a consistência
                    eventos.set(tabela.convertRowIndexToModel(linhaSelecionada), evento);
                    // A tabela já deve refletir visualmente, mas uma atualização completa pode ser mais segura
                    // atualizarTabelaView(); // Descomente se necessário
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar o evento (via tabela): " + resultado);
                    atualizarTabelaView(); // Reverter para os dados do banco
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar alterações da tabela: " + ex.getMessage());
                ex.printStackTrace();
                atualizarTabelaView(); // Reverter em caso de erro de formato, etc.
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum evento selecionado na tabela para salvar.");
        }
    }

    private void abrirJanelaParticipantes() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            try {
                Evento eventoSelecionado = eventos.get(tabela.convertRowIndexToModel(linhaSelecionada));
                int idEvento = eventoSelecionado.getId();
                String tituloEvento = eventoSelecionado.getTitulo();

                ParticipantesEventoDialog dialogo = new ParticipantesEventoDialog(this, idEvento, tituloEvento);
                dialogo.setVisible(true);
            } catch (IndexOutOfBoundsException e) { // Segurança adicional
                 JOptionPane.showMessageDialog(this, "Erro ao obter dados do evento selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um evento na tabela.", "Nenhum Evento Selecionado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
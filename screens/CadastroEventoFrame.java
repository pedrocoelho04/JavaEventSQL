package screens;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import table.Evento; // Import da classe Evento
import service.EventoService;

public class CadastroEventoFrame extends JFrame {
    private JTextField tituloField;
    private JTextField localField;
    private JFormattedTextField dataField;
    private JTextField detalhesField;
    private JButton btnAcao; // Botão que será "Cadastrar" ou "Atualizar"

    private EventoService eventoService;
    private Evento eventoParaEditar; // Armazena o evento em edição (null se for cadastro)
    private ConsultaEventosFrame consultaFramePai; // Referência para atualizar a tabela pai

    // Construtor para Novo Cadastro
    public CadastroEventoFrame(ConsultaEventosFrame consultaFramePai) {
        this(null, consultaFramePai); // Chama o construtor principal com evento nulo
    }

    // Construtor Principal (para Cadastro ou Edição)
    public CadastroEventoFrame(Evento evento, ConsultaEventosFrame consultaFramePai) {
        this.eventoParaEditar = evento;
        this.eventoService = new EventoService();
        this.consultaFramePai = consultaFramePai; // Armazena a referência

        if (eventoParaEditar != null) {
            setTitle("Editar Evento");
        } else {
            setTitle("Cadastrar Novo Evento");
        }

        setSize(450, 280); // Ajustado
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Título do Evento:"), gbc);
        tituloField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(tituloField, gbc);

        // Local
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Local do Evento:"), gbc);
        localField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(localField, gbc);

        // Data
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Data (DD/MM/AAAA):"), gbc);
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            dataField = new JFormattedTextField(dateFormatter);
            dataField.setColumns(10);
        } catch (ParseException e) {
            System.err.println("Erro máscara de data: " + e.getMessage());
            dataField = new JFormattedTextField();
            dataField.setToolTipText("Use DD/MM/AAAA");
        }
        gbc.gridx = 1;
        panel.add(dataField, gbc);

        // Detalhes
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Detalhes do Evento:"), gbc);
        detalhesField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(detalhesField, gbc);

        // Botão Ação (Cadastrar ou Atualizar)
        btnAcao = new JButton();
        if (eventoParaEditar != null) {
            btnAcao.setText("Atualizar");
        } else {
            btnAcao.setText("Cadastrar");
        }
        btnAcao.addActionListener(e -> executarAcaoPrincipal());
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(btnAcao, gbc);

        add(panel, BorderLayout.CENTER);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (eventoParaEditar != null) {
            preencherCamposParaEdicao();
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void preencherCamposParaEdicao() {
        tituloField.setText(eventoParaEditar.getTitulo());
        localField.setText(eventoParaEditar.getLocal());
        limparEValidarDataField(eventoParaEditar.getData()); // Usa o método existente
        detalhesField.setText(eventoParaEditar.getDetalhes());
    }

    private void executarAcaoPrincipal() {
        String titulo = tituloField.getText().trim();
        String local = localField.getText().trim();
        String dataStr = dataField.getText().trim();
        String detalhes = detalhesField.getText().trim();

        if (titulo.isEmpty() || local.isEmpty() || detalhes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos (Título, Local, Detalhes).", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dataStr.contains("_")) {
            JOptionPane.showMessageDialog(this, "A data está incompleta. Use o formato DD/MM/AAAA.", "Data Inválida", JOptionPane.ERROR_MESSAGE);
            dataField.requestFocus();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dataStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato DD/MM/AAAA.", "Data Inválida", JOptionPane.ERROR_MESSAGE);
            limparEValidarDataField(null);
            dataField.requestFocus();
            return;
        }

        if (eventoParaEditar != null) { // Modo Edição
            Evento eventoAtualizado = new Evento(
                eventoParaEditar.getId(), // Mantém o ID original
                titulo,
                local,
                dataStr,
                detalhes
            );
            String resultado = eventoService.atualizar(eventoAtualizado); // Supondo que EventoService tem o método atualizar
            if ("sucesso".equalsIgnoreCase(resultado)) {
                JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                if (consultaFramePai != null) {
                    consultaFramePai.atualizarTabelaView(); // Atualiza a tabela na tela de consulta
                }
                dispose(); // Fecha a janela de edição
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar evento: " + resultado, "Erro na Atualização", JOptionPane.ERROR_MESSAGE);
            }
        } else { // Modo Cadastro
            String resultado = eventoService.inserir(titulo, local, dataStr, detalhes);
            if ("sucesso".equalsIgnoreCase(resultado) || "ok".equalsIgnoreCase(resultado)) {
                JOptionPane.showMessageDialog(this, "Evento cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                tituloField.setText("");
                localField.setText("");
                limparEValidarDataField(null);
                detalhesField.setText("");
                 if (consultaFramePai != null) { // Se foi chamado da tela de consulta, atualiza ela
                    consultaFramePai.atualizarTabelaView();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar evento: " + resultado, "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void limparEValidarDataField(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            dataField.setValue(null);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            if (!dataStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                throw new ParseException("Formato de data inválido, esperado DD/MM/AAAA", 0);
            }
            Date data = sdf.parse(dataStr);
            dataField.setText(sdf.format(data));
        } catch (ParseException e) {
            System.err.println("Data inválida para campo: '" + dataStr + "'. Campo limpo. Erro: " + e.getMessage());
            dataField.setValue(null);
        }
    }
}
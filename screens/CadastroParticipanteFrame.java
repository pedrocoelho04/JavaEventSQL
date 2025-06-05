package screens;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import service.ParticipanteService;

public class CadastroParticipanteFrame extends JFrame {

    // --- Constantes de Estilo ---
    private static final Color COR_FUNDO_PAINEL = new Color(250, 250, 250);
    private static final Color COR_BOTAO_FUNDO = new Color(0, 120, 215);
    private static final Color COR_BOTAO_FUNDO_HOVER = new Color(0, 100, 195);
    private static final Color COR_BOTAO_TEXTO = Color.WHITE;
    private static final Font FONTE_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONTE_TEXTFIELD = new Font("Segoe UI", Font.PLAIN, 14);
    // --- Fim das Constantes de Estilo ---

    private JTextField nomeField;
    private JTextField sexoField;
    private JTextField emailField;
    private JFormattedTextField celularField;
    private JCheckBox ePalestranteBox;
    private JTextArea curriculoArea;
    private JTextField areaAtuacaoField;
    private JButton salvarButton;

    // Declare os JLabels que precisam ser acessados pelo ActionListener como campos da classe
    // ou como variáveis locais 'final' ou 'efetivamente final' antes da sua utilização no ActionListener
    private JLabel curriculoLabelComponent; // Tornando campo da classe
    private JLabel areaAtuacaoLabelComponent; // Tornando campo da classe

    private ParticipanteService participanteService;

    public CadastroParticipanteFrame() {
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

        setTitle("Cadastro de Participante/Palestrante");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        participanteService = new ParticipanteService();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO_PAINEL);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int linhaGBC = 0; // Variável para layout GridBag, não será usada no ActionListener

        // Nome
        addFormField(formPanel, gbc, new JLabel("Nome:"), nomeField = new JTextField(25), linhaGBC++);

        // Sexo
        sexoField = new JTextField(3);
        sexoField.setToolTipText("Use 'F' para Feminino ou 'M' para Masculino");
        addFormField(formPanel, gbc, new JLabel("Sexo (F/M):"), sexoField, linhaGBC++);

        // E-mail
        addFormField(formPanel, gbc, new JLabel("E-mail:"), emailField = new JTextField(25), linhaGBC++);

        // Celular
        try {
            MaskFormatter celularFormatter = new MaskFormatter("(##) #####-####");
            celularFormatter.setPlaceholderCharacter('_');
            celularField = new JFormattedTextField(celularFormatter);
            celularField.setColumns(15);
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara de celular: " + e.getMessage());
            celularField = new JFormattedTextField();
            celularField.setToolTipText("Formato: (XX) XXXXX-XXXX");
        }
        addFormField(formPanel, gbc, new JLabel("Celular:"), celularField, linhaGBC++);

        // É Palestrante?
        ePalestranteBox = new JCheckBox("É Palestrante?");
        ePalestranteBox.setFont(FONTE_LABEL);
        ePalestranteBox.setBackground(COR_FUNDO_PAINEL);
        gbc.gridx = 0;
        gbc.gridy = linhaGBC;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(ePalestranteBox, gbc);
        linhaGBC++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Currículo
        curriculoLabelComponent = new JLabel("Currículo:"); // Instancia o JLabel aqui
        curriculoArea = new JTextArea(5, 25);
        curriculoArea.setFont(FONTE_TEXTFIELD);
        curriculoArea.setLineWrap(true);
        curriculoArea.setWrapStyleWord(true);
        JScrollPane curriculoScroll = new JScrollPane(curriculoArea);
        curriculoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        addFormField(formPanel, gbc, curriculoLabelComponent, curriculoScroll, linhaGBC++); // Passa a instância do JLabel

        // Área de Atuação
        areaAtuacaoLabelComponent = new JLabel("Área de Atuação:"); // Instancia o JLabel aqui
        areaAtuacaoField = new JTextField(25);
        addFormField(formPanel, gbc, areaAtuacaoLabelComponent, areaAtuacaoField, linhaGBC++); // Passa a instância do JLabel

        // Ação do CheckBox - Agora usa as referências diretas aos JLabels
        ePalestranteBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isPalestrante = ePalestranteBox.isSelected();
                curriculoLabelComponent.setEnabled(isPalestrante); // Usa a referência direta
                curriculoArea.setEnabled(isPalestrante);
                areaAtuacaoLabelComponent.setEnabled(isPalestrante); // Usa a referência direta
                areaAtuacaoField.setEnabled(isPalestrante);
            }
        });

        // Desabilitar campos e labels de palestrante inicialmente
        curriculoLabelComponent.setEnabled(false);
        curriculoArea.setEnabled(false);
        areaAtuacaoLabelComponent.setEnabled(false);
        areaAtuacaoField.setEnabled(false);

        // Painel de Botão
        salvarButton = createStyledButton("Salvar Participante");
        salvarButton.addActionListener(e -> salvarParticipante());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(COR_FUNDO_PAINEL);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(salvarButton);

        getContentPane().setBackground(new Color(220,220,220));
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(500, getSize().height));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método auxiliar modificado para aceitar uma instância de JLabel
    private void addFormField(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent component, int row) {
        label.setFont(FONTE_LABEL); // Aplica a fonte ao label passado
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        panel.add(label, gbc);

        if (component instanceof JTextField || component instanceof JFormattedTextField || component instanceof JComboBox) {
            component.setFont(FONTE_TEXTFIELD);
        } else if (component instanceof JScrollPane) { // Para o JTextArea dentro do JScrollPane
            JViewport viewport = ((JScrollPane) component).getViewport();
            if (viewport != null && viewport.getView() instanceof JTextArea) {
                ((JTextArea) viewport.getView()).setFont(FONTE_TEXTFIELD);
            }
        }
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(component, gbc);
    }

    private void salvarParticipante() {
        String nome = nomeField.getText().trim();
        String sexo = sexoField.getText().trim().toUpperCase();
        String email = emailField.getText().trim();
        String celularOriginal = (String) celularField.getValue();
        String celular = "";
        if (celularOriginal != null) {
             celular = celularOriginal.replaceAll("[^0-9]", "");
        }


        String ePalestrante = ePalestranteBox.isSelected() ? "S" : "N";
        String curriculo = "";
        String areaAtuacao = "";

        if (nome.isEmpty() || sexo.isEmpty() || email.isEmpty() || celular.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios: Nome, Sexo, E-mail, Celular.", "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!sexo.equals("F") && !sexo.equals("M")) {
            JOptionPane.showMessageDialog(this, "O campo 'Sexo' deve ser 'F' para feminino ou 'M' para masculino.", "Sexo Inválido", JOptionPane.ERROR_MESSAGE);
            sexoField.requestFocus();
            return;
        }
        
        if (celular.length() < 10 || celular.length() > 11) {
             JOptionPane.showMessageDialog(this, "Número de celular inválido ou incompleto.", "Celular Inválido", JOptionPane.ERROR_MESSAGE);
             celularField.requestFocus();
             return;
        }

        if (ePalestrante.equals("S")) {
            curriculo = curriculoArea.getText().trim();
            areaAtuacao = areaAtuacaoField.getText().trim();
            if (curriculo.isEmpty() || areaAtuacao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Para palestrantes, preencha os campos 'Currículo' e 'Área de Atuação'.", "Campos Palestrante", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Supondo que participanteService.inserir espera o celular sem formatação
        String resultado = participanteService.inserir(nome, sexo, email, celular, ePalestrante, curriculo, areaAtuacao);

        if ("sucesso".equalsIgnoreCase(resultado)) {
            JOptionPane.showMessageDialog(this, "Participante cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar participante:\n" + resultado, "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
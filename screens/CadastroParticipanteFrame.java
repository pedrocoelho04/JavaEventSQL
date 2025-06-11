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
    // Removido: private JTextField sexoField;
    private JRadioButton femininoRadioButton;
    private JRadioButton masculinoRadioButton;
    private ButtonGroup sexoButtonGroup;
    private JTextField emailField;
    private JFormattedTextField celularField;
    private JFormattedTextField cpfField;
    private JCheckBox ePalestranteBox;
    private JTextArea curriculoArea;
    private JTextField areaAtuacaoField;
    private JButton salvarButton;

    private JLabel curriculoLabelComponent;
    private JLabel areaAtuacaoLabelComponent;

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

        int linhaGBC = 0;

        // Nome
        addFormField(formPanel, gbc, new JLabel("Nome:"), nomeField = new JTextField(25), linhaGBC++);

        // Sexo (agora com Radio Buttons)
        JLabel sexoLabel = new JLabel("Sexo:");
        sexoLabel.setFont(FONTE_LABEL);
        
        femininoRadioButton = new JRadioButton("Feminino");
        femininoRadioButton.setFont(FONTE_LABEL);
        femininoRadioButton.setBackground(COR_FUNDO_PAINEL);
        
        masculinoRadioButton = new JRadioButton("Masculino");
        masculinoRadioButton.setFont(FONTE_LABEL);
        masculinoRadioButton.setBackground(COR_FUNDO_PAINEL);

        sexoButtonGroup = new ButtonGroup();
        sexoButtonGroup.add(femininoRadioButton);
        sexoButtonGroup.add(masculinoRadioButton);

        JPanel sexoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sexoPanel.setBackground(COR_FUNDO_PAINEL);
        sexoPanel.add(femininoRadioButton);
        sexoPanel.add(masculinoRadioButton);
        
        addFormField(formPanel, gbc, sexoLabel, sexoPanel, linhaGBC++);

        // E-mail
        addFormField(formPanel, gbc, new JLabel("E-mail:"), emailField = new JTextField(25), linhaGBC++);

        // CPF
        try {
            MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
            cpfFormatter.setPlaceholderCharacter('_');
            cpfField = new JFormattedTextField(cpfFormatter);
            cpfField.setColumns(15);
        } catch (ParseException e) {
            System.err.println("Erro ao criar máscara de CPF: " + e.getMessage());
            cpfField = new JFormattedTextField();
            cpfField.setToolTipText("Formato: 111.111.111-11");
        }
        addFormField(formPanel, gbc, new JLabel("CPF:"), cpfField, linhaGBC++);

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
        curriculoLabelComponent = new JLabel("Currículo:");
        curriculoArea = new JTextArea(5, 25);
        curriculoArea.setFont(FONTE_TEXTFIELD);
        curriculoArea.setLineWrap(true);
        curriculoArea.setWrapStyleWord(true);
        JScrollPane curriculoScroll = new JScrollPane(curriculoArea);
        curriculoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        addFormField(formPanel, gbc, curriculoLabelComponent, curriculoScroll, linhaGBC++);

        // Área de Atuação
        areaAtuacaoLabelComponent = new JLabel("Área de Atuação:");
        areaAtuacaoField = new JTextField(25);
        addFormField(formPanel, gbc, areaAtuacaoLabelComponent, areaAtuacaoField, linhaGBC++);

        // Ação do CheckBox
        ePalestranteBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isPalestrante = ePalestranteBox.isSelected();
                curriculoLabelComponent.setEnabled(isPalestrante);
                curriculoArea.setEnabled(isPalestrante);
                areaAtuacaoLabelComponent.setEnabled(isPalestrante);
                areaAtuacaoField.setEnabled(isPalestrante);
            }
        });

        // Desabilitar campos de palestrante inicialmente
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

        getContentPane().setBackground(new Color(220, 220, 220));
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(500, getSize().height));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent component, int row) {
        label.setFont(FONTE_LABEL);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        panel.add(label, gbc);

        if (component instanceof JTextField || component instanceof JFormattedTextField
                || component instanceof JComboBox) {
            component.setFont(FONTE_TEXTFIELD);
        } else if (component instanceof JScrollPane) {
            JViewport viewport = ((JScrollPane) component).getViewport();
            if (viewport != null && viewport.getView() instanceof JTextArea) {
                ((JTextArea) viewport.getView()).setFont(FONTE_TEXTFIELD);
            }
        }
        // Não aplica fonte a JPanel (para o sexoPanel)
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(component, gbc);
    }

    private void salvarParticipante() {
        // 1. Obter dados dos campos
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        
        // Obter sexo dos Radio Buttons
        String sexo = "";
        if (femininoRadioButton.isSelected()) {
            sexo = "F";
        } else if (masculinoRadioButton.isSelected()) {
            sexo = "M";
        }

        // 2. Obter e limpar os valores dos campos formatados
        String celularOriginal = (String) celularField.getValue();
        String celular = "";
        if (celularOriginal != null) {
            celular = celularOriginal.replaceAll("[^0-9]", "");
        }

        String cpfOriginal = (String) cpfField.getValue();
        String cpf = "";
        if (cpfOriginal != null) {
            cpf = cpfOriginal.replaceAll("[^0-9]", "");
        }

        // 3. Validação dos campos obrigatórios
        if (nome.isEmpty() || sexo.isEmpty() || email.isEmpty() || celular.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Campos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. Validação do CPF
        if (!isCPFValido(cpf)) {
            JOptionPane.showMessageDialog(this, "O CPF inserido é inválido! Verifique o número e tente novamente.",
                    "CPF Inválido", JOptionPane.ERROR_MESSAGE);
            cpfField.requestFocus();
            return;
        }

        // 5. Obter dados de palestrante
        String ePalestrante = ePalestranteBox.isSelected() ? "S" : "N";
        String curriculo = "";
        String areaAtuacao = "";

        if (ePalestrante.equals("S")) {
            curriculo = curriculoArea.getText().trim();
            areaAtuacao = areaAtuacaoField.getText().trim();
            if (curriculo.isEmpty() || areaAtuacao.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Para palestrantes, os campos 'Currículo' e 'Área de Atuação' são obrigatórios.",
                        "Campos de Palestrante", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // 6. Chamar o serviço para inserir
        String resultado = participanteService.inserir(nome, sexo, email, celular, cpf, ePalestrante, curriculo,
                areaAtuacao);

        // 7. Tratar o resultado
        if ("sucesso".equalsIgnoreCase(resultado)) {
            JOptionPane.showMessageDialog(this, "Participante cadastrado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao cadastrar o participante:\n" + resultado,
                    "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // O restante da classe (createStyledButton, isCPFValido) permanece o mesmo...

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

    public static boolean isCPFValido(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }
            if (primeiroDigito != Integer.parseInt(String.valueOf(cpf.charAt(9)))) {
                return false;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }
            if (segundoDigito != Integer.parseInt(String.valueOf(cpf.charAt(10)))) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
package screens;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import service.ParticipanteService;
import table.Participante;

public class ConsultaFrame extends JFrame {
  public ConsultaFrame() {
    setTitle("Consulta de Participantes");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    ParticipanteService ps = new ParticipanteService();
    List<Participante> participantes = ps.listarTodos();

    String[] colunas = { "ID", "Nome", "E-mail", "Celular" };
    DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);

    for (Participante p : participantes) {
      tableModel.addRow(new Object[] { p.getId(), p.getNome(), p.getEmail(), p.getCelular() });
    }

    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    add(scrollPane, BorderLayout.CENTER);
    setVisible(true);
  }
}
package screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import service.EventoService;
import table.Evento;

public class ConsultaEventosFrame extends JFrame {
  public ConsultaEventosFrame() {
    setTitle("Consulta de Eventos");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    EventoService es = new EventoService();
    List<Evento> eventos = es.listarTodos();

    String[] colunas = { "ID", "Nome", "Data", "Local" };
    DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);

    for (Evento e : eventos) {
      tableModel.addRow(new Object[] { e.getId(), e.getNome(), e.getData(), e.getLocal() });
    }

    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    add(scrollPane, BorderLayout.CENTER);
    setVisible(true);
  }
}
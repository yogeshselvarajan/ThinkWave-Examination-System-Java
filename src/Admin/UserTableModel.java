package Admin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel {
    private String[] columnNames = {"User ID", "Name"};
    private Object[][] data;

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public void setData(Object[][] data) {
        this.data = data;
        fireTableDataChanged();
    }
}


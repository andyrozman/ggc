package ggc.core.data;

/**
 * Created by andy on 28.01.17.
 */
public interface DataValuesInterface
{

    int getColumnCount();


    int getRowCount();


    Object getValueAt(int row, int column);


    String getColumnName(int column);


    Class<?> getColumnClass(int row);


    boolean isCellEditable(int row, int col);

}

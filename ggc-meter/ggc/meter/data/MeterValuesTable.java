/*
 * Created on 15.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.data;


import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class MeterValuesTable extends JTable //implements TableModelListener
{
	static final long serialVersionUID = 0;

//x    private I18nControl m_ic = I18nControl.getInstance();    
	MeterValuesTableModel model = null;

    public MeterValuesTable()
    {
        super();
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public MeterValuesTable(MeterValuesTableModel model)
    {
        super(model);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        //setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /**
     * @see javax.swing.JTable#createDefaultTableHeader()
     */
    @Override
    protected JTableHeader createDefaultTableHeader()
    {
        JTableHeader header = super.createDefaultTableHeader();

        //header.setPreferredSize(new Dimension(100, 20));
        header.setFont(header.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));

        return header;
    }

    public void setModel(MeterValuesTableModel model)
    {
        super.setModel(model);
        this.model = model;
        //createGlucoTable(model);

        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                
             /*   int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel)e.getSource();
                String columnName = model.getColumnName(column);
                Object data = model.getValueAt(row, column);                
               */ 
                /* FIX
                if (model.getRowCount() == 0 && deleteRowAction.isEnabled())
                    deleteRowAction.setEnabled(false);
                if (model.getRowCount() > 0 && !deleteRowAction.isEnabled())
                    deleteRowAction.setEnabled(true);
                    */
            }
        });
    }

    public static JComponent createMeterValuesTable(final MeterValuesTableModel model)
    {
        MeterValuesTable table = new MeterValuesTable(model);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final AddRowAction addRowAction = new AddRowAction(table);
        final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
        toolBar.add(addRowAction);
        toolBar.add(deleteRowAction);
        //UIUtilities.addToolBarButton(toolBar, addRowAction);
        //UIUtilities.addToolBarButton(toolBar, deleteRowAction);
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);

        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) 
            {
                column.setPreferredWidth(100); //third column is bigger
            } 
            else 
            {
                column.setPreferredWidth(50);
            }
        }        

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;
    }

    /*
    public static JComponent createMeterValuesTable(final MeterValuesTable table)
    {
        //MeterValuesTable table = new MeterValuesTable(model);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final AddRowAction addRowAction = new AddRowAction(table);
        final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
        toolBar.add(addRowAction);
        toolBar.add(deleteRowAction);
        //UIUtilities.addToolBarButton(toolBar, addRowAction);
        //UIUtilities.addToolBarButton(toolBar, deleteRowAction);
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);

        

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;
    }
    */
    
    
    public static class AddRowAction extends AbstractAction
    {
    	
    	static final long serialVersionUID = 0;
    	
    	MeterValuesTable table = null;

        public AddRowAction(MeterValuesTable table)
        {
            this.table = table;

            //putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/RowInsertAfter16.gif")));
            putValue(Action.SHORT_DESCRIPTION, I18nControl.getInstance().getMessage("ADD_NEW_ROW_TO_TABLE"));
            //putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
/*
  TO-DO
 
            Window window = (Window)table.getTopLevelAncestor();
            if (window == null)
                return;

            DailyValuesRow valueRow = null;
            if (window instanceof Dialog)
                valueRow = DailyRowDialog.getNewDailyValuesRow((Dialog)window);
            else if (window instanceof Frame)
                valueRow = DailyRowDialog.getNewDailyValuesRow((Frame)window);

            if (valueRow != null)
                ((GlucoTableModel)table.getModel()).getDayData().setNewRow(valueRow);
                */
        }

    }

    public static class DeleteRowAction extends AbstractAction
    {
    	static final long serialVersionUID = 0;
    	MeterValuesTable table = null;

        public DeleteRowAction(MeterValuesTable table)
        {
            this.table = table;

            //putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/Remove16.gif")));
            putValue(Action.SHORT_DESCRIPTION, I18nControl.getInstance().getMessage("DELETE_SELECTED_ROWS"));

            if (table.getModel().getRowCount() == 0)
                setEnabled(false);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
//            ((GlucoTableModel)table.getModel()).getDayData().deleteRow(table.getSelectedRow());
        }

    }

}

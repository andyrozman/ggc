package ggc.meter.data;


import ggc.meter.util.DataAccessMeter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class MeterValuesTable extends JTable //implements TableModelListener
{

//x    private I18nControl m_ic = I18nControl.getInstance();    
	MeterValuesTableModel model = null;
	DataAccessMeter m_da = DataAccessMeter.getInstance();

    public MeterValuesTable()
    {
        super();
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        this.setDefaultRenderer(Boolean.class, new CheckCellRenderer());
        this.setDefaultRenderer(Integer.class, new StatusCellRenderer());

        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        
    }

    public MeterValuesTable(MeterValuesTableModel model)
    {
        super(model);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        this.setDefaultRenderer(Boolean.class, new CheckCellRenderer());
        this.setDefaultRenderer(Integer.class, new StatusCellRenderer());
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

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
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
    
    

    
    
    class CheckCellRenderer extends JCheckBox implements TableCellRenderer
    {
        protected Border m_noFocusBorder;

        public CheckCellRenderer()
        {
            super();
            m_noFocusBorder = new EmptyBorder(1, 2, 1, 2);
            setOpaque(true);
            setBorder(m_noFocusBorder);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
        {
            if (value instanceof Boolean)
            {
                Boolean b = (Boolean) value;
                setSelected(b.booleanValue());
            }

            setBackground(isSelected && !hasFocus ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected && !hasFocus ? table.getSelectionForeground() : table.getForeground());

            setFont(table.getFont());
            setBorder(hasFocus ? UIManager.getBorder("Table.focusCellHighlightBorder") : m_noFocusBorder);

            return this;
        }
    }

    class StatusCellRenderer extends JLabel implements TableCellRenderer
    {
        private static final long serialVersionUID = -3848642009093946959L;
        protected Border m_noFocusBorder;

        public StatusCellRenderer()
        {
            super();
            m_noFocusBorder = new EmptyBorder(1, 2, 1, 2);
            setOpaque(true);
            setBorder(m_noFocusBorder);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
        {

            int status;
            if (value instanceof Integer)
            {
                Integer i = (Integer) value;
                // setSelected(b.booleanValue());
                status = i.intValue();
                setText(MeterValuesEntry.entry_statuses[status]);
                setIcon(m_da.getImageIcon(MeterValuesEntry.entry_status_icons[status], 8, 8, this));
            }

            setBackground(isSelected && !hasFocus ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected && !hasFocus ? table.getSelectionForeground() : table.getForeground());

            setFont(table.getFont());
            setBorder(hasFocus ? UIManager.getBorder("Table.focusCellHighlightBorder") : m_noFocusBorder);

            return this;
        }
    }
    
    
    
    
}

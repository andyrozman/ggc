/*
 * Created on 15.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.gui;


import ggc.data.DailyValuesRow;
import ggc.data.GlucoTableModel;
import ggc.util.I18nControl;
import ggc.util.UIUtilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class GlucoTable extends JTable
{

    private I18nControl m_ic = I18nControl.getInstance();    
    GlucoTableModel model = null;

    public GlucoTable()
    {
        super();
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public GlucoTable(GlucoTableModel model)
    {
        super(model);

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /**
     * @see javax.swing.JTable#createDefaultTableHeader()
     */
    protected JTableHeader createDefaultTableHeader()
    {
        JTableHeader header = super.createDefaultTableHeader();

        //header.setPreferredSize(new Dimension(100, 20));
        header.setFont(header.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));

        return header;
    }

    public void setModel(GlucoTableModel model)
    {
        super.setModel(model);
        this.model = model;
        //createGlucoTable(model);

        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                /* FIX
                if (model.getRowCount() == 0 && deleteRowAction.isEnabled())
                    deleteRowAction.setEnabled(false);
                if (model.getRowCount() > 0 && !deleteRowAction.isEnabled())
                    deleteRowAction.setEnabled(true);
                    */
            }
        });
    }

    public static JComponent createGlucoTable(final GlucoTableModel model)
    {
        GlucoTable table = new GlucoTable(model);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final AddRowAction addRowAction = new AddRowAction(table);
        final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
        UIUtilities.addToolBarButton(toolBar, addRowAction);
        UIUtilities.addToolBarButton(toolBar, deleteRowAction);
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);

        

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;
    }

    public static class AddRowAction extends AbstractAction
    {
        GlucoTable table = null;

        public AddRowAction(GlucoTable table)
        {
            this.table = table;

            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/RowInsertAfter16.gif")));
            putValue(Action.SHORT_DESCRIPTION, I18nControl.getInstance().getMessage("ADD_NEW_ROW_TO_TABLE"));
            //putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            Window window = (Window)table.getTopLevelAncestor();
            if (window == null)
                return;

            DailyValuesRow valueRow = null;
            if (window instanceof Dialog)
                valueRow = DailyValuesRowDialog.getNewDailyValuesRow((Dialog)window);
            else if (window instanceof Frame)
                valueRow = DailyValuesRowDialog.getNewDailyValuesRow((Frame)window);

            if (valueRow != null)
                ((GlucoTableModel)table.getModel()).getDayData().setNewRow(valueRow);
        }

    }

    public static class DeleteRowAction extends AbstractAction
    {
        GlucoTable table = null;

        public DeleteRowAction(GlucoTable table)
        {
            this.table = table;

            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/Remove16.gif")));
            putValue(Action.SHORT_DESCRIPTION, I18nControl.getInstance().getMessage("DELETE_SELECTED_ROWS"));

            if (table.getModel().getRowCount() == 0)
                setEnabled(false);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            ((GlucoTableModel)table.getModel()).getDayData().deleteRow(table.getSelectedRow());
        }

    }

}

/*
 * Created on 15.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

import datamodels.DailyValuesRow;
import datamodels.GlucoTableModel;

import util.UIUtilities;

/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class GlucoTable extends JTable {

	public GlucoTable(GlucoTableModel model) {
		super(model);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	/**
	 * @see javax.swing.JTable#createDefaultTableHeader()
	 */
	protected JTableHeader createDefaultTableHeader() {
		JTableHeader header = super.createDefaultTableHeader();

		//header.setPreferredSize(new Dimension(100, 20));
		header.setFont(header.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));

		return header;
	}

	public static JComponent createGlucoTable(final GlucoTableModel model) {
		GlucoTable table = new GlucoTable(model);
		JScrollPane scroller =
			new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		final AddRowAction addRowAction = new AddRowAction(table);
		final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		toolBar.setFloatable(false);
		UIUtilities.addToolBarButton(toolBar, addRowAction);
		UIUtilities.addToolBarButton(toolBar, deleteRowAction);
		//toolBar.add(addRowAction);
		//toolBar.add(deleteRowAction);
		
		model.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (model.getRowCount() == 0 && deleteRowAction.isEnabled())
					deleteRowAction.setEnabled(false);
				if (model.getRowCount() > 0 && !deleteRowAction.isEnabled())
					deleteRowAction.setEnabled(true);
			}
		});

		JPanel container = new JPanel(new BorderLayout());
		container.add(toolBar, "North");
		container.add(scroller, "Center");

		return container;
	}

	public static class AddRowAction extends AbstractAction {
		GlucoTable table = null;
		public AddRowAction(GlucoTable table) {
			this.table = table;

			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/RowInsertAfter16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Add's a new row of values to the table.");
			//putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
			putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
		}

		/**
		 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			Window window = (Window) table.getTopLevelAncestor();
			if (window == null)
				return;
			
			DailyValuesRow valueRow	= null;
			if (window instanceof Dialog)
				valueRow = DailyValuesRowDialog.getNewDailyValuesRow((Dialog) window);
			else if (window instanceof Frame)
				valueRow = DailyValuesRowDialog.getNewDailyValuesRow((Frame) window);
				
			if (valueRow != null)
				((GlucoTableModel) table.getModel()).getDayData().setNewRow(valueRow);
		}

	}

	public static class DeleteRowAction extends AbstractAction {
		GlucoTable table = null;
		public DeleteRowAction(GlucoTable table) {
			this.table = table;

			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/icons/Remove16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Delete selected rows.");
			
			if (table.getModel().getRowCount() == 0)
				setEnabled(false);
		}

		/**
		 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			((GlucoTableModel) table.getModel()).getDayData().deleteRow(table.getSelectedRow());
		}

	}

}

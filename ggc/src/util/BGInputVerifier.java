/*
 * Created on 16.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */
package util;

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class BGInputVerifier extends InputVerifier {

	private Border border = null;
	private boolean nonNull;
	
	public BGInputVerifier(boolean nonNull) {
		super();
		
		this.nonNull = nonNull;
	}

	/**
	 * @see javax.swing.InputVerifier#verify(JComponent)
	 */
	public boolean verify(JComponent input) {
		if (border == null)
			border = input.getBorder();

		try {
			String bgStr = ((JTextField) input).getText();
			
			if (!nonNull) {
				if (bgStr == null || bgStr.equals(""))
					return true;
			}
			
			float bg = Float.parseFloat(bgStr);
		} catch (NumberFormatException e) {
			input.setBorder(new LineBorder(Color.red));
			return false;
		}
		
		input.setBorder(border);
		return true;
	}

}

/*
 * Created on 15.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.event;


import java.util.EventListener;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public interface GlucoValueEventListener extends EventListener
{

    void glucoValuesChanged(GlucoValueEvent event);

}

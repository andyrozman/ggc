/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: VersionChecker.java
 *  Purpose:  Checks for new versions of the software.
 *
 *  Author:   schultd
 */

package ggc.util;

// import ggc.GGC;
import ggc.gui.MainFrame;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class VersionChecker
{

    private I18nControl m_ic = I18nControl.getInstance();

    private BufferedReader in = null;

    public static void main(String[] args)
    {
        VersionChecker vC = new VersionChecker();
        vC.checkForUpdate();
    }

    public boolean checkForUpdate()
    {

        String installedVersion = MainFrame.s_version;
        String availableVersion = new String();
        HttpURLConnection conn = null;

        try
        {
            URL url = new URL("http://ggc.sourceforge.net/LATEST_VERSION.txt");
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                in = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                availableVersion = nextLine();
                if (availableVersion == null)
                    conn.disconnect();

                String line = new String();
                StringBuffer sb = new StringBuffer();

                while ((line = nextLine()) != null)
                    sb.append(line + "\n");

                String message = null;

                if (isNewVersion(installedVersion, availableVersion))
                    message = m_ic.getMessage("YOUR_VERSION") + ":\t\t"
                            + installedVersion + "\n"
                            + m_ic.getMessage("AVAILABLE_VERSION") + ":\t"
                            + availableVersion + "\n\n" + sb.toString();
                else
                    message = m_ic.getMessage("NO_NEW_VERSION");

                JOptionPane.showMessageDialog(null, message, m_ic
                        .getMessage("CHECK_FOR_NEW_VERSION"),
                        JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (UnknownHostException e)
        {
            JOptionPane.showMessageDialog(null, m_ic
                    .getMessage("HOST_UNAVAILABLE_MSG"), m_ic
                    .getMessage("HOST_UNAVAILABLE_INFO"),
                    JOptionPane.ERROR_MESSAGE);
        } catch (java.io.IOException e)
        {
            System.err.println(e);
        } finally
        {
            if (conn != null)
            conn.disconnect();
        }
        return true;

    }

    private String nextLine()
    {
        if (in == null)
            return null;

        String tmp = null;

        try
        {
            tmp = in.readLine();
        } catch (IOException e)
        {
            System.out.println(e);
        }

        return tmp;
    }

    /**
     * Checks if the &quot;new&quot; version <code>newVers</code> is newer than
     * the &quot;old&quot; version <code>oldVers</code>.
     * @param oldVers
     * @param newVers
     * @return <code>true</code>, if newVers is newer than oldVers,
     *         <code>false</code> if it isn't.
     */
    private boolean isNewVersion(String oldVers, String newVers)
    {
        int oldMajor = 0;
        int oldMinor = 0;
        int oldBogus = 0;

        int newMajor = 0;
        int newMinor = 0;
        int newBogus = 0;

        StringTokenizer tok = new StringTokenizer(oldVers, ".");
        if (tok.hasMoreTokens())
            oldMajor = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens())
            oldMinor = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens())
            oldBogus = Integer.parseInt(tok.nextToken());

        tok = new StringTokenizer(newVers, ".");
        if (tok.hasMoreTokens())
            newMajor = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens())
            newMinor = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens())
            newBogus = Integer.parseInt(tok.nextToken());

        if (oldMajor < newMajor)
            return true;

        if (oldMinor < newMinor)
            return true;

        if (oldBogus < newBogus)
            return true;

        return false;
    }
}

package ggc.cgms.manager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     CGMSDevicesIds
 *  Description:  This class contains all ids from this plugins. This means companies and CGMS devices,
 *                all classes should use Ids from here, so that Ids can change globally. 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDevicesIds
{

    /**
     * CGMS Company: Abbott
     */
    public static final int COMPANY_ABBOTT = 1;

    /**
     * CGMS Company: Dexcom
     */
    public static final int COMPANY_DEXCOM = 2;

    /**
     * CGMS Company: Minimed
     */
    public static final int COMPANY_MINIMED = 3;

    /**
     * CGMS Company: Animas (integration only)
     */
    public static final int COMPANY_ANIMAS = 4;

    public static final int COMPANY_TANDEM = 5;

    /**
     * CGMS (Abbott) - Freestyle Navigator
     */
    public static final int CGMS_FREESTYLE_NAVIGATOR1 = 10001;

    public static final int CGMS_FREESTYLE_NAVIGATOR2 = 10002;

    public static final int CGMS_FREESTYLE_NEO = 10003;

    /**
     * CGMS (Dexcom) - Dexcom 7
     */
    public static final int CGMS_DEXCOM_7 = 20001;

    /**
     * CGMS (Dexcom) - Dexcom 7 Plus
     */
    public static final int CGMS_DEXCOM_7_PLUS = 20002;

    /**
     * CGMS (Dexcom) - Dexcom G4 / G4 Platinum
     */
    public static final int CGMS_DEXCOM_G4 = 20003;

    public static final int CGMS_DEXCOM_G5 = 20004;

    /**
     * CGMS (Minimed) - Guardian
     */
    public static final int CGMS_MINIMED_GOLD = 30001;

    /**
     * CGMS (Minimed) - Guardian RealTime
     */
    public static final int CGMS_GUARDIAN_REALTIME = 30002;

    /**
     * CGMS (Minimed) - Minimed RealTime
     */
    // public static final int CGMS_MINIMED_REALTIME = 30003;

    public static final int CGMS_MINIMED_522 = 30003;

    public static final int CGMS_MINIMED_523 = 30004;

    public static final int CGMS_MINIMED_553 = 30005;

    public static final int CGMS_MINIMED_554 = 30006;

    public static final int CGMS_MINIMED_640G = 30007;

    /**
     * CGMS (Animas) - Animas Vibe (Dexcom 4 integration)
     */
    public static final int CGMS_ANIMAS_VIBE_DEXCOM_INTEGRATION = 40001;

    /**
     * CGMS (Tandem) - Tandem G4 (Dexcom 4 integration)
     */
    public static final int CGMS_TANDEM_TSLIM_G4_DEXCOM_INTEGRATION = 50001;

}

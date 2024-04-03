package ggc.meter.manager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterDevicesIds  
 *  Description:  This class contains all ids from this plugins. This means companies and meter devices,
 *                all classes should use Ids from here, so that Ids can change globally.
 *                NOT USED ANYMORE (SHOULD BE REMOVED WHEN ALL METERS ARE AT MeterInterface V2), JUST FOR NUMBERS
 *                STORING. See MeterDeviceDefintion instead.
 * 
 *  Author: Andy {andy@atech-software.com}
 */
@Deprecated
public class MeterDevicesIds
{

    /**
     * Meter Company: Ascensia (Bayer)
     */
    public static final int COMPANY_ASCENSIA = 1;

    /**
     * Meter Company: Roche (Accu-Chek)
     */
    public static final int COMPANY_ROCHE = 2;

    /**
     * Meter Company: LifeScan (actually is J&J)
     */
    public static final int COMPANY_LIFESCAN = 3;

    /**
     * Meter Company: Abbott
     */
    public static final int COMPANY_ABBOTT = 4;

    /**
     * Meter Company: Menarini
     */
    public static final int COMPANY_MENARINI = 5;
    public static final int COMPANY_ARKRAY = 6;
    public static final int COMPANY_HIPOGUARD = 7;
    public static final int COMPANY_HOME_DIAGNOSTICS = 8;
    public static final int COMPANY_PRODIGY = 9;
    public static final int COMPANY_SANVITA = 10;
    public static final int COMPANY_US_DIAGNOSTICS = 11;
    public static final int COMPANY_WAVESENSE = 12;
    public static final int COMPANY_DIABETIC_SUPPLY_OF_SUNCOAST = 13;

    /**
     *  Ascensia/Bayer
     */
    public static final int METER_ASCENSIA_ELITE_XL = 10001;
    public static final int METER_ASCENSIA_DEX = 10002;
    public static final int METER_ASCENSIA_BREEZE = 10003;
    public static final int METER_ASCENSIA_CONTOUR = 10004;
    public static final int METER_ASCENSIA_BREEZE2 = 10005;

    // V2
    public static final int METER_ASCENSIA_CONTOUR_LINK = 10006;
    public static final int METER_ASCENSIA_USB = 10007;
    public static final int METER_ASCENSIA_NEXT = 10008;
    public static final int METER_ASCENSIA_NEXT_USB = 10009;
    public static final int METER_ASCENSIA_NEXT_LINK = 10010;

    /**
     * Meter Device: SmartPix
     */
    public static final int METER_ROCHE_SMARTPIX_DEVICE = 20001;
    public static final int METER_ACCUCHEK_ACTIVE = 20002;
    public static final int METER_ACCUCHEK_ADVANTAGE = 20003;
    public static final int METER_ACCUCHEK_AVIVA = 20004;
    public static final int METER_ACCUCHEK_COMFORT = 20005;
    public static final int METER_ACCUCHEK_COMPACT = 20006;
    public static final int METER_ACCUCHEK_COMPACT_PLUS = 20007;
    public static final int METER_ACCUCHEK_GO = 20008;
    public static final int METER_ACCUCHEK_INTEGRA = 20009;
    public static final int METER_ACCUCHEK_PERFORMA = 20010;
    public static final int METER_ACCUCHEK_SENSOR = 20011;
    public static final int METER_ACCUCHEK_NANO = 20012;
    public static final int METER_ACCUCHEK_AVIVA_COMBO = 20013;

    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRA = 30001;
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRA_2 = 30002; // NP
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRASMART = 30003; // NI
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRALINK = 30004; // NI NP
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_SELECT = 30005; // NI NP
    /**
     * 
     */
    public static final int METER_LIFESCAN_INDUO = 30006; // NI
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_BASIC = 30007; // NI
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_SURESTEP = 30008; // NI
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_FASTTAKE = 30009; // NI
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_PROFILE = 30010;
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_II = 30011; // NI

    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRA_MINI = 30012; // NI
                                                                         // NP
    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRA_EASY = 30013; // NI
                                                                         // NP

    /**
     * 
     */
    public static final int METER_LIFESCAN_ONE_TOUCH_ULTRA_SMART = 30014; // NI

    /**
     * Abbott Devices (FreeStyle and Medisense) 
     */

    /**
     * * */
    public static final int METER_ABBOTT_FREESTYLE = 40001; // Not tested yet
    public static final int METER_ABBOTT_FREESTYLE_LITE = 40002; // NS
    public static final int METER_ABBOTT_FREESTYLE_FREEDOM = 40003; // NS
    public static final int METER_ABBOTT_FREESTYLE_FREEDOM_LITE = 40004; // NS
    public static final int METER_ABBOTT_FREESTYLE_FLASH = 40005; // NS
    public static final int METER_ABBOTT_PRECISION_XTRA = 40006;
    public static final int METER_ABBOTT_OPTIUM_XCEED = 40007;

    /**
     * Menarini Devices
     */
    public static final int METER_MENARINI_GLUCOFIX_MIO = 50001;
    public static final int METER_MENARINI_GLUCOFIX_MIO_PLUS = 50002;
    public static final int METER_MENARINI_GLUCOFIX_ID = 50003;
    public static final int METER_MENARINI_GLUCOFIX_PREMIUM = 50004;
    public static final int METER_MENARINI_GLUCOFIX_TECH = 50005;
    public static final int METER_MENARINI_GLUCOMEN_VISIO = 50006;
    public static final int METER_MENARINI_GLUCOMEN_GM = 50007;
    public static final int METER_MENARINI_GLUCOMEN_LX_PLUS = 50008;
    public static final int METER_MENARINI_GLUCOMEN_LX_2 = 50009;
    public static final int METER_MENARINI_GLUCOMEN_MENDOR = 50010;
    public static final int METER_MENARINI_GLUCOMEN_READY = 50011;
    public static final int METER_MENARINI_GLUCOMEN_AREO = 50012;
    public static final int METER_MENARINI_GLUCOMEN_AREO_2K = 50013;

    // COMPANY_ARKRAY

    // MenariniGlucoCardSM(), // 500
    // MenariniGlucoCardMX(), // 500
    //
    // ArkrayGlucoCardPlus(), // 450

    /**
     * Arkray Devices
     */

}

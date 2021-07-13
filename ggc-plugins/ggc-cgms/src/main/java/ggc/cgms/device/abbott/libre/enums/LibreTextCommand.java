package ggc.cgms.device.abbott.libre.enums;

import ggc.plugin.device.impl.abbott.hid.AbbottHidTextCommand;

/**
 * Created by andy on 07/09/17.
 */
public enum LibreTextCommand implements AbbottHidTextCommand
{

    SerialNumber("$sn?", "Serial Number"), //
    SoftwareVersion("$swver?", "Software Version"), //
    Date("$date?", "Date"), //
    Time("$time?", "Time"), //
    History("$history?", "History"), //
    OtherHistory("$arresult?", ""), //

    ComputerDateTime("", null), //

    NtSound("$ntsound?", "Sound #1"), //
    BtSound("$btsound?", "Sound #2"), //

    // NOT SUPPORTED FULLY
    // Language("$lang?"), //
    // AllLanguages("$alllang?"), //

    Test("$frststrt?", "Test"), //

    // NOT SUPPORTED ON LIBRE
    // GlucoseUnits("$gunits?"), // TODO no0t sure if this is correct
    // FoodUnits("$foodunits?"), // TODO
    // ClockType("$clktyp?"), //
    // BgTargets("$bgtrgt?"), //
    // PatientName("$ptname?"), // ?
    // PatientId("$ptid?"), // ?
    // DatabaseRecordNumber("$dbrnum?"), //
    ;

    // DONE:
    // $serlnum?, $swver?, $date?, $time?,
    //
    // TO BE IMPLEMENTED
    // $btsound? $ntsound? $lang? $alllang?
    //
    // NOT SUPPORTED:
    // $gunits?, $foodunits? $clktyp? $bgtrgt? $taglang? $bgdrop? $tagorder? $cttype? $custthm?
    // $gettags,2,2 $corsetup? $insdose? $svgsdef? $svgsratio? $carbratio? $wktrend? $actthm?
    // $rmdstrorder? $carbratio? $result? $inslock? "$ptname? $ptid? $actinscal? $bgtgrng?
    // $actinscal? $iobstatus? $tagsenbl? $tagsenbl? $getrmndrst,0 $getrmndr,0 $inslog?
    // $inscalsetup? $inscalsetup? $frststrt? $mlcalget,3

    private String commandText;
    private String description;


    LibreTextCommand(String commandText, String description)
    {
        this.commandText = commandText;
        this.description = description;
    }


    public String getCommandText()
    {
        return this.commandText;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return this.name();
    }
}

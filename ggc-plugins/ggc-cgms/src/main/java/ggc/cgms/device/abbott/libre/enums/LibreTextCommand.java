package ggc.cgms.device.abbott.libre.enums;

/**
 * Created by andy on 07/09/17.
 */
public enum LibreTextCommand
{

    SerialNumber("$sn?"), //
    SoftwareVersion("$swver?"), //
    Date("$date?"), //
    Time("$time?"), //
    History("$history?"), //
    OtherHistory("$arresult?"), //

    ComputerDateTime(""), //

    NtSound("$ntsound?"), //
    BtSound("$btsound?"), //

    // NOT SUPPORTED FULLY
    // Language("$lang?"), //
    // AllLanguages("$alllang?"), //

    Test("$frststrt?"), //

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


    LibreTextCommand(String commandText)
    {
        this.commandText = commandText;
    }


    public String getCommandText()
    {
        return this.commandText;
    }
}

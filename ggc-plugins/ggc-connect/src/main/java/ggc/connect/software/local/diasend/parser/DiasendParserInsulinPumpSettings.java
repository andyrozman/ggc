package ggc.connect.software.local.diasend.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.IntRange;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.connect.data.ConnectConfigEntry;
import ggc.connect.data.ConnectPumpSettingsDTO;
import ggc.connect.enums.ConnectOperationType;
import ggc.connect.util.DataAccessConnect;
import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.device.impl.animas.enums.advsett.BolusSpeed;
import ggc.plugin.device.impl.animas.enums.advsett.BolusStepSize;
import ggc.plugin.device.impl.animas.enums.advsett.OcclusionSensitivity;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.data.defs.RatioType;
import ggc.pump.data.dto.BasalPatternEntryDTO;
import ggc.pump.data.dto.RatioDTO;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 14/09/17.
 */
public class DiasendParserInsulinPumpSettings extends DiasendParserAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(DiasendParserInsulinPumpSettings.class);

    private static final String PROGRAM = "Program: ";
    private static final String ENABLED = "Enabled";
    private static final String HEADER_RECORD = "Insulin pump settings for serial number:";

    ConnectPumpSettingsDTO pumpSettings;
    SettingMode mode;
    String modeString;

    DataAccessPlugInBase dataAccess;


    public DiasendParserInsulinPumpSettings()
    {
        DataAccessPump instance = DataAccessPump.getInstance();

        dataAccess = instance == null ? DataAccessConnect.getInstance() : instance;
        RatioDTO.i18nControl = dataAccess.getI18nControlInstance();
    }


    public boolean isOperationSupported(ConnectOperationType connectOperationType)
    {
        return (connectOperationType == ConnectOperationType.ViewConfiguration);
    }


    public boolean isSupported()
    {
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseExcelSheetViewConfig(Sheet sheet)
    {
        List<IntRange> configurationRanges = new ArrayList<IntRange>();

        int first = 0;

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);

            if (row == null)
                continue;

            Cell cell = row.getCell(row.getFirstCellNum());

            if (cell.getCellTypeEnum() != CellType.STRING)
                continue;

            if (HEADER_RECORD.equals(cell.getStringCellValue()))
            {
                configurationRanges.add(new IntRange(first, i - 1));
                first = i;
            }
        }

        configurationRanges.add(new IntRange(first, sheet.getLastRowNum()));

        mode = SettingMode.General;

        for (IntRange configRange : configurationRanges)
        {
            mode = SettingMode.None;

            pumpSettings = new ConnectPumpSettingsDTO(dataAccess);

            for (int i = configRange.getMinimumInteger(); i < configRange.getMaximumInteger() + 1; i++)
            {
                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                int lastCellNr = row.getLastCellNum();

                if (lastCellNr == 1)
                {
                    mode = getMode(row);
                }

                if (mode == SettingMode.BasalProfiles)
                {
                    int result = readBasalProfiles(i + 1, configRange.getMaximumInteger(), sheet);

                    if (result > 0)
                    {
                        i += result;
                    }
                    else
                    {
                        break;
                    }
                }
                else if ((mode == SettingMode.IC_Ratio) || //
                        (mode == SettingMode.ISF_Ratio) || //
                        (mode == SettingMode.BG_Target))
                {
                    int result = readRatio3Params(i + 1, configRange.getMaximumInteger(), sheet);

                    if (result > 0)
                    {
                        i += result;
                    }
                    else
                    {
                        break;
                    }
                }
                else
                {
                    if (lastCellNr == 2)
                    {
                        decodeSetting2(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue());
                    }
                }

            } // for

            pumpSettings.writeAllSettingsToGGC(outputWriter);
        }
    }


    private Integer readBasalProfiles(int startRow, int lastRow, Sheet sheet)
    {
        boolean running = true;

        int rowNum = startRow;

        // System.out.println("Read Basal Profiles: ");
        int program = 0;

        do
        {
            if (isLastRow(rowNum, lastRow))
            {
                return -1;
            }

            Row row = sheet.getRow(rowNum);

            if (row == null)
            {
                rowNum++;
                continue;
            }

            Cell firstCell = row.getCell(0);

            if (isNewMode(row))
            {
                return (rowNum - startRow) - 1;
            }
            else
            {
                if (firstCell.getCellTypeEnum() == CellType.STRING)
                {
                    String cellText = firstCell.getStringCellValue();

                    if (cellText.contains("Program"))
                    {
                        String programText = cellText.substring(PROGRAM.length());

                        program = Integer.valueOf(programText);
                    }
                }
                else if (firstCell.getCellTypeEnum() == CellType.NUMERIC)
                {
                    // int index = (new Double(firstCell.getNumericCellValue())).intValue();

                    String time = row.getCell(1).getStringCellValue();
                    float value = (new Double(row.getCell(2).getNumericCellValue())).floatValue();

                    // LOG.debug("program={}, index={}, time={}, value={}", program, index, time,
                    // value);

                    pumpSettings.addBasalPatternEntry(program, //
                        new BasalPatternEntryDTO(getATechDate(time), value));
                }

            }

            rowNum++;

        } while (running);

        return (rowNum - startRow);
    }


    private ATechDate getATechDate(String value)
    {
        String val[] = value.split(":");

        return new ATechDate(0, 0, 0, //
                Integer.valueOf(val[0]), Integer.valueOf(val[1]), Integer.valueOf(val[2]), //
                ATechDateType.TimeOnlySec);
    }


    private Integer readRatio3Params(int startRow, int lastRow, Sheet sheet)
    {
        boolean running = true;

        int rowNum = startRow;

        // System.out.println("readRatio3Params: " + mode);

        do
        {
            if (isLastRow(rowNum, lastRow))
            {
                return -1;
            }

            Row row = sheet.getRow(rowNum);

            if (row == null)
            {
                rowNum++;
                continue;
            }

            Cell firstCell = row.getCell(0);

            if (isNewMode(row))
            {
                return (rowNum - startRow) - 1;
            }
            else
            {
                if (firstCell.getCellTypeEnum() == CellType.STRING)
                {
                    // String units = row.getCell(2).getStringCellValue();
                    // LOG.debug("mode: {}, units={}", mode, units);
                }
                else if (firstCell.getCellTypeEnum() == CellType.NUMERIC)
                {
                    int index = (new Double(firstCell.getNumericCellValue())).intValue();

                    String time = row.getCell(1).getStringCellValue();
                    float value = (new Double(row.getCell(2).getNumericCellValue())).floatValue();

                    if (mode.getRatioType() == RatioType.BGTargetDelta)
                    {
                        String deltaString = row.getCell(3).getStringCellValue();

                        deltaString = deltaString.substring("+/- ".length());

                        float delta = Float.valueOf(deltaString);

                        pumpSettings.addSettingTimeValueEntry( //
                            new RatioDTO(mode.getRatioType(), index, getATechDate(time), value, delta));
                    }
                    else
                    {
                        pumpSettings.addSettingTimeValueEntry( //
                            new RatioDTO(mode.getRatioType(), index, getATechDate(time), value));

                        // LOG.debug("mode={}, program={}, index={}, time={}, value={}", mode,
                        // program, index, time,
                        // value);
                    }
                }
            }

            rowNum++;

        } while (running);

        return (rowNum - startRow);
    }


    private boolean isLastRow(int rowNum, int lastRow)
    {
        return rowNum == lastRow;
    }


    private boolean isNewMode(Row row)
    {
        Cell firstCell = row.getCell(0);

        int lastCellNr = row.getLastCellNum();

        if (lastCellNr == 1)
        {
            String stringCellValue = firstCell.getStringCellValue();

            if ((stringCellValue.contains("Program")) || (stringCellValue.contains("Sum")))
            {
                return false;
            }

            SettingMode modeByKey = SettingMode.getModeByKey(firstCell.getStringCellValue());

            return (modeByKey != mode);

        }
        else
            return false;
    }


    private SettingMode getMode(Row row)
    {
        Cell firstCell = row.getCell(0);

        int lastCellNr = row.getLastCellNum();

        if (lastCellNr == 1)
        {
            SettingMode modeByKey = SettingMode.getModeByKey(firstCell.getStringCellValue());

            return modeByKey;
        }
        else
            return null;
    }


    private void decodeSetting2(String key, String value)
    {
        if (HEADER_RECORD.equals(key))
        {
            String serialNo = value.substring(0, value.indexOf(" ("));
            String model = value.substring(value.indexOf(" (") + 2, value.indexOf(")"));

            pumpSettings.serialNumber = serialNo;
            pumpSettings.model = model;

            pumpSettings.setSourceItem("Insulin Pump Settings");
            pumpSettings.setSourceSubItem(model);
        }
        else if ("Audio Bolus Enable".equals(key))
        {
            pumpSettings.audioBolusEnabled = isEnabled(value);
        }
        else if ("Audio Bolus Stepsize per program keypress".equals(key))
        {
            pumpSettings.audioBolusStepSize = BolusStepSize.getByDescription(value);
        }
        else if ("Advanced Bolus Options enable".equals(key))
        {
            pumpSettings.advancedBolusEnabled = isEnabled(value);
        }
        else if ("Bolus Reminder Options enable".equals(key))
        {
            pumpSettings.bolusReminderEnabled = isEnabled(value);
        }
        else if ("Bolus Delivery Speed".equals(key))
        {
            pumpSettings.bolusSpeed = BolusSpeed.valueOf(value);
        }
        else if ("Max Bolus".equals(key))
        {
            pumpSettings.maxBolusProHour = getValueWithoutUnit(value, "U");
        }
        else if ("Max Basal".equals(key))
        {
            pumpSettings.maxBasalAmountProHour = getValueWithoutUnit(value, "U/h");
        }
        else if ("Max Total Daily Dose".equals(key))
        {
            pumpSettings.totalDailyDose = getValueWithoutUnit(value, "U");
        }
        else if ("Active basal program".equals(key))
        {
            pumpSettings.activeBasalPattern = Integer.valueOf(value);
        }
        else if ("Language Selection Index".equals(key))
        {
            pumpSettings.languageString = value;
        }
        else if ("Last Keypress to display timeout".equals(key))
        {
            pumpSettings.displayTimeout = getValueWithoutUnit(value, "s").shortValueExact();
        }
        else if ("Auto-Off Enable".equals(key))
        {
            pumpSettings.autoOffEnabled = isEnabled(value);
        }
        else if ("Auto-Off Timeout".equals(key))
        {
            pumpSettings.autoOffTimeoutHr = getValueWithoutUnit(value, "h").shortValueExact();
        }
        else if ("Max 2-Hr limit".equals(key))
        {
            pumpSettings.maxDoseIn2h = getValueWithoutUnit(value, "U");
        }
        else if ("Occlusion Sensitivity Level".equals(key))
        {
            pumpSettings.occlusionSensitivity = OcclusionSensitivity.valueOf(value);
        }
        else if ("Insulin-On-Board".equals(key))
        {
            pumpSettings.iOBEnabled = isEnabled(value);
        }
        else if ("Insulin-On-Board Duration".equals(key))
        {
            pumpSettings.iOBDecay = getValueWithoutUnit(value, "h");
        }
        else if ("Sick days, BG over limit".equals(key))
        {
            String unit = value.contains("mmol/L") ? "mmol/L" : "mg/dl";
            pumpSettings.sickDaysCheckOverLimit = getValueWithoutUnit(value, unit);
        }
        else if ("Sick days, check ketones".equals(key))
        {
            pumpSettings.sickDaysCheckKetones = getValueWithoutUnit(value, "h").shortValueExact();
        }
        else if ("Sick days, check BG".equals(key))
        {
            pumpSettings.sickDaysCheckBG = getValueWithoutUnit(value, "h").shortValueExact();
        }
        else if ("Low Cartridge Warning Level".equals(key))
        {
            pumpSettings.lowCartridgeWarning = getValueWithoutUnit(value, "U").shortValueExact();
        }
        else if ("Time format".equals(key))
        {
            pumpSettings.clockMode = ClockModeType.getByDescription(value);
        }
        else if ("BG unit".equals(key))
        {
            pumpSettings.glucoseUnitType = GlucoseUnitType.getByDescription(value);
        }
        else
        {
            ConnectConfigEntry entry = new ConnectConfigEntry(key, value, mode.configurationGroup, null, null);

            if (mode == SettingMode.None)
            {
                entry.setGroupName(modeString);
            }

            pumpSettings.addConfigEntry(entry);

            LOG.warn("Unknown Setting: key={}, value={}", key, value);
        }

    }


    private BigDecimal getValueWithoutUnit(String value, String unit)
    {
        String unitFull = " " + unit;

        try
        {
            String valueX = value.substring(0, value.indexOf(" " + unit));
            return new BigDecimal(valueX);
        }
        catch (Exception ex)
        {
            LOG.error("Error on parsing: value={}, expectedUnit={}", value, unitFull);
            return BigDecimal.ZERO;
        }
    }


    private Boolean isEnabled(String value)
    {
        return ENABLED.equals(value.trim());
    }

    enum SettingMode
    {
        None("X", PumpConfigurationGroup.UnknownGroup), //
        Bolus("Bolus", PumpConfigurationGroup.Bolus), //
        General("General", PumpConfigurationGroup.General), //
        BasalProfiles("Basal profiles", PumpConfigurationGroup.Basal), //
        Basal("Basal", PumpConfigurationGroup.Basal), //
        IC_Ratio("I:C ratio settings", PumpConfigurationGroup.Bolus, RatioType.InsulinCHRatio), //
        ISF_Ratio("ISF programs", PumpConfigurationGroup.Bolus, RatioType.InsulinBGRatio), //
        BG_Target("BG target range settings", PumpConfigurationGroup.Bolus, RatioType.BGTargetDelta), //
        ; //

        String key;
        private PumpConfigurationGroup configurationGroup;
        RatioType ratioType;

        static Map<String, SettingMode> mapKeyMode = null;

        static
        {
            mapKeyMode = new HashMap<String, SettingMode>();

            for (SettingMode mode : values())
            {
                mapKeyMode.put(mode.key, mode);
            }
        }


        SettingMode(String key, PumpConfigurationGroup configurationGroup)
        {
            this.key = key;
            this.configurationGroup = configurationGroup;
        }


        SettingMode(String key, PumpConfigurationGroup configurationGroup, RatioType ratioType)
        {
            this.key = key;
            this.configurationGroup = configurationGroup;
            this.ratioType = ratioType;
        }


        public static SettingMode getModeByKey(String key)
        {
            if (mapKeyMode.containsKey(key))
            {
                return mapKeyMode.get(key);
            }
            else
            {
                return SettingMode.None;
            }
        }


        public PumpConfigurationGroup getConfigurationGroup()
        {
            return configurationGroup;
        }


        public RatioType getRatioType()
        {
            return this.ratioType;
        }

    }

}

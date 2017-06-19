package ggc.core.db.tool.impexp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateObject;
import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;
import com.atech.db.hibernate.tool.data.management.impexp.DbExportConverterAbstract;
import com.atech.db.hibernate.transfer.ExportTool;
import com.atech.utils.xml.XmlPrettyPrintUtil;

import ggc.core.db.hibernate.cgms.CGMSDataExtendedH;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.db.hibernate.food.*;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.db.hibernate.settings.SettingsH;
import ggc.core.db.tool.data.GGCDatabaseTableConfiguration;

/**
 * Created by andy on 15/12/16.
 */
public class GGCDbExportConverter extends DbExportConverterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbExportConverter.class);


    public String convert(DatabaseTableConfiguration tableConfiguration, HibernateObject hibernateObject)
    {
        GGCDatabaseTableConfiguration ggcTableConfig = (GGCDatabaseTableConfiguration) tableConfiguration;

        switch (ggcTableConfig)
        {
            case DayValueH:
                break;

            case SettingsH:
                {
                    SettingsH settings = (SettingsH) hibernateObject;

                    return createTableExportString(settings.getId(), settings.getKey(), settings.getValue(),
                        settings.getType(), settings.getDescription(), settings.getPersonId());
                }

            case ColorSchemeH:
                {
                    ColorSchemeH colorScheme = (ColorSchemeH) hibernateObject;

                    return createTableExportString(colorScheme.getId(), colorScheme.getName(),
                        colorScheme.getCustom_type(), colorScheme.getColor_bg(), colorScheme.getColor_bg_avg(),
                        colorScheme.getColor_bg_low(), colorScheme.getColor_bg_high(), colorScheme.getColor_bg_target(),
                        colorScheme.getColor_ins(), colorScheme.getColor_ins1(), colorScheme.getColor_ins2(),
                        colorScheme.getColor_ins_perbu(), colorScheme.getColor_ch());
                }

            case PumpDataH:
                {
                    PumpDataH pumpData = (PumpDataH) hibernateObject;

                    return createTableExportString(pumpData.getId(), pumpData.getDtInfo(), pumpData.getBaseType(),
                        pumpData.getSubType(), pumpData.getValue(), pumpData.getExtended(), pumpData.getPersonId(),
                        pumpData.getComment(), pumpData.getChanged());
                }

            case PumpDataExtendedH:
                {
                    PumpDataExtendedH pumpDataExtended = (PumpDataExtendedH) hibernateObject;

                    return createTableExportString(pumpDataExtended.getId(), pumpDataExtended.getDtInfo(),
                        pumpDataExtended.getType(), pumpDataExtended.getValue(), pumpDataExtended.getExtended(),
                        pumpDataExtended.getPersonId(), pumpDataExtended.getComment(), pumpDataExtended.getChanged());
                }

            case PumpProfileH:
                {
                    PumpProfileH pumpProfile = (PumpProfileH) hibernateObject;

                    return createTableExportString(pumpProfile.getId(), pumpProfile.getName(),
                        pumpProfile.getBasalBase(), pumpProfile.getBasalDiffs(), pumpProfile.getActiveFrom(),
                        pumpProfile.getActiveTill(), pumpProfile.getExtended(), pumpProfile.getPersonId(),
                        pumpProfile.getComment(), pumpProfile.getChanged());
                }

            case CGMSDataH:
                {
                    CGMSDataH cgmsData = (CGMSDataH) hibernateObject;

                    return createTableExportString(cgmsData.getId(), cgmsData.getDtInfo(), cgmsData.getBaseType(),
                        cgmsData.getSubType(), cgmsData.getValue(), cgmsData.getExtended(), cgmsData.getPersonId(),
                        cgmsData.getComment(), cgmsData.getChanged());
                }

            case CGMSDataExtendedH:
                {
                    CGMSDataExtendedH cgmsDataExtended = (CGMSDataExtendedH) hibernateObject;

                    return createTableExportString(cgmsDataExtended.getId(), cgmsDataExtended.getDtInfo(),
                        cgmsDataExtended.getType(), cgmsDataExtended.getValue(), cgmsDataExtended.getExtended(),
                        cgmsDataExtended.getPersonId(), cgmsDataExtended.getComment(), cgmsDataExtended.getChanged());
                }

            case FoodGroupH:
                {
                    FoodGroupH eh = (FoodGroupH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getNameI18n(), eh.getDescription());
                }

            case FoodDescriptionH:
                {
                    FoodDescriptionH eh = (FoodDescriptionH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getGroupId(), eh.getName(), eh.getNameI18n(),
                        eh.getRefuse(), eh.getNutritions(), eh.getHomeWeights());
                }

            case FoodUserGroupH:
                {
                    FoodUserGroupH eh = (FoodUserGroupH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getNameI18n(), eh.getDescription(),
                        eh.getParentId(), eh.getChanged());
                }

            case FoodUserDescriptionH:
                {
                    FoodUserDescriptionH eh = (FoodUserDescriptionH) hibernateObject;

                    String nutr = eh.getNutritions();
                    if (nutr == null)
                    {
                        nutr = "";
                    }
                    else
                    {
                        nutr = nutr.replace(",", ".");
                    }

                    return createTableExportString(eh.getId(), eh.getName(), eh.getNameI18n(), eh.getGroupId(),
                        eh.getRefuse(), eh.getDescription(), eh.getHomeWeights(), nutr, eh.getChanged());
                }

            case MealH:
                {
                    MealH eh = (MealH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getNameI18n(), eh.getGroupId(),
                        eh.getDescription(), eh.getParts(), eh.getNutritions(), eh.getExtended(), eh.getComment(),
                        eh.getChanged());
                }

            case MealGroupH:
                {
                    MealGroupH eh = (MealGroupH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getNameI18n(), eh.getDescription(),
                        eh.getParentId(), eh.getChanged());
                }

            case NutritionDefinitionH:
                {
                    NutritionDefinitionH eh = (NutritionDefinitionH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getWeightUnit(), eh.getTag(), eh.getName(),
                        eh.getDecimalPlaces(), eh.getStaticEntry());
                }

            case NutritionHomeWeightTypeH:
                {
                    NutritionDefinitionH eh = (NutritionDefinitionH) hibernateObject;

                    return createTableExportString(eh.getId(), eh.getName(), eh.getStaticEntry());
                }

            case DoctorH:
                {
                    DoctorH doc = (DoctorH) hibernateObject;

                    return createTableExportString(doc.getId(), doc.getName(), doc.getDoctorType().getId(),
                        doc.getAddress(), doc.getPhone(), doc.getPhoneGsm(), doc.getEmail(),
                        XmlPrettyPrintUtil.unPrettyPrint(doc.getWorkingTime()), doc.getActiveFrom(),
                        doc.getActiveTill(), doc.getPersonId(), doc.getExtended(), doc.getComment());
                }

            case DoctorTypeH:
                {
                    DoctorTypeH doctorType = new DoctorTypeH();

                    return createTableExportString(doctorType.getId(), doctorType.getName(),
                        doctorType.getPredefined());
                }

            case DoctorAppointmentH:
                {
                    DoctorAppointmentH doctorAppointment = new DoctorAppointmentH();

                    return createTableExportString(doctorAppointment.getId(), doctorAppointment.getDoctor().getId(),
                        doctorAppointment.getAppointmentDateTime(), doctorAppointment.getAppointmentText(),
                        doctorAppointment.getPersonId(), doctorAppointment.getExtended(),
                        doctorAppointment.getComment());
                }

            default:
                String errorString = String.format(
                    "GGCDbExportConverter doesn't support this database object for export (%s)",
                    tableConfiguration.getObjectName());
                LOG.error(errorString);
                throw new RuntimeException(errorString);

        }

        return null;

    }


    private String createTableExportString(Object... elements)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object element : elements)
        {
            stringBuilder.append(ExportTool.DELIMITER);
            stringBuilder.append(element);
        }

        return stringBuilder.substring(ExportTool.DELIMITER.length());
    }

}

package ggc.core.db.tool.impexp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateObject;
import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;
import com.atech.db.hibernate.tool.data.dto.DbTableExportInfoDto;
import com.atech.db.hibernate.tool.data.management.impexp.DbImportConverterAbstract;
import com.atech.utils.xml.XmlPrettyPrintUtil;

import ggc.core.db.hibernate.cgms.CGMSDataExtendedH;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.db.hibernate.food.FoodUserDescriptionH;
import ggc.core.db.hibernate.food.FoodUserGroupH;
import ggc.core.db.hibernate.food.MealGroupH;
import ggc.core.db.hibernate.food.MealH;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.db.hibernate.settings.SettingsH;
import ggc.core.db.tool.data.GGCDatabaseTableConfiguration;
import ggc.core.util.DataAccess;

public class GGCDbImportConverter extends DbImportConverterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbImportConverter.class);


    public GGCDbImportConverter(DataAccess dataAccess)
    {
        super(dataAccess);
    }


    public HibernateObject convert(DatabaseTableConfiguration databaseTableConfiguration, String dataLine,
            DbTableExportInfoDto exportInfoDto)
    {
        GGCDatabaseTableConfiguration ggcTableConfig = (GGCDatabaseTableConfiguration) databaseTableConfiguration;

        String[] dataArray = getSplittedValues(dataLine);

        switch (ggcTableConfig)
        {
            case DayValueH:
                return createDayValueH(dataArray, exportInfoDto);

            case SettingsH:
                return createSettingsH(dataArray);

            case ColorSchemeH:
                return createColorSchemeH(dataArray);

            case PumpDataH:
                return createPumpDataH(dataArray);

            case PumpDataExtendedH:
                return createPumpDataExtendedH(dataArray);

            case PumpProfileH:
                return createPumpProfileH(dataArray);

            case CGMSDataH:
                return createCGMSDataH(dataArray);

            case CGMSDataExtendedH:
                return createCGMSDataExtendedH(dataArray);

            case FoodGroupH:
                return createFoodGroupH(dataArray);

            case FoodDescriptionH:
                return createFoodDescriptionH(dataArray);

            case FoodUserGroupH:
                return createFoodUserGroupH(dataArray);

            case FoodUserDescriptionH:
                return createFoodUserDescriptionH(dataArray);

            case MealH:
                return createMealH(dataArray);

            case MealGroupH:
                return createMealGroupH(dataArray);

            case NutritionDefinitionH:
                return createNutritionDefinitionH(dataArray);

            case NutritionHomeWeightTypeH:
                return createNutritionHomeWeightTypeH(dataArray);

            // case StocksXYZ:

            case DoctorH:
                return createDoctorH(dataArray);

            case DoctorTypeH:
                return createDoctorTypeH(dataArray);

            case DoctorAppointmentH:
                return createDoctorAppointmentH(dataArray);

            default:
                String errorString = String.format(
                    "GGCDbImportConverter doesn't support this database object for import (%s)",
                    databaseTableConfiguration.getObjectName());
                LOG.error(errorString);
                throw new RuntimeException(errorString);

        }

        // return null;
    }


    // public HibernateObject convert(DataDefinitionEntry dataDefinitionEntry,
    // String dataLine,
    // DbTableExportInfoDto exportInfoDto)
    // {
    //
    // }

    private HibernateObject createDayValueH(String[] dataArray, DbTableExportInfoDto exportInfoDto)
    {
        // TODO
        return null;
    }


    private HibernateObject createSettingsH(String[] dataArray)
    {
        SettingsH settings = new SettingsH();

        setId(dataArray[0], settings);

        settings.setKey(getString(dataArray[1]));
        settings.setValue(getString(dataArray[2]));
        settings.setType(getInt(dataArray[3]));
        settings.setDescription(getString(dataArray[4]));

        settings.setPersonId(getPersonId(dataArray[5]));

        return settings;
    }


    private HibernateObject createColorSchemeH(String[] dataArray)
    {
        ColorSchemeH colorScheme = new ColorSchemeH();

        setId(dataArray[0], colorScheme);

        colorScheme.setName(dataArray[1]);
        colorScheme.setCustom_type(getInt(dataArray[2]));
        colorScheme.setColor_bg(getInt(dataArray[3]));
        colorScheme.setColor_bg_avg(getInt(dataArray[4]));
        colorScheme.setColor_bg_low(getInt(dataArray[5]));
        colorScheme.setColor_bg_high(getInt(dataArray[6]));
        colorScheme.setColor_bg_target(getInt(dataArray[7]));
        colorScheme.setColor_ins(getInt(dataArray[8]));
        colorScheme.setColor_ins1(getInt(dataArray[9]));
        colorScheme.setColor_ins2(getInt(dataArray[10]));
        colorScheme.setColor_ins_perbu(getInt(dataArray[11]));
        colorScheme.setColor_ch(getInt(dataArray[12]));

        return colorScheme;
    }


    private HibernateObject createPumpDataH(String[] dataArray)
    {
        PumpDataH pumpData = new PumpDataH();

        setId(dataArray[0], pumpData);
        pumpData.setDtInfo(getLong(dataArray[1]));
        pumpData.setBaseType(getInt(dataArray[2]));
        pumpData.setSubType(getInt(dataArray[3]));
        pumpData.setValue(getString(dataArray[4]));
        pumpData.setExtended(getString(dataArray[5]));
        pumpData.setPersonId(getPersonId(dataArray[6]));
        pumpData.setComment(getString(dataArray[7]));
        pumpData.setChanged(getLong(dataArray[8]));

        return pumpData;
    }


    private HibernateObject createPumpDataExtendedH(String[] dataArray)
    {
        PumpDataExtendedH pumpDataExtended = new PumpDataExtendedH();

        setId(dataArray[0], pumpDataExtended);
        pumpDataExtended.setDtInfo(getLong(dataArray[1]));
        pumpDataExtended.setType(getInt(dataArray[2]));
        pumpDataExtended.setValue(getString(dataArray[3]));
        pumpDataExtended.setExtended(getString(dataArray[4]));
        pumpDataExtended.setPersonId(getPersonId(dataArray[5]));
        pumpDataExtended.setComment(getString(dataArray[6]));
        pumpDataExtended.setChanged(getLong(dataArray[7]));

        return pumpDataExtended;
    }


    private HibernateObject createPumpProfileH(String[] dataArray)
    {
        PumpProfileH pumpProfile = new PumpProfileH();

        setId(dataArray[0], pumpProfile);
        pumpProfile.setName(getString(dataArray[1]));
        pumpProfile.setBasalBase(getFloat(dataArray[2]));
        pumpProfile.setBasalDiffs(getString(dataArray[3]));
        pumpProfile.setActiveFrom(getLong(dataArray[4]));
        pumpProfile.setActiveTill(getLong(dataArray[5]));
        pumpProfile.setExtended(getString(dataArray[6]));
        pumpProfile.setPersonId(getPersonId(dataArray[7]));
        pumpProfile.setComment(getString(dataArray[8]));
        pumpProfile.setChanged(getLong(dataArray[9]));

        return pumpProfile;
    }


    private HibernateObject createCGMSDataH(String[] dataArray)
    {
        CGMSDataH cgmsData = new CGMSDataH();

        setId(dataArray[0], cgmsData);
        cgmsData.setDtInfo(getLong(dataArray[1]));
        cgmsData.setBaseType(getInt(dataArray[2]));
        cgmsData.setSubType(getInt(dataArray[3]));
        cgmsData.setValue(getString(dataArray[4]));
        cgmsData.setExtended(getString(dataArray[5]));
        cgmsData.setPersonId(getPersonId(dataArray[6]));
        cgmsData.setComment(getString(dataArray[7]));
        cgmsData.setChanged(getLong(dataArray[8]));

        return cgmsData;
    }


    private HibernateObject createCGMSDataExtendedH(String[] dataArray)
    {
        CGMSDataExtendedH cgmsDataExtended = new CGMSDataExtendedH();

        setId(dataArray[0], cgmsDataExtended);

        cgmsDataExtended.setDtInfo(getLong(dataArray[1]));
        cgmsDataExtended.setType(getInt(dataArray[2]));
        cgmsDataExtended.setValue(getString(dataArray[3]));
        cgmsDataExtended.setExtended(getString(dataArray[4]));
        cgmsDataExtended.setPersonId(getPersonId(dataArray[5]));
        cgmsDataExtended.setComment(getString(dataArray[6]));
        cgmsDataExtended.setChanged(getLong(dataArray[7]));

        return cgmsDataExtended;
    }


    private HibernateObject createNutritionHomeWeightTypeH(String[] dataArray)
    {
        // TODO
        return null;
    }


    private HibernateObject createNutritionDefinitionH(String[] dataArray)
    {
        // TODO
        return null;
    }


    private HibernateObject createMealH(String[] dataArray)
    {
        MealH meal = new MealH();

        // ; Columns: id; name; name_i18n; group_id; description;
        // parts;nutritions; extended; comment; changed

        setId(dataArray[0], meal);

        meal.setName(getString(dataArray[1]));
        meal.setNameI18n(getString(dataArray[2]));

        int group = getInt(dataArray[3]);

        if (group == 0)
        {
            group = 1;
        }

        meal.setGroupId(group);

        meal.setDescription(getString(dataArray[4]));
        meal.setParts(getString(dataArray[5]));
        meal.setNutritions(getString(dataArray[6]));
        meal.setExtended(getString(dataArray[7]));
        meal.setComment(getString(dataArray[8]));
        meal.setChanged(getLong(dataArray[9]));

        return meal;
    }


    private HibernateObject createMealGroupH(String[] dataArray)
    {
        MealGroupH mealGroup = new MealGroupH();

        setId(dataArray[0], mealGroup);

        mealGroup.setName(getString(dataArray[1]));
        mealGroup.setNameI18n(getString(dataArray[2]));
        mealGroup.setDescription(getString(dataArray[3]));

        int parent_id = getInt(dataArray[4]);

        // if (group==0) // root can't have elements
        // group=1;

        mealGroup.setParentId(parent_id);
        mealGroup.setChanged(getLong(dataArray[5]));

        return mealGroup;
    }


    private HibernateObject createFoodUserDescriptionH(String[] dataArray)
    {
        FoodUserDescriptionH foodUserDescription = new FoodUserDescriptionH();

        // ; Columns: id; name; name_i18n; group_id; refuse;
        // description; home_weights; nutritions; changed
        setId(dataArray[0], foodUserDescription);

        foodUserDescription.setName(getString(dataArray[1]));
        foodUserDescription.setNameI18n(getString(dataArray[2]));

        int group = getInt(dataArray[3]);

        if (group == 0)
        {
            group = 1;
        }

        foodUserDescription.setGroupId(group);

        foodUserDescription.setRefuse(getFloat(dataArray[4]));
        foodUserDescription.setDescription(getString(dataArray[5]));
        foodUserDescription.setHomeWeights(getString(dataArray[6]));
        foodUserDescription.setNutritions(getString(dataArray[7]));
        foodUserDescription.setChanged(getLong(dataArray[8]));

        return foodUserDescription;
    }


    private HibernateObject createFoodUserGroupH(String[] dataArray)
    {
        FoodUserGroupH foodUserGroup = new FoodUserGroupH();

        setId(dataArray[0], foodUserGroup);

        foodUserGroup.setName(getString(dataArray[1]));
        foodUserGroup.setNameI18n(getString(dataArray[2]));
        foodUserGroup.setDescription(getString(dataArray[3]));

        int parent_id = getInt(dataArray[4]);

        // if (group==0) // root can't have elements
        // group=1;

        foodUserGroup.setParentId(parent_id);
        foodUserGroup.setChanged(getLong(dataArray[5]));

        return foodUserGroup;
    }


    private HibernateObject createFoodDescriptionH(String[] dataArray)
    {
        // TODO
        return null;
    }


    private HibernateObject createFoodGroupH(String[] dataArray)
    {
        // TODO
        return null;
    }


    private HibernateObject createDoctorH(String[] dataArray)
    {
        DoctorH doctor = new DoctorH();

        setId(dataArray[0], doctor);
        doctor.setName(getString(dataArray[1]));

        doctor.setDoctorType(getCachedObject(DoctorTypeH.class, dataArray[2]));

        doctor.setAddress(getString(dataArray[3]));
        doctor.setPhone(getString(dataArray[4]));
        doctor.setPhoneGsm(getString(dataArray[5]));
        doctor.setEmail(getString(dataArray[6]));
        doctor.setWorkingTime(XmlPrettyPrintUtil.prettyPrint(dataArray[7]));
        doctor.setActiveFrom(getLong(dataArray[8]));
        doctor.setActiveTill(getLong(dataArray[9]));
        doctor.setPersonId(getPersonId(dataArray[10]));
        doctor.setExtended(getString(dataArray[11]));
        doctor.setComment(getString(dataArray[12]));

        return doctor;
    }


    private HibernateObject createDoctorTypeH(String[] dataArray)
    {
        DoctorTypeH doctorType = new DoctorTypeH();

        setId(dataArray[0], doctorType);

        doctorType.setName(getString(dataArray[1]));
        doctorType.setPredefined(getInt(dataArray[2]));

        return doctorType;
    }


    private HibernateObject createDoctorAppointmentH(String[] dataArray)
    {
        DoctorAppointmentH doctorAppointment = new DoctorAppointmentH();

        setId(dataArray[0], doctorAppointment);

        doctorAppointment.setDoctor(getCachedObject(DoctorH.class, dataArray[1]));

        doctorAppointment.setAppointmentDateTime(getLong(dataArray[2]));
        doctorAppointment.setAppointmentText(getString(dataArray[3]));

        doctorAppointment.setPersonId(getPersonId(dataArray[4]));
        doctorAppointment.setExtended(getString(dataArray[5]));
        doctorAppointment.setComment(getString(dataArray[6]));

        return doctorAppointment;
    }


    private int getPersonId(String personIdString)
    {
        int personId = dataAccess.getIntValueFromString(personIdString);

        return personId == 0 ? 1 : personId;
    }

}

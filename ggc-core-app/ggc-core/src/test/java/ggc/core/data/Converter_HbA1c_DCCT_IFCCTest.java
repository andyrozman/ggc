package ggc.core.data;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.core.data.defs.HbA1cType;

/**
 * Created by andy on 22/11/17.
 */

@Slf4j
@RunWith(Parameterized.class)
public class Converter_HbA1c_DCCT_IFCCTest {

    //private static final Logger LOG = LoggerFactory.getLogger(Converter_HbA1c_DCCT_IFCCTest.class);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // IFCC	NGSP
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 31f, 5.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 42f, 6.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 53f, 7.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 64f, 8.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 75f, 9.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 86f, 10.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 97f, 11.0f },
                { HbA1cType.IFCC_mmolmol, HbA1cType.NGSP_Percent, 108f, 12.0f },

                // FIXME               IFCC	eAg (mmol/L)
//  FIXME              IFCC	eAg (mg/dL)
// FIXME               IFCC	JDS
// FIXME               IFCC	Mono-S


                // NGSP	IFCC
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 5.0f, 31f  },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 6.0f, 42f  },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 7.0f, 53f },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 8.0f, 64f  },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 9.0f, 75f  },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 10.0f, 86f  },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 11.0f, 97f  },
                { HbA1cType.NGSP_Percent, HbA1cType.IFCC_mmolmol, 12.0f, 108f  },


//                28
//   FIXME             NGSP	eAg (mmol/L)
//  FIXME              NGSP	eAg (mg/dL)
// FIXME               NGSP	JDS
//  FIXME              NGSP	Mono-S
//  FIXME              eAg (mmol/L)	IFCC
//  FIXME              eAg (mmol/L)	NGSP
//  FIXME              eAg (mmol/L)	eAg (mg/dL)
//  FIXME              eAg (mmol/L)	JDS
//  FIXME              eAg (mmol/L)	Mono-S
//  FIXME              eAg (mg/dL)	IFCC
//  FIXME              eAg (mg/dL)	NGSP
//  FIXME              eAg (mg/dL)	eAg (mmol/L)
//  FIXME              eAg (mg/dL)	JDS
//  FIXME              eAg (mg/dL)	Mono-S
//  FIXME              JDS	IFCC
//  FIXME              JDS	NGSP
//  FIXME              JDS	eAg (mmol/L)
//  FIXME              JDS	eAg (mg/dL)
//  FIXME              JDS	Mono-S
//  FIXME              Mono-S	IFCC
//  FIXME              Mono-S	NGSP
//  FIXME              Mono-S	eAg (mmol/L)
//  FIXME              Mono-S	eAg (mg/dL)
//  FIXME              Mono-S	JDS


                //
        });
    }


    HbA1cType inputType;
    HbA1cType outputType;
    float value;
    float expected;

    static Converter_HbA1c_DCCT_IFCC converter = new Converter_HbA1c_DCCT_IFCC();

    public Converter_HbA1c_DCCT_IFCCTest(HbA1cType inputType, HbA1cType outputType, float value, float expected) {
        this.inputType = inputType;
        this.outputType = outputType;
        this.value = value;
        this.expected = expected;
    }

    @Test
    public void test() {
        float result = converter.getValueByType(inputType, outputType, value);
        log.info("{}: Converting {} {} to {}. Expected: {}, Got: {}", expected==result  ? "Success" : "FAIL  " , value, inputType.name(), outputType.name(), expected, result);
        assertEquals(expected, converter.getValueByType(inputType, outputType, value), 0.0f);
    }
}
package ggc.connect.test;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Created by andy on 3/29/18.
 */
public class NightScoutImporterUTest {

    int[] monthData;

    private int getMonthQuickSortWay(int first, int last, boolean lastPart) {
        System.out.println("getMonthQuickSortWay: first=" + first + ", last=" + last);

        if (first==last)
            return first;

        if (lastPart)
        {
            int x = (last - first);

            if (x%2==0)
            {
                int y = x/2;

                int z = last - y;

                int cnt = getCountByMonth(z);

                if (cnt==0)
                {
                    return getMonthQuickSortWay(z, last,  true);
                }
                else
                {
                    return getMonthQuickSortWay(first, z,  true);
                }
            }
            else
            {
                return getMonthHardWay(first, last);
            }
        }
        else
        {
            float l = last/2.0f;
            int month = 0;

            if (last%2==0)
            {
                month = (last/2);
            }
            else
            {
                return getMonthHardWay(first, last);
            }

            int cnt = getCountByMonth(month);

            if (cnt>0)
            {
                return getMonthQuickSortWay(first, month, false);
            }
            else
            {
                return getMonthQuickSortWay(month, last, true);
            }
        }
    }

    private int getCountByMonth(int month) {

        return this.monthData[month];
    }

    private int getMonthHardWay(int first, int last)
    {
        //int lastMonth = 0;
        int monthWithData = 0;

        for(int i=last; i>=first; i--)
        {
            int cnt = getCountByMonth(i);

            if (cnt>0)
            {
                monthWithData = i;
            }
        }

        return monthWithData;
    }


    @Test
    public void testMonth1()
    {
        doTest(1, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth2()
    {
        doTest(2, new int[] { 0, 0, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth3()
    {
        doTest(3, new int[] { 0, 0, 0, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth4()
    {
        doTest(4, new int[] { 0, 0, 0, 0, 4, 5, 6, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth5()
    {
        doTest(5, new int[] { 0, 0, 0, 0, 0, 5, 6, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth6()
    {
        doTest(6, new int[] { 0, 0, 0, 0, 0, 0, 6, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth7()
    {
        doTest(7, new int[] { 0, 0, 0, 0, 0, 0, 0, 7, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth8()
    {
        doTest(8, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 8 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth9()
    {
        doTest(9, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 ,9, 10, 11, 12});
    }

    @Test
    public void testMonth10()
    {
        doTest(10, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0, 10, 11, 12});
    }

    @Test
    public void testMonth11()
    {
        doTest(11, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0,  0, 11, 12});
    }

    @Test
    public void testMonth12()
    {
        doTest(12, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0,  0,  0, 12});
    }



    private void doTest(int correct, int[] monthData)
    {
        this.monthData = monthData;

        System.out.println(" ==== Expect month: " + correct + " ====");

        //getMonthQuickSortWay(1, 12, false);

        Assert.assertEquals(correct, getMonthQuickSortWay(1, 12, false));
    }

} 
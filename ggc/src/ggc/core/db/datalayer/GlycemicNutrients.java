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
 *  Filename: NutritionHomeWeightType
 *  Purpose:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). 
 *      This one is used for Food's Home Weights.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.core.db.datalayer;

import ggc.core.util.DataAccess;
import ggc.core.util.NutriI18nControl;

import java.util.Hashtable;

public class GlycemicNutrients
{

    NutriI18nControl ic = NutriI18nControl.getInstance();

    public boolean debug = false;
    String text_idx;
    DataAccess m_da = DataAccess.getInstance();

    long id;
    String name;
    // String amount;
    String weight;
    Hashtable<String, MealNutrition> nutrients;

    public GlycemicNutrients(MealNutrition mn)
    {
	this.nutrients = new Hashtable<String, MealNutrition>();
	loadGI_GL();
	processEntry(mn);
    }
    
    public void mergeGlycemicData()
    {
	// TODO: this need to be done
	
	System.out.println("mergeGlycemicData not implemented");
    }
    
    

    private void loadGI_GL()
    {
	// GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
	// GL_MAX = 4005
	this.nutrients.put("4002", new MealNutrition(4002, 0.0f, "GI Min"));
	this.nutrients.put("4003", new MealNutrition(4003, 0.0f, "GI Max"));
	this.nutrients.put("4004", new MealNutrition(4004, 0.0f, "GL Min"));
	this.nutrients.put("4005", new MealNutrition(4005, 0.0f, "GL Max"));
    }

    public void addNutrient(MealNutrition mn)
    {
	this.processEntry(mn);
    }

    private void processEntry(MealNutrition mn)
    {
	// GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
	// GL_MAX = 4005

	if (mn.getId() == 4000)
	{
	    checkGI_GL(this.nutrients, mn, true);
	}
	else if (mn.getId() == 4001)
	{
	    checkGI_GL(this.nutrients, mn, false);
	}
	else if (mn.getId() == 4002)
	{
	    checkGI_GL_Min(this.nutrients, mn, true);
	}
	else if (mn.getId() == 4003)
	{
	    checkGI_GL_Max(this.nutrients, mn, true);
	}
	else if (mn.getId() == 4004)
	{
	    checkGI_GL_Min(this.nutrients, mn, false);
	}
	else
	// if (mn.getId()==4005)
	{
	    checkGI_GL_Max(this.nutrients, mn, false);
	}

    }

    private void checkGI_GL(Hashtable<String, MealNutrition> nutres,
	    MealNutrition mn, boolean GI)
    {
	// GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
	// GL_MAX = 4005

	String gi_gl_min;
	String gi_gl_max;

	if (GI)
	{
	    gi_gl_min = "4002";
	    gi_gl_max = "4003";
	}
	else
	{
	    gi_gl_min = "4004";
	    gi_gl_max = "4005";
	}

	if ((nutres.get(gi_gl_min).getAmount() == 0))
	{
	    nutres.get(gi_gl_min).setAmount(mn.getAmount());
	}
	else
	{
	    if (nutres.get(gi_gl_min).getAmount() > mn.getAmount())
	    {
		nutres.get(gi_gl_min).setAmount(mn.getAmount());
	    }
	}

	if ((nutres.get(gi_gl_max).getAmount() == 0))
	{
	    nutres.get(gi_gl_max).setAmount(mn.getAmount());
	}
	else
	{
	    if (nutres.get(gi_gl_max).getAmount() < mn.getAmount())
	    {
		nutres.get(gi_gl_max).setAmount(mn.getAmount());
	    }
	}

    }

    private void checkGI_GL_Max(Hashtable<String, MealNutrition> nutres,
	    MealNutrition mn, boolean GI)
    {
	// GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
	// GL_MAX = 4005

	String par;

	if (GI)
	    par = "4003";
	else
	    par = "4005";

	if ((nutres.get(par).getAmount() == 0))
	{
	    nutres.get(par).setAmount(mn.getAmount());
	}
	else
	{
	    if (nutres.get(par).getAmount() < mn.getAmount())
	    {
		nutres.get(par).setAmount(mn.getAmount());
	    }
	}

    }

    private void checkGI_GL_Min(Hashtable<String, MealNutrition> nutres,
	    MealNutrition mn, boolean GI)
    {
	// GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
	// GL_MAX = 4005

	String par;

	if (GI)
	    par = "4002";
	else
	    par = "4004";

	if ((nutres.get(par).getAmount() == 0))
	{
	    nutres.get(par).setAmount(mn.getAmount());
	}
	else
	{
	    if (nutres.get(par).getAmount() > mn.getAmount())
	    {
		nutres.get(par).setAmount(mn.getAmount());
	    }
	}

    }

}

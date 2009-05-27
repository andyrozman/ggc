using System;
using System.Linq;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using GGCMobileNET.Data.Db.Objects;
using GGCMobileNET.Data.Utils;
using GGCMobileNET.Data;

namespace GGCMobileNET.GUI.DailyData
{
    public partial class DailyDataItemWindow : Form
    {
        private bool new_item = false;
        private DailyValuesRow data_object;

        public DailyDataItemWindow()
        {
            InitializeComponent();
            this.new_item = true;
        }

        public DailyDataItemWindow(DailyValuesRow dobject)
        {
            this.data_object = dobject;
        }




        private void button7_Click(object sender, EventArgs e)
        {
            // cancel
            this.cmdCanel();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            // ok
            cmdOK();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            // ok
            cmdOK();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            // cancel
            this.cmdCanel();
        }

        private void cmdOK()
        {
            int type = 0;

            if (this.comboBox1.SelectedIndex == 0)
                type = DataAccessMobile.BG_MMOL;
            else
                type = DataAccessMobile.BG_MGDL;

            if (this.new_item)
            {
                this.data_object = new DailyValuesRow();
                this.data_object.SetDateTime(this.dateTimePicker1.Value, this.dateTimePicker2.Value);
                this.data_object.setBG(type, this.textBox1.Text);
                this.data_object.setIns1(this.textBox2.Text);

                this.data_object.setIns2(this.textBox3.Text);
                this.data_object.setCH(this.textBox4.Text);
                this.data_object.setUrine(this.textBox5.Text);
                this.data_object.setActivity(this.textBox6.Text);
                this.data_object.SetComment(this.textBox7.Text);
                this.data_object.SetChanged(DateTime.Now.ToFileTimeUtc());

                DataAccessMobile.Instance.Db.AddDAO(this.data_object);
            }
            else
            {
                this.data_object.SetDateTime(this.dateTimePicker1.Value, this.dateTimePicker2.Value);

                this.data_object.setBG(type, this.textBox1.Text);
                this.data_object.setIns1(this.textBox2.Text);

                this.data_object.setIns2(this.textBox3.Text);
                this.data_object.setCH(this.textBox4.Text);
                this.data_object.setUrine(this.textBox5.Text);
                this.data_object.setActivity(this.textBox6.Text);
                this.data_object.SetComment(this.textBox7.Text);
                this.data_object.SetChanged(DateTime.Now.ToFileTimeUtc());
                this.data_object.Comment = this.textBox7.Text;
//                this.data_object.Changed = DateTime.Now.ToFileTimeUtc();

                if (this.data_object.WasChanged)
                    DataAccessMobile.Instance.Db.EditDAO(this.data_object);
            
            }

            this.Dispose();

        }

        private void cmdCanel()
        {
            this.Dispose();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            // bolus wizard
            BolusWizardWindow bww = new BolusWizardWindow();
            bww.ShowDialog();

            this.Focus();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            // refresh CH
        }

        private void button3_Click(object sender, EventArgs e)
        {
            // foods
            FoodSelectorWindow fsw = new FoodSelectorWindow();
            fsw.ShowDialog();

            this.Focus();
        }



    }
}
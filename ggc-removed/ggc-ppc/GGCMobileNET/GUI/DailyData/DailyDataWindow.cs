using System;
using System.Linq;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using GGCMobileNET.Data.Utils;
using GGCMobileNET.Data;
using GGCMobileNET.GUI.DailyData;

namespace GGCMobileNET.GUI
{
    public partial class DailyDataWindow : Form
    {
        DataAccessMobile da = DataAccessMobile.Instance;

        DateTime selected_date = DateTime.Now;

        public DailyDataWindow()
        {
            InitializeComponent();
            //this.monthCalendar1
        }

        private void monthCalendar1_DateChanged(object sender, DateRangeEventArgs e)
        {
            // show data
            FillList(e.Start);
        }


        private void FillList(DateTime dt)
        {
            DailyValues dv = da.getDb().GetDayStats(dt);

            this.listBox1.Items.Clear();

            for (int i = 0; i < dv.getRowCount(); i++)
            {
                this.listBox1.Items.Add(dv.getRow(i));
            }

            this.selected_date = dt;
 
        }

        private void DailyDataWindow_KeyDown(object sender, KeyEventArgs e)
        {
            if ((e.KeyCode == System.Windows.Forms.Keys.Up))
            {
                // Up
            }
            if ((e.KeyCode == System.Windows.Forms.Keys.Down))
            {
                // Down
            }
            if ((e.KeyCode == System.Windows.Forms.Keys.Left))
            {
                // Left
            }
            if ((e.KeyCode == System.Windows.Forms.Keys.Right))
            {
                // Right
            }
            if ((e.KeyCode == System.Windows.Forms.Keys.Enter))
            {
                // Enter
            }

        }

        private void button4_Click(object sender, EventArgs e)
        {
            // close
            this.Dispose();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            // add item
            DailyDataItemWindow ddiw = new DailyDataItemWindow();
            ddiw.ShowDialog();

            this.Focus();
            FillList(this.selected_date);
        }
    }
}
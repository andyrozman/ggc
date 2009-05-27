using System;
using System.Linq;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using GGCMobileNET.GUI;

namespace GGCMobileNET
{
    public partial class StartScreen : Form
    {
        public StartScreen()
        {
            InitializeComponent();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.Dispose();
        }



        private void button1_Click(object sender, EventArgs e)
        {
            // daily data
            DailyDataWindow ddw = new DailyDataWindow();
            ddw.ShowDialog();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            // food
            FoodWindow fw = new FoodWindow();
            fw.ShowDialog();

        }

        private void button3_Click(object sender, EventArgs e)
        {
            // statistics
            StatisticsWindow sw = new StatisticsWindow();
            sw.ShowDialog();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            // data
            DataWindow dw = new DataWindow();
            dw.ShowDialog();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            // configuration
            ConfigurationWindow cw = new ConfigurationWindow();
            cw.ShowDialog();
        }

        /// <summary>
        /// About button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button7_Click(object sender, EventArgs e)
        {
            // about
            AboutWindow aw = new AboutWindow();
            aw.ShowDialog();
        }


    }
}
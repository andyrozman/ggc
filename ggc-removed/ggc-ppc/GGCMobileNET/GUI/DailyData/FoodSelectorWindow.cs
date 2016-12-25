using System;
using System.Linq;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using GGCMobileNET.GUI.Food;

namespace GGCMobileNET.GUI.DailyData
{
    public partial class FoodSelectorWindow : Form
    {
        public FoodSelectorWindow()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            // +
            FoodSearchWindow fsw = new FoodSearchWindow();
            fsw.ShowDialog();

            this.Focus();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            // Edit
        }

        private void button4_Click(object sender, EventArgs e)
        {
            // -
        }



    }
}
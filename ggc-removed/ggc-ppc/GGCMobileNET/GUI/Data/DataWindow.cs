using System;
using System.Linq;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using GGCMobileNET.Data.Utils;
using GGCMobileNET.Data.Db.Tools;
using System.IO;

namespace GGCMobileNET.GUI
{
    public partial class DataWindow : Form
    {
        public DataWindow()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            // init db
            DataAccessMobile da = DataAccessMobile.Instance;
            da.Db.InitialInitDb();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            // import
            ImportDailyValues idv = new ImportDailyValues(@".\SD Card\GGCData\import\DayValueH.dbe", true, true);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            // close
            this.Dispose();
        }
    }
}
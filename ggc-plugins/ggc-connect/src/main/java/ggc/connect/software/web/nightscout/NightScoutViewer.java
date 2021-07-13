package ggc.connect.software.web.nightscout;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.fit.cssbox.swingbox.BrowserPane;
import org.fit.cssbox.swingbox.util.DefaultHyperlinkHandler;
import org.fit.cssbox.swingbox.util.GeneralEvent;
import org.fit.cssbox.swingbox.util.GeneralEventListener;
import org.fit.net.DataURLHandler;

/**
 * Created by andy on 08/03/18.
 */
public class NightScoutViewer extends JDialog implements HyperlinkListener
{

    private static final long serialVersionUID = 166774627352960726L;


    // private final JEditorPane displayEditorPane;

    public NightScoutViewer(JFrame parent, String url)
    {
        super(parent, "", true);

        // Set window size.
        setSize(640, 480);

        // Handle closing events.
        addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                actionExit();
            }
        });

        // Set up page display.
        // net.sf.sw

        swingBox = createSwingbox();

        // displayEditorPane = new JEditorPane();
        // displayEditorPane.setContentType("text/html");
        // displayEditorPane.setEditable(false);
        // displayEditorPane.addHyperlinkListener(this);

        try
        {
            // displayEditorPane.setPage(new URL(url));

            this.swingBox.setPage(new URL(url));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error loading page: " + e.getMessage());
        }

        getContentPane().setLayout(new BorderLayout());
        // getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(swingBox), BorderLayout.CENTER);

        this.setVisible(true);
    }

    BrowserPane swingBox;


    private BrowserPane createSwingbox()
    {
        BrowserPane swingbox = new BrowserPane();
        swingbox.addHyperlinkListener(new SwingBrowserHyperlinkHandler(this));
        swingbox.addGeneralEventListener(new GeneralEventListener()
        {

            private long time;


            public void generalEventUpdate(GeneralEvent e)
            {
                if (e.event_type == GeneralEvent.EventType.page_loading_begin)
                {
                    this.time = System.currentTimeMillis();
                }
                else if (e.event_type == GeneralEvent.EventType.page_loading_end)
                {
                    // Object title = SwingBrowser.this.swingbox.getDocument().getProperty("title");
                    // if(title != null) {
                    // SwingBrowser.this.tabs.setTitleAt(0, title.toString());
                    // }

                    System.out.println("SwingBox: page loaded in: " + (System.currentTimeMillis() - this.time) + " ms");
                }

            }
        });
        return swingbox;
    }


    private void actionExit()
    {
        this.dispose();
    }


    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent)
    {
        System.out.println("Hyoer evnet " + hyperlinkEvent);

    }

    public class SwingBrowserHyperlinkHandler extends DefaultHyperlinkHandler
    {

        private NightScoutViewer browser;


        public SwingBrowserHyperlinkHandler(NightScoutViewer browser)
        {
            this.browser = browser;
        }


        protected void loadPage(JEditorPane pane, HyperlinkEvent evt)
        {
            this.browser.displayURL(evt.getURL().toString());
        }
    }


    public void displayURL(String urlstring)
    {
        try
        {
            if (!urlstring.startsWith("http:") && !urlstring.startsWith("https:") && !urlstring.startsWith("ftp:")
                    && !urlstring.startsWith("file:") && !urlstring.startsWith("data:"))
            {
                urlstring = "http://" + urlstring;
            }

            URL e = DataURLHandler.createURL((URL) null, urlstring);
            // this.urlText.setText(e.toString());

            // while(this.historyPos < this.history.size()) {
            // this.history.remove(this.history.size() - 1);
            // }

            this.swingBox.setPage(e);

            // this.history.add(e);
            // ++this.historyPos;
            // this.displayURLSwingBox(e);
        }
        catch (Exception var3)
        {
            System.err.println("*** Error: " + var3.getMessage());
            var3.printStackTrace();
        }

    }

    // protected void displayURLSwingBox(URL url) throws IOException {
    // if(this.swingBox == null) {
    // this.swingBox = this.createSwingbox();
    // this.tabs.add("New Tab", new JScrollPane(this.swingbox));
    // }
    //
    // this.swingbox.setPage(url);
    // }
}

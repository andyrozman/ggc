package ggc.core.web;






public class GGCNetworkSend //implements Runnable 
{
/*        String text;
        
        public GGCNetworkSend() 
        {
        }
        
        public void start() {
            Thread t = new Thread(this);
            t.start();
        }
        
        public void run() {
            StringBuffer sb = new StringBuffer();
            try {
                HttpConnection c = (HttpConnection) Connector.open(url);
                c.setRequestProperty(
                  "User-Agent","Profile/MIDP-1.0, Configuration/CLDC-1.0");
                
                c.setRequestProperty("Content-Language","en-US");
                c.setRequestMethod(HttpConnection.POST);
                
                DataOutputStream os = 
                        (DataOutputStream)c.openDataOutputStream();
                
                os.writeUTF(text.trim());
                os.flush();
                os.close();
                
                // Get the response from the servlet page.
                DataInputStream is =(DataInputStream)c.openDataInputStream();
                //is = c.openInputStream();
                int ch;
                sb = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    sb.append((char)ch);
                }
                showAlert(sb.toString());
                is.close();
                c.close();
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        }
                /* This method takes input from user like text and pass
                to servlet */
  /*      public void getServlet(String text) {
            this.text = text;
        }
        
        /* Display Error On screen*/
  /*      private void showAlert(String err) {
            Alert a = new Alert("");
            a.setString(err);
            a.setTimeout(Alert.FOREVER);
            display.setCurrent(a);
        }
    }; */
}
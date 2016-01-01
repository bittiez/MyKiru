/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mykiru;

/**
 *
 * @author Tad
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Downloader extends JFrame {

    Functions fs = new mykiru.Functions();

    //args = {url, filelocation/filename}, progressbar, labels = {current, speed};
    public boolean main(final String[] args, final JProgressBar current, final JLabel[] labels, JCheckBox[] pc) throws Exception {
        long start = System.currentTimeMillis();
        long now, mbs;
        String site = args[0];
        String filename = args[1];
        try {
            File theFile = new File(filename);
            if(!theFile.exists())
                theFile.createNewFile();
            
            URL url = new URL(site);
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            int filesize = connection.getContentLength();
            current.setMaximum(filesize);
            float totalDataRead = 0;
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(theFile);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int i;
            int flush = 0;
            while ((i = in.read(data, 0, 1024)) >= 0 && pc[1].isSelected() != true) {
                while (pc[0].isSelected() == true) {
                    TimeUnit.SECONDS.sleep(1);
                }
                now = System.currentTimeMillis();
                now = now - start;
                now = now / 1000;
                if (now < 1) {
                    now = 1;
                }
                mbs = (((int) totalDataRead) / now);

                totalDataRead = totalDataRead + i;
                bout.write(data, 0, i);
                if(flush > 50){
                    flush = 0;
                    bout.flush();
                }
                flush++;
                    
                current.setValue((int) totalDataRead);
                labels[0].setText(fs.formatint((int) totalDataRead) + "/" + fs.formatint(filesize));
                labels[1].setText("(~" + fs.formatint((int) mbs) + "/s)");
            }
            bout.close();
            in.close();
            if (pc[1].isSelected() == true) {
                Files.deleteIfExists(Paths.get(filename));
            }
            return true;
        } catch (Exception e) {
            javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, e.getMessage(), "Error",
                    javax.swing.JOptionPane.DEFAULT_OPTION);
            return false;
        }

    }
}

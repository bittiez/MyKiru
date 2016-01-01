/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mykiru;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

/**
 *
 * @author Tad
 */
public class ExtractFile {
    public Functions fs = new mykiru.Functions();
    public boolean Extract(String filename, JEditorPane text){
               try {
             // destination folder to extract the contents     
             
             String destinationname = SelDir() + "/";          
             if("FAIL".equals(destinationname)){destinationname = fs.findPath(filename) + "/";}
             byte[] buf = new byte[1024];
             ZipInputStream zipinputstream = null;
             ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(filename));
            zipentry = zipinputstream.getNextEntry();
            fs.w(fs.find_message("fullclient_start_extraction", fs.lang()), text);
           while (zipentry != null) {

                 // for each entry to be extracted
                 String entryName = zipentry.getName();
     
                 int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);

              String directory = newFile.getParent();

              // to creating the parent directories
              if (directory == null) {
                   if (newFile.isDirectory()){
                         break;
                      }
             } else {
                 new File(destinationname+directory).mkdirs();
              }
     

            if(!zipentry.isDirectory()){ 
                       fs.w(fs.find_message("current_file_extracting", fs.lang(), "{FileName}", entryName), text, true);
                       fileoutputstream = new FileOutputStream(destinationname  + entryName);
                      while ((n = zipinputstream.read(buf, 0, 1024)) > -1){
                              fileoutputstream.write(buf, 0, n);
                       }
                      fileoutputstream.close();
            }
              
           zipinputstream.closeEntry();
           zipentry = zipinputstream.getNextEntry();
          }// while
   
     zipinputstream.close();
   } catch (Exception e) {
    return false;
   }
   return true;
   }
    
    public String SelDir(){
      try {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Where would you like to install");
        chooser.showOpenDialog(null);
        File cd = chooser.getSelectedFile();
        String fp = cd.getAbsolutePath();
        Settings settings = new mykiru.Settings();
        settings.UO_DIR.setText(fp);
        settings.savereg();
        return fp;
        
        } catch (Exception e) {}
      return "FAIL";
    }
}

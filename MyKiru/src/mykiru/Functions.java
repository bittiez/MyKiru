/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
Runnable aRunnable = new Runnable(){
@Override
public void run(){

}
};
Thread th = new Thread(aRunnable);
th.start();
 */
package mykiru;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

/**
 *
 * @author Tad
 */
public class Functions {

    public String currentDir = new File(".").getAbsolutePath().trim().substring(0, new File(".").getAbsolutePath().trim().length() - 1);

    public String[] razorPath = {"D:\\Program Files (x86)\\Razor",
        "D:\\Program Files\\Razor",
        "C:\\Razor",
        "D:\\Razor",
        "C:\\Program Files\\Razor",
        "C:\\Program Files (x86)\\Razor"};

    public String[] assistUOPath = {"D:\\Program Files (x86)\\AssistUO",
        "D:\\Program Files\\AssistUO",
        "C:\\AssistUO",
        "D:\\AssistUO",
        "C:\\Program Files\\AssistUO",
        "C:\\Program Files (x86)\\AssistUO"};

    public String[] full_Client(String IPA) {
        try {
            URL game = new URL(IPA + "/UOAU/?data=fullclienturl");
            URLConnection connection = game.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String data = "";
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    data = data + inputLine;
                }

                String[] fullClientData = {"",""};
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(data);
                if (element.isJsonObject()) {
                    JsonObject jsonOb = element.getAsJsonObject();
                    fullClientData[0] = jsonOb.get("file_size").getAsString();
                    fullClientData[1] = jsonOb.get("file").getAsString();
                }
                return fullClientData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] f = {"FAIL", "FAIL"};
        return f;
    }

    public ArrayList GetFiles(String URL) {
        ArrayList fail = new ArrayList();
        fail.add("FAIL");
        String files = "";
        try {
            URL game = new URL(URL + "/UOAU/?data=files");
            URLConnection connection = game.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    files = files + inputLine;
                }
            }
        } catch (Exception e) {
            return fail;
        }
        ArrayList file_data = new ArrayList();
        try {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(files);
            if (element.isJsonObject()) {
                JsonObject jsonOb = element.getAsJsonObject();
                JsonArray datasets = jsonOb.get("files").getAsJsonArray();

                for (int i = 0; i < datasets.size(); i++) {
                    JsonObject dataset = datasets.get(i).getAsJsonObject();
                    ArrayList newFile = new ArrayList();
                    newFile.add(dataset.get("filename").getAsString());
                    newFile.add(dataset.get("size").getAsString());
                    newFile.add(dataset.get("hash").getAsString());
                    file_data.add(newFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            return fail;
        }
        return file_data;
    }

    public String getmd(String filePath) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            return "0";
        }
        MessageDigest hasher;
        try (InputStream inStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024];
            hasher = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = inStream.read(buffer);
                if (numRead > 0) {
                    hasher.update(buffer, 0, numRead);
                }
            }
        }
        byte[] md5Bytes = hasher.digest(); // compute the hash     
        return ConvertHashToString(md5Bytes);
    }

    public boolean hash_is_same(String serv, String clien) {
        //System.out.print("\n\n" + serv.toUpperCase() + "\n" + clien.toUpperCase());
        if (serv.toUpperCase().equals(clien.toUpperCase())) {
            return true;
        }
        return false;
    }

    public static String ConvertHashToString(byte[] md5Bytes) {
        String returnVal = "";
        for (int i = 0; i < md5Bytes.length; i++) {
            returnVal += Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toUpperCase();
    }

    public String findPath(String fn) {
        File temp = Paths.get(fn).toFile();
        String absolutePath = temp.getAbsolutePath();
        //System.out.println("File path : " + absolutePath);
        String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        //System.out.println("File path : " + filePath);
        return filePath;
    }

    public boolean allowed(String filename, JTextField Allowed) {
        String allowed1 = Allowed.getText();
        String[] types = allowed1.split(",");
        String[] extension = filename.split("\\.");
        String ex = extension[extension.length - 1].trim();
        //String ex = extension[1].trim();
        for (String t : types) {
            t = t.trim();
            if (t.toLowerCase().equals(ex.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void w(String data, JEditorPane text) {
        Calendar now = Calendar.getInstance();
        String h, m, s, ts, nl;
        h = doubleDigit(inttostring(now.get(Calendar.HOUR)));
        m = doubleDigit(inttostring(now.get(Calendar.MINUTE)));
        s = doubleDigit(inttostring(now.get(Calendar.SECOND)));
        ts = "[" + h + ":" + m + ":" + s + "]";
        nl = "\n";
        text.setText(ts + data + nl + nl + text.getText());
    }

    public void w(String data, JEditorPane text, boolean NoDoubleSpace) {
        Calendar now = Calendar.getInstance();
        String h, m, s, ts, nl;
        h = doubleDigit(inttostring(now.get(Calendar.HOUR)));
        m = doubleDigit(inttostring(now.get(Calendar.MINUTE)));
        s = doubleDigit(inttostring(now.get(Calendar.SECOND)));
        ts = "[" + h + ":" + m + ":" + s + "]";
        nl = "\n";
        text.setText(ts + data + nl + text.getText());
    }

    public String doubleDigit(String i) {
        switch (i) {
            case "0":
                return "0" + i;
            case "1":
                return "0" + i;
            case "2":
                return "0" + i;
            case "3":
                return "0" + i;
            case "4":
                return "0" + i;
            case "5":
                return "0" + i;
            case "6":
                return "0" + i;
            case "7":
                return "0" + i;
            case "8":
                return "0" + i;
            case "9":
                return "0" + i;
        }

        return i;
    }

    public Runnable Start_Program(final String[] paramStr, final String prog) {

        Runnable aRunnable = new Runnable() {
            @Override
            public void run() {
                for (String d : paramStr) {
                    File f = new File(d + "\\" + prog);
                    if (f.exists()) {
                        try {
                            final ProcessBuilder pb = new ProcessBuilder(d + "\\" + prog);
                            pb.directory(new File(d + "\\"));
                            final Process p = pb.start();
                            break;
                        } catch (Exception exc) {
                        }
                    }
                }
            }
        };
        Thread th = new Thread(aRunnable);
        th.start();
        return th;

    }

    public String inttostring(int i) {
        return i + "";
    }

    public String inttostring(long i) {
        return i + "";
    }

    public String formatint(int bytes) {
        String d = "b";
        if (bytes > 1024) {
            bytes = bytes / 1000;
            d = "kb";
        }
        if (bytes > 1024) {
            bytes = bytes / 1000;
            d = "mb";
        }
        return bytes + d;
    }

    public String formatint(String bytesamount) {
        int bytes = Integer.parseInt(bytesamount);
        String d = "b";
        if (bytes > 1024) {
            bytes = bytes / 1000;
            d = "kb";
        }
        if (bytes > 1024) {
            bytes = bytes / 1000;
            d = "mb";
        }
        return bytes + d;
    }

    public void w_reg(String reg, String text) {
        final Preferences userRoot = Preferences.userRoot();
        userRoot.put("Ultima Online/MyKiru/" + reg, text);
    }

    public String r_reg(String reg, String nodata) {
        final Preferences userRoot = Preferences.userRoot();
        return userRoot.get("Ultima Online/MyKiru/" + reg, nodata);
    }

    public String findsetting(String find, String from) {
        String[] one;

        try {
            one = from.split("<" + find.toLowerCase() + ">");
            one = one[1].split("</" + find.toLowerCase() + ">");
            return one[0];
        } catch (Exception e) {
            return "";
        }
    }

    public String find_message(String find, String from) {
        String[] one;

        try {
            one = from.split("<" + find.toLowerCase() + ">");
            one = one[1].split("</" + find.toLowerCase() + ">");
            return one[0];
        } catch (Exception e) {
            return "";
        }
    }

    public String lang() {
        String path = currentDir + "bin\\language.xml";
        File f = new File(path);
        if (f.exists()) {
            try {
                String cf = readFileAsString(path);
                return cf;
            } catch (Exception ex) {
            }
        }
        return "";
    }

    public String find_message(String find, String from, String replace, String replace_with) {
        String[] one;
        if (from.contains("<" + find.toLowerCase() + ">") == false || from.contains("</" + find.toLowerCase() + ">") == false) {
            return "";
        }
        try {
            one = from.split("<" + find.toLowerCase() + ">");
            one = one[1].split("</" + find.toLowerCase() + ">");
            return one[0].replace(replace, replace_with);
        } catch (Exception e) {
            return "";
        }
    }

    public String readFileAsString(String file) {
        String result = null;
        DataInputStream in = null;

        try {
            File f = new File(file);
            byte[] buffer = new byte[(int) f.length()];
            in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            return "";
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                /* ignore it */
            }
        }
        return result;
    }

    protected ImageIcon createImageIcon(String path, String description) {
        return new ImageIcon(path, description);
//        
//    java.net.URL imgURL = getClass().getResource(path);
//    if (imgURL != null) {
//        return new ImageIcon(imgURL, description);
//    } else {
//        return null;
//    }
    }
}

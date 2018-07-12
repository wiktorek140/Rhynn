//���
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FWUtils;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author STeeL
 */
public class FWGBridge {
    public static String currentLang = "english";
    public static Hashtable language = new Hashtable();
    public static Class utilsobj = null;
    public FWGBridge()
    {
        utilsobj = getClass();
    }
    public static String gl(String id)
    {
        try
        {
            String lang_entry = FWGBridge.language.get(id).toString();
            lang_entry = replace(lang_entry,"~~","\n");
            return lang_entry;
        }
        catch(Exception e)
        {
            return "LANG_NOT_FOUND";
        }
    }
    public static String replace( String str, String pattern, String replace ) 
{
    int s = 0;
    int e = 0;
    StringBuffer result = new StringBuffer();

    while ( (e = str.indexOf( pattern, s ) ) >= 0 ) 
    {
        result.append(str.substring( s, e ) );
        result.append( replace );
        s = e+pattern.length();
    }
    result.append( str.substring( s ) );
    return result.toString();
}   
    public static String ReadFile5(String path) {
        DataInputStream dis = new DataInputStream(utilsobj.getResourceAsStream(path));
        StringBuffer strBuff = new StringBuffer();
        int ch = 0;
        try {
            while ((ch = dis.read()) != -1) {
                //System.out.println("а".toCharArray()[0]);
                //System.out.println(ch);
                //System.out.println((char ) ((ch >= 0xc0 && ch <= 0xFF) ? (ch + 0x350) : ch));
                strBuff.append((char ) ((ch >= 0xc0 && ch <= 0xFF) ? (ch + 0x350) : ch));
            }
            dis.close();
        } catch (Exception e) {
            System.err.println("ERROR in getText() " + e);
        }
        return strBuff.toString();
}
    public static String ReadFile4(String resName)
    {
        try
        {
            String[] text;
    InputStream in = utilsobj.getResourceAsStream(resName);
    try {
        DataInputStream din = new DataInputStream(in);
        int size = din.readShort();
        text = new String[size];
        for (int i = 0; i < size; i++) {
            text[i] = din.readUTF();
        }
    } finally {
        in.close();
    }
        //build final
        String ret = "";
        for(int i=0;i<text.length;i++)
        {
            ret+=text[i];
        }
    return ret;
        }
        catch(Exception e)
        {
            return "";
        }
    }
    public String ReadFile(String file)
    {
        /*if(file.startsWith("/"))
        {
            file = file.substring(1);
        }*/
        StringBuffer buffer = null;
 InputStream is = null;
 InputStreamReader isr = null;
 try {
     
     //System.out.println(file);
      Class myClass;
      myClass = Class.forName("rhynn.FantasyWorlds"); 
   is = myClass.getResourceAsStream(file);
   if (is == null)
   {
       is = myClass.getResourceAsStream("/assets"+file);
       if(is == null)
       {
            System.out.println("File Does Not Exist");
            return "";
       }
   }

   isr = new InputStreamReader(is,"UTF-8");

   buffer = new StringBuffer();
   int ch;
   while ((ch = isr.read()) > -1) {
     buffer.append((char)ch);
   }
   if (isr != null)
     isr.close();
 } catch (Exception ex) {
   System.out.println(ex);
 }
 return buffer.toString();
}
    public static String ReadFile2(String file)
    {
        try
        {
                String content = "";
                Reader in = new InputStreamReader(utilsobj.getResourceAsStream(file), "UTF-8");
            StringBuffer temp = new StringBuffer(1024);
            char[] buffer = new char[1024];
            int read;
            while ((read=in.read(buffer, 0, buffer.length)) != -1)
            {
                temp.append(buffer, 0, read);
            }
            content = temp.toString();
            return content;
        }
        catch(Exception ex)
        {
            return "";
        }
    }
    
    public static String[] Split(String splitStr, String delimiter) {
     StringBuffer token = new StringBuffer();
     Vector tokens = new Vector();
     // split
     char[] chars = splitStr.toCharArray();
     for (int i=0; i < chars.length; i++) {
         if (delimiter.indexOf(chars[i]) != -1) {
             // we bumbed into a delimiter
             if (token.length() > 0) {
                 tokens.addElement(token.toString());
                 token.setLength(0);
             }
         } else {
             token.append(chars[i]);
         }
     }
     // don't forget the "tail"...
     if (token.length() > 0) {
         tokens.addElement(token.toString());
     }
     // convert the vector into an array
     String[] splitArray = new String[tokens.size()];
     for (int i=0; i < splitArray.length; i++) {
         splitArray[i] = (String)tokens.elementAt(i);
     }
     return splitArray;
 }
    
}
/*
 * gfxFont.java
 * тест
 * Created on 19. Mai 2003, 16:28
 */


package graphics;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import FWUtils.FWGBridge;



/**
 *
 * @author  Jochai Papke (Jochai.Papke@AwareDreams.com)
 */
public class GFont {

    /////////////////////
    //// MEMBER VARS ////
    /////////////////////
    
    /** The image to use for the font. */
    public Image fontImg=null;

    /** Number of chars the font can display. */
    public int noOfChars;

    /** Width of a single char. */
    public int charWidth = 5;

    /** Height of a single char. */    
    public int charHeight = 6;
    
    private int cols;
    
    private int i,j;
    
    /** Table that maps each char to its index as defined by the image 
     (e.g. if 'a' is the 4th letter in your image fontCharTable['a']==3 should be true). 
     */
    public int[] fontCharTable = new int[10000];//256
    

    
    ////////////////////////
    //// MEMBER METHODS ////
    ////////////////////////
    
    /** Creates a new instance of gfxFont */
    public GFont(Image fontImg, int noOfChars, int charWidth, int charHeight, int spaceID) {
        
        FWGBridge br = new FWGBridge();
        String[] font_desc = FWGBridge.Split(br.ReadFile("/langs/"+FWGBridge.currentLang+"_descr.txt"), " ");
        //72,5,6,26
        noOfChars = Integer.parseInt(font_desc[1]);
        charWidth = Integer.parseInt(font_desc[2]);
        charHeight = Integer.parseInt(font_desc[3]);
        spaceID = Integer.parseInt(font_desc[4]);
        
        this.fontImg = fontImg;
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.noOfChars = noOfChars;

        cols = this.fontImg.getWidth() / charWidth;
        
        //default all characters to space tile
        for (int i=0; i<256; i++) {
            fontCharTable[i]=spaceID;
        }
    }
    
    /** Draws a char on a given graphics position at the requested position. */
    public void drawChar(Graphics g, char c, int xPos, int yPos) {
        if (g!=null && fontImg!=null) {
            //draw the char
            g.setClip(xPos, yPos, charWidth, charHeight);

            /*if (c > 255) {
             */
            if (c > 9999) {
               // we assume that byte conversion to char has changed the char value from negative byte to 1-padded char
                // 1111 1100 byte becomes 1111 1111 1111 1100 -> 1 padding
                // 0111 1100 however becomes 0000 0000 0111 1100 -> 0 padding
                // note char is always unsigned, thus never negative
                c = (char)(((int)c) & 0x00ff);
                //c = (char)(c % 255);
            }
            
            try {
                g.drawImage(fontImg, xPos-((fontCharTable[c]%cols)*charWidth), yPos-((fontCharTable[c]/cols)*charHeight), Graphics.LEFT | Graphics.TOP);
           } catch (Exception e) {
           }
        }
    }

    /** Draws a char on a given graphics position at the requested position. */
    public void drawChar(Graphics g, char c, int xPos, int yPos, int maxClipX, int maxClipY) {
        if (g!=null && fontImg!=null) {
             if (c > 9999) {
               // we assume that byte conversion to char has changed the char value from negative byte to 1-padded char
                // 1111 1100 byte becomes 1111 1111 1111 1100 -> 1 padding
                // 0111 1100 however becomes 0000 0000 0111 1100 -> 0 padding
                // note char is always unsigned, thus never negative
                c = (char)(((int)c) & 0x00ff);
                //c = (char)(c % 255);
            }
             
            //draw the char
            g.setClip(xPos, yPos, maxClipX, maxClipY);
            g.drawImage(fontImg, xPos-((fontCharTable[c]%cols)*charWidth), yPos-((fontCharTable[c]/cols)*charHeight), Graphics.LEFT | Graphics.TOP);
        }
    }

 
    
    /** Draws a complete string on a Graphics context at the requested position. */
    public void drawString(Graphics g, String s, int xPos, int yPos) {
        if (g!=null && fontImg!=null && s!=null) {
            //draw the chars
            for (i=0, j=0; i<s.length(); i++, j+=charWidth) {
                drawChar(g, s.charAt(i), xPos + j, yPos);
                
            }
        }
    }

    /** Draws a complete string on a Graphics context at the requested position. */
    public void drawString(Graphics g, String s, int xPos, int yPos, int visibleWidth, int visibleHeight) {
        if (g!=null && fontImg!=null && s!=null) {
            //draw the chars
            if (visibleHeight > charHeight)
                visibleHeight = charHeight;
            for (i=0, j=0; i<s.length(); i++, j+=charWidth) {
                if (j > xPos + visibleWidth) {
                    break;
                } else if (j + charWidth > visibleWidth) {
                    drawChar(g, s.charAt(i), xPos + j, yPos, visibleWidth - j, visibleHeight);
                } else {
                    drawChar(g, s.charAt(i), xPos + j, yPos, charWidth, visibleHeight);
                }
            }
        }
    }
    

    
    

    /** Draws a complete string on a Graphics context at the requested position. */
    public void drawString(Graphics g, char[] s, int xPos, int yPos) {
        if (g!=null && fontImg!=null && s!=null) {
            //draw the chars
            for (i=0; i<s.length; i++) {
                drawChar(g, s[i], xPos + i*charWidth, yPos);
            }
        }
    }
    

    /** Draws a complete string on a Graphics context at the requested position. */
    public void drawString(Graphics g, char[] s, int xPos, int yPos, int visibleWidth, int visibleHeight) {
        drawString(g, s, xPos, yPos, visibleWidth, visibleHeight, 0, 0);
    }

    /** Draws a complete string on a Graphics context at the requested position. */
    public void drawString(Graphics g, char[] s, int xPos, int yPos, int visibleWidth, int visibleHeight, int startPos, int charXSpacing) {
        if (g!=null && fontImg!=null && s!=null) {
            //draw the chars
            if (visibleHeight > charHeight)
                visibleHeight = charHeight;
            for (i=startPos, j=0; i<s.length; i++, j+=(charWidth + charXSpacing)) {
                if (j > xPos + visibleWidth) {
                    break;
                } else if (j + charWidth > visibleWidth) {
                    drawChar(g, s[i], xPos + j, yPos, visibleWidth - j, visibleHeight);
                } else {
                    drawChar(g, s[i], xPos + j, yPos, charWidth, visibleHeight);
                }
            }
        }
    }    
    
    
    /** Sets the default mapping of the Alphabet a-z,A-Z, digits 0-9 and special chars
     the fontCharTable array assigns each ASCII char its index in the image 
     all chars that are not assigned are automatically mapped to the space index 
     used in the image (and which is specified when calling the constructor. */            
    public void setFontCharTableDefaults(boolean specialChars) {
        setDefaultSmallAlphabet(0);
        setDefaultBigAlphabet(0);
        setDefaultDigits(27);
        int index = 60;
        if (specialChars) {
            setDefaultSpecialChars(37);
            //index = 60;
        }
        setDefaultLangSpec(index);
    }

   
    /** Sets the default mapping of the big Alphabet A-Z
     the fontCharTable array assigns each ASCII char its index in the image 
     all chars that are not assigned are automatically mapped to the space index 
     used in the image (and which is specified when calling the constructor. */            
     public void setDefaultBigAlphabet(int indexOf_A) {
        for (i=0; i<26; i++) {
            fontCharTable['A'+i] = indexOf_A + i;
        }
    }


    /** Sets the default mapping of the small Alphabet a-z
     the fontCharTable array assigns each ASCII char its index in the image 
     all chars that are not assigned are automatically mapped to the space index 
     used in the image (and which is specified when calling the constructor. */            
    public void setDefaultSmallAlphabet(int indexOf_a) {
        for (i=0; i<26; i++) {
            fontCharTable['a'+i] = indexOf_a + i;
        }
    }

    
    /** Sets the default mapping of the digits 0-9
     the fontCharTable array assigns each ASCII char its index in the image 
     all chars that are not assigned are automatically mapped to the space index 
     used in the image (and which is specified when calling the constructor. */            
    public void setDefaultDigits(int indexOf_0) {
        for (i=0; i<10; i++) {
            fontCharTable['0'+i] = indexOf_0 + i;
        }
    }
    
    /** for other languages */
    public void setDefaultLangSpec(int indexOf_all) {
        //32 should be removed with max language specific letters count
        //ok now i did it for all languages
        if(FWGBridge.currentLang != "english")
        {
            FWGBridge br = new FWGBridge();
            //String[] mtest = FWGBridge.Split(FWGBridge.ReadFile("/langs/"+FWGBridge.currentLang+"_stringtable.txt"),"\r\n");
            //System.out.println(mtest[0]+"()"+mtest[1]);
            String splitme = br.ReadFile("/langs/"+FWGBridge.currentLang+"_lettertable_little.txt");
            //System.out.println(splitme);
            String[] small = FWGBridge.Split(splitme, " ");
            for(int i=0;i<small.length;i++)
            {
                try
                {
                    //System.out.println(small[i].trim()+";");
                    fontCharTable[small[i].trim().toCharArray()[0]] = indexOf_all + i;
                }
                catch(Exception e)
                {
                    //e.printStackTrace();
                }
            }
            String[] big = FWGBridge.Split(br.ReadFile("/langs/"+FWGBridge.currentLang+"_lettertable_big.txt"), " ");
            for(int i=0;i<big.length;i++)
            {
                try
                {
                    fontCharTable[big[i].toCharArray()[0]] = indexOf_all + i;
                }
                catch(Exception e)
                {
                    
                }
            }
        }
    }

    /** Sets the default mapping of the special chars, 
     the fontCharTable array assigns each ASCII char its index in the image 
     all chars that are not assigned are automatically mapped to the space index 
     used in the image (and which is specified when calling the constructor. */            
    public void setDefaultSpecialChars(int indexOf_Point) {
        fontCharTable['.']=0 + indexOf_Point;
        fontCharTable[':']=1 + indexOf_Point;
        fontCharTable[',']=2 + indexOf_Point;
        fontCharTable[';']=3 + indexOf_Point;
        fontCharTable['?']=4 + indexOf_Point;
        fontCharTable['!']=5 + indexOf_Point;
        fontCharTable['(']=6 + indexOf_Point;
        fontCharTable[')']=7  + indexOf_Point;
        fontCharTable['+']=8 + indexOf_Point;
        fontCharTable['-']=9 + indexOf_Point;
        fontCharTable['*']=10 + indexOf_Point;
        fontCharTable['/']=11 + indexOf_Point;
        fontCharTable['=']=12  + indexOf_Point;
        fontCharTable['\'']=13 + indexOf_Point;
        fontCharTable['_']=14 + indexOf_Point;
        fontCharTable['\\']=15 + indexOf_Point;
        fontCharTable['#']=16 + indexOf_Point;
        fontCharTable['[']=17 + indexOf_Point;
        fontCharTable[']']=18 + indexOf_Point;
        fontCharTable['@']=19 + indexOf_Point;
        fontCharTable['ä']=20 + indexOf_Point;
        fontCharTable['ö']=21 + indexOf_Point;
        fontCharTable['ü']=22 + indexOf_Point;
        fontCharTable['Ä']=fontCharTable['ä'];
        fontCharTable['Ö']=fontCharTable['ö'];
        fontCharTable['Ü']=fontCharTable['ü'];
    }

}

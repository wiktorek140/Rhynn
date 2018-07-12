/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rhynn;

import graphics.GTools;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author STeeL
 */
public class ConsoleKeyboard extends Canvas {
    int     w, h;
    Font    font;
    
    int BaseSqW, BaseSqH;
    int startX, startY;
    public boolean status = false;
    
    String _final_str = "";
    
    //abcdefghijklmnopqrstuvwxyz
    String[] keyboard = {
        "0","1","2","3","4","5","6","7","8","9",
        "q","w","e","r","t","y","u","i","o","p",
        "a","s","d","f","g","h","j","k","l","*",
        "z","x","c","v","b","n","m",".","!","@",        
        "#","_","^","(",")","-","+","spc","bck","ent"
    };
    
    int CurrentKey = 0;
        
    ConsoleKeyboard() {        
        w = getWidth();
        h = getHeight();
        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        
        BaseSqW = 70;//65
        BaseSqH = 60;//55
        
        //10 in cell line,x4 cells
        int maxw = BaseSqW * 10;
        int maxh = BaseSqH * 5;
        startX = w/2 - maxw/2;
        startY = h/2 - maxh/2;
        
        this.setFullScreenMode(true);
     }
    int lastHandled = -5;
    protected void keyReleased(int key) {
        lastHandled = 0;
    }
    protected void keyPressed(int key) {
        if(lastHandled == key)
        {
            return;
        }
        lastHandled = key;
        if(key==-1)
        {
            //up
            int curK = CurrentKey;
            /*if(curK<10)
            {
                CurrentKey = 30+curK;
            }
            else if(curK<20)
            {
                CurrentKey = curK-10;
            }
            else if(curK<30)
            {
                CurrentKey = curK-20;
            }
            else if(curK<40)
            {
                CurrentKey = curK-30;
            }*/
            if(curK>=10)
            {
                CurrentKey = curK-10;
            }
        }
        else if(key==-2)
        {
            //down
            int curK = CurrentKey;
            if(curK<40)
            {
                CurrentKey = curK+10;
            }
        }
        else if(key==-3)
        {
            //left
            if(CurrentKey>0)
            {
                CurrentKey -=1;
            }
        }
        else if(key==-4)
        {
            //right
            if(CurrentKey<49)
            {
                CurrentKey +=1;
            }
        }
        else if(key==-5)
        {
            //fire
            if(keyboard[CurrentKey]=="bck")
            {
                try
                {
                    _final_str = _final_str.substring(0,_final_str.length()-1);
                }
                catch(Exception e)
                {
                    _final_str = "";
                }
            }
            else if(keyboard[CurrentKey]=="ent")
            {
                //change back to game
                status = true;
                try
                {
                    if(Storage.fwgstor.CurInput==0)
                    {
                        Storage.fwgstor.clientName=_final_str; 
                        GTools.textWindowSetText(Storage.fwgstor.usernameWindow, _final_str);
                    }
                    else if(Storage.fwgstor.CurInput==1)
                    {
                        Storage.fwgstor.clientPass=_final_str;
                        GTools.textWindowSetText(Storage.fwgstor.passwordWindow, _final_str);
                    }
                    else if(Storage.fwgstor.CurInput==2)
                    {
                            GTools.textWindowSetText(Storage.fwgstor.emailField1, _final_str); 
                    }
                    else if(Storage.fwgstor.CurInput==3)
                    {
                            GTools.textWindowSetText(Storage.fwgstor.emailField2, _final_str);
                    }
                    else if(Storage.fwgstor.CurInput==4)
                    {
                            GTools.textWindowSetText(Storage.fwgstor.editBoxInput, _final_str);
                    }
                    else if(Storage.fwgstor.CurInput==5)
                    {
                            GTools.textWindowSetText(Storage.fwgstor.inputChatWindow, _final_str);
                    }
                    Storage.fwgstor.display.setCurrent(Storage.fwgstor);
                }
                catch(Exception e)
                {
            
                }
            }
            else if(keyboard[CurrentKey]=="spc")
            {
                _final_str += " ";
            }
            else
            {
                _final_str += keyboard[CurrentKey];
            }
        }
        
        repaint();
    }
    
    protected void paint(Graphics g) {
        g.setColor(255,255,255);
        g.fillRect(0, 0, w, h);
        g.setFont(font);
        
        g.setColor(0, 0, 0);
        g.drawString(_final_str, 10, 10, Graphics.TOP|Graphics.LEFT);
        
        int yr = startY;
        int s = 0;
        for(int i=0;i<5;i++)
        {
            for(int i2=0;i2<10;i2++)
            {
                int xr = startX + BaseSqW * (i2);
                if(CurrentKey==s)
                {
                    g.setColor(180, 205, 205);
                    g.fillRect(xr, yr, BaseSqW, BaseSqH);
                }
                else
                {
                    g.setColor(0,0,0);
                    g.drawRect(xr, yr, BaseSqW, BaseSqH);
                }                        
                
                
                g.setColor(0,0,0);
                g.drawString(keyboard[s], xr+10, yr+10, Graphics.TOP|Graphics.LEFT);
                s++;
            }
            yr = startY + BaseSqH * (i+1);
        }
    }
}

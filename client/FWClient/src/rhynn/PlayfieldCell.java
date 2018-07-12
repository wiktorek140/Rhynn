package rhynn;




import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marlowe
 */
public class PlayfieldCell {
    public static final int function_none = 0x0;
	public static final int function_blocked = 0x1;
	public static final int function_peaceful = 0x2;
	public static final int function_reserved1 = 0x4;
	public static final int function_reserved2 = 0x8;

    public static final int trigger_none = 0x0;
	public static final int trigger_default = 0x1;

    public static final int defaultWidth = 24;
    public static final int defaultHeight = 24;

    private int tilesetIndex = 0;
    private int tileIndex = 0;
    private int functionValue = 0;
    private int triggerValue = 0;

    private Image backgroundImage = null;
    private int imgClipX = 0;
    private int imgClipY = 0;
    
    public int DrawX = 0;
    public int DrawY = 0;

    public PlayfieldCell() {
    }

    public void setGraphicsInfo(Image img, int imgClipX, int imgClipY) {
        backgroundImage = img;
        this.imgClipX = imgClipX;
        this.imgClipY = imgClipY;
    }

    public void drawToRect(Graphics g, int x, int y, int screenX, int screenY, int rectWidth, int rectHeight,int cellX,int cellY) {
        int curWidth = defaultWidth;
        int curHeight = defaultHeight;
        int xDraw = screenX + x;
        int yDraw = screenY + y;

        // adjust the clipping region if part of the cell is out of the screen bounds
        if (x < 0) {
            curWidth += x;
            x = 0;
        } else if (x + defaultWidth > rectWidth) {
            curWidth = rectWidth - x;
        }
        if (y < 0) {
            curHeight += y;
            y = 0;
        } else if (y + defaultHeight > rectHeight) {
            curHeight = rectHeight - y;
        }

        g.setClip(screenX + x, screenY + y, curWidth, curHeight);        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, xDraw-imgClipX, yDraw-imgClipY, Graphics.TOP | Graphics.LEFT);
            String key2 = ""+String.valueOf(cellX)+String.valueOf(cellY)+"pid";
            if(Storage.fwgstor.firewalls.get(""+String.valueOf(cellX)+String.valueOf(cellY))!=null && Storage.fwgstor.firewalls.get(key2).toString()==Storage.fwgstor.playfieldName)
            {
                //draw firewall phase from 0 to 3
                String tkey = ""+String.valueOf(cellX)+String.valueOf(cellY)+"time";
                String key = ""+String.valueOf(cellX)+String.valueOf(cellY);
                int stage = Integer.parseInt(Storage.fwgstor.firewalls.get(key).toString());
                int time = Integer.parseInt(Storage.fwgstor.firewalls.get(tkey).toString());
                int sbak = stage;
                if(Storage.fwgstor.curGametime>time+200)
                {
                    sbak+=1;
                    if(sbak>3)
                    {
                        sbak = 0;
                    }
                    Storage.fwgstor.firewalls.remove(key);
                    Storage.fwgstor.firewalls.put(key, String.valueOf(sbak));
                    Storage.fwgstor.firewalls.remove(tkey);
                    Storage.fwgstor.firewalls.put(tkey, String.valueOf(Storage.fwgstor.curGametime));
                }
                //int s = stage * 7;
                //g.setClip(s, 60, 7, 7);
                //g.setClip(screenX+s, screenY+60, 7, 7);                
                //g.drawImage(GlobalResources.imgIngame, xDraw-imgClipX, yDraw-imgClipY, Graphics.TOP | Graphics.LEFT);
                //int _x = x + screenX;
                //int _y = y + screenY;
                int _x = xDraw;
                int _y = yDraw;
                GlobalResources.getFirewall(stage).draw(g, _x + 4, _y);
                GlobalResources.getFirewall(stage).draw(g, _x + 4 + 9, _y);
                GlobalResources.getFirewall(stage).draw(g, _x + 4 + 4, _y + 9);
            }
                
        } else {
            g.setColor(0,0,128);
            g.fillRect(x, y, curWidth, curHeight);
        }
        this.DrawX = x;
        this.DrawY = y;
        
        /*int xPosRelDraw = xDraw-imgClipX;
        int yPosRelDraw = yDraw-imgClipY;
         boolean draw = true;
                   if(cellX==Storage.fwgstor.blockTriggerX && cellY==Storage.fwgstor.blockTriggerY)
                   {
                        if (Storage.fwgstor.blockDuration > 0)
                        {
                            draw = false;
                            g.setClip(xPosRelDraw+10, yPosRelDraw+26, 6, 11);                           
                            g.drawImage(GlobalResources.imgIngame, xPosRelDraw+10-41, yPosRelDraw +26 - 12, Storage.fwgstor.anchorTopLeft);   // blocking trigger flash
                        }
                        else
                        {
                            //ONLY DEBUG!!
                            Storage.fwgstor.waitingForTrigger = false;
                        }
                   }
                   if(hasTrigger(15) && draw)
                   {
                       g.setClip(screenX + x, screenY + y, curWidth, curHeight);   
                       Storage.fwgstor.drawTriggerFlash(xPosRelDraw, yPosRelDraw+25-3);
                   }    */               
    }

    public void setBaseValues(int functionValue, int triggerValue, int tilesetIndex, int tileIndex) {
        this.tilesetIndex = tilesetIndex;
        this.tileIndex = tileIndex;
        this.functionValue = functionValue;
        this.triggerValue = triggerValue;
    }

    public void addFunction(int newFunction) {
        this.functionValue |= newFunction;
    }

    public int getTilesetIndex() {return tilesetIndex;}
    public int getTileIndex() {return tileIndex;}

    public boolean hasFunction(int checkFunctionValue) {
        return (functionValue & checkFunctionValue) == checkFunctionValue;
    }

    public boolean hasTrigger(int checkTriggerValue) {
        return (triggerValue & checkTriggerValue) == checkTriggerValue;
    }

    /*
    public void setTileInfo(int data) {}
    public void setFunction(int data) {}
    public void setTrigger(int data) {}
     */

}

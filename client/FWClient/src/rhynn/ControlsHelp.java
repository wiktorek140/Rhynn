/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rhynn;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author STeeL
 */
public class ControlsHelp extends Canvas {
    Image image = null;
    int screenWidth,screenHeight;
    ControlsHelp()
    {
        setFullScreenMode(true);
        screenWidth = getWidth();
        screenHeight = getHeight();
        try
        {
            image = Image.createImage("/logo_ouya.png");
        }
        catch(Exception e)
        {
            
        }
        image = resizeImage(image);
    }
    private Image resizeImage(Image src) {
      int srcWidth = src.getWidth();
      int srcHeight = src.getHeight();
      Image tmp = Image.createImage(screenWidth, srcHeight);
      Graphics g = tmp.getGraphics();
      int ratio = (srcWidth << 16) / screenWidth;
      int pos = ratio/2;

      //Horizontal Resize        

      for (int x = 0; x < screenWidth; x++) {
          g.setClip(x, 0, 1, srcHeight);
          g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
          pos += ratio;
      }

      Image resizedImage = Image.createImage(screenWidth, screenHeight);
      g = resizedImage.getGraphics();
      ratio = (srcHeight << 16) / screenHeight;
      pos = ratio/2;        

      //Vertical resize

      for (int y = 0; y < screenHeight; y++) {
          g.setClip(0, y, screenWidth, 1);
          g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
          pos += ratio;
      }
      return resizedImage;

  }
    int lastHandled = -5;
    protected void keyReleased(int key) {
        lastHandled = -5;
    }
    protected void keyPressed(int key) {
        if(lastHandled == key)
        {
            return;
        }
        Storage.fwgstor.display.setCurrent(Storage.fwgstor);
    }
    protected void paint(Graphics g) {
        g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
    }
}

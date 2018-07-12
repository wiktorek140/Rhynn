package rhynn;




import graphics.GImageClip;
import javax.microedition.lcdui.Image;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marlowe
 */
public class GlobalResources {
    public static Image imgIngame = null;

    public final static int SPRITE_ICON_ATTACK = 1;
    public final static int SPRITE_ICON_DEFEND = 2;

    public final static int SPRITE_DEAD = 64;
    
    public final static int SPRITE_ICON_SHEAL = 3;
    public final static int SPRITE_ICON_SATTACK = 4;
    
    public final static int SPRITE_ICON_SDOWNGRADE = 5;
    public final static int SPRITE_ICON_SUPGRADE = 6;

    public static void init() {
        try {
            imgIngame = Image.createImage("/ingame.png");
        } catch (Exception e) {}
    }

    public static GImageClip getImageClip(int imageType) {
        switch (imageType) {
            case SPRITE_ICON_ATTACK:
                return new GImageClip(imgIngame, 32, 16, 9, 7);
            case SPRITE_ICON_DEFEND:
                return new GImageClip(imgIngame, 32, 0, 9, 7);
            case SPRITE_DEAD:
                return new GImageClip(imgIngame, 41, 0, 10, 12);
            case SPRITE_ICON_SATTACK:
                return new GImageClip(imgIngame, 14, 16, 9, 12);
            case SPRITE_ICON_SHEAL:
                return new GImageClip(imgIngame, 23, 16, 9, 12);
            case SPRITE_ICON_SDOWNGRADE:
                return new GImageClip(imgIngame, 0, 67, 7, 7);
            case SPRITE_ICON_SUPGRADE:
                return new GImageClip(imgIngame, 7, 67, 7, 7);
        }

        return null;
    }
    public static GImageClip getFirewall(int id) {
        int s1 = 7 * id;
        return new GImageClip(imgIngame,s1,60,7,7);
    }

    

}

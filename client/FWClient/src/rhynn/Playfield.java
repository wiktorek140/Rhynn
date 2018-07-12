package rhynn;



import java.util.Enumeration;
import java.util.Hashtable;
//import java.util.Vector;
import java.util.Vector;
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
public class Playfield {
    public static int s_VisibilityCellRange = 5;
    public static int s_VisibilityCellRangeWSize = (s_VisibilityCellRange*2)+1;
            
    private static final int bytesPerCell = 2;


    private Hashtable characters = new Hashtable();
    private Hashtable items = new Hashtable();
    private Hashtable tilesetsLoaded = new Hashtable();

    private PlayfieldCell[][] data = null;
    private String name;
    private int writeIndex = 0;
    private int numCellsX = 0;
    private int numCellsY = 0;
    private int width = 0;
    private int height = 0;

    private PlayfieldObserver observer = null;

    public void setObserver(PlayfieldObserver obs) {
        observer = obs;
    }

    //private int lastSelectedCharacterId = 0;


    public Playfield(String name, int numCellsX, int numCellsY) {
        data = new PlayfieldCell[numCellsX][numCellsY];
        for (int h = 0; h<numCellsY; h++) {
            for (int w = 0; w<numCellsX; w++) {
                data[w][h] = new PlayfieldCell();
            }
        }        
        this.name = name;
        this.numCellsX = numCellsX;
        this.numCellsY = numCellsY;
        this.width = numCellsX * PlayfieldCell.defaultWidth;
        this.height = numCellsY * PlayfieldCell.defaultHeight;
        //System.out.println("PF data:"+name+",numCellsX="+numCellsX+",numCellsY="+numCellsY+",w="+width+",h="+height);
    }

    public boolean setNextCellBaseValues(byte[] rawData, int offset) {

        int numBytes = rawData.length - offset;

        //System.out.println("setting numBytes: " + numBytes);

        for (int i=0; i<numBytes; i+=bytesPerCell) {
            int functionByte = (int)(rawData[i+offset] & 0xFF);
            int tilesetByte = (int)(rawData[i+offset+1] & 0xFF);
            if (!setNextCellBaseValue(functionByte, tilesetByte)) {
                return false;
                //return true;
            }
        }
        return true;
    }

    public boolean setNextCellBaseValue(int triggerAndFunctionData, int tilesetData) {

        if (writeIndex < numCellsX*numCellsY) {
            int y = (int)(writeIndex / numCellsX);
            int x = (writeIndex % numCellsX);
            if (setCellBaseValueAt(x, y, triggerAndFunctionData, tilesetData)) {
                writeIndex++;                
                return true;
            }
        }
        //System.out.println("bad :( 1");
        return false;
    }

    public boolean setCellBaseValueAt(int x, int y, int triggerAndFunctionData, int tilesetData) {
        int tilesetIndex = ((tilesetData & 0xE0) >> 5);
        int tileIndex = (tilesetData & 0x1F);
        int trigger = ((triggerAndFunctionData & 0xF0) >> 4);
        int function = (triggerAndFunctionData & 0x0F);

        //if(trigger>0)
        //{
        //        System.out.println(trigger+"=="+function);
        //}//thats ok
        
        if (tilesetIndex >= 0 && tilesetIndex < tilesetsLoaded.size()) {
            Tileset tileset = (Tileset)tilesetsLoaded.get(new Integer(tilesetIndex));
            data[x][y].setBaseValues(function, trigger, tilesetIndex, tileIndex);
            if (tileset != null) {
                data[x][y].setGraphicsInfo(tileset.tilesetImage, tileset.clipX(tileIndex), tileset.clipY(tileIndex));
            } else {
                data[x][y].setGraphicsInfo(null, 0, 0);
            }
            return true;
        }
        //System.out.println("bad :( 2");
        return false;//origin false
    }


    public void addTileset(int index, int graphicId, Image img) {
        tilesetsLoaded.put(new Integer(index), new Tileset(graphicId, img));
    }



    public Tileset nextUnloadedTileset() {
        Enumeration e = tilesetsLoaded.keys();
        
        while(e.hasMoreElements()) {
            Integer index = (Integer)e.nextElement();
            Tileset ts = (Tileset)tilesetsLoaded.get(index);
            if (ts.tilesetImage == null) {
                return ts;
            }
        }
        return  null;
    }

    //public void clearTilesets()
    public int getLoadedCellCount() {
        return writeIndex;
    }
    public int getCellCount() {
        return numCellsX*numCellsY;
    }

    public int getNumCellsY() {
        return numCellsY;
    }

    public int getNumCellsX() {
        return numCellsX;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void removeCharacter(Character c) {
        characters.remove(new Integer(c.objectId));
    }

    public void removeCharacter(int id) {
        characters.remove(new Integer(id));
        if (observer != null) {
            observer.onCharacterRemoved(id);
        }
    }

    public void addCharacter(Character c) {
        Integer key = new Integer(c.objectId);
        if (characters.contains(key)) {
            characters.remove(key);
        }
        characters.put(key, c);
    }

    public void addItem(Item it) {
        Integer key = new Integer(it.objectId);
        if (items.contains(key)) {
            items.remove(key);
        }
        items.put(key, it);
    }

    public void removeItem(int id) {
        items.remove(new Integer(id));
    }

    public Character getCharacter(int objectId) {
        try
        {
            Character c = (Character)characters.get(new Integer(objectId));
            return c;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public Hashtable getCharacters() {
        return characters;
    }

    public Hashtable getItems() {
        return items;
    }


    public Item getItem(int objectId) {
        Item it = (Item)items.get(new Integer(objectId));
        return it;
    }

    public Item getClosestItem(Character c) {
        int distX;
        int distY;
        int shortest=-1;

        Item fwgo = null;
        Item local = null;
        Enumeration e = items.elements();
        int radiusSquared = Character.ITEM_PICKUP_RADIUS*Character.ITEM_PICKUP_RADIUS;
        int halfDimC = c.graphicsDimHalf();
        while(e.hasMoreElements()) {
            fwgo = (Item)e.nextElement();
            int halfDim = fwgo.graphicsDimHalf();
            distX = (fwgo.x + halfDim - (c.x + halfDimC));
            distY = (fwgo.y + halfDim - (c.y + halfDimC));
            if (distX * distX + distY * distY < radiusSquared
            &&  (distX * distX + distY * distY < shortest || shortest==-1)
            ){
                local = fwgo;
                shortest = distX * distX + distY * distY;
            }
        }

        return local;
    }

    public boolean isBlockedLine(int x1, int y1, int x2, int y2) {
        // avoid division by zero
        int xDistance = x1 - x2;
        int yDistance = y1 - y2;


        if (xDistance==0) {xDistance=1;}
        if (yDistance==0) {yDistance=1;}

        int tx, ty;
        int totalX = 0;
        int totalY = 0;
        int xExtra = 0;
        int yExtra = 0;
        int xStep = PlayfieldCell.defaultWidth;
        int yStep = PlayfieldCell.defaultHeight;
        int yOther = 0;
        int xOther = 0;

        // make sure tracing is done in correct directions
        if (xDistance > 0) {xStep *= -1;}
        if (yDistance > 0) {yStep *= -1;}


        if (xDistance*xDistance > yDistance*yDistance) {
            yStep = ((yDistance * 100) / xDistance) * xStep;    // gradient of direct line * 100
            yOther = yStep % 100;   // y line correction value, 2 decimal precision
            yStep = yStep / 100;    // rounded number of pixels in y per one xStep
        } else if (yDistance*yDistance > xDistance*xDistance) {
            xStep = ((xDistance * 100) / yDistance) * yStep;    // gradient of direct line * 100
            xOther = xStep % 100;   // y line correction value, 2 decimal precision
            xStep = xStep / 100;    // rounded number of pixels in y per one xStep
        }

        while (totalX*totalX < xDistance*xDistance && totalY*totalY < yDistance*yDistance) {

            xExtra += xOther;
            yExtra += yOther;
            if (xExtra*xExtra >= 10000) {totalX += xExtra/100; xExtra = xExtra%100;}
            if (yExtra*yExtra >= 10000) {totalY += yExtra/100; yExtra = yExtra%100;}

            totalX += xStep;
            totalY += yStep;

            if (totalX*totalX > xDistance * xDistance) {totalX = -xDistance;}
            if (totalY*totalY > yDistance * yDistance) {totalY = -yDistance;}


            tx = (x1 + totalX); // / PlayfieldCell.defaultWidth;
            ty = (y1 + totalY); // / PlayfieldCell.defaultHeight;
            if (tx>=0 && tx<width && ty>=0 && ty<height && hasFunctionAt(tx, ty, PlayfieldCell.function_blocked))
            {
                return true;
            }
        }
        return false;
    }


    public boolean hasFunctionAt(int function, int xPosPx, int yPosPx) {
        int cellX = (int)(xPosPx / PlayfieldCell.defaultWidth);
        int cellY = (int)(yPosPx / PlayfieldCell.defaultHeight);

        if (cellX < numCellsX && cellY < numCellsY) {
            return data[cellX][cellY].hasFunction(function);
        }
        return false;
    }
    public boolean hasTriggerAt(int function, int xPosPx, int yPosPx) {
        int cellX = (int)(xPosPx / PlayfieldCell.defaultWidth);
        int cellY = (int)(yPosPx / PlayfieldCell.defaultHeight);

        if (cellX < numCellsX && cellY < numCellsY) {
            return data[cellX][cellY].hasTrigger(function);
        }
        return false;
    }

    public void addFunctionForCell(int function, int cellX, int cellY) {
        if (cellX < numCellsX && cellY < numCellsY) {
            data[cellX][cellY].addFunction(function);
        }
    }
    public int cellXAt(int xPosPx)
    {
        return (int)(xPosPx / PlayfieldCell.defaultWidth);
    }
    public int cellYAt(int yPosPx)
    {
        return (int)(yPosPx / PlayfieldCell.defaultHeight);
    }
    public PlayfieldCell cellAt(int xPosPx, int yPosPx) {
        int cellX = (int)(xPosPx / PlayfieldCell.defaultWidth);
        int cellY = (int)(yPosPx / PlayfieldCell.defaultHeight);

        if (cellX < numCellsX && cellY < numCellsY) {
            return data[cellX][cellY];
        }
        return null;
    }

    public void draw(Graphics g, int leftPosX, int topPosY, int viewX, int viewY, int viewWidth, int viewHeight) {
        int cellXStart = (int)(leftPosX / PlayfieldCell.defaultWidth);
        int cellYStart = (int)(topPosY / PlayfieldCell.defaultHeight);
        int cellXEnd = (int)((leftPosX+viewWidth) / PlayfieldCell.defaultWidth);
        int cellYEnd = (int)((topPosY+viewHeight) / PlayfieldCell.defaultHeight);

        if (cellXEnd >= numCellsX) cellXEnd = numCellsX-1;
        if (cellYEnd >= numCellsY) cellYEnd = numCellsY-1;

        if (cellXStart >= 0  && cellYStart >= 0) {
            int leftCutOff = leftPosX - (cellXStart * PlayfieldCell.defaultWidth);
            int topCutOff = topPosY - (cellYStart * PlayfieldCell.defaultHeight);

            int xPosRelDraw = -leftCutOff;
            int yPosRelDraw = -topCutOff;

            for (int cellY=cellYStart; cellY<=cellYEnd; cellY++) {
                xPosRelDraw = -leftCutOff;
                for (int cellX=cellXStart; cellX<=cellXEnd; cellX++) {
                   PlayfieldCell cell = data[cellX][cellY];
                   
                   cell.drawToRect(g, xPosRelDraw, yPosRelDraw, viewX, viewY, viewWidth, viewHeight,cellX,cellY);
                   
                   
                   
                   boolean draw = true;
                   int viewX2=0;
                   int viewY2=0;
                   if(FantasyWorldsGame.DISPLAYWIDTH>this.width)
                   {
                       viewX2 = viewX;
                   }
                   if(FantasyWorldsGame.DISPLAYHEIGHT>this.height)
                   {
                       viewY2 = viewY-25;
                   }
                   if(cellX==Storage.fwgstor.blockTriggerX && cellY==Storage.fwgstor.blockTriggerY)
                   {
                        if (Storage.fwgstor.blockDuration > 0)
                        {
                            draw = false;
                            g.setClip(viewX2+xPosRelDraw+10, viewY2+yPosRelDraw+26, 6, 11);                           
                            g.drawImage(GlobalResources.imgIngame, viewX2+xPosRelDraw+10-41, viewY2+yPosRelDraw +26 - 12, Storage.fwgstor.anchorTopLeft);   // blocking trigger flash
                        }
                        else
                        {
                            //ONLY DEBUG!!
                            Storage.fwgstor.waitingForTrigger = false;
                        }
                   }
                   if(cell.hasTrigger(15) && draw)
                   {
                       Storage.fwgstor.drawTriggerFlash(viewX2+xPosRelDraw, viewY2+yPosRelDraw+25-3);
                   }               
                   xPosRelDraw += PlayfieldCell.defaultWidth;
                }
                yPosRelDraw += PlayfieldCell.defaultHeight;
            }

        }
    }

}
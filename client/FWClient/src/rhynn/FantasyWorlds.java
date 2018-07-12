package rhynn;



/*
 * FantasyWorlds.java
 */

import graphics.GTools;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.lcdui.*;
import rhynn.FantasyWorldsGame;
import javax.microedition.midlet.*;
//import javax.microedition.lcdui.Displayable;

/**
 * Main class of the game client.
 *
 * @author  marlowe
 */
public class FantasyWorlds extends MIDlet implements CommandListener,Runnable,ItemCommandListener {
    
    
    /**
     * Thread for updating the screen.
     */
    private Thread                  updateThread;
    
    /**
     * Global current game time.
     */
    private long                    gameTime;

    private long                    lastTime;

    private long                    currentTime;
    
    private long                    timeDiff;
    
    /**
     * Last global game time.
     */
    private long                    lastGameTime;
    
    /**
     * Display of the MIDlet.
     */
    private Display                 display;
    
    /**
     * The Fantasy Worlds game.
     */
    private FantasyWorldsGame       fwg;
    
    /**
     * New input
     */
    public Form form = new Form("OpenRhynn - Enter text");
    public TextField txt=null;
    public Command ok;
    public StringItem btnLogin;
    
    /**
     * Indicates if the FW shutdown was already done properly.
     */
    private boolean                 shutDownDone = false;

    /**
     * Constructor.
     */
    public int id=0;
    public FantasyWorlds() {
        updateThread = null;
        fwg = new FantasyWorldsGame();

        fwg.initCanvas();
                    //fwg.setFullScreenMode(true);
        fwg.mainInputFrm=form;
        
        display = Display.getDisplay(this);
        fwg.display=display;
        display.setCurrent(fwg);
        
        go();
        
        txt=new TextField("", "", 30, TextField.ANY);
        id=form.append(txt);
        ok = new Command("OK", Command.OK, 2);
        //txt.addCommand(ok);
        form.addCommand(ok);
        form.setCommandListener(this);
        //display.setCurrent(form);
        btnLogin = new StringItem(null, "OK");  
        btnLogin.setDefaultCommand(ok);  
        btnLogin.setItemCommandListener(this);  
        form.append(btnLogin);    
    }
    
    public void commandAction(Command c, Displayable d)
    {
        String label = c.getLabel();
        if(label.equals("OK"))
        {
            //showInput();
            BeginKeyHandle();
        }
    }
    
    public void BeginKeyHandle()
    {
        try
        {
            //System.out.println(fwg.CurInput);
        if(fwg.CurInput==0)
        {
            fwg.clientName=txt.getString(); 
            //System.out.println(fwg.clientName);
            if (fwg.clientName!=null) {
                //GTools.textWindowAddText(null, null);
                            GTools.textWindowSetText(fwg.usernameWindow, txt.getString());
            }
            else
            {
                            GTools.textWindowRemoveText(fwg.usernameWindow);
            }  
        }
        else if(fwg.CurInput==1)
        {
            fwg.clientPass=txt.getString();
            if (fwg.clientPass!=null) {
                            GTools.textWindowSetText(fwg.passwordWindow, txt.getString());
            }
            else
            {
                            GTools.textWindowRemoveText(fwg.passwordWindow);
            }  
        }
        else if(fwg.CurInput==2)
        {
            if (txt.getString()!=null) {
                            GTools.textWindowSetText(fwg.emailField1, txt.getString());
            }
            else
            {
                            GTools.textWindowRemoveText(fwg.emailField1);
            }  
        }
        else if(fwg.CurInput==3)
        {
            if (txt.getString()!=null) {
                            GTools.textWindowSetText(fwg.emailField2, txt.getString());
            }
            else
            {
                            GTools.textWindowRemoveText(fwg.emailField2);
            }  
        }
        else if(fwg.CurInput==4)
        {
            if (txt.getString()!=null) {
                            GTools.textWindowSetText(fwg.editBoxInput, txt.getString());
            }
            else
            {
                            GTools.textWindowRemoveText(fwg.editBoxInput);
            }  
        }
        else if(fwg.CurInput==5)
        {
            if (txt.getString()!=null) {
                            GTools.textWindowSetText(fwg.inputChatWindow, txt.getString());
            }
            else
            {
                            GTools.textWindowRemoveText(fwg.inputChatWindow);
            }  
        }
        }
        catch(Exception e)
        {
            
        }
                    try
            {
                form.deleteAll();
                txt=new TextField("", "", 30, TextField.ANY);                
                form.append(txt);
                txt.setString("");
                //txt.addCommand(ok);
                
                btnLogin = new StringItem(null, "OK");  
                btnLogin.setDefaultCommand(ok);  
                btnLogin.setItemCommandListener(this);  
                form.append(btnLogin); 
            }
            catch(Exception ex)
            {
                        
            }
                    
        display.setCurrent(fwg);
    }
    
    protected void startApp() {
    }
    
    protected void pauseApp() {   
    }
    
    public void destroyApp(boolean unconditional) {
        if (!shutDownDone) {
            shutDown();
            shutDownDone = true;
        }
        
        updateThread = null;
        display.setCurrent(null);
        try
        {
            System.exit(0);
        }
        catch(Exception ex)
        {
            
        }
    }
    
    public void go() {
        if(updateThread == null) {
            updateThread = new Thread(this);
            updateThread.setPriority(Thread.MIN_PRIORITY);
        }
        updateThread.start();
    }
    
    public void run() {
        // save the real start time ...
        currentTime = System.currentTimeMillis();
        // ... for updating the gameTime
        lastTime = currentTime;
        
        while(!fwg.shutdown) {
            
            lastTime = currentTime;
            // here we update the game time with the passed time in the real world
            currentTime = System.currentTimeMillis();
            
            // remember the time
            lastGameTime = gameTime;
            gameTime += currentTime - lastTime;
            
            fwg.curGametime = gameTime;
            fwg.lastGametime = lastGameTime;
            fwg.actualTime = currentTime;
            
            // update the game scene
            //fwg.updateGame(gameTime, lastGameTime);
            //fwg.updateGame();
            
            
            // render the game
            fwg.doPaint = true;
            fwg.repaint();

            // wait a little bit
            //updateThread.yield();
            try {
                //updateThread.yield();

                /*
                timeDiff = System.currentTimeMillis() - currentTime;
                if(timeDiff > 20) {
                    updateThread.sleep(20);
                } else {
                    updateThread.sleep(40-timeDiff);
                    //System.out.println("SLEEPING " + (100-timeDiff));
                }
                */
                
                // -> MAX 20 fps
                
                timeDiff = System.currentTimeMillis() - currentTime;
                updateThread.sleep(20);

                /*
                if(timeDiff >= 0 && timeDiff < 20) {
                    updateThread.sleep(20 + 20-timeDiff);
                    //System.out.println("SLEEPING " + (100-timeDiff));
                } else {
                    updateThread.sleep(20);
                }*/
                
            } catch(Exception e) {
                System.out.println("ex");
            }
            
        }
        if (fwg.shutdown) {
            shutDown();
        }
    }
    
    private void shutDown() {
        //System.out.println("exitted");
        // Logout + stop net
        fwg.shutDown();
        shutDownDone = true;

        display.setCurrent(null);
        destroyApp(false);
        notifyDestroyed();
    }

    public void commandAction(Command c, javax.microedition.lcdui.Item item) {
        BeginKeyHandle();
    }
}
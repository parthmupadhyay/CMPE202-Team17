import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.World;
import greenfoot.core.WorldHandler;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.security.AccessControlException;

/**
 * GUI_Component
 * <p>
 * A component to be used in GUI system.<p>
 * Handles listening for mouse hover, and statuses for being hidden, will show, and focus.
 * 
 * @author Taylor Born
 * @version March 2013 - January 2014
 */
public abstract class GUI_Component extends Actor
{
    protected static TextTransfer textTransfer = new TextTransfer();

    public static Color defaultBackColor = Color.WHITE;
    public static Color defaultTextColor = Color.BLACK;
    public static Color defaultBorderColor = Color.BLACK;
    public static Color defaultHoverColor = new Color(192, 192, 192);
    public static Color defaultDisableColor = Color.GRAY;
    public static Color defaultSelectColor = Color.YELLOW;
    public static Font defaultFont = new Font("Helvetica", Font.PLAIN, 12);
    public static void setDefaultColors(Color backColor, Color textColor, Color borderColor, Color hoverColor, Color disableColor, Color selectColor)
    {
        defaultBackColor = backColor;
        defaultTextColor = textColor;
        defaultBorderColor = borderColor;
        defaultHoverColor = hoverColor;
        defaultDisableColor = disableColor;
        defaultSelectColor = selectColor;
    }
    
    protected Color backColor = defaultBackColor;
    protected Color textColor = defaultTextColor;
    protected Color borderColor = defaultBorderColor;
    protected Color hoverColor = defaultHoverColor;
    protected Color disableColor = defaultDisableColor;
    protected Color selectColor = defaultSelectColor;
    protected Font font = defaultFont;
    
    public void setBackColor(Color color)
    {
        backColor = color;
    }
    public void setTextColor(Color color)
    {
        textColor = color;
    }
    public void setBorderColor(Color color)
    {
        borderColor = color;
    }
    public void setHoverColor(Color color)
    {
        hoverColor = color;
    }
    public void setDisableColor(Color color)
    {
        disableColor = color;
    }
    public void setSelectColor(Color color)
    {
        selectColor = color;
    }
    public void setFont(Font font)
    {
        this.font = font;
    }

    private boolean overThis;
    private boolean focus;
    private boolean hiding;
    private boolean show;

    protected ScrollingListener initializeScroller()
    {
        ScrollingListener sl = new ScrollingListener();
        WorldHandler.getInstance().getWorldCanvas().addMouseWheelListener(sl);
        return sl;
    }
    
    /**
     * Listens for:<p>
     * Status for if the mouse is directly over this Actor. (Not over an Actor that is on top of this one).<p>
     * Status for if this GUI_Component was last pressed on by the mouse. (Has focus).
     */
    public void act()
    {
        if (Greenfoot.mouseMoved(this) || Greenfoot.mouseDragged(this))
            overThis = true;
        else if (Greenfoot.mouseMoved(null) || Greenfoot.mouseDragged(null))
            overThis = false;
        if (Greenfoot.mousePressed(this))
        {
            boolean f = focus;
            focus = true;
            if (!f)
                gainedFocus();
        }
        else if (Greenfoot.mousePressed(null))
            focus = false;
    }
    
    /**
     * Check to see if the mouse is directly over this Actor. (Not over an Actor that is on top of this one).
     * @return Whether or not the mouse is directly over this Actor.
     */
    protected boolean mouseOverThis()
    {
        return overThis;
    }
    
    /**
     * Called when this Component gains focus.<p>
     * Overwrite to have it do something.
     */
    protected void gainedFocus()
    {
        
    }
    
    /**
     * Check to see if this Actor was the last Actor pressed on by the mouse.
     * @return Whether or not this Actor was the last Actor pressed on by the mouse.
     */
    public boolean hasFocus()
    {
        return focus;
    }
    
    /**
     * Force this GUI_Component to steal focus. (Removing focus from all others).
     */
    public void giveFocus()
    {
        if (getWorld() == null)
            return;
        for (Object o : getWorld().getObjects(GUI_Component.class))
            ((GUI_Component)o).removeFocus();
        boolean f = focus;
        focus = true;
        if (!f)
            gainedFocus();
    }
    
    /**
     * Remove focus from this GUI_Component.
     */
    public void removeFocus()
    {
        focus = false;
    }
    
    /**
     * Check if this GUI_Component is set to be hidden. (Typically set by something else telling this GUI_Component to be okay to display or not).<p>
     * @return Whether or not this GUI_Component is set to be hidden.
     * @see willShow()
     * @see hide(boolean)
     */
    public boolean isHidden()
    {
        return hiding;
    }
    
    /**
     * Set whether or not this GUI_Component to be hidden.
     * @param h Whether or not this GUI_Component is to be set to be hidden.
     * @see isHidden()
     */
    public void hide(boolean h)
    {
        hiding = h;
    }
    
    /**
     * Check if this GUI_Component status on being set to be showing is true.<p>
     * Status to show means if this GUI_Component is/will-be in the World. (When not set to be hidden).
     * @return Whether or not this GUI_Component is set to show.
     * @see isHidden()
     * @see toggleShow()
     */
    public boolean willShow()
    {
        return show;
    }
    
    /**
     * Set this GUI_Component to show if not already set to show and vice versa.
     * @see willShow()
     */
    public void toggleShow()
    {
        show = !show;
    }
    
    /**
     * Check if this GUI_Component or any subcomponent has been pressed on by the mouse.<p>
     * Overwrite to include additional components.
     * @return Whether or not this GUI_Component or any subcomponent has been pressed on by the mouse.
     */
    public boolean mousePressedOnThisOrComponents()
    {
        return Greenfoot.mousePressed(this);
    }
    
    /**
     * Inherited from Actor, is called when this Window is added to the World.<p>
     * Sets status to show to be true.
     * @param world World to be added to.
     * @see willShow()
     */
    public void addedToWorld(World world)
    {
        show = true;
    }
    
    /**
     * Remove this GUI_Component from the World.
     */
    public void removeFromWorld()
    {
        if (getWorld() != null)
        {
            getWorld().removeObject(this);
            overThis = false;
            focus = false;
        }
    }
    
    /**
     * Check if this GUI_Component is within the World.
     * @return Whether or not this GUI_Component is within the World.
     */
    public boolean inWorld()
    {
        return getWorld() != null;
    }
    
    /**
     * Get the width in pixels that this GUI_Component will occupy.<p>
     * Default the width of its image.
     * @return The width in pixels that this GUI_Component will occupy.
     */
    public int getGUIWidth()
    {
        if (getImage() == null)
            return 0;
        return getImage().getWidth();
    }
    
    /**
     * Get the height in pixels that this GUI_Component will occupy.<p>
     * Default the height of its image.
     * @return The height in pixels that this GUI_Component will occupy.
     */
    public int getGUIHeight()
    {
        if (getImage() == null)
            return 0;
        return getImage().getHeight();
    }
    
    protected class ScrollingListener implements MouseWheelListener
    {
        int amount = 0;
        
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            amount += e.getWheelRotation();
            e.consume();
        }
        
        public int getScroll()
        {
            int s = amount;
            amount = 0;
            return s;
        }
    }
    
    protected static class TextTransfer implements ClipboardOwner
    {
        private static Clipboard clipboard;
        
        public TextTransfer()
        {
            try
            {
                clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            }
            catch (AccessControlException ex)
            {
                clipboard = new Clipboard("Greenfoot GUI clipboard");
            }
        }
        
        /**
         * Empty implementation of the ClipboardOwner interface.
         */
        public void lostOwnership(Clipboard aClipboard, Transferable aContents)
        {
             // do nothing
        }
        
        /**
         * Place a String on the clipboard, and make this class the owner of the Clipboard's contents.
         */
        public void setClipboardContents(String aString)
        {
            StringSelection stringSelection = new StringSelection(aString);
            clipboard.setContents(stringSelection, this);
        }
    
        /**
         * Get the String residing on the clipboard.
         *
         * @return any text found on the Clipboard; if none found, return an empty String.
         */
        public String getClipboardContents()
        {
            String result = "";
            Transferable contents = clipboard.getContents(null);
            boolean hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            if (hasTransferableText)
            {
                try
                {
                    result = (String)contents.getTransferData(DataFlavor.stringFlavor);
                }
                catch (UnsupportedFlavorException ex)
                {
                    // highly unlikely since we are using a standard DataFlavor
                    ex.printStackTrace();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            return result;
        }
    }
}
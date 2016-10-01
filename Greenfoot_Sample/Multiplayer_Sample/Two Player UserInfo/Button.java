import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Point;

/**
 * Button
 * <p>
 * GUI item that is a box that captures click events on itself.<p>
 * <p>
 * Action listener: wasClicked()
 * 
 * @author Taylor Born
 * @version November 2010 - April 2013
 */
public class Button  extends WindowComponent
{
    private static final Font FONT = new Font("Helvetica", Font.PLAIN, 12);

    private Point size;
    private String text;
    private boolean hover;
    private boolean pressed;
    private GreenfootImage icon = new GreenfootImage(1, 1);
    private boolean iconVisible;
    private boolean clicked;
    private boolean enabled = true;
    private Color backColor = Color.WHITE;
    private Color textColor = Color.BLACK;
    private Color hoverColor = new Color(220, 220, 220);
    private Color pressColor = new Color(190, 190, 190);
    private Color disableColor = new Color(102, 102, 102);
    
    private boolean acceptByEnterKey;
    private boolean enterKeyPressed;
    
    private boolean continuePress;
    private int pressCount;
    
    private GreenfootImage image;

    /**
     * Create a new Button.
     * @param text The text the Button is to display.
     * @param size The width and height of the Button.
     */
    public Button(String text, Point size)
    {
        this.text = text;
        this.size = size;
        image = new GreenfootImage((int)size.getX(), (int)size.getY());
        setImage(image);
        draw();
    }
    
    public void act() 
    {
        super.act();
        if (enabled)
        {
            boolean last = hover;
            boolean lastP = pressed;
            if (Greenfoot.mouseMoved(this))
                hover = true;
            else if (Greenfoot.mouseMoved(null) || Greenfoot.mouseDragged(null))
                hover = false;
            if (Greenfoot.mousePressed(this))
                pressed = true;
            if (continuePress && pressed)
                if (++pressCount == 5)
                {
                    clicked = true;
                    pressCount = 0;
                }
            if (Greenfoot.mouseClicked(this) && pressed)
                clicked = true;
            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null))
            {
                pressed = false;
                pressCount = 0;
            }
            if (last != hover || lastP != pressed)
                draw();
            
            if (acceptByEnterKey)
            {
                boolean eKeyDown = Greenfoot.isKeyDown("enter");
                if (!enterKeyPressed && eKeyDown)
                    clicked = true;
                enterKeyPressed = eKeyDown;
            }
        }
    }
    
    private void draw()
    {
        image.clear();
        if (pressed)
            image.setColor(pressColor);
        else if (hover)
            image.setColor(hoverColor);
        else
            image.setColor(backColor);
        image.fill();
        
        Graphics2D g = image.getAwtImage().createGraphics();
        g.setFont(FONT);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(enabled ? textColor : disableColor);
        g.drawString(text, ((int)size.getX() - g.getFontMetrics().charsWidth(text.toCharArray(), 0, text.length())) / 2 + (size.getX() % 2 == 0 ? 0 : 1), ((int)size.getY() + image.getFont().getSize()) / 2 - 1);
        g.dispose();
        
        image.setColor(textColor);
        image.drawRect(0, 0, (int)size.getX() - 1, (int)size.getY() - 1);
        if (iconVisible)
            image.drawImage(icon, 3, 4);
        
        //setImage(image);
    }
    
//     public void setColorScheme(Color back, Color text, Color hover, Color press, Color disable)
//     {
//         backColor = back;
//         textColor = text;
//         hoverColor = hover;
//         pressColor = press;
//         disableColor = disable;
//     }
    
    public void setTextColor(Color c)
    {
        textColor = c;
    }
    
    /**
     * The action listener for the Button.
     * @return Whether the Button was clicked or not.
     */
    public boolean wasClicked()
    {
        boolean c = clicked;
        clicked = false;
        return c;
    }
    
    public void addedToWorld(World world)
    {
        pressCount = 0;
        pressed = false;
        hover = false;
        draw();
    }
    
    /**
     * Set if the Button is enabled.
     * @param e Whether the Button will be enabled or not.
     */
    public void setEnable(boolean e)
    {
        enabled = e;
        if (!e)
            hover = false;
        draw();
    }
    
    /**
     * Set if the Button is to display its icon.
     * @param show Whether the Button will display its icon.
     */
    public void showIcon(boolean show)
    {
        iconVisible = show;
        draw();
    }
    
    /**
     * Get the text the Button is displaying.
     * @return The text the Button is displaying.
     */
    public String getText()
    {
        return text;
    }
    
    /**
     * Set what text the Button is displaying.
     * @param text The text the Button will display.
     */
    public void setText(String text)
    {
        this.text = text;
        draw();
    }
    
    public void setContinuePress(boolean cont)
    {
        continuePress = cont;
    }
    
    public void setAcceptByEnterKey(boolean accept)
    {
        acceptByEnterKey = accept;
    }
    
    public boolean mousePressedOnThisOrComponents()
    {
        return false;
    }
}
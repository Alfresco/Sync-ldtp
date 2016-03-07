package org.alfresco.os.win.app.misc;

import org.alfresco.os.common.GuiObject;
import org.alfresco.os.win.app.WindowsExplorerContextMenu;

import com.cobra.ldtp.Ldtp;

/**
 * Generic class that will handle Contextual Menus in Windows
 * Inherit this class further in your code
 * Take a look at {@link WindowsExplorerContextMenu} implemented
 * 
 * @author pbrodner
 */
public abstract class ContextMenu extends GuiObject
{
    ContextMenuItem menuItem = null;

    protected ContextMenuItem getMenuItem()
    {
        return menuItem;
    }

    protected void setMenuItem(ContextMenuItem menuItem)
    {
        this.menuItem = menuItem;
    }

    public ContextMenu()
    {
        setLdtp(new Ldtp("Context"));
    }

    /**
     * will contain the implementation code of opening this context menu
     * 
     * @throws Exception
     */
    public abstract void open() throws Exception;

    /**
     * how Context Menu Item is visible in LDTP, define the new ContextMenuItem class
     * 
     * @return
     */
    public abstract ContextMenuItem getContextMenuItem(String itemName);

    public ContextMenuItem openItem(String itemName) throws Exception
    {
        open();
        setMenuItem(getContextMenuItem(itemName));
        getLdtp().selectMenuItem(itemName);
        return menuItem;
    }
}

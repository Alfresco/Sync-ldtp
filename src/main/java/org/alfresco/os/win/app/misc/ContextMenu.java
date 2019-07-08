package org.alfresco.os.win.app.misc;

import org.alfresco.os.common.GuiObject;

import com.cobra.ldtp.Ldtp;

/**
 * Generic class that will handle Contextual Menus in Windows
 * Inherit this class further in your code
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
    public abstract void openWhenUpdateIsAvailable() throws Exception;

    /**
     * how Context Menu Item is visible in LDTP, define the new ContextMenuItem class
     * 
     * @return
     */
    protected abstract ContextMenuItem getContextMenuItem(String itemName);

    public ContextMenuItem openItem(String itemName) throws Exception
    {
        logger.info("Open: " + itemName);
        open();
        setMenuItem(getContextMenuItem(itemName));
        getLdtp().waitTillGuiExist(itemName, 10);
        getLdtp().selectMenuItem(itemName);
        return menuItem;
    }

    public ContextMenuItem openItemWhenUpdateIsAvailable(String itemName) throws Exception
    {
        logger.info("Open: " + itemName);
        openWhenUpdateIsAvailable();
        setMenuItem(getContextMenuItem(itemName));
        getLdtp().waitTillGuiExist(itemName, 10);
        getLdtp().selectMenuItem(itemName);
        return menuItem;
    }
}

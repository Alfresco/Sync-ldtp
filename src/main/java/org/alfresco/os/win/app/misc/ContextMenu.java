package org.alfresco.os.win.app.misc;

import org.alfresco.os.common.GuiObject;

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

    public ContextMenuItem openItem(String item) throws Exception
    {
        open();
        getLdtp().selectMenuItem(item);

        return menuItem;
    }

    public abstract void open() throws Exception;
}

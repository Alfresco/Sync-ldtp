package org.alfresco.os.win.app.misc;

import org.alfresco.os.common.GuiObject;

import com.cobra.ldtp.Ldtp;

/**
 * Handles Context Menus sub items
 * This class works in corelation with {@link ContextMenu}
 * 
 * Take a look at {@link WindowsExplorerContextMenu} implemented
 * 
 * @author pbrodner
 */
public class ContextMenuItem extends GuiObject
{
    public ContextMenuItem(String name)
    {
        setLdtp(new Ldtp(name));
    }

    public ContextMenuItem openSubMenu(String value)
    {
        getLdtp().mouseMove(value);
        getLdtp().click(value);
        return this;
    }
}
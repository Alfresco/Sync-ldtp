package org.alfresco.os.win.app.misc;

import org.alfresco.os.common.GuiObject;

import com.cobra.ldtp.Ldtp;

/**
 * Handles Context Menus sub items
 * This class works in corelation with {@link ContextMenu}
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
        logger.info(String.format("Select '%s'", value));
        //getLdtp().mouseMove(value);
        getLdtp().click(value);
        return this;
    }
}
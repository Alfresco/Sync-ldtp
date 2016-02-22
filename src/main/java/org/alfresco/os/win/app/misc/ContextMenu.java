package org.alfresco.os.win.app.misc;

import org.alfresco.os.common.GuiObject;

import com.cobra.ldtp.Ldtp;

/**
 * Generic class that will handle Contextula Menus in Windows
 * Inherit this class further in your code
 * 
 * @author pbrodner
 */
public class ContextMenu extends GuiObject
{
    public ContextMenu()
    {
        setLdtp(new Ldtp("Context"));
    }
}

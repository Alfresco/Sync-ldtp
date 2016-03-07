package org.alfresco.os.win.app;

import java.awt.Desktop;
import java.io.File;

import org.alfresco.os.win.app.misc.ContextMenu;
import org.alfresco.os.win.app.misc.ContextMenuItem;

import com.cobra.ldtp.Ldtp;

/**
 * Handle Windows Context Menu
 * You need to pass only the folder root path and then play with the available menus available
 * Example:
 * WindowsExplorerContextMenu menu = new WindowsExplorerContextMenu("C:/test/paul")
 * menu.openItem("View").openSubMenu("Large icons");
 * menu.openItem("New").openSubMenu("Text Document");
 * menu.openItem("View").openSubMenu("List");
 *
 * @author pbrodner
 */
public class WindowsExplorerContextMenu extends ContextMenu
{
    File rootFolder = null;

    public WindowsExplorerContextMenu(File rootFolder)
    {
        this.rootFolder = rootFolder;
    }

    @Override
    public void open() throws Exception
    {
        Desktop.getDesktop().open(rootFolder);
        new Ldtp(rootFolder.getName()).mouseRightClick("Items View");        
    }

    @Override
    public ContextMenuItem getContextMenuItem(String itemName)
    {
        return new ContextMenuItem(rootFolder.getName());
    }

}

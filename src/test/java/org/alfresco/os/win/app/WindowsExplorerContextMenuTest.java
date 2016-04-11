package org.alfresco.os.win.app;

import java.io.File;

import org.alfresco.utilities.LdtpUtils;
import org.testng.annotations.Test;

/**
 * Visual Testing Context Explorer
 */
public class WindowsExplorerContextMenuTest
{

    @Test
    public void changeListViewFromContextualMenu() throws Exception
    {
        File createFolder = new File(LdtpUtils.getDocumentsFolder(), "testOpenFolderFromCurrent");
        createFolder.mkdir();

        WindowsExplorerContextMenu menu = new WindowsExplorerContextMenu(createFolder);
        menu.openItem("View").openSubMenu("Large icons");
        menu.openItem("New").openSubMenu("Text Document");
        menu.openItem("View").openSubMenu("List");
    }

}

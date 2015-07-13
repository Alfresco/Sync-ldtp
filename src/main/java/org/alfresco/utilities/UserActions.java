package org.alfresco.utilities;

import org.alfresco.os.win.app.WindowsExplorer;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by rdorobantu on 7/13/2015.
 */
public class UserActions
{
    private static Logger logger = Logger.getLogger(UserActions.class);

    public static void renameFile(File originalFile, File newName)
    {
        WindowsExplorer explorer = new WindowsExplorer();
        try
        {
            explorer.openApplication();
            explorer.openFolder(originalFile.getParentFile());
            explorer.rename(originalFile, newName);
            explorer.closeExplorer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteFile(File fileName)
    {
        WindowsExplorer explorer = new WindowsExplorer();
        try
        {
            explorer.openApplication();
            explorer.deleteFile(fileName, true);
            explorer.closeExplorer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

package org.alfresco.utilities;

import org.alfresco.os.win.Application;
import org.alfresco.os.win.Application.type;
import org.alfresco.os.win.app.Notepad;
import org.alfresco.os.win.app.WindowsExplorer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.ObjectInputStream.GetField;

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

    /**
     * method to create a folder and file inside
     * 
     * @throws Exception
     */
    public  void createFolderAndFile(File folderName, String fileName) throws Exception
    {
        WindowsExplorer explorer = openFolder(folderName.getParentFile());
        explorer.createAndOpenFolder(folderName.getName());
        explorer.rightClickCreate(folderName.getName(), fileName, type.TEXTFILE);
        explorer.closeExplorer();
        
    }
    /**
     * open explorer and open folder    
     * @throws Exception 
     */
    
    public WindowsExplorer openFolder(File folderName) throws Exception
    {
        WindowsExplorer explorer = new WindowsExplorer();
        explorer.openApplication();
        explorer.openFolder(folderName);
        return explorer;
    }
} 
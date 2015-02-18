/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package org.alfresco.os.win.app;

import java.io.File;
import java.io.IOException;

import org.alfresco.os.common.ApplicationBase;
import org.alfresco.os.win.Application;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.LdtpExecutionError;

/**
 * * This class will handle Windows based operation over Windows Explorer
 * As a Windows OS environment settings, please just turn off "Hide extension for known file types"
 * 
 * @author Subashni Prasanna
 * @author Paul Brodner
 */
public class WindowsExplorer extends Application
{

    public WindowsExplorer()
    {
        setApplicationName("explorer.exe");
        setApplicationPath(LdtpUtils.getDocumentsFolder().getPath());
        setWaitWindow("Libraries");
    }

    /**
     * Focus a file name using <windowName> passes as parameter
     * 
     * @param windowName
     */
    public void focus(String windowName)
    {
        getLdtp().setWindowName(windowName);
    }

    public void focus()
    {
        getLdtp().setWindowName(getWaitWindow());
    }

    @Override
    public ApplicationBase openApplication()
    {
        super.openApplication();

        getLdtp().doubleClick("tblcDocuments");
        // after we open the explorer on applicationPath, we open "Documents" folder
        try
        {
            waitForApplicationWindow("Documents", true);
        }
        catch (Exception e)
        {
        }
        return this;
    }

    /**
     * Method to open a particular folder in windows explorer
     * 
     * @param Full path of the folder as String
     * @throws Exception
     * @throws InterruptedException
     */
    public String openFolder(File folderPath) throws Exception
    {
        String windowName = "";

        if (!folderPath.isDirectory())
        {
            throw new IOException("Please provide a folder");
        }
        getLdtp().mouseLeftClick("pane2");
        getLdtp().enterString("uknLibraries", folderPath.getPath());
        getLdtp().keyPress("<enter>");

        windowName = LdtpUtils.getFullWindowList(getLdtp(), folderPath.getName());
        getLdtp().setWindowName(windowName);
        return windowName;
    }

    /**
     * Create a new folder from the new folder option in the menu of windows explorer.
     * This method assume that you have already have explorer opened and you have navigated to the particular folder
     * to create a folder inside it.
     * 
     * @param - Name of the folder to create.
     */
    public void createNewFolderMenu(String folderName)
    {
        getLdtp().click("New folder");
        LdtpUtils.waitToLoopTime(3);
        getLdtp().generateKeyEvent(folderName);
        getLdtp().keyPress("<enter>");
        LdtpUtils.waitToLoopTime(3);
    }

    /**
     * Create a new folder from the new folder option in the menu of windows explorer.
     * This method assume that you have already have explorer opened and you have navigated to the particular folder
     * to create a folder inside it. Upon successful create the folder will be opened
     * 
     * @param - Name of the folder to create String
     */
    public String createAndOpenFolder(String folderName)
    {
        createNewFolderMenu(folderName);
        return openFolderFromCurrent(folderName);
    }

    /**
     * This method will help us to open a particular folder assuming you have already browsed to the location
     * 
     * @param - String Folder name to open
     */
    public String openFolderFromCurrent(String folderName) throws LdtpExecutionError
    {
        String _folderName = LdtpUtils.getFullObjectList(getLdtp(), folderName);
        getLdtp().doubleClick(_folderName);
        getLdtp().setWindowName(folderName);
        return _folderName;
    }

    /**
     * set window name for the opened file
     * 
     * @throws InterruptedException
     */
    public void activateApplicationWindow(String name)
    {
        String _name = LdtpUtils.getFullWindowList(getLdtp(), name);
        focus(_name);
        getLdtp().activateWindow(_name);
    }

    /**
     * Set the name and password on Windows Security
     * 
     * @param userName
     * @param password
     * @throws Exception
     */
    public void operateOnSecurity(String userName, String password) throws Exception
    {
        if (waitForWindow("Windows Security") != null)
        {
            getLdtp().deleteText("txtUsername", 0);
            getLdtp().enterString("txtUsername", userName);
            getLdtp().enterString("txtPassword", password);
            getLdtp().click("OK");
        }
    }

    /**
     * open a file from the windows explorer based on the give path
     * 
     * @throws Exception
     */
    public void openFile(File file) throws Exception
    {
        openFolder(file.getParentFile());
        openFileInCurrentFolder(file);
    }

    /**
     * Close explorer window
     */
    public void closeExplorer()
    {
        getLdtp().click("Close");
    }

    @Override
    public void exitApplication()
    {
        closeExplorer();
    }

    /**
     * Open a file in a particular folder - Assume we are already in that folder and we trying to open the file
     * 
     * @param - String - file to open
     */
    public void openFileInCurrentFolder(File file) throws LdtpExecutionError
    {
        getLdtp().doubleClick(file.getName());
    }

    /**
     * Delete a file or folder in a given path
     * 
     * @throws Exception
     */
    public void deleteFile(File fileToDelete, boolean confirmationOption) throws Exception
    {
        openFolder(fileToDelete.getParentFile());
        getLdtp().mouseRightClick(fileToDelete.getName());
        onContextMenuPerform("Delete");
        alertConfirmation("Delete File", confirmationOption);
    }

    /**
     * Delete a file or folder in a given path
     * 
     * @throws Exception
     */
    public void deleteFile(File fileToDelete) throws Exception
    {
        deleteFile(fileToDelete, true);
    }

    /**
     * Delete a folder
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void deleteFolder(String folderName, boolean areYouSure)
    {
        rightClickOn(folderName);
        getLdtp().click("Delete");
        LdtpUtils.waitToLoopTime(3);
        alertConfirmation("Delete Folder", areYouSure);
    }

    /**
     * Delete a folder
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    public void deleteFolder(String folderName)
    {
        deleteFolder(folderName, true);
    }

    /**
     * Right click Cut and Paste which will act like a move operation
     * 
     * @throws Exception
     */
    public File moveFolder(File sourceFolder, File destinationLocation) throws Exception
    {
        getLdtp().mouseRightClick(sourceFolder.getName());
        onContextMenuPerform("Cut");

        openFolder(destinationLocation);

        getLdtp().mouseRightClick("lstItemsView");
        onContextMenuPerform("Paste");

        File newMovedFolder = new File(destinationLocation, sourceFolder.getName());
        LdtpUtils.waitUntilFileExistsOnDisk(newMovedFolder);
        return newMovedFolder;
    }

    /**
     * Just go back in Windows Explorer based on <folder>
     * 
     * @param file
     */
    public void goBack(String folder)
    {
        getLdtp().click("Back to " + folder);
        focus(folder);
    }

    /**
     * Delete Confirmation dialog options
     */
    private void alertConfirmation(String window, boolean areYouSure) throws LdtpExecutionError
    {
        String oldWindow = getLdtp().getWindowName();
        getLdtp().setWindowName(window);
        if (areYouSure)
        {
            getLdtp().click("Yes");
        }
        else
        {
            getLdtp().click("No");
        }
        getLdtp().waitTillGuiNotExist(window);
        getLdtp().setWindowName(oldWindow);
    }

    /**
     * Open context menu for a specific folderOrFile
     * 
     * @param contentToCheck
     * @return
     */
    private boolean rightClickOn(String folderOrFile)
    {
        String deleteObject = LdtpUtils.getFullObjectList(getLdtp(), folderOrFile);

        if (!deleteObject.isEmpty())
        {
            getLdtp().mouseRightClick(deleteObject);
            getLdtp().setWindowName("Context");
            return true;
        }
        else
        {
            logger.error(String.format("Context [%s] was not found on rightClickOn method", folderOrFile));
            return false;
        }
    }

    /**
     * Perform an action from Context Menu (e.g. Cut, Paste, etc.)
     * Is assumed that Context menu is already opened.
     * 
     * @param action
     */
    private void onContextMenuPerform(String action)
    {
        String oldWindowName = getLdtp().getWindowName();
        getLdtp().setWindowName("Context");
        getLdtp().click(action);
        getLdtp().setWindowName(oldWindowName);
    }
}

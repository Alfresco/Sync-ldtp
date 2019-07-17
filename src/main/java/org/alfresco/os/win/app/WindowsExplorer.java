/*
* Copyright (C) 2005-2019 Alfresco Software Limited.
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

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;
import com.google.common.io.Files;
import org.alfresco.os.common.ApplicationBase;
import org.alfresco.os.win.Application;
import org.alfresco.utilities.LdtpUtils;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * * This class will handle Windows based operation over Windows Explorer
 * As a Windows OS environment settings, please just turn off "Hide extension for known file types"
 * 
 * @author Subashni Prasanna
 * @author Paul Brodner
 */
public class WindowsExplorer extends Application
{
    private static Logger logger = Logger.getLogger(WindowsExplorer.class);
    private final String replaceIfExitsDialog = "frmReplace or Skip Files";
    private final String btnReplaceFile = "btnReplacethefileinthedestination";

    public WindowsExplorer()
    {
        setApplicationName("explorer.exe");
        setApplicationPath(LdtpUtils.getDocumentsFolder().getParentFile().getPath());

        switch(LdtpUtils.getOS())
        {
            case "Windows 8.1":
            {
                 setWaitWindow("This PC");
                 break;
            }
            case  "Windows 7":
            {
                setWaitWindow("Libraries");
                break;
            }
            case  "Windows 10":
            {
                setWaitWindow("File Explorer");
                break;
            }
            default:
            {
                setWaitWindow("This PC");
                break;
            }
        }
    }

    /**
     * Focus a file name using <windowName> passes as parameter
     * 
     * @param windowName
     */
    public void focus(String windowName)
    {
        getLdtp().setWindowName(windowName);
        getLdtp().waitTillGuiExist();
        getLdtp().activateWindow(windowName);
    }

    public void focus()
    {
        focus(getWaitWindow());
    }

    @Override
    public ApplicationBase openApplication() throws Exception
    {
        super.openApplication();
        return this;
    }

    public ApplicationBase openApplication(String pathToFolder)
    {
        setWaitWindow(new File(pathToFolder).getName());
        LdtpUtils.executeOnWin("start " + pathToFolder);
        waitForApplicationWindow(getWaitWindow(), true);
        return this;
    }

    public ApplicationBase openRecycleBin()
    {
        setWaitWindow("Recycle Bin");
        LdtpUtils.executeOnWin("start shell:RecycleBinFolder");
        waitForApplicationWindow(getWaitWindow(), true);
        return this;
    }

    public ApplicationBase replaceIfExits()
    {
        LdtpUtils.waitForWindowPartialName(getLdtp(), replaceIfExitsDialog);
        Ldtp replaceDialog = new Ldtp(replaceIfExitsDialog);
        replaceDialog.click(btnReplaceFile);
        LdtpUtils.waitForWindowToDisappear(getLdtp(), replaceIfExitsDialog);
        return this;
    }

    /**
     * Method to open a particular folder in windows explorer
     * 
     * @param folderPath path of the folder as String
     * @throws Exception
     * @throws InterruptedException
     */
    public void openFolder(File folderPath) throws Exception
    {
        LdtpUtils.logInfo("open folder in the " + folderPath.getPath());
        if (!folderPath.exists())
        {
            throw new IOException("Folder does not exists: " + folderPath.getPath());
        }
        if (!folderPath.isDirectory())
        {
            throw new IOException("Please provide a folder");
        }
        if ((LdtpUtils.isWin81()) || (LdtpUtils.isWin10()))
        {
            getLdtp().grabFocus("File Explorer");
            getLdtp().generateKeyEvent("<alt>d"); // focusing address editor
            pasteString(folderPath.getPath());
        }
        else
        {
            getLdtp().mouseLeftClick("pane2");
            try
            {
                getLdtp().enterString("uknLibraries", "");
            }
            catch (Exception e)
            {
            }
            getLdtp().generateKeyEvent("<alt>d");
            pasteString(folderPath.getPath());
        }
        getLdtp().keyPress("<enter>");
        getLdtp().setWindowName(folderPath.getName());
        getLdtp().waitTillGuiExist();
    }

    public boolean isWindowDisplayed(String windowName)
    {
        return getLdtp().getWindowName().equals(windowName);
    }

    /**
     * This method will help us to open a particular folder assuming you have already browsed to the location
     * 
     * @param - String Folder name to open
     */
    public String openFolderFromCurrent(String folderName) throws LdtpExecutionError
    {
        logger.info("open the folder in the current location " + folderName);
        getLdtp().doubleClick(folderName);
        getLdtp().setWindowName(folderName);
        return folderName;
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
        logger.info("close the explorer");
        getLdtp().waitTillGuiExist("Close");
        getLdtp().click("Close");
        setLdtp(null);
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
        logger.info("open the file present in the current folder " + file.getName());
        getLdtp().doubleClick(file.getName());
    }

    /**
     * Right click Cut and Paste which will act like a move operation
     * 
     * @throws Exception
     */
    public File moveContent(File sourceFolder, File destinationLocation) throws Exception
    {
        logger.info("move source folder " + sourceFolder.getAbsolutePath() + " destination location " + destinationLocation.getAbsolutePath());
        cut(sourceFolder);
        openFolder(destinationLocation);
        paste();
        File newMovedFolder = new File(destinationLocation, sourceFolder.getName());
        LdtpUtils.waitUntilFileExistsOnDisk(newMovedFolder);
        return newMovedFolder;
    }

    /**
     * Right click copy and Paste which will act like a move operation
     * 
     * @throws Exception
     */
    public File copyContent(File sourceFolder, File destinationLocation) throws Exception
    {
        logger.info("copy source  " + sourceFolder.getAbsolutePath() + " destination location " + destinationLocation.getAbsolutePath());
        copy(sourceFolder);
        openFolder(destinationLocation);
        paste();
        File newCopyContent = new File(destinationLocation, sourceFolder.getName());
        LdtpUtils.waitUntilFileExistsOnDisk(newCopyContent);
        return newCopyContent;
    }

    /**
     * Just go back in Windows Explorer based on <folder>
     * On Windows 8.1 you don't need to pass folder name
     * 
     * @param folder
     */
    public void goBack(String folder)
    {
        if (LdtpUtils.isWin81() || (LdtpUtils.isWin10()))
        {
            getLdtp().generateKeyEvent("<alt><left>");
        }
        else
        {
            folder = folder.toLowerCase();
            getLdtp().click("Back to " + folder);
        }
        focus(folder);
    }

    /**
     * Going back in Windows Explorer
     */
    public void goBack()
    {
        if (LdtpUtils.isWin81() || (LdtpUtils.isWin10()))
        {
            logger.info("Going back using send keys ALT+LEFT");
            getLdtp().generateKeyEvent("<alt><left>");
        }
    }

    /**
     * Rename a file <fileName> using <renamedFileName> parameter
     * Is assumed that we are already on the folder path of the <fileName>
     * 
     * @param fileName
     * @param renamedFileName
     */
    public void rename(String fileName, String renamedFileName)
    {
        String oldWindow = getLdtp().getWindowName();
        getLdtp().mouseRightClick(fileName);
        onContextMenuPerform("Rename");
        getLdtp().generateKeyEvent("<ctrl>a");
        getLdtp().generateKeyEvent(renamedFileName);
        getLdtp().generateKeyEvent("<enter>");
        getLdtp().waitTime(1);
        getLdtp().setWindowName(oldWindow);
    }

    /**
     * Rename a file <fileName> using <renamedFileName> parameter
     * Is assumed that we are already on the folder path of the <fileName>
     * 
     * @param fileName
     * @param renamedFileName
     */
    public void rename(File fileName, File renamedFileName)
    {
        rename(fileName.getName(), renamedFileName.getName());
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
     * @param folderOrFile
     * @return
     */
    private boolean rightClickOn(String folderOrFile)
    {
        try
        {
            getLdtp().mouseRightClick(folderOrFile);
            getLdtp().setWindowName("Context");
            return true;
        }
        catch (Exception e)
        {
            logger.error(String.format("Context [%s] was not found on rightClickOn method: [%s]", folderOrFile, e.getCause()));
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

    /*
     * Return the temporary file with image captured
     */
    public File getIconImage(File fileOrFolder)
    {
        Ldtp app = new Ldtp(fileOrFolder.getParentFile().getName());
        logger.info("Get Icon Image of: " + fileOrFolder.getPath());
        Integer[] a = app.getObjectSize(Files.getNameWithoutExtension(fileOrFolder.getName()));

        String img = app.imageCapture(a[0], a[1], 20, a[3]);
        logger.info("Saved image in tmp location:" + img);
        File actualImage = new File(img);
        return actualImage;
    }

    /**
     * CUT operation
     * 
     * @param fileNameOrFolder
     */
    public void cut(File fileNameOrFolder)
    {
        getLdtp().mouseRightClick(fileNameOrFolder.getName());
        onContextMenuPerform("Cut");
    }

    /**
     * COPY operation
     * 
     * @param fileNameOrFolder
     */
    public void copy(File fileNameOrFolder)
    {
        getLdtp().mouseRightClick(fileNameOrFolder.getName());
        onContextMenuPerform("Copy");
    }

    /**
     * PASTE operation

     */
    public void paste()
    {
        getLdtp().mouseRightClick("lstItemsView");
        onContextMenuPerform("Paste");
    }

    /**
     * jump to a specific folder.
     * If that folder is ALREADY open, it will auto-focus.
     */
    public void jumpToLocation(File location) throws IOException
    {
        Desktop.getDesktop().open(location);
        focus(location.getName());
        getLdtp().waitTillGuiExist();
    }
    
    /**
     * DELETE operation
     * 
     * @param objectName
     * @param confirmationOption
     */
    public void delete(String objectName, boolean confirmationOption)
    {
        getLdtp().waitTillGuiExist(objectName);
        getLdtp().mouseRightClick(objectName);
        onContextMenuPerform("Delete");
        getLdtp().waitTime(2);
        alertConfirmation("Delete F*", confirmationOption);
    }

    /**
     * DELETE operation
     *
     * @param objectName
     */
    public void delete(String objectName)
    {
        getLdtp().waitTillGuiExist(objectName);
        getLdtp().mouseRightClick(objectName);
        onContextMenuPerform("Delete");
    }

    public boolean isContentDisplayed(String contentName)
    {
        return getLdtp().objectExist(contentName) == 1;
    }
}

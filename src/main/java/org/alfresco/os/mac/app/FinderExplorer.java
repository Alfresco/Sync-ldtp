/*
 * Copyright (C) 2005-2015 Alfresco Software Limited. This file is part of Alfresco Alfresco is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. Alfresco is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received a copy of the GNU Lesser General
 * Public License along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.os.mac.app;

import com.cobra.ldtp.Ldtp;
import org.alfresco.exceptions.WindowNotOpenedException;
import org.alfresco.os.mac.utils.AlertDialog;
import org.alfresco.os.mac.utils.KeyboardShortcut;
import org.alfresco.utilities.LdtpUtils;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This class will handle Finder based action over Files (CRUD) and Folder (CRUD).
 */
public class FinderExplorer extends KeyboardShortcut
{
    private static Logger logger = Logger.getLogger(FinderExplorer.class);
    private final String syncExtension = "cbo2";
    private final String syncNowMenu = "mnuSyncNow";
    private final String ALFRESCO_EXTENSION = "com.alfresco.AlfrescoDesktopSync.AlfrescoSyncExtension";

    /**
     * Initialize the FinderExplorer window for Mac Opening first the Documents folder of the current user
     */
    public FinderExplorer()
    {
        this(LdtpUtils.getDocumentsFolder().getPath());
    }

    /**
     * Initialize a new Finder Windows starting on <startUpFolder>
     * 
     * @param startUpFolder
     */
    public FinderExplorer(String startUpFolder)
    {
        setApplicationName("Finder");
        // set the root path of the Finder Window to the current user Documents folder
        setApplicationPath(startUpFolder);
        // each finder has the window name set to the current folder name
        setWaitWindow("frmDocuments");
    }

    public boolean isLayoutViewEnabled(LayoutView layout)
    {
        return isBtnEnabled(layout.getIdentifiler());
    }

    /**
     * use the AlertDialog class
     * 
     * @return AlertDialog
     */
    public AlertDialog alert()
    {
        return new AlertDialog();
    }

    /**
     * Enumerate possible Layouts that we can define before we start using the Finder explorer. Use LIST as default
     * 
     * @author Paul Brodner
     */
    public enum LayoutView
    {
        ICONS("0"), LIST("1"), GROUP("2"), COVER_FLOW("3");
        private String position;

        private LayoutView(String position)
        {
            this.position = position;
        }

        public String getIdentifiler()
        {
            return "rbtn" + position;
        }
    }

    /*
     * (non-Javadoc)
     * @see org.alfresco.utilities.ApplicationAbstract#openApplication() Overrite the openApplication in order to also set the default view of the folder
     */
    public FinderExplorer openApplication()
    {
        setWaitWindow("frmDocuments");
        super.openApplication();
        waitForApplicationWindow(getWaitWindow(), true);
        setViewLayout(LayoutView.LIST);
        return this;
    }

    public FinderExplorer openFinder(String path)
    {
        String name = new File(path).getName();
        if(name.startsWith("."))
        {
            name = name.replaceFirst(".", "");
        }
        setWaitWindow("frm" + name);
        LdtpUtils.executeOnMac("open " + path);
        waitForApplicationWindow(getWaitWindow(), true);
        setViewLayout(LayoutView.LIST);
        return this;
    }

    @Override
    public void exitApplication()
    {
        minimize();
        killPython();
    }

    public void closeExplorer()
    {
        logger.info("Close Explorer Window.");
        getLdtp().generateKeyEvent("<command>w");
        setWaitWindow("frmDocuments");
    }

    public void closeExplorer(String windowName)
    {
        logger.info(String.format("Close Explorer Window of %s.", windowName));
        getLdtp().setWindowName(windowName);
        getLdtp().activateWindow(windowName);
        LdtpUtils.waitToLoopTime(1);
        getLdtp().generateKeyEvent("<option><command>w");
    }

    /**
     * Define a default Layout on the Finder Window
     * 
     * @param layoutView
     */
    public void setViewLayout(LayoutView layoutView)
    {
        logger.info("Set view layout to:" + layoutView.name());
        getLdtp().click(layoutView.getIdentifiler());
    }

    /**
     * This will open a folder based on <folderPath> parameter passed
     * 
     * @param folderPath
     */
    public void openFolder(File folderPath) throws Exception
    {
        logger.info("Open Folder: " + folderPath.getPath());
        getLdtp().generateKeyEvent("<shift><command>g");
        pasteString(folderPath.getPath());
        getLdtp().generateKeyEvent("<enter>");
        focus(folderPath);
        if(folderPath.isFile())
        {
            waitForWindow(folderPath.getParentFile().getName());
            getLdtp().setWindowName(folderPath.getParentFile().getName());
        }
        else
        {
            waitForWindow(folderPath.getName());
            getLdtp().setWindowName(folderPath.getName());
        }
    }

    public void openFolderByTypingPath(File folderPath) throws Exception
    {
        logger.info("Open Folder: " + folderPath.getPath());
        getLdtp().generateKeyEvent("<shift><command>g");
        getLdtp().generateKeyEvent(folderPath.getPath());
        getLdtp().generateKeyEvent("<enter>");
        focus(folderPath);
        if(folderPath.isFile())
        {
            waitForWindow(new File(folderPath.getParent()).getName());
        }
        else
        {
            waitForWindow(folderPath.getName());
        }
    }

    /**
     * Activate window folder
     * 
     * @param folder
     */
    public void focus(File folder)
    {
        if (folder.isDirectory())
        {
            getLdtp().activateWindow(folder.getName());
        }
        else
        {
            getLdtp().activateWindow(new File(folder.getParent()).getName());
        }
    }

    /**
     * Create a new Folder based on MAC shortcut keys
     * 
     * @param folderPath
     */
    public void createFolder(File folderPath)
    {
        logger.info("Creating Folder: " + folderPath.getPath());
        getLdtp().generateKeyEvent("<shift><command>n");
        LdtpUtils.waitToLoopTime(2);
        getLdtp().generateKeyEvent(folderPath.getName());
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Create and open a folder
     * 
     * @param folder
     */
    public void createAndOpenFolder(File folder) throws Exception
    {
        logger.info("Create and Open Folder:" + folder.getPath());
        openFolder(folder.getParentFile());
        createFolder(folder);
        openFolder(folder);
    }

    /**
     * Delete a folder based on MAC shortcut keys First we navigate to target folder, go up one level and delete it using cmd+bksp
     * 
     * @param folderPath
     */
    public void deleteFolder(File folderPath) throws Exception
    {
        logger.info("Deleting folder:" + folderPath.getPath());
        openFolder(folderPath);
        goToEnclosingFolder();
        getLdtp().setWindowName(folderPath.getParentFile().getName());
        cmdDelete();
    }

    /**
     * Move a folder from <source> to <destination>
     * 
     * @param source
     * @param destination
     */
    public void moveFolder(File source, File destination) throws Exception
    {
        logger.info("Move Folder[" + source.getPath() + "] to:" + destination.getPath());
        openFolder(source);
        goToEnclosingFolder();
        LdtpUtils.waitToLoopTime(1);
        cmdCopy();
        openFolderByTypingPath(destination);
        cmdMove();
    }

    public void copyFolder(File source, File destination) throws Exception
    {
        logger.info(String.format("Copy folder [%s] to [%s]", source.getPath(), destination.getPath()));
        openFolder(source);
        goToEnclosingFolder();
        cmdCopy();
        openFolderByTypingPath(destination);
        cmdPaste();
    }

    /**
     * Rename <folder> with <newName>
     * 
     * @param folder
     * @param newName
     */
    public void renameFolder(File folder, File newName) throws Exception
    {
        logger.info(String.format("Rename folder {%s} to {%s}", folder.getPath(), newName));
        openFolder(folder);
        goToEnclosingFolder();
        LdtpUtils.waitToLoopTime(1);
        getLdtp().generateKeyEvent("<enter>");
        getLdtp().generateKeyEvent(newName.getName());
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Restore any deleted Folder
     * 
     * @param folder
     * @deprecated
     */
    public void restoreDeletedFolder(File folder) throws Exception
    {
        logger.info("Restore Delted Folder from Trash: " + folder.getName());
        File deletedFolder = new File(LdtpUtils.getTrashFolderLocation(), folder.getName());
        deleteFolder(deletedFolder); // on MAC: cmd+bkspace will do the trick and restore the file
    }

    /**
     * Open a file using default Application
     * 
     * @param file
     */
    public void openFile(File file) throws Exception
    {
        logger.info("Open File:" + file.getPath());
        selectFile(file);
        goActivate();
    }

    /**
     * Select a file in Finder
     * 
     * @param file
     */
    public void selectFile(File file) throws Exception
    {
        logger.info("Select File: " + file.getPath());
        openFolder(file);
    }

    public boolean isContentDisplayed(File file)
    {
        return getLdtp().objectExist(file.getName()) == 1;
    }

    /**
     * Delete a file
     * 
     * @param file
     */
    public void deleteFile(File file) throws Exception
    {
        logger.info("Delete File: " + file.getPath());
        selectFile(file);

    }

    /**
     * Move a file from <source> to <destination>
     * 
     * @param source
     * @param destinationFolder
     */
    public void moveFile(File source, File destinationFolder) throws Exception
    {
        logger.info("Move File[" + source.getPath() + "] to folder:" + destinationFolder.getPath());
        selectFile(source);
        cmdCopy();
        openFolderByTypingPath(destinationFolder);
        cmdMove();
    }

    public void replaceIfExists() throws Exception
    {
        Ldtp replaceDialog = new Ldtp("frmCopy");
        LdtpUtils.waitForWindowPartialName(replaceDialog, "frmCopy");
        replaceDialog.click("btnReplace");
        waitUntilWindowIsClosed("frmCopy");
    }

    /**
     * Copy a file from <source> to <destination>
     * 
     * @param source
     * @param destinationFolder
     */
    public void copyFile(File source, File destinationFolder) throws Exception
    {
        logger.info(String.format("Copy file {%s} to {%s}.", source.getPath(), destinationFolder.getPath()));
        selectFile(source);
        cmdCopy();
        openFolderByTypingPath(destinationFolder);
        cmdPaste();
    }

    /**
     * Rename <folder> with <newName>
     * 
     * @param file
     * @param newName
     */
    public void renameFile(File file, File newName) throws Exception
    {
        logger.info(String.format("Rename file {%s} to {%s}.", file.getPath(), newName));
        selectFile(file);
        getLdtp().generateKeyEvent("<enter>");
        cmdAll();
        getLdtp().generateKeyEvent(newName.getName());
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Restore any deleted files
     * 
     * @param filename
     * @deprecated
     */
    public void restoreDeletedFile(File filename) throws Exception
    {
        logger.info("Restore File from Trash:" + filename.getName());
        File deletedFile = new File(LdtpUtils.getTrashFolderLocation(), filename.getName());
        deleteFile(deletedFile); // on MAC: cmd+bkspace will do the trick and restore the file
    }

    /**
     * Empty the trash file Command-Shift-Option-Delete Empty Trash without confirmation dialog
     * 
     * @throws Exception
     */
    public void emptyTrash() throws Exception
    {
        logger.info("Empty Trash!");
        String trashWin = "Trash";
        openFolder(LdtpUtils.getTrashFolderLocation());
        if (isWindowOpened(trashWin))
        {
            cmdAll();
            // Command-Shift-Option-Delete Empty Trash without confirmation dialog
            getLdtp().generateKeyEvent("<command><shift><alt><bksp>");
        }
        else
        {
            throw new WindowNotOpenedException("Could not open Trash folder");
        }
    }

    /**
     * move content
     */
    public void moveContent(File contentToMove, File newLocation) throws Exception
    {
        if (contentToMove.isFile())
        {
            moveFile(contentToMove, newLocation);
        }
        else
        {
            moveFolder(contentToMove, newLocation);
        }
        getLdtp().setWindowName(newLocation.getName());
    }

    /**
     * copy Content content
     */
    public void copyContent(File contentToMove, File newLocation) throws Exception
    {
        if (contentToMove.isFile())
        {
            copyFile(contentToMove, newLocation);
        }
        else
        {
            copyFolder(contentToMove, newLocation);
        }
        getLdtp().setWindowName(newLocation.getName());
    }

    /**
     * rename content
     */
    public void renameContent(File orginialContent, File newName) throws Exception
    {
        if (orginialContent.isFile())
        {
            renameFile(orginialContent, newName);
        }
        else
        {
            renameFolder(orginialContent, newName);
        }
    }
    /**
     * delete content
     */
    public void deleteContent(File content)
    {
        getLdtp().waitTillGuiExist(content.getName());
        getLdtp().click(content.getName());
        cmdDelete();
    }

    public void clickSyncNow()
    {
        getLdtp().activateWindow(getWaitWindow());
        if(getLdtp().objectExist(syncExtension) == 0)
        {
            LdtpUtils.executeOnMac(String.format("pluginkit -e use -i %s", ALFRESCO_EXTENSION));
        }
        getLdtp().waitTillGuiExist(syncExtension);
        getLdtp().click(syncExtension);
        getLdtp().waitTillGuiExist(syncNowMenu);
        getLdtp().click(syncNowMenu);
    }
}

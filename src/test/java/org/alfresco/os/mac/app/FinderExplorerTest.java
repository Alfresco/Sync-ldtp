/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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

package org.alfresco.os.mac.app;

import java.io.File;
import java.io.IOException;

import org.alfresco.os.mac.app.FinderExplorer;
import org.alfresco.os.mac.app.FinderExplorer.LayoutView;
import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests of FinderExplorer class.
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class FinderExplorerTest
{
    private FinderExplorer f = new FinderExplorer();

    @BeforeMethod()
    private void openFinder()
    {
        f.openApplication();
    }

    @Test(groups = { "MacOnly" })
    public void testFinderWindow()
    {
        f.openFolder(new File(f.getApplicationPath()));
        Assert.assertTrue(f.isWindowOpened("Documents"), "Finder Window is opened on User's Documents folder");
    }

    @Test(groups = { "MacOnly" })
    public void testOpenFolder()
    {
        File testPath = new File(f.getApplicationPath());
        f.openFolder(testPath);
        Assert.assertTrue(f.isWindowOpened(testPath.getName()), testPath.getName() + " window was opened successfuly");
    }

    @Test(groups = { "MacOnly" })
    public void testSetViewLayout()
    {
        for (LayoutView layout : LayoutView.values())
        {
            f.setViewLayout(layout);
            Assert.assertTrue(f.isBtnEnabled(layout.getIdentifiler()), "Layout " + layout.name() + " is enabled");
        }
        f.setViewLayout(LayoutView.LIST);
    }

    @Test(groups = { "MacOnly" })
    public void testCreateFolder()
    {
        File testFolder = new File(f.getApplicationPath(), "testCreate");
        f.createFolder(testFolder);
        Assert.assertTrue(testFolder.exists(), "Folder: " + testFolder.getName() + " was created successfuly");
        testFolder.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testDeleteFolder()
    {
        File testFolder = new File(f.getApplicationPath(), "testDirectoryDelete");
        testFolder.mkdir();
        f.deleteFolder(testFolder);
        Assert.assertFalse(testFolder.exists(), "Folder was successfuly deleted");
        testFolder.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testMoveFolder()
    {
        File folderSource = new File(f.getApplicationPath(), "testMoveFolderSource");
        folderSource.mkdir();

        File folderDestination = new File(f.getApplicationPath(), "testMoveFolderDestination");
        folderDestination.mkdir();
        f.moveFolder(folderSource, folderDestination);

        File folderMoved = new File(folderDestination.getPath(), folderSource.getName());
        Assert.assertTrue(folderMoved.exists(), "Folder was successfully moved to source");
        Assert.assertFalse(folderSource.exists(), "Source folder was deleted due to move operation");

        folderMoved.delete();
        folderDestination.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testCopyFolder()
    {
        File folderSource = new File(f.getApplicationPath(), "testCopyFolder");
        folderSource.mkdir();
        File folderDestination = new File(f.getApplicationPath(), "testCopyFolderDestination");
        folderDestination.mkdir();

        File folderCopied = new File(folderDestination.getPath(), folderSource.getName());
        f.copyFolder(folderSource, folderDestination);
        Assert.assertTrue(folderCopied.exists(), "Folder was copied successfuly");
        Assert.assertTrue(folderSource.exists(), "Source folder exist");
        f.goToEnclosingFolder();

        folderCopied.delete();
        folderDestination.delete();
        folderSource.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testRenameFolder() throws InterruptedException
    {
        File folderSource = new File(f.getApplicationPath(), "testRenameFolder");
        folderSource.mkdir();
        File renamedFolder = new File(folderSource.getParentFile().getPath(), "renamedFolder");
        renamedFolder.delete();

        f.renameFolder(folderSource, renamedFolder.getName());

        Assert.assertTrue(renamedFolder.exists(), "Folder was successfully renamed");
        Assert.assertFalse(folderSource.exists(), "Orininal folder doesn't exist due to renaming operation");

        new File(folderSource.getParentFile().getPath(), "renamedFolder").delete();
    }

    @Test(groups = { "MacOnly" })
    public void testRestoreDeletedFolder() throws IOException
    {
        File folder = new File(f.getApplicationPath(), "FolderRestore");
        folder.mkdir();
        f.deleteFolder(folder);
        Assert.assertFalse(folder.exists(), "Folder was successfuly deleted");

        f.restoreDeletedFolder(folder);
        Assert.assertTrue(folder.exists(), "Folder was successfuly Restored");
    }

    @Test(groups = { "MacOnly" })
    public void testEmptyTrash()
    {
        File folder = new File(f.getApplicationPath(), "folderEmptyTrash");
        folder.mkdir();
        f.deleteFolder(folder);
        try
        {
            f.emptyTrash();
            Assert.assertFalse(LdtpUtils.getFolderFromTrash(folder.getName()).exists(), "Trash folder is empty");
        }
        catch (Exception e)
        {
            Assert.fail("Could not empty Trash folder", e);
        }
    }

    @Test(groups = { "MacOnly" })
    public void testGoEnclosingFolder()
    {
        File folderSource = new File(f.getApplicationPath(), "testEnclose");
        folderSource.mkdir();
        f.openFolder(folderSource);
        Assert.assertTrue(f.isWindowOpened(folderSource.getName()), "Browse successfully on " + folderSource.getName() + " Window");
        f.goToEnclosingFolder();
        Assert.assertTrue(f.isWindowOpened(folderSource.getParentFile().getName()), "Successfully browse to Enclosing Folder");
        folderSource.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testGoBack()
    {
        File folderSource = new File(f.getApplicationPath(), "testGoBack");
        folderSource.mkdir();
        f.openFolder(folderSource);
        f.goBack();
        Assert.assertTrue(f.isWindowOpened(folderSource.getParentFile().getName()), "Successfully browsed BACK in folders");
        folderSource.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testGoForward()
    {
        File folderSource = new File(f.getApplicationPath(), "testGoForward");
        folderSource.mkdir();
        f.openFolder(folderSource);
        f.goBack();
        f.goForward();
        Assert.assertTrue(f.isWindowOpened(folderSource.getName()), "Successfully browsed FORWARD in folders");
        folderSource.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testGoActivate()
    {
        File folderSource = new File(f.getApplicationPath());
        f.openFolder(folderSource);
        f.goToEnclosingFolder();
        f.goActivate();
        Assert.assertTrue(f.isWindowOpened(folderSource.getName()), "Successfully used ACTIVATION in folders");
    }

    @Test(groups = { "MacOnly" })
    public void testSelectFile() throws IOException
    {
        File fileTest = new File(f.getApplicationPath(), "testSelectFile.txt");
        fileTest.createNewFile();
        f.selectFile(fileTest);
        fileTest.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testDeleteFile() throws IOException
    {
        File fileTest = new File(f.getApplicationPath(), "testDeleteFile.txt");
        fileTest.createNewFile();
        f.deleteFile(fileTest);
        Assert.assertFalse(fileTest.exists(), "File was successfully deleted");
    }

    @Test(groups = { "MacOnly" })
    public void testMoveFile() throws IOException
    {
        File fileSource = new File(f.getApplicationPath(), "testMoveFile.rtf");
        fileSource.createNewFile();
        File folderDestination = new File(f.getApplicationPath(), "movedFiles");
        folderDestination.mkdir();
        File fileDestination = new File(folderDestination, fileSource.getName());

        f.moveFile(fileSource, folderDestination);
        Assert.assertFalse(fileSource.exists(), "Source file doesn't exist due to move operation");
        Assert.assertTrue(fileDestination.exists(), "Destination file exist due to move operation");

        f.deleteFolder(folderDestination);
    }

    @Test(groups = { "MacOnly" })
    public void testCopyFile() throws IOException
    {
        File fileSource = new File(f.getApplicationPath(), "testCopyFile.rtf");
        fileSource.createNewFile();
        File destinationFolder = new File(f.getApplicationPath(), "copiedFiles");
        destinationFolder.mkdir();

        File fileDestination = new File(destinationFolder, fileSource.getName());
        f.copyFile(fileSource, destinationFolder);

        Assert.assertTrue(fileDestination.exists(), "File was copied successfully");
        f.deleteFolder(destinationFolder);
        fileSource.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testRenameFile() throws IOException
    {
        File fileSource = new File(f.getApplicationPath(), "testOriginalFile.rtf");
        fileSource.createNewFile();
        File fileRenamed = new File(f.getApplicationPath(), "testRenamedFile.rtf");
        f.renameFile(fileSource, fileRenamed.getName());
        Assert.assertFalse(fileSource.exists(), "Original file doesn't exist due ro renaming operation");
        Assert.assertTrue(fileRenamed.exists(), "File was successfuly renamed");
        f.deleteFile(fileRenamed);
    }

    @Test(groups = { "MacOnly" })
    public void testRestoreDeletedFile() throws IOException
    {
        File fileSource = new File(f.getApplicationPath(), "testRestore.rtf");
        fileSource.createNewFile();
        f.deleteFile(fileSource);
        Assert.assertFalse(fileSource.exists(), "File was successfuly deleted");

        f.restoreDeletedFile(fileSource);
        Assert.assertTrue(fileSource.exists(), "File was successfuly Restored");
    }
}
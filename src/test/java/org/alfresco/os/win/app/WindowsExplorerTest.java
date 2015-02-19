package org.alfresco.os.win.app;

import java.io.File;
import java.io.IOException;

import org.alfresco.os.AbstractTestClass;
import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WindowsExplorerTest extends AbstractTestClass
{
    WindowsExplorer app = new WindowsExplorer();

    @Test
    public void testOpenFolder() throws Exception
    {
        File myDocs = LdtpUtils.getHomeFolder();
        app.openApplication();
        app.openFolder(myDocs);
        app.exitApplication();
    }

    @Test
    public void testGoBack() throws Exception
    {
        File myDocs = LdtpUtils.getHomeFolder();
        app.openApplication();
        app.openFolder(myDocs);
        app.goBack("Documents");
        app.exitApplication();
    }

    @Test
    public void testCreateNewFolderMenu()
    {
        File createFolder = new File(app.getApplicationPath(), "TestCreateNewFolder");

        app.openApplication();
        app.createNewFolderMenu(createFolder.getName());
        Assert.assertTrue(createFolder.exists(), "Folder was successfuly created");

        createFolder.delete();
        app.focus();
        app.exitApplication();
    }

    @Test
    public void testCreateAndOpenFolder()
    {
        File createFolder = new File(app.getApplicationPath(), "TestCreateAndOpen");
        app.openApplication();
        app.createAndOpenFolder(createFolder.getName());
        app.exitApplication();
        createFolder.delete();
    }

    @Test
    public void testOpenFolderFromCurrent()
    {
        app.openApplication();
        app.openFolderFromCurrent("Music");
        app.exitApplication();
    }

    @Test
    public void testActivateApplicationWindow()
    {
        app.openApplication();
        app.activateApplicationWindow("Documents");
        app.exitApplication();
    }

    @Test
    public void testOpenFile() throws Exception
    {
        File fileTest = getRandomTestFile("testFile.txt");
        fileTest.createNewFile();

        app.openApplication();
        app.openFile(fileTest);

        boolean isFileOpen = app.isWindowOpened(fileTest.getName());
        Assert.assertTrue(isFileOpen, "File was successfuly opened");
        LdtpUtils.killProcessByWindowName("notepad.exe");
        app.exitApplication();
    }

    @Test
    public void testRenameFile() throws Exception
    {
        File fileTest = getRandomTestFile("testFile.txt");
        String renameFileName = "renamed" + System.currentTimeMillis() + ".txt";
        fileTest.createNewFile();

        app.openApplication();
        app.openFolder(fileTest.getParentFile());
        app.rename(fileTest.getName(), renameFileName);

        File renamedFile = new File(fileTest.getParentFile(), renameFileName);
        Assert.assertTrue(renamedFile.exists(), "File was successfuly renamed");
        renamedFile.delete();
        fileTest.delete();
        app.exitApplication();
    }

    @Test
    public void testOpenFileInCurrentFolder() throws IOException
    {
        File file = new File(LdtpUtils.getDocumentsFolder(), "testOpenFileInCurrentFolder.txt");
        file.createNewFile();

        app.openApplication();
        app.openFileInCurrentFolder(file);

        LdtpUtils.waitToLoopTime(1);
        boolean isFileOpen = app.isWindowOpened(file.getName());
        Assert.assertTrue(isFileOpen, "File was successfuly opened");

        LdtpUtils.killProcessByWindowName("notepad.exe");
        app.exitApplication();
    }

    @Test
    public void testDeleteFile() throws Exception
    {
        File file = new File(LdtpUtils.getDocumentsFolder(), "testDeleteFile.txt");
        file.createNewFile();
        app.openApplication();
        app.deleteFile(file, true);

        Assert.assertFalse(file.exists(), "File was successfuly deleted");
        app.exitApplication();
    }

    @Test
    public void testMoveFolderInCurrent() throws Exception
    {
        File folderSource = new File(LdtpUtils.getDocumentsFolder(), "folderSource");
        folderSource.mkdir();

        File folderDestination = new File(LdtpUtils.getDocumentsFolder(), "folderDestination");
        folderDestination.mkdir();

        app.openApplication();
        File ok = app.moveFolder(folderSource, folderDestination);

        Assert.assertTrue(ok.exists(), "Folder was successfuly moved to another destination");
        app.exitApplication();
    }
}

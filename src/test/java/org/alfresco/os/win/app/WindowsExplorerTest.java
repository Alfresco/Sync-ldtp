package org.alfresco.os.win.app;

import java.io.File;

import org.alfresco.os.AbstractTestClass;
import org.alfresco.os.win.Application.type;
import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WindowsExplorerTest extends AbstractTestClass
{
    WindowsExplorer app = new WindowsExplorer();

    @Test
    public void testOpenFolder() throws Exception
    {
        File myDocs = LdtpUtils.getDocumentsFolder();
        app.openApplication();
        app.openFolder(myDocs);
        
        app.rightClickCreate(myDocs.getName(), "test1.txt", type.TEXTFILE);        
        app.exitApplication();
    }

    @Test
    public void testGoBack() throws Exception
    {
        File myDocs = LdtpUtils.getDocumentsFolder();
        app.openApplication();
        app.openFolder(myDocs);
        app.goBack("This PC");
        app.exitApplication();
    }

    @Test
    public void testCreateNewFolderMenu() throws Exception
    {
        File createFolder = new File(LdtpUtils.getDocumentsFolder(), "TestCreateNewFolder");

        app.openApplication();
        app.openFolder(createFolder.getParentFile());
        app.createNewFolderMenu(createFolder.getName());
        Assert.assertTrue(createFolder.exists(), "Folder was successfuly created");

        createFolder.delete();
        app.exitApplication();
    }

    @Test
    public void testCreateAndOpenFolder() throws Exception
    {
        File createFolder = new File(LdtpUtils.getDocumentsFolder(), "TestCreateAndOpen");
        app.openApplication();
        app.openFolder(createFolder.getParentFile());
        app.createAndOpenFolder(createFolder.getName());
        app.exitApplication();
        createFolder.delete();
    }

    @Test
    public void testOpenFolderFromCurrent() throws Exception
    {
        app.openApplication();
        File createFolder = new File(LdtpUtils.getDocumentsFolder(), "testOpenFolderFromCurrent");
        createFolder.mkdir();
        app.openFolder(createFolder.getParentFile());
        app.openFolderFromCurrent(createFolder.getName());
        app.exitApplication();
    }

    @Test
    public void testOpenFile() throws Exception
    {
        File fileTest = getRandomTestFile("testFile.txt");
        fileTest.createNewFile();

        app.openApplication();
        app.openFile(fileTest);
        app.getLdtp().waitTime(2);
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
    public void testOpenFileInCurrentFolder() throws Exception
    {
        File file = new File(LdtpUtils.getDocumentsFolder(), "testOpenFileInCurrentFolder.txt");
        file.createNewFile();

        app.openApplication();
        app.openFolder(file.getParentFile());
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
        
        LdtpUtils.waitUntilFileExistsOnDisk(file);
        app.openApplication();
        app.deleteFile(file, false);
        app.getLdtp().waitTime(3);
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
        app.openFolder(folderSource.getParentFile());
        File ok = app.moveContent(folderSource, folderDestination);

        Assert.assertTrue(ok.exists(), "Folder was successfuly moved to another destination");
        app.exitApplication();
    }
}

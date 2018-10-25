package org.alfresco.os.win.app.misc;

import java.io.File;

import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;
import com.google.common.io.Files;

/**
 * Covers Properties Dialog of a File or Folder
 * 
 * @author Paul Brodner
 */
public class PropertiesDialog
{

    public PropertiesDialog(File fileName)
    {
        this.filename = fileName;
    }

    protected static Logger logger = Logger.getLogger(PropertiesDialog.class);
    private File filename;
    private Ldtp propertyObject;

    /**
     * @return the File under test
     */
    public File getFileName()
    {
        return this.filename;
    }

    /**
     * Opens a tab from Property Dialog
     * 
     * @param tabName
     * @throws Exception
     */
    public void openTab(String tabName) throws Exception
    {
        logger.info("Opening TAB:" + tabName);

        if (propertyObject == null)
        {
            logger.error("First openDialog() in your code, prior to opening a tab.");
            throw new Exception("First openDialog() of Properties object");
        }
        propertyObject.waitTillGuiExist(tabName);
        propertyObject.selectTab("*", tabName);
    }

    /**
     * Open the Properties Dialog of a File/Folder
     */
    public Ldtp openDialog()
    {
        String fileList = null;
        logger.info("Opening Properties Dialog for: " + getFileName().getPath());
        String fileWithoutExtension = Files.getNameWithoutExtension(getFileName().getName());
        logger.info("getFileName().getParentFile().getName() " + getFileName().getParentFile().getName());
        propertyObject = new Ldtp(getFileName().getParentFile().getName());
        if (getFileName().isFile())
        {
            fileList = "lst" + fileWithoutExtension + ".*";
        }
        else
        {
            fileList = fileWithoutExtension;
        }
        propertyObject.mouseLeftClick(fileList);
        propertyObject.mouseRightClick(fileList);
        propertyObject.setWindowName("Context");
        propertyObject.selectMenuItem("Properties");
        propertyObject.setWindowName("dlg" + fileWithoutExtension + ".*");
        propertyObject.waitTillGuiExist();
        propertyObject.activateWindow("dlg" + fileWithoutExtension + ".*");
        return propertyObject;
    }

    /**
     * @return the LDTP propertyObject
     */
    public Ldtp getLdtp()
    {
        return propertyObject;
    }

    /**
     * @return the screenshot of this Property Dialog
     */
    public File getScreenshot()
    {
        return new File(getLdtp().imageCapture("dlg" + Files.getNameWithoutExtension(getFileName().getName()) + ".*"));
    }

    public void clickCancel()
    {
        getLdtp().click("Cancel");
    }

    public void clickOk()
    {
        getLdtp().click("Ok");
    }
}

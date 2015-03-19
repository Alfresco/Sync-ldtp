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

package org.alfresco.os.mac;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.alfresco.exceptions.OfficeCrashException;
import org.alfresco.os.mac.utils.AppleMenuBar;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;

/**
 * This class will add a mix of methods used on Editors applications,like Edit document, save it, close it.
 * This class should be extended by any other Desktop Editor application.
 * Save a document As, etc.
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public abstract class Editor extends Application
{
    protected String fileName = "NOT DEFINED"; // this is the filename from Editor
    private AppleMenuBar appleMenu;

    protected AppleMenuBar getAppleMenu()
    {
        if (appleMenu == null)
            appleMenu = new AppleMenuBar(new Ldtp(getWaitWindow()));

        return appleMenu;
    }

    /**
     * This will just edit the opened document
     * 
     * @param data
     * @throws LdtpExecutionError
     */
    public void edit(String data) throws LdtpExecutionError
    {
        logger.info("Editing document:" + data);
        getLdtp().enterString(data);
    }

    /**
     * Save the document
     */
    public void save()
    {
        logger.info("Save document.");
        getLdtp().generateKeyEvent("<command>s");
    }

    /**
     * Method to close Application
     */
    public void close(String filename)
    {
        logger.info("Closing filename:" + filename);
        getLdtp().closeWindow(filename);
    }

    /**
     * Method to close Application
     */
    public void close(File filename)
    {
        logger.info("Closing filename:" + filename.getName());
        getLdtp().closeWindow(filename.getName());
    }

    public void closeDontSave(String filename)
    {
        logger.info("Close editor and not save the docuemnt.");
        focus();
        close(filename);
        getLdtp().waitTime(1);
        getLdtp().click("*.btnDon.*");
    }

    /**
     * Focus Editor based by filename.
     * 
     * @param filename
     * @throws Exception
     */
    public void focus(String filename)
    {
        logger.info("Focus: " + filename);
        getLdtp().activateWindow(filename);
    }

    /**
     * @return the fileName of the active file
     */
    protected String getFileName()
    {
        return fileName;
    }

    /**
     * Set the fileName of the active file
     * 
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Just Close and Save the current file
     */
    public void saveAndClose()
    {
        logger.info("Start a Save and Close.");
        close(getFileName());
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            logger.error("Error encounterd on Save: " + e.getMessage());
        }

        getLdtp().generateKeyEvent("<enter>");
    }

    public void save(String location) throws Exception
    {
        logger.info("Save document to: " + location);
        close(getFileName());
        LdtpUtils.waitToLoopTime(1);
        getLdtp().generateKeyEvent("<command>a");
        LdtpUtils.waitToLoopTime(1);
        getLdtp().generateKeyEvent(location);
        getLdtp().generateKeyEvent("<enter>");
    }

    public void saveAs(File file)
    {
        logger.info("Save document as: " + file.getPath());
        getLdtp().generateKeyEvent("<command><shift>s");

        // add folder based on location
        getLdtp().generateKeyEvent("<command>a");
        getLdtp().enterString(file.getParent());
        getLdtp().generateKeyEvent("<enter>");

        // now add filename
        getLdtp().enterString(file.getName());
        getLdtp().generateKeyEvent("<enter>");
    }

    public void openFromFileMenu(File file) throws LdtpExecutionError
    {
        logger.info("Open file from Menu: " + file.getPath());
        focus();
        setFileName(file.getName());
        getLdtp().generateKeyEvent("<command>o");
        // go to parent directory
        getLdtp().enterString(file.getPath());
        getLdtp().waitTime(1);
        getLdtp().generateKeyEvent("<enter>");
        getLdtp().waitTime(1);
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Using the AppleMenu for navigation
     */
    public void goToFile()
    {
        logger.info("Go to File");
        getAppleMenu().activate();
        getAppleMenu().open(AppleMenuBar.Menu.FILE);
    }

    public void closeFile()
    {
        logger.info("Just close the current file.");
        focus();
        getLdtp().generateKeyEvent("<command>q");
    }

    @Override
    public void exitApplication()
    {
        try
        {
            logger.debug("Exiting Application and all processes: " + getApplicationName());
            killProcess();
        }

        catch (Exception e)
        {
            logger.error("Exception on exitApplication: " + e.getMessage());
        }
    }

    /**
     * Check if the application crashed, due to MNT-12847
     * 
     * @return boolean value
     */
    public boolean applicationCrashedReportShown()
    {
        return Arrays.asList(getLdtp().getWindowList()).contains("frmProblemReportforMicrosoftErrorReporting");
    }

    public boolean applicationCrashedDontSentShown()
    {
        return Arrays.asList(getLdtp().getWindowList()).contains("frmMicrosoftErrorReporting");
    }

    /**
     * Will cleanup the crash and Problem Reporter
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public void handleCrash() throws Exception
    {
        initializeLdtp();
        if (applicationCrashedReportShown())
        {
            LdtpUtils.killProcessByWindowName("Problem Reporter");
            killProcess();
            throw new OfficeCrashException(getApplicationName() + " " + getApplicationVersion());
        }
        if (applicationCrashedDontSentShown())
        {
            logger.info("Crash Occured: Closing MIcrosoft Error Don't Sent Windows");
            LdtpUtils.killProcessByWindowName("Microsoft Error Reporting");
            killProcess();
            throw new OfficeCrashException(getApplicationName() + " " + getApplicationVersion());
        }
    }

    public boolean isFileOpened(String filename)
    {
        return Arrays.asList(getLdtp().getWindowList()).contains("frm" + filename);
    }

    /**
     * This will wait for a couple of times, until the file is closed
     */
    public void waitUntilFileCloses(String filename)
    {
        logger.info("Wait until the file: " + filename + " is closed.");
        int retries = 1;
        while (retries <= LdtpUtils.RETRY_COUNT && isFileOpened(filename))
        {
            retries++;
            getLdtp().waitTime(1);
        }
    }

    public Ldtp waitForWindow(String windowName) throws Exception
    {
        Ldtp tmp_ = super.waitForWindow(windowName);
        setFileName(windowName);
        return tmp_;
    }

    /**
     * This will wait for a file, until it exist on disk or a retry count is reached.
     * 
     * @param filePath
     */
    protected void waitForFileOnDisk(File filePath)
    {
        LdtpUtils.logInfo("Waiting until file: " + filePath.getPath() + " exists on disk.");
        LdtpUtils.waitUntilFileExistsOnDisk(filePath);
    }
}

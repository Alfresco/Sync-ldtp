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

package org.alfresco.os.win.app.office;

import java.io.File;

import org.alfresco.os.common.ApplicationBase;
import org.alfresco.os.win.Application;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;
import org.apache.log4j.Logger;

/**
 * This class has all the method involved in using the actions in Office application
 *
 * @author Subashni Prasanna
 * @author Paul Brodner
 */
/**
 * @author qa
 *
 */
public class MicrosoftOfficeBase extends Application
{
    private static Logger logger = Logger.getLogger(MicrosoftOfficeBase.class);
    public String OFFICE_PATH = "C:\\Program Files (x86)\\Microsoft Office\\Office14";
    protected VersionDetails applicationDetails;
    protected String fileMenuPage = "File";
    protected String userName;
    protected String userPassword;

    public MicrosoftOfficeBase(VersionDetails officeApplication, String version)
    {
        applicationDetails = officeApplication;
        setApplicationName(applicationDetails.getName());
        setApplicationVersion(version);
        setApplicationPath(new File(getOfficePath(), applicationDetails.getExeName()).getPath());
        setWaitWindow(applicationDetails.getWaitWindow());
    }

    /**
     * Enums to hold all the applications name.
     *
     * @author Subashni Prasanna
     * @author Paul Brodner
     */
    public enum VersionDetails
    {

        WORD("Microsoft Word Document", "Microsoft Word", "WINWORD.EXE", "Blank document"), EXCEL(
                "Microsoft Excel Worksheet",
                "Microsoft Excel",
                "EXCEL.EXE",
                "Blank workbook"), POWERPOINT("Microsoft PowerPoint Presentation", "Microsoft PowerPoint", "POWERPNT.EXE", "Blank Presentation"), OUTLOOK(
                "Outlook",
                "Microsoft Outlook",
                "OUTLOOK.exe",
                "TBD");

        private String application;
        private String waitWindow;
        private String exeName;
        private String blank;

        private VersionDetails(String type, String waitWindow, String exeName, String blank)
        {
            this.application = type;
            this.waitWindow = waitWindow;
            this.exeName = exeName;
            this.blank = blank;
        }

        public String getName()
        {
            return application;
        }

        public String getWaitWindow()
        {
            return waitWindow;
        }

        public String getExeName()
        {
            return exeName;
        }

        public String getBlankDoc()
        {
            return blank;
        }
    }

    /*
     * Need to use the EXE NAME in order to kill the application
     */
    public void killProcess()
    {
        LdtpUtils.execute(new String[] { "taskkill", "/F", "/IM", applicationDetails.getExeName() });
    }

    /**
     * Close Application from Close button
     */
    public void closeApplication()
    {
        getLdtp().generateKeyEvent("<esc>");
        getLdtp().click("Close");
        setWaitWindow(applicationDetails.getWaitWindow());
    }

    /**
     * Close Application from Close button, based on filename already opened
     * @param file
     */
    public void closeApplication(File file)
    {
        focus(file);
        getLdtp().click("btnFileTab");
        getLdtp().waitTillGuiExist("File", LdtpUtils.RETRY_COUNT);
        getLdtp().click("Exit");
        setWaitWindow(applicationDetails.getWaitWindow());
//        closeApplication();
    }

    /**
     * Focus application based on Filename
     * You can pass as arguments just Files or Strings
     *
     * @param excelFileTitle
     */
    public void focus(Object fileName)
    {
        String _waitFor = "";
        try
        {
            if (fileName instanceof File)
            {
                _waitFor = ((File) fileName).getName();
            }
            else if (fileName instanceof String)
            {
                _waitFor = fileName.toString();
            }
            waitForApplicationWindow(_waitFor, true);
            getLdtp().activateWindow(getWaitWindow());
        }
        catch (Exception e)
        {
            logger.error("Could not find window: " + fileName + " to focus!");
        }
    }

    public ApplicationBase openApplication()
    {
        try
        {
            openApplication(new String[] { getApplicationPath() });
            getLdtp().generateKeyEvent("<esc>");
        }
        catch (Exception e)
        {
            logger.error("Could not open Application " + getApplicationName() + "Error: " + e);
        }
        return this;
    }

    /*
     * Method to implement adding a data inside the Office application based on the application
     */
    public void editOffice(String data)
    {
        getLdtp().generateKeyEvent(data);
    }

    public void saveOffice() throws LdtpExecutionError
    {
        getLdtp().generateKeyEvent("<ctrl><s>");
    }

    /*
     * Method to implement Save for the first time
     */
    public void saveOffice(String location) throws Exception
    {
        getLdtp().click("btnSave");
        waitForWindow("Save As");
        getLdtp().deleteText("txtFilename", 0);
        getLdtp().enterString("txtFilename", location);
        getLdtp().click("btnSave");
        getLdtp().waitTillGuiNotExist("Save As");
    }

    /*
     * Method to implement Opening a file inside the office application
     */
    public void openOfficeFromFileMenu(String location) throws Exception
    {
        goToFile();
        getLdtp().click("Open");
        waitForWindow("dlgOpen");
        getLdtp().activateWindow("dlgOpen");
        getLdtp().enterString("txtFilename", location);
        getLdtp().mouseLeftClick("uknOpen");
    }

    /**
     * Click in File
     *
     * @param ldtp
     */
    public void goToFile()
    {
        LdtpUtils.logInfo("Go to File");
        getLdtp().click("btnFileTab");
        getLdtp().waitTillGuiExist("File", LdtpUtils.RETRY_COUNT);
    }

    /**
     * Set the name and password on Windows Security
     *
     * @param userName
     * @param password
     */
    public void operateOnSecurity(String userName, String password)
    {
        LdtpUtils.logInfo("Waiting for... 'Windows Security' window");
        String _oldWaitWindow = getWaitWindow(); // retain the old window wait, because this will be overwritten on waitForWindow

        // "Windows Security"
        try
        {
            if (waitForWindow("Security") != null)
            {
                LdtpUtils.logInfo("'Windows Security' found. Type user/password and click OK on 'Windows Security'");
                getLdtp().deleteText("txtUsername", 0);
                getLdtp().enterString("txtUsername", userName);
                getLdtp().enterString("txtPassword", password);
                getLdtp().click("OK");
                setWaitWindow(_oldWaitWindow); // just to regain the old window LDTP string
            }
        }
        catch (Exception e)
        {
            logger.error("Could not operate on Security", e);
        }
    }

    /**
     * Set a comment on Check In window
     *
     * @param Ldtp
     * @param comment
     * @param checkBoxState
     */
    public void operateOnCheckIn(String comment, boolean checkBoxState)
    {
        LdtpUtils.logInfo("Type a comment and click OK on 'Check In'");

        getLdtp().waitTime(2);

        if (!comment.isEmpty())
        {
            getLdtp().enterString("txtVersionComments", comment);
        }
        if (checkBoxState == true)
        {
            getLdtp().check("chkKeepthedocumentcheckedoutaftercheckinginthisversion");
        }
        getLdtp().mouseLeftClick("btnOK");

        getLdtp().waitTime(2);
    }

    /***
     * @param filename
     */
    public void operateOnOpen(String path, String siteName, String fileName, String userName, String password)
    {
        activateDialog("dlgOpen");
        getLdtp().enterString("txtFilename", path);
        getLdtp().mouseLeftClick("uknOpen");
        operateOnSecurity(userName, password);
        LdtpUtils.waitObjectHasValue(getLdtp(), "txtFilename", "");
        getLdtp().enterString("txtFilename", siteName.toLowerCase());
        getLdtp().mouseLeftClick("uknOpen");
        LdtpUtils.waitObjectHasValue(getLdtp(), "txtFilename", "");
        getLdtp().enterString("txtFilename", "documentLibrary");
        getLdtp().mouseLeftClick("uknOpen");
        operateOnSecurity(userName, password);
        LdtpUtils.waitObjectHasValue(getLdtp(), "txtFilename", "");
        getLdtp().enterString("txtFilename", fileName);
        getLdtp().mouseLeftClick("uknOpen");
        operateOnSecurity(userName, password);
        getLdtp().waitTime(5);
    }

    /**
     * Helper method that will activate the Open dialog window
     */
    public void activateDialog(String dialogObjectName)
    {
        LdtpUtils.logInfo("Activate '" + dialogObjectName + "' dialog");

        try
        {
            if (waitForWindow(dialogObjectName) == null)
            {
                throw new LdtpExecutionError("Cannot find the diaglog: " + dialogObjectName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        getLdtp().activateWindow(getWaitWindow()); // getWaitWindow defined on LDTP
    }

    /***
     * Overloads the operateOnOpen to open a document already created on the client machine
     *
     * @param filename
     */
    public void operateOnOpen(String path, String fileName)
    {
        activateDialog("dlgOpen");
        getLdtp().enterString("txtFilename", path);
        getLdtp().mouseLeftClick("uknOpen");
        LdtpUtils.waitObjectHasValue(getLdtp(), "txtFilename", "");
        getLdtp().enterString("txtFilename", fileName);
        getLdtp().mouseLeftClick("uknOpen");
        getLdtp().waitTime(2);
    }

    /**
     * Set a comment on Save As window
     *
     * @param path
     *            SharePoint Path where to save the file
     */
    public void operateOnSaveAs(String path, String siteName, String fileName, String userName, String password)
    {
        activateDialog("Save As");

        getLdtp().enterString("txtFilename", path);
        getLdtp().mouseLeftClick("btnSave");

        operateOnSecurity(userName, password);

        String siteObject = siteName.replace(".", "").replace("_", "");

        LdtpUtils.waitForObject(getLdtp(), siteObject.toLowerCase());

        getLdtp().doubleClick(siteObject.toLowerCase());

        operateOnSecurity(userName, password);

        LdtpUtils.waitForObject(getLdtp(), "lstdocumentLibrary");
        getLdtp().doubleClick("lstdocumentLibrary");

        String addressBarObject = "tbarAddress" + path.replace(":", "").replace(".", "") + "/" + siteName.replace(".", "").replace("_", "").toLowerCase() + "/"
                + "documentLibrary";
        LdtpUtils.waitForObject(getLdtp(), addressBarObject);
        getLdtp().enterString("txtFilename", fileName);
        getLdtp().mouseLeftClick("btnSave");

        operateOnConfirmSaveAs();

        operateOnSecurity(userName, password);

        getLdtp().waitTime(5);
    }

    /**
     * Operate on Save As window by giving the fullpath to the fileName
     *
     * @param path
     *            Path where to save the file
     */
    public void operateOnSaveAsWithFullPath(String path, String fileName, String userName, String password)
    {
        activateDialog("Save As");
        operateOnSecurity(userName, password);
        getLdtp().activateWindow(getWaitWindow());
        getLdtp().enterString("txtFilename", path);
        getLdtp().mouseLeftClick("btnSave");
        operateOnSecurity(userName, password);
        String addressBarObject = "tbarAddress" + path.substring(0, path.length() - 1).replace(":", "");
        LdtpUtils.waitForObject(getLdtp(), addressBarObject);
        getLdtp().enterString("txtFilename", fileName);
        getLdtp().mouseLeftClick("btnSave");
        operateOnConfirmSaveAs();
        operateOnSecurity(userName, password);
        getLdtp().waitTime(5);
    }

    //
    /**
     * Click on 'Yes' button from 'Confirm Save As'
     *
     * @param ldtp1
     * @throws Exception
     */
    public void operateOnConfirmSaveAs()
    {
        LdtpUtils.logInfo("Operate on Confirming Save As...");

        try
        {
            waitForWindow("Confirm Save As");
            getLdtp().activateWindow("Confirm Save As");
            getLdtp().click("Yes");
            waitUntilWindowIsClosed("Confirm Save As");
            LdtpUtils.waitToLoopTime(2);
        }
        catch (Exception e)
        {
            logger.error("Error Confirming Save As dialog", e);
        }
    }

    /**
     * This method creates a new meeting workspace in Outlook 2010
     *
     * @param sharePointPath - path for SharePoint
     * @param siteName - name of the site
     * @param location - location for the meeting
     * @param userName
     * @param password
     */
    public void operateOnCreateNewMeetingWorkspace(String sharePointPath, String siteName, String location, String userName, String password, boolean withSubject, boolean clickRemove)
    {

        LdtpUtils.logInfo("Start creating new meeting workspace");
        getLdtp().click("btnMeeting");

        activateDialog("Untitled");
        getLdtp().click("btnMeetingWorkspace");
        getLdtp().click("hlnkChangesettings");
        getLdtp().waitTime(2);
        getLdtp().selectItem("cboWebsiteDropdown", "Other...");

        activateDialog("Other Workspace Server");
        getLdtp().deleteText("txtServerTextbox", 0);
        getLdtp().enterString("txtServerTextbox", sharePointPath);
        getLdtp().click("btnOK");
        getLdtp().waitTime(3);

        activateDialog("Windows Security");
        operateOnSecurity(userName, password);
        getLdtp().click("chkAlldayevent");

        activateDialog("Untitled");
        getLdtp().click("btnOK");

        if (withSubject == true)
        {
            getLdtp().enterString("txtLocation", location);
            getLdtp().enterString("txtSubject", siteName);
        }
        else
        {
            getLdtp().enterString("txtLocation", location);
        }

        if (withSubject == true)
        {
            LdtpUtils.logInfo("Creating the event");
            getLdtp().click("btnCreate");

            getLdtp().waitTime(4);

            // first verification
            operateOnSecurity(userName, password);
            // second verification
            operateOnSecurity(userName, password);

            if (clickRemove == true)
            {
                activateDialog(siteName);
                getLdtp().doubleClick("btnRemove");

                activateDialog("Microsoft Outlook");
                getLdtp().click("btnYes");
            }
        }
        else
        {
            // Your attempt to create a Meeting Workspace or link to an existing one can't be completed.
            // Reason: Site name is not specified. Please fill up subject field.
            getLdtp().click("btnCreate");
            getLdtp().waitTime(4);
            operateOnSecurity(userName, password);
            LdtpUtils.logInfo("Error when subject is not filled");
        }
    }

    /**
     * This method creates a new meeting workspace in Outlook 2010
     *
     * @param sharePointPath - path for SharePoint
     * @param siteName - name of the site
     * @param location - location for the meeting
     * @param userName
     * @param password
     * @throws Exception
     */
    public void operateOnRecurrenceAppointment(String startDate, String endDate, String noOfOccurences) throws Exception
    {
        waitForWindow("Appointment");
        getLdtp().deleteText("txtStart", 0);
        getLdtp().enterString("txtStart", startDate);
        getLdtp().deleteText("txtEnd", 0);
        getLdtp().enterString("txtEnd", endDate);
        getLdtp().click("rbtnDaily");
        getLdtp().waitTime(1);

        getLdtp().click("rbtnEndafter");
        getLdtp().deleteText("txtEndafterEditableTextoccurences", 0);
        getLdtp().enterString("txtEndafterEditableTextoccurences", noOfOccurences);
        getLdtp().click("btnOK");
    }

    /**
     * This method creates meeting workspace in Outlook 2010 from an existing Site
     *
     * @param sharePointPath - path for SharePoint
     * @param siteName - name of the site
     * @param subject
     * @param location - location for the meeting
     * @param userName
     * @param password
     */
    public void operateOnLinkToExistingWorkspace(String sharePointPath, String siteName, String subject, String location, String userName, String password, boolean withSubject)
    {
        // click meeting
        getLdtp().click("btnMeeting");

        // set focus on new window
        activateDialog("Untitled");
        getLdtp().click("btnMeetingWorkspace");

        getLdtp().waitTime(2);
        getLdtp().click("hlnkChangesettings");
        getLdtp().click("rbtnLinktoanexistingworkspace");

        getLdtp().mouseLeftClick("cboWorkspaceDropdown");
        getLdtp().waitTime(4);

        operateOnSecurity(userName, password);
        getLdtp().selectItem("cboWorkspaceDropdown", siteName);
        getLdtp().click("btnOK");
        getLdtp().click("chkAlldayevent");

        activateDialog("Untitled");
        if (withSubject == true)
        {
            getLdtp().enterString("txtLocation", location);
            getLdtp().enterString("txtSubject", subject);
        }
        else
        {
            getLdtp().enterString("txtLocation", location);
        }

        if (withSubject == true)
        {
            // click Link button
            getLdtp().click("btnLink");
            getLdtp().waitTime(4);
            operateOnSecurity(userName, password);
        }
        else
        {
            // Your attempt to create a Meeting Workspace or link to an existing one can't be completed.
            // Reason: Site name is not specified. Please fill up subject field.
            activateDialog("Untitled");

            LdtpUtils.logInfo("Error when subject is not filled");

            // click Link button
            getLdtp().click("btnLink");
            getLdtp().waitTime(4);
            operateOnSecurity(userName, password);
        }
    }

    /**
     * Method to open a file from the CIFS path
     */
    public Ldtp openFileFromCMD(String docPath, String fileName, String userName, String password, boolean checkSecurityWindow)
    {
        try
        {
            runProcess(new File(OFFICE_PATH, applicationDetails.getExeName()).getPath() + " " + docPath);
            if (checkSecurityWindow)
                operateOnSecurity(userName, password);

            waitForWindow(fileName);
            return new Ldtp(fileName);

        }
        catch (Exception ee)
        {
        }
        return null;
    }

    /**
     * Get the CIFS path from property
     */
    public String getCIFSPath()
    {
        return getProperty("cifs.path");
    }

    /**
     * Get the map drive path from property
     */
    public String getMapDriver()
    {
        String val = getProperty("network.map.driver");
        return ((val == null) ? "Z:" : val);
    }

    /**
     * Get the map path from property
     */
    public String getMapPath()
    {
        String val = getProperty("network.map.path");
        return ((val == null) ? "\\\\localhost:8080\\alfresco\\" : val);
    }

    /**
     * get the path of the office on client machine
     *
     * @return
     */
    protected String getOfficePath()
    {
        String val = getProperty("win.office" + getApplicationVersion() + ".path");
        return ((val == null) ? OFFICE_PATH : val);
    }

    /**
     * get the sharepoint path
     *
     * @return
     */
    public String getSharePointPath()
    {
        String val = getProperty("sharepoint.path");
        return ((val == null) ? "http://localhost:7070/alfresco" : val);
    }

    public void saveAsOffice(String path) throws Exception
    {
        goToSaveAsMenu();
        waitForWindow("Save As");
        getLdtp().activateWindow("Save As");
        getLdtp().deleteText("txtFilename", 0);
        getLdtp().enterString("txtFilename", path);
        getLdtp().click("btnSave");
        getLdtp().waitTillGuiNotExist("Save As");
    }

    /**
     * Open SaveAs dialog from File menu
     */
    private void goToSaveAsMenu()
    {
        goToFile();
        getLdtp().selectMenuItem("mnuSaveAs");
    }

}

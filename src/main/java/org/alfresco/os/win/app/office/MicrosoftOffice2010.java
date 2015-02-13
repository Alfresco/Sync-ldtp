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
/**
 * This class has all the method involved in using the actions in office 2010
 * 
 * @author sprasanna
 */
package org.alfresco.os.win.app.office;

import java.io.File;

import org.alfresco.os.common.ApplicationBase;
import org.alfresco.os.win.Application;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.LdtpExecutionError;

public class MicrosoftOffice2010 extends Application
{
    public String OFFICE_PATH = "C:\\Program Files (x86)\\Microsoft Office\\Office14";
    protected OfficeApplication applicationDetails;

    protected String fileMenuPage = "File";
    protected String userName;
    protected String userPassword;

    public MicrosoftOffice2010(OfficeApplication officeApplication)
    {
        applicationDetails = officeApplication;
        setApplicationName(applicationDetails.getName());
        setApplicationVersion("2010");
        setApplicationPath(new File(OFFICE_PATH, applicationDetails.getExeName()).getPath());
        setWaitWindow(applicationDetails.getWaitWindow());
    }

    /*
     * Need to use the EXE NAME in order to kill the application
     */
    public void killProcess()
    {
        LdtpUtils.execute(new String[] { "taskkill", "/IM", applicationDetails.getExeName() });
    }

    public void exitApplication()
    {
        getLdtp().generateKeyEvent("<esc>");
        super.exitApplication();
    }

    public ApplicationBase openApplication()
    {
        try
        {
            return openApplication(new String[] { getApplicationPath() });
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
        getLdtp().activateWindow("Save As");
        getLdtp().deleteText("txtFilename", 0);
        getLdtp().enterString("txtFilename", location);
        getLdtp().click("btnSave");
        getLdtp().waitTillGuiNotExist("Save As");
    }

    /*
     * Method to implement Opening a file inside the office application
     */
    public void openOfficeFromFileMenu(String location) throws LdtpExecutionError
    {
        goToFile();
        getLdtp().click("Open");
        LdtpUtils.waitForObject(getLdtp(), "dlgOpen");
        getLdtp().activateWindow("dlgOpen");
        getLdtp().enterString("txtFilename", location);
        getLdtp().mouseLeftClick("uknOpen");
    }

    //
    // /**
    // * Method set the wait time for dialog / window to open
    // */
    // protected void setWaitTime()
    // {
    // try
    // {
    // Properties officeAppProperty = new Properties();
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    // String wait = officeAppProperty.getProperty("window.wait.time");
    // abstractUtil.waitInSeconds = Integer.parseInt(wait);
    // }
    // catch (IOException e)
    // {
    // throw new LdtpExecutionError("Cannot find the waiting time value");
    // }
    //
    // }
    //
    /**
     * Click in File
     *
     * @param ldtp
     */
    public void goToFile()
    {
        LdtpUtils.logInfo("Go to File");
        getLdtp().click("File Tab");
        getLdtp().waitTillGuiExist("File", LdtpUtils.RETRY_COUNT);
    }

    //
    // /**
    // * Set the name and password on Windows Security
    // *
    // * @param userName
    // * @param password
    // */
    // public void operateOnSecurity(Ldtp ldtp1, String userName, String password)
    // {
    // logger.info("Waiting for... 'Windows Security' window");
    // String securyWin = abstractUtil.waitForWindow("Windows Security");
    // if (securyWin.isEmpty())
    // return;
    //
    // logger.info("'Windows Security' found. Type user/password and click OK on 'Windows Security'");
    // ldtp1.deleteText("txtUsername", 0);
    // ldtp1.enterString("txtUsername", userName);
    // ldtp1.enterString("txtPassword", password);
    // ldtp1.click("OK");
    //
    // }
    //
    // public void operateOnSecurityAndWait(Ldtp ldtp1, String userName, String password) throws InterruptedException
    // {
    // int counter = 1;
    // int retryRefreshCount = 7;
    // String windowName = "";
    //
    // while (counter <= retryRefreshCount)
    // {
    // String windows2[] = ldtp1.getWindowList();
    //
    // for (String window : windows2)
    // {
    // if (window.contains("Windows Security"))
    // {
    // windowName = window;
    // break;
    // }
    // }
    //
    // if (windowName.contains("Windows Security"))
    // break;
    // else
    // {
    // logger.info("Wait one second for Window security");
    // Thread.sleep(1000);
    // counter++;
    // }
    // }
    //
    // if (windowName.toString().isEmpty())
    // return;
    // ldtp1.deleteText("txtUsername", 0);
    // ldtp1.enterString("txtUsername", userName);
    // ldtp1.enterString("txtPassword", password);
    // ldtp1.click("OK");
    // }
    //
    // /**
    // * Set the name and password on Windows Security
    // */
    // public void operateOnSecurity(Ldtp ldtp1)
    // {
    // operateOnSecurity(ldtp1, userName, userPassword);
    // }
    //
    // /**
    // * Set a comment on Check In window
    // *
    // * @param Ldtp
    // * @param comment
    // * @param checkBoxState
    // */
    // public void operateOnCheckIn(Ldtp l, String comment, boolean checkBoxState)
    // {
    // logger.info("Type a comment and click OK on 'Check In'");
    //
    // l.waitTime(2);
    // // String[] aa = l.getWindowList();
    // // String currentWin = abstractUtil.waitForWindow("Check In");
    // // if (currentWin.isEmpty())
    // // throw new LdtpExecutionError("Cannot find the Check in window");
    //
    // if (!comment.isEmpty())
    // {
    // l.enterString("txtVersionComments", comment);
    // }
    // if (checkBoxState == true)
    // {
    // l.check("chkKeepthedocumentcheckedoutaftercheckinginthisversion");
    // }
    // l.mouseLeftClick("btnOK");
    //
    // l.waitTime(2);
    // }
    //
    // /***
    // * @param filename
    // */
    // public void operateOnOpen(Ldtp ldtp1, String path, String siteName, String fileName, String userName, String password)
    // {
    // logger.info("Operate on 'Open'");
    //
    // String currentWin = abstractUtil.waitForWindow("dlgOpen");
    // if (currentWin.isEmpty())
    // {
    // throw new LdtpExecutionError("Cannot find the Open window");
    // }
    // ldtp1.activateWindow(currentWin);
    // ldtp1.enterString("txtFilename", path);
    // ldtp1.mouseLeftClick("uknOpen");
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // waitObjectHasValue(ldtp1, "txtFilename", "");
    //
    // ldtp1.enterString("txtFilename", siteName.toLowerCase());
    // ldtp1.mouseLeftClick("uknOpen");
    // waitObjectHasValue(ldtp1, "txtFilename", "");
    //
    // ldtp1.enterString("txtFilename", "documentLibrary");
    // ldtp1.mouseLeftClick("uknOpen");
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // waitObjectHasValue(ldtp1, "txtFilename", "");
    //
    // ldtp1.enterString("txtFilename", fileName);
    // ldtp1.mouseLeftClick("uknOpen");
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // ldtp1.waitTime(5);
    //
    // }
    //
    // /***
    // * Overloads the operateOnOpen to open a document already created on the client machine
    // *
    // * @param filename
    // */
    // public void operateOnOpen(Ldtp ldtp1, String path, String fileName)
    // {
    // logger.info("Operate on 'Open'");
    //
    // String currentWin = abstractUtil.waitForWindow("dlgOpen");
    // if (currentWin.isEmpty())
    // {
    // throw new LdtpExecutionError("Cannot find the Open window");
    // }
    // ldtp1.activateWindow(currentWin);
    // ldtp1.enterString("txtFilename", path);
    // ldtp1.mouseLeftClick("uknOpen");
    //
    // waitObjectHasValue(ldtp1, "txtFilename", "");
    //
    // ldtp1.enterString("txtFilename", fileName);
    // ldtp1.mouseLeftClick("uknOpen");
    //
    // ldtp1.waitTime(2);
    //
    // }
    //
    // /**
    // * Waits for an object to have a certain value
    // *
    // * @param ldtp
    // * @param objectName
    // * @param valueToWait
    // */
    // private void waitObjectHasValue(Ldtp ldtp, String objectName, String valueToWait)
    // {
    // int waitInSeconds = 2;
    // int counter = 0;
    // while (counter < abstractUtil.retryRefreshCount)
    // {
    // String fileNameContent = ldtp.getTextValue(objectName);
    // if (fileNameContent.equals(valueToWait))
    // break;
    // else
    // {
    // ldtp.waitTime(waitInSeconds);
    // waitInSeconds = (waitInSeconds * 2);
    // }
    // }
    // }
    //
    // /**
    // * Set a comment on Save As window
    // *
    // * @param path
    // * SharePoint Path where to save the file
    // */
    // public void operateOnSaveAs(Ldtp ldtp1, String path, String siteName, String fileName, String userName, String password)
    // {
    // logger.info("Operate on 'Save As'");
    //
    // String currentWin = abstractUtil.waitForWindow("Save As");
    // if (currentWin.isEmpty())
    // {
    // throw new LdtpExecutionError("Cannot find the 'Save As' window");
    // }
    //
    // ldtp1.activateWindow(currentWin);
    // ldtp1.enterString("txtFilename", path);
    // ldtp1.mouseLeftClick("btnSave");
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // String siteObject = siteName.replace(".", "").replace("_", "");
    //
    // abstractUtil.waitForObject(ldtp1, siteObject.toLowerCase());
    //
    // ldtp1.doubleClick(siteObject.toLowerCase());
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // abstractUtil.waitForObject(ldtp1, "lstdocumentLibrary");
    // ldtp1.doubleClick("lstdocumentLibrary");
    //
    // String addressBarObject = "tbarAddress" + path.replace(":", "").replace(".", "") + "/" + siteName.replace(".", "").replace("_", "").toLowerCase() + "/"
    // + "documentLibrary";
    // abstractUtil.waitForObject(ldtp1, addressBarObject);
    // ldtp1.enterString("txtFilename", fileName);
    // ldtp1.mouseLeftClick("btnSave");
    //
    // operateOnConfirmSaveAs(ldtp1);
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // ldtp1.waitTime(5);
    //
    // }
    //
    // /**
    // * Operate on Save As window by giving the fullpath to the fileName
    // *
    // * @param path
    // * Path where to save the file
    // */
    // public void operateOnSaveAsWithFullPath(Ldtp ldtp1, String path, String fileName, String userName, String password)
    // {
    // logger.info("Operate on 'Save As'");
    //
    // String currentWin = abstractUtil.waitForWindow("Save As");
    // if (currentWin.isEmpty())
    // {
    // throw new LdtpExecutionError("Cannot find the 'Save As' window");
    // }
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // ldtp1.activateWindow(currentWin);
    // ldtp1.enterString("txtFilename", path);
    // ldtp1.mouseLeftClick("btnSave");
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // String addressBarObject = "tbarAddress" + path.substring(0, path.length() - 1).replace(":", "");
    // abstractUtil.waitForObject(ldtp1, addressBarObject);
    // ldtp1.enterString("txtFilename", fileName);
    // ldtp1.mouseLeftClick("btnSave");
    //
    // operateOnConfirmSaveAs(ldtp1);
    //
    // operateOnSecurity(ldtp1, userName, password);
    //
    // ldtp1.waitTime(5);
    //
    // }
    //
    /**
     * Click on 'Yes' button from 'Confirm Save As'
     *
     * @param ldtp1
     * @throws Exception
     */
    public void operateOnConfirmSaveAs()
    {
        LdtpUtils.logInfo("Confirming Save As...");

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

    //
    // /**
    // * This method creates a new meeting workspace in Outlook 2010
    // *
    // * @param sharePointPath - path for SharePoint
    // * @param siteName - name of the site
    // * @param location - location for the meeting
    // * @param userName
    // * @param password
    // */
    // public void operateOnCreateNewMeetingWorkspace(Ldtp l, String sharePointPath, String siteName, String location, String userName, String password, boolean
    // withSubject, boolean clickRemove)
    // {
    //
    // logger.info("Start creating new meeting workspace");
    //
    // Ldtp security = new Ldtp("Windows Security");
    //
    // l.click("btnMeeting");
    //
    // // set focus on new window
    // String windowNameUntitled = abstractUtil.waitForWindow("Untitled");
    // Ldtp l1 = new Ldtp(windowNameUntitled);
    // l.activateWindow(windowNameUntitled);
    //
    // l1.click("btnMeetingWorkspace");
    //
    // l1.click("hlnkChangesettings");
    // l1.waitTime(2);
    // l1.selectItem("cboWebsiteDropdown", "Other...");
    //
    // String windowNameServer = abstractUtil.waitForWindow("Other Workspace Server");
    // Ldtp l2 = new Ldtp(windowNameServer);
    // l.activateWindow(windowNameServer);
    //
    // l2.deleteText("txtServerTextbox", 0);
    // l2.enterString("txtServerTextbox", sharePointPath);
    //
    // l2.click("btnOK");
    // l2.waitTime(3);
    // operateOnSecurity(security, userName, password);
    //
    // l1.click("chkAlldayevent");
    //
    // windowNameUntitled = abstractUtil.waitForWindow("Untitled");
    // Ldtp l3 = new Ldtp(windowNameUntitled);
    // l3.activateWindow(windowNameUntitled);
    //
    // l3.click("btnOK");
    //
    // if (withSubject == true)
    // {
    // l3.enterString("txtLocation", location);
    // l3.enterString("txtSubject", siteName);
    // }
    // else
    // {
    // l3.enterString("txtLocation", location);
    // }
    //
    // if (withSubject == true)
    // {
    // logger.info("Creating the event");
    // l3.click("btnCreate");
    //
    // l3.waitTime(4);
    //
    // // first verification
    // operateOnSecurity(security, userName, password);
    // // second verification
    // operateOnSecurity(security, userName, password);
    //
    // if (clickRemove == true)
    // {
    // String forRemove = abstractUtil.waitForWindow(siteName);
    // Ldtp remove = new Ldtp(forRemove);
    // remove.activateWindow(forRemove);
    //
    // remove.doubleClick("btnRemove");
    //
    // String message = abstractUtil.waitForWindow("Microsoft Outlook");
    // Ldtp l_error = new Ldtp(message);
    // l_error.click("btnYes");
    //
    // }
    //
    // }
    // else
    // {
    // // Your attempt to create a Meeting Workspace or link to an existing one can't be completed.
    // // Reason: Site name is not specified. Please fill up subject field.
    // l3.click("btnCreate");
    // l3.waitTime(4);
    // operateOnSecurity(security, userName, password);
    //
    // logger.info("Error when subject is not filled");
    //
    // }
    //
    // }
    //
    // /**
    // * This method creates a new meeting workspace in Outlook 2010
    // *
    // * @param sharePointPath - path for SharePoint
    // * @param siteName - name of the site
    // * @param location - location for the meeting
    // * @param userName
    // * @param password
    // * @throws IOException
    // * @throws LdtpExecutionError
    // */
    // public void operateOnRecurrenceAppointment(Ldtp l1, String startDate, String endDate, String noOfOccurences) throws LdtpExecutionError, IOException
    // {
    //
    // l1 = abstractUtil.setOnWindow("Appointment");
    //
    // l1.deleteText("txtStart", 0);
    // l1.enterString("txtStart", startDate);
    // l1.deleteText("txtEnd", 0);
    // l1.enterString("txtEnd", endDate);
    // l1.click("rbtnDaily");
    // l1.waitTime(1);
    //
    // l1.click("rbtnEndafter");
    // l1.deleteText("txtEndafterEditableTextoccurences", 0);
    // l1.enterString("txtEndafterEditableTextoccurences", noOfOccurences);
    // l1.click("btnOK");
    //
    // }
    //
    // /**
    // * This method creates meeting workspace in Outlook 2010 from an existing Site
    // *
    // * @param sharePointPath - path for SharePoint
    // * @param siteName - name of the site
    // * @param subject
    // * @param location - location for the meeting
    // * @param userName
    // * @param password
    // */
    // public void operateOnLinkToExistingWorkspace(Ldtp l, String sharePointPath, String siteName, String subject, String location, String userName, String
    // password, boolean withSubject)
    // {
    //
    // Ldtp security = new Ldtp("Windows Security");
    //
    // // click meeting
    // l.click("btnMeeting");
    //
    // // set focus on new window
    // String windowNameUntitled = abstractUtil.waitForWindow("Untitled");
    // Ldtp l1 = new Ldtp(windowNameUntitled);
    // l.activateWindow(windowNameUntitled);
    //
    // l1.click("btnMeetingWorkspace");
    //
    // l1.waitTime(2);
    // l1.click("hlnkChangesettings");
    // l1.click("rbtnLinktoanexistingworkspace");
    //
    // l1.mouseLeftClick("cboWorkspaceDropdown");
    // l1.waitTime(4);
    // operateOnSecurity(security, userName, password);
    //
    // l1.selectItem("cboWorkspaceDropdown", siteName);
    //
    // l1.click("btnOK");
    //
    // l1.click("chkAlldayevent");
    // windowNameUntitled = abstractUtil.waitForWindow("Untitled");
    // Ldtp allDay = new Ldtp(windowNameUntitled);
    // allDay.activateWindow(windowNameUntitled);
    //
    // if (withSubject == true)
    // {
    // allDay.enterString("txtLocation", location);
    // allDay.enterString("txtSubject", subject);
    // }
    // else
    // {
    // allDay.enterString("txtLocation", location);
    // }
    //
    // if (withSubject == true)
    // {
    // // click Link button
    // allDay.click("btnLink");
    // allDay.waitTime(4);
    // operateOnSecurity(security, userName, password);
    // }
    // else
    // {
    // // Your attempt to create a Meeting Workspace or link to an existing one can't be completed.
    // // Reason: Site name is not specified. Please fill up subject field.
    // windowNameUntitled = abstractUtil.waitForWindow("Untitled");
    // Ldtp l2 = new Ldtp(windowNameUntitled);
    // l2.activateWindow(windowNameUntitled);
    //
    // logger.info("Error when subject is not filled");
    //
    // // click Link button
    // l2.click("btnLink");
    // l2.waitTime(4);
    // operateOnSecurity(security, userName, password);
    //
    // }
    //
    // }
    //
    // /**
    // * Method to exit any office application
    // */
    // public void exitOfficeApplication(Ldtp ldtp)
    // {
    // // clickOnObject(ldtp, "btnClose1");
    //
    // ldtp.generateKeyEvent("<alt><f4>");
    // }
    //
    // /**
    // * Method to exit any office application but after focusing on the specified window
    // *
    // * @throws IOException
    // * @throws LdtpExecutionError
    // */
    // public void exitOfficeApplication(Ldtp ldtp, String windowName) throws LdtpExecutionError, IOException
    // {
    //
    // abstractUtil.setOnWindow(windowName);
    // ldtp.generateKeyEvent("<alt><f4>");
    // }
    //
    // /**
    // * Method to open a file from the CIFS path
    // */
    // public Ldtp openFileFromCMD(String docPath, String fileName, String userName, String password, boolean checkSecurityWindow)
    // {
    // Ldtp security = new Ldtp("Windows Security");
    // String exe = getAbstractUtil().applicationExe;
    // try
    // {
    // Properties officeAppProperty = new Properties();
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    //
    // String officePath = officeAppProperty.getProperty("office" + officeVersion + ".path");
    //
    // docPath = docPath + fileName;
    //
    // Runtime rt = Runtime.getRuntime();
    // rt.exec(officePath + exe + " " + docPath);
    // if (checkSecurityWindow)
    // operateOnSecurityAndWait(security, userName, password);
    //
    // abstractUtil.waitForWindow(fileName);
    // return new Ldtp(fileName);
    //
    // }
    // catch (Exception ee)
    // {
    // }
    // return null;
    //
    // }
    //
    // /**
    // * Get the CIFS path from property
    // */
    // public String getCIFSPath()
    // {
    // Properties officeAppProperty = new Properties();
    // try
    // {
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    // String cifsPath = officeAppProperty.getProperty("cifs.path");
    // return cifsPath;
    // }
    // catch (IOException e)
    // {
    // logger.info("Path not foud");
    // }
    //
    // return "";
    //
    // }
    //
    // /**
    // * Get the map drive path from property
    // */
    // public String getMapDriver()
    // {
    // Properties officeAppProperty = new Properties();
    // try
    // {
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    // String mapDrive = officeAppProperty.getProperty("network.map.driver");
    // return mapDrive;
    // }
    // catch (IOException e)
    // {
    // logger.info("Path not foud");
    // }
    //
    // return "";
    //
    // }
    //
    // /**
    // * Get the map path from property
    // */
    // public String getMapPath()
    // {
    // Properties officeAppProperty = new Properties();
    // try
    // {
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    // String mapPath = officeAppProperty.getProperty("network.map.path");
    // return mapPath;
    // }
    // catch (IOException e)
    // {
    // logger.info("Path not foud");
    // }
    //
    // return "";
    //
    // }
    //
    // /**
    // * get the path of the office on client machine
    // *
    // * @return
    // */
    // protected String getOfficePath()
    // {
    // Properties officeAppProperty = new Properties();
    // try
    // {
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    // String officePath = officeAppProperty.getProperty("office" + officeVersion + ".path");
    // return officePath;
    // }
    // catch (IOException e)
    // {
    // logger.info("Path not foud");
    // }
    // return "";
    // }
    //
    // /**
    // * get the sharepoint path
    // *
    // * @return
    // */
    // public String getSharePointPath()
    // {
    // Properties officeAppProperty = new Properties();
    // try
    // {
    // officeAppProperty.load(this.getClass().getClassLoader().getResourceAsStream("office-application.properties"));
    // String officePath = officeAppProperty.getProperty("sharepoint.path");
    // return officePath;
    // }
    // catch (IOException e)
    // {
    // logger.info("Path not foud");
    // }
    // return "";
    // }

    public void saveAsOffice(String path) throws Exception
    {
        saveOffice(path);
    }

}

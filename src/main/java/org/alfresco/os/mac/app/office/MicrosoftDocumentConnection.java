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

package org.alfresco.os.mac.app.office;

import java.io.File;
import java.io.FileInputStream;

import com.google.common.io.Files;

import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.os.mac.Application;
import org.alfresco.utilities.LdtpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class will cover the MDC Office tool on MAC OS.
 * (Tested with Office2011)
 * 
 */
public class MicrosoftDocumentConnection extends Application
{
	private static Logger logger = LogManager.getLogger(MicrosoftDocumentConnection.class);
    public MicrosoftDocumentConnection(String version)
    {
        setApplicationPath("/Applications/Microsoft Office " + version.toString() + "/Microsoft Document Connection.app");
        setApplicationName("Microsoft Document Connection");

        // we need to wait for this particular window
        setWaitWindow("frmDocumentConnection");
    }

    /**
     * Cleanup the any user's history related to SharePoint or SkyDrive connections
     * This method will edit the <Document Connection.xml> file and remove all the node that contains
     * some defined values
     * In this way we will have a clean MDC environment as desired
     */
    public void cleanUpHistoryConnectionList()
    {
        File documentConnectionSettings = new File(System.getProperty("user.home"),
                "/Library/Application Support/Microsoft/Office/14.0/Document Connection/Document Connection.xml");

        LdtpUtils.logDebug(String.format("cleanUpHistoryConnectionList based on [%s]", documentConnectionSettings.getPath()));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);

        boolean changesMade = false;
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(documentConnectionSettings));

            Element root = doc.getDocumentElement();

            NodeList nodes = root.getChildNodes();

            // navigate on each node and remove only desired nodes
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node node = nodes.item(i);
                NamedNodeMap nm = node.getAttributes();
                if (nm != null && nm.getLength() > 0)
                {
                    String nodeType = nm.getNamedItem("type").toString();

                    if (nodeType.contains("HZDSITE") || nodeType.contains("HZDCONNECTION") || nodeType.contains("HZDLIST"))
                    {
                        root.removeChild(node);
                        changesMade = true;
                    }

                }
            }

            if (changesMade)
            {
                if (documentConnectionSettings.exists())
                {
                    documentConnectionSettings.delete();
                }

                // write modification to file
                Transformer tf = TransformerFactory.newInstance().newTransformer();

                tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                tf.setOutputProperty(OutputKeys.INDENT, "yes");

                File tmpFile = new File(System.getProperty("user.home"), "Document Connection.xml");
                StreamResult out = new StreamResult(tmpFile);

                tf.transform(new DOMSource(doc), out);

                // we cannot writing directly to destination file so I use the move method
                Files.move(tmpFile, documentConnectionSettings);
            }

        }
        catch (Exception e)
        {
            logger.error("Error cleaning up Microsoft Document Connection settings file" + e.getMessage());
        }

    }

    @Override
    public void exitApplication()
    {
        try
        {
            LdtpUtils.logDebug("Now Exit MDC application.");
            if (Arrays.asList(getLdtp().getWindowList()).contains(getWaitWindow()))
            {
                focus();
                killProcess();
                getLdtp().waitTime(1);
            }
            destroyProcesses();
        }
        catch (Exception e)
        {
            logger.error("Error on exit for Microsoft Document Connection application: " + e.getMessage());
        }
    }

    /**
     * Assuming that we already save the credentials in Keychain, we add the location url and continue to repository
     * 
     * @param location
     */
    public void addLocation(String location, String username, String password)
    {
        LdtpUtils.logInfo("Add Sharepoint location: " + location);
        focus();
        getLdtp().generateKeyEvent("<command>k");
        getLdtp().generateKeyEvent("<command>a");
        getLdtp().enterString(location);
        getLdtp().click("Connect");
        getLdtp().waitTime(1);
        addLocationCredentials(username, password);
        getLdtp().generateKeyEvent("<enter>");
        getLdtp().waitTime(4);
    }

    /**
     * This will add the SharePoint credentials in the Login dialog
     * 
     * @param username
     * @param password
     */
    public void addLocationCredentials(String username, String password)
    {
        getLdtp().enterString(username);
        getLdtp().generateKeyEvent("<tab>");
        getLdtp().enterString(password);

        LdtpUtils.waitToLoopTime(3);
        // press on active alert: if you continue, your password will be transmitted on an unsecure connection.
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Select any file, clicking on the name
     * 
     * @param filename
     */
    public void selectFile(String filename)
    {
        getLdtp().click(filename);
    }

    public void clickNewFile()
    {
        getLdtp().click("btnNewFile");
    }

    public void clickAddFile()
    {
        getLdtp().click("btnAddFile");
    }

    public void clickRead()
    {
        getLdtp().click("btnRead");
    }

    /**
     * At this point I use key commands in order to navigate though each location path
     * 
     * @param locations
     * @throws InterruptedException
     */
    public void addFile(File file) throws InterruptedException
    {
        clickButton("btnAddFile");
        getLdtp().activateWindow("frmUploadNewFiles");

        // go to parent folder
        getLdtp().enterString(file.getParent());
        getLdtp().generateKeyEvent("<enter>");

        // add the file
        getLdtp().enterString(file.getName());
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * @return boolean value if state is enabled (1 in LDTP api)
     */
    public boolean isBtnAddFileEnabled()
    {
        return getLdtp().stateEnabled("btnAddFile")==1;
    }

    /**
     * @return boolean value if button is enabled
     */
    public boolean isBtnCheckOutEnabled()
    {
        getLdtp().waitTime(1);
        return getLdtp().stateEnabled("btnCheckOut")==1;
    }

    /**
     * @return boolean value if button is enabled
     */
    public boolean isBtnCheckInEnabled()
    {
        getLdtp().waitTime(1);
        return getLdtp().stateEnabled("btnCheckIn")==1;
    }

    /**
     * Clicking on the filename column within Document Connection Windows
     * Assumes a connection is already made
     */
    public void clickOnFilenameColumn()
    {
        getLdtp().click("btnFileName");
    }

    /**
     * Search for a particular filename
     * 
     * @param filename Full name of the file
     */
    public void search(String filename)
    {
        focus();
        getLdtp().setTextValue("txtsearchtextfield", filename);
        getLdtp().click("txtsearchtextfield");
        getLdtp().generateKeyEvent("<enter>");
        LdtpUtils.waitToLoopTime(1);
    }

    /**
     * this will try to Save As the first document displayed on Document Connection window
     */
    public void saveAsFirstDocumentAs(File filename)
    {
        clickFirstDocument();
        getLdtp().generateKeyEvent("<command><shift>s");
        getLdtp().waitTime(1);

        // go to parent folder
        getLdtp().enterString(filename.getParent());
        getLdtp().generateKeyEvent("<enter>");

        // save the file
        getLdtp().enterString(filename.getName());
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Selects first document and click on Read button
     */
    public void readFirstDocument()
    {
        clickFirstDocument();
        getLdtp().waitTime(1);
        clickRead();
        getLdtp().waitTime(3);
    }

    public void editFirstDocument()
    {
        clickFirstDocument();
        getLdtp().waitTime(1);
        clickEdit();
        getLdtp().waitTime(3);
    }

    public void clickEdit()
    {
        focus();
        getLdtp().click("btnEdit");
    }

    public void clickFirstDocument()
    {
        getLdtp().click("txttextfield1");
    }

    public void clickCheckOut()
    {
        clickButton("btnCheckOut");
    }

    public void clickCheckIn()
    {
        clickButton("btnCheckIn");
    }

    public void checkOutFile(String name)
    {
        search(name);
        clickFirstDocument();
        clickCheckOut();
    }

    public void checkInFile(String name)
    {
        search(name);
        clickFirstDocument();
        clickCheckIn();
    }

    /**
     * This will add a comment in the Check in Dialog and submit it
     * You should prior open the Check In Dialog using checkInFile(String name) method
     * 
     * @param comment
     */
    public void checkInWithComment(String comment)
    {
        comment = LdtpUtils.toLdapString(comment);
        getLdtp().enterString(comment);
        getLdtp().click("btnCheckIn1");
        getLdtp().waitTime(2);
    }

    /**
     * This will Check/Uncheck the "Keep file checked out after checking in this version" option
     * 
     * @param keepFileCheckout
     */
    public void keepFileCheckedOut(Boolean keepFileCheckout)
    {
        String idKeep = "chkKeepfilecheckedoutaftercheckinginthisversion";
        if (keepFileCheckout)
        {
            getLdtp().check(idKeep);
        }
        else
        {
            getLdtp().unCheck(idKeep);
        }
    }

    /**
     * Click on the Cancel button
     */
    public void clickCancel()
    {
        getLdtp().click("btnCancel.*");
    }

    /**
     * Click on the Discard button
     */
    public void clickDiscard()
    {
        clickButton("btnDiscard.*");
    }

    /**
     * @return boolean value if Button Edit is enabled
     */
    public boolean isBtnEditEnabled()
    {
        return getLdtp().stateEnabled("btnEdit")==1;
    }

}

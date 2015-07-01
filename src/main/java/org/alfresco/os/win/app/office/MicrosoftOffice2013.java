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

import java.io.IOException;

import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;

/**
 * This class has all the method involved in using the actions in office 2010
 * 
 * @author Subashni Prasanna
 */
public class MicrosoftOffice2013 extends MicrosoftOfficeBase
{
    private static Logger logger = Logger.getLogger(MicrosoftOffice2013.class);

    public MicrosoftOffice2013(VersionDetails officeApplication)
    {
        super(officeApplication, "2013");
    }

    /**
     * Method to up check the start up window
     * 
     * @throws IOException
     * @throws LdtpExecutionError
     */
    public void unCheckStartUp(String path) throws LdtpExecutionError, IOException
    {
       
        getLdtp().click(applicationDetails.getBlankDoc());
        
        getLdtp().click("File Tab");
        getLdtp().click("Options");
        getLdtp().activateWindow("Excel Options");
        if (getLdtp().check("Show the Start screen when this application starts") == 1)
        {
            getLdtp().unCheck("Show the Start screen when this application starts");
        }
        getLdtp().click("OK");
        getLdtp().closeWindow(getWaitWindow());
    }

    /**
     * Click in File
     * 
     * @param ldtp
     */
    public void goToFile(Ldtp ldtp)
    {
        logger.info("Go to file");

        ldtp.click("File Tab");
        ldtp.waitTillGuiExist(fileMenuPage, 2);

    }

    /*
     * Method to implement Opening a file inside the office application
     */
    @Override
    public void openOfficeFromFileMenu(String location) throws Exception
    {
        Ldtp ldtp = getLdtp();
        goToFile(ldtp);
        ldtp.click("Open");
        ldtp.click("Computer");
        ldtp.click("Browse");
        ldtp =  waitForWindow("dlgOpen");
        ldtp.enterString("txtFilename", location);
        ldtp.mouseLeftClick("uknOpen");
    }

    /**
     * Operates on Save As dialog
     * 
     * @param ldtp1
     * @param path
     * @throws Exception 
     */
    public void saveAs(String path) throws Exception
    {
        logger.info("Operate on 'Save As'");
        Ldtp ldtp = waitForWindow("Save As");
        ldtp.enterString("txtFilename", path);
        ldtp.mouseLeftClick("btnSave");
    }

    /*
     * Method to implement SaveAs of office application
     */
    @Override
    public void saveAsOffice(String location) throws Exception
    {

        goToFile();
        getLdtp().click("SaveAs");

        getLdtp().click("Browse");
        waitForWindow("Save As");
        getLdtp().deleteText("txtFilename", 0);
        getLdtp().enterString("txtFilename", location);
        getLdtp().click("btnSave");
        getLdtp().waitTillGuiNotExist("Save As");
    }
}

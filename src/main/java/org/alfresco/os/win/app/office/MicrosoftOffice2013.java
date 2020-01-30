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

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;
import org.alfresco.os.common.ApplicationBase;
import org.alfresco.utilities.LdtpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * This class has all the method involved in using the actions in office 2010
 * 
 * @author Subashni Prasanna
 */
public class MicrosoftOffice2013 extends MicrosoftOfficeBase
{
    private static Logger logger = LogManager.getLogger(MicrosoftOffice2013.class);

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
//        Ldtp ldtp = getLdtp();
        Ldtp ldtp = new Ldtp("Document*");
        goToFile(ldtp);
        ldtp.mouseMove("Open");
        LdtpUtils.waitToLoopTime(2);
        ldtp.mouseLeftClick("Open");
        ldtp.mouseMove("Recent Documents");
        LdtpUtils.waitToLoopTime(2);
        ldtp.mouseLeftClick("Recent Documents");
        ldtp.click("btn"+location);
//        ldtp.click("Open");
//        ldtp.click("Computer");
//        ldtp.click("btnMyDocuments");
//        ldtp =  waitForWindow("dlgOpen");
//        ldtp.enterString("txtFilename", location);
//        ldtp.mouseLeftClick("uknOpen");
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
        getLdtp().click("Save");
        getLdtp().mouseMove("Computer");
        LdtpUtils.waitToLoopTime(2);
        getLdtp().mouseLeftClick("Computer");

        getLdtp().click("btnMyDocuments");
        waitForWindow("Save As");
        getLdtp().deleteText("txtFilename", 0);
        getLdtp().enterString("txtFilename", location);
        getLdtp().click("btnSave");
     //   getLdtp().click("OK");
     //   getLdtp().waitTillGuiNotExist("Save As");
    }

    @Override
    public ApplicationBase openApplication()
    {
        logger.info("Try to open application: " + getApplicationPath());
        try
        {
            openApplication(new String[]{getApplicationPath()});
            maximize();
            getLdtp().generateKeyEvent("<enter>");
        }
        catch (Exception e)
        {
            logger.error("Could not open Application " + getApplicationName() + "Error: " + e);
        }
        return this;
    }
    
   public void  goToFile()
   {
       LdtpUtils.logInfo("Go to File");
       getLdtp().setWindowName("Document*");
       getLdtp().click("File Tab");
       getLdtp().waitTillGuiExist("File", LdtpUtils.RETRY_COUNT);
   }

    /**
     * Close Application from Close button, based on filename already opened
     * @param file
     */
    @Override
    public void closeApplication(File file)
    {
        focus(file);
//        getLdtp().click("btnFileTab");
//        getLdtp().waitTillGuiExist("File", LdtpUtils.RETRY_COUNT);
//        getLdtp().click("btnClose1");
        getLdtp().generateKeyEvent("<alt>"+"<f4>");
        setWaitWindow(applicationDetails.getWaitWindow());
//        closeApplication();
    }


}

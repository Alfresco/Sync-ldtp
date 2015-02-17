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

package org.alfresco.os.win.app.office;

import java.io.File;
import java.io.IOException;

import org.alfresco.os.AbstractTestClass;
import org.alfresco.os.win.app.office.MicrosoftOfficeBase.VersionDetails;
import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class Excel2010Test extends AbstractTestClass
{
    MicrosoftOffice2010 excel = new MicrosoftOffice2010(VersionDetails.EXCEL);
    public File fileName;

    @AfterClass
    public void tearDown()
    {
        super.tearDown();
    }
    
    @AfterMethod
    private void cleanUp(){
        fileName.delete();
    }

    /**
     * Steps
     * 1) Open excel application
     * 2) Add a text
     * 3) Click on Save as button
     * 
     * @throws Exception
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testExcelCreation() throws Exception
    {
        fileName = LdtpUtils.getRandomFileName("xlsx");
        randomFiles.add(fileName);
        excel.openApplication();
        excel.editOffice("hello world");
        excel.saveOffice(fileName.getPath());
        Assert.assertTrue(fileName.exists(), "File was successuly saved from Excel");

        excel.closeApplication(fileName);

    }

    @Test
    public void testGoToFile()
    {
        excel.openApplication();
        excel.goToFile();
        Assert.assertTrue(excel.getLdtp().objectExist("Info") == 1, "Check Info label is diaplyed on File Tab");
        excel.closeApplication();
    }

    @Test
    public void testOperateOnConfirmSaveAs() throws Exception
    {
        fileName = LdtpUtils.getRandomFileName("xlsx");
        randomFiles.add(fileName);
        fileName.delete();

        excel.openApplication();
        excel.saveAsOffice(fileName.getPath());

        Assert.assertTrue(fileName.exists(), "File was successfully replaced.");
        excel.closeApplication(fileName);
    }

    @Test
    public void testOpenOfficeFromFileMenu() throws Exception
    {
        fileName = LdtpUtils.getNewRandomFileFromResource("testFile.xlsx");
        fileName.createNewFile();
        randomFiles.add(fileName);

        excel.openApplication();
        excel.openOfficeFromFileMenu(fileName.getPath());
        excel.closeApplication(fileName);
    }

    @Test
    public void testExitOfficeApplication()
    {
        excel.openApplication();

        boolean isOpened = LdtpUtils.isProcessRunning(VersionDetails.EXCEL.getExeName());
        Assert.assertTrue(isOpened, "Excel application is opened");

        LdtpUtils.waitToLoopTime(4);

        excel.exitApplication();
        LdtpUtils.waitToLoopTime(6);
        isOpened = LdtpUtils.isProcessRunning(VersionDetails.EXCEL.getExeName());
        Assert.assertFalse(isOpened, "Excel application is closed");
    }
}
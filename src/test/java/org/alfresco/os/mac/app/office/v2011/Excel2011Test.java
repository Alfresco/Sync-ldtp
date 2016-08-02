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

package org.alfresco.os.mac.app.office.v2011;

import org.alfresco.os.AbstractTestClass;
import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for validation of MicrosoftExcel2011 class
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class Excel2011Test extends AbstractTestClass
{
    @BeforeMethod
    public void beforeMethod() throws Exception
    {
        excel = new Excel2011();
        excel.openApplication();

        // this will create a random filename in Documents directory based on testFile.xlsx
        // just call deleteAllRandomGeneratedFiles method in order to clean it up
        tmpFile = getRandomTestFile("testFile.xlsx");
    }

    @AfterMethod
    public void tearDown()
    {
        super.tearDown();
        excel.exitApplication();
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testSaveAs()
    {
        try
        {
            excel.focus();
            excel.saveAs(tmpFile);
            LdtpUtils.waitToLoopTime(2);
            Assert.assertTrue(tmpFile.exists());
        }
        catch (Exception e)
        {
            Assert.fail("The Test Case FAILED " + this.getClass(), e);
        }
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testOpenFromFileMenu()
    {
        try
        {
            excel.openFromFileMenu(tmpFile);
            excel.setFileName(tmpFile.getName());
            excel.focus();
            Assert.assertTrue(excel.isFileOpened(tmpFile.getName()), "Excel file is opened");
        }
        catch (Exception e)
        {
            Assert.fail("The Test Case FAILED", e);
        }
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testEdit() throws Exception
    {
        excel.openFromFileMenu(tmpFile);
        excel.edit("testdata");
        excel.save();
        Assert.assertEquals(tmpFile.length(), 26446);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testSave() throws Exception
    {
        excel.openFromFileMenu(tmpFile);
        excel.save();
        Assert.assertEquals(tmpFile.length(), 26446);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testSaveString() throws Exception
    {
        excel.openFromFileMenu(tmpFile);
        excel.save();
        Assert.assertEquals(tmpFile.length(), 26446);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testGoToFile()
    {
        excel.goToFile();
        Assert.assertEquals(excel.getLdtp().isTextStateEnabled("File"), 0);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testSaveAndClose() throws Exception
    {
        excel.openFromFileMenu(tmpFile);
        excel.saveAndClose();
        Assert.assertTrue(tmpFile.exists());
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testCloseFile() throws Exception
    {
        excel.openFromFileMenu(tmpFile);
        excel.setFileName(tmpFile.getName());
        excel.close(tmpFile.getName());
        Assert.assertFalse(excel.isFileOpened(tmpFile.getName()));
    }

    // @Test(groups = { "MacOnly", "Office2011" })
    // public void testGetProperty()
    // {
    // Assert.assertNotNull(excel.getProperty("window.wait.time"), "Properties values are initialised as expected");
    // }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testGetProcesses()
    {
        Assert.assertFalse(excel.getProcesses().isEmpty());
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testRunProcess()
    {
        try
        {
            excel.runProcess(new String[] { "open", "/Applications/Calculator.app" });
            Assert.assertTrue(excel.isWindowOpened("Calculator"), "Calculator app is opened");
            excel.runProcess(new String[] { "killall", "Calculator" });
            Assert.assertFalse(excel.isWindowOpened("Calculator"), "Calculator app is closed");
        }
        catch (Exception e)
        {
            Assert.fail("Could not run process: " + e.getStackTrace());
        }
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testGetApplicationPath()
    {
        Assert.assertFalse(excel.getApplicationPath().isEmpty());
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testGetOpenApplications()
    {
        Assert.assertTrue(excel.getOpenApplications().length > 0);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testKillProcesses()
    {
        try
        {
            excel.killProcess();
            Excel2011 tmp = new Excel2011();
            Assert.assertFalse(tmp.isWindowOpened(excel.getWaitWindow()));
        }
        catch (Exception e)
        {
            Assert.fail("Coud not kill process: " + e.getStackTrace());
        }
    }
}
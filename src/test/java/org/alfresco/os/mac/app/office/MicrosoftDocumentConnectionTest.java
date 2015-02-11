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
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Prerequisites:
 * LDTP site created and MacOffice.docx, MacOffice.xlsx are uploaded
 * Note: SharePoint related methods were not unit tested in this class in order to achieve < decoupling
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class MicrosoftDocumentConnectionTest
{
    private MicrosoftDocumentConnection md;

    @BeforeClass
    private void init()
    {
        md = new MicrosoftDocumentConnection("2011");
    }

    @AfterClass
    private void tearDown()
    {
        md.exitApplication();
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testMicrosoftDocumentConnection()
    {
        MicrosoftDocumentConnection md = new MicrosoftDocumentConnection("2011");
        Assert.assertNotNull(md);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testExitApplication() throws Exception
    {
        md.openApplication();
        md.exitApplication();
        md.waitUntilWindowIsClosed(md.getWaitWindow());
        boolean found = Arrays.asList(md.getOpenApplications()).contains(md.getApplicationName());
        Assert.assertFalse(found);
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testCleanUpHistoryConnectionList()
    {
        md.cleanUpHistoryConnectionList();
        File tmpFile = new File(System.getProperty("user.home"), "Document Connection.xml");
        Assert.assertFalse(tmpFile.exists());
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testAddLocation() throws Exception
    {
        md.cleanUpHistoryConnectionList();
        md.openApplication();
        Assert.assertFalse(md.isBtnAddFileEnabled(), "Button Add File is disabled by default");
        md.exitApplication();
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testSelectFile() throws Exception
    {
        md.cleanUpHistoryConnectionList();
        md.openApplication();
        Assert.assertFalse(md.isBtnEditEnabled(), "Button Edit is disabled by default");
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testClickRead()
    {
        md.cleanUpHistoryConnectionList();
        md.openApplication();
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testAddFile()
    {
        md.cleanUpHistoryConnectionList();
        md.openApplication();
        Assert.assertFalse(md.isBtnAddFileEnabled(), "Button Add File is disabled by default");
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testIsBtnCheckOutEnabled()
    {
        md.openApplication();
        Assert.assertFalse(md.isBtnCheckOutEnabled(), "Button Checkout is disabled by default");
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testIsBtnCheckInEnabled()
    {
        md.openApplication();
        Assert.assertFalse(md.isBtnCheckInEnabled(), "Button Check in is disabled by default");
    }

    @Test(groups = { "MacOnly", "Office2011" })
    public void testSearch()
    {
        md.openApplication();
        try
        {
            md.search("a");
        }
        catch (Exception e)
        {
            Assert.fail("Could not search in MDC: " + e.getStackTrace());
        }
    }
}

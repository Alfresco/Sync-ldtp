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

package org.alfresco.os.mac.app;

import java.io.File;

import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test that will validate TextEdit class.
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class TextEditTest
{

    TextEdit app = new TextEdit();
    private File tmpFile = new File(System.getProperty("user.home").toString() + File.separator + "Documents/" + "test.rtf");
    private File tmpFile2 = new File(System.getProperty("user.home").toString() + File.separator + "Documents/" + "test2.rtf");

    @BeforeClass
    private void setup()
    {
        app.exitApplication();
        tmpFile.delete();
        tmpFile2.delete();
    }

    @AfterClass
    private void tearDown()
    {
        tmpFile.delete();
        tmpFile2.delete();
        app.exitApplication();
    }

    @Test(groups = { "MacOnly" })
    public void testOpenApplication()
    {
        app.openApplication();
        AssertJUnit.assertTrue(LdtpUtils.isProcessRunning("TextEdit"));
    }

    @Test(dependsOnMethods = { "testOpenApplication" }, groups = { "MacOnly" })
    public void testEdit()
    {
        try
        {
            app.edit("test");
        }
        catch (Exception e)
        {
            Assert.fail("Could not edit File" + e.getStackTrace());
        }
    }

    @Test( groups = { "MacOnly" })
    public void testSave() throws Exception
    {
        app.openApplication();
        app.save(tmpFile.getPath());
        Assert.assertTrue(tmpFile.exists(), "File was successfuly created on disk.");
    }

    @Test(groups = { "MacOnly" }, dependsOnMethods = { "testSave" })
    public void testSaveAndClose()
    {
        app.edit("new data");
        app.saveAndClose();
        tmpFile.delete();
    }

    @Test(dependsOnMethods = { "testOpenApplication" }, groups = { "MacOnly" })
    public void testSaveAs()
    {
        tmpFile2.delete();
        app.exitApplication();
        app.openApplication();
        app.saveAs(tmpFile2);
        Assert.assertTrue(tmpFile2.exists(), "File was successfuly created on disk.");
        app.exitApplication();
    }

    @Test(groups = { "MacOnly" })
    public void testCreateFile()
    {
        app.exitApplication();
        tmpFile.delete();
        app.createFile(tmpFile);
        Assert.assertTrue(tmpFile.exists(), "File was successfuly created");
    }
}
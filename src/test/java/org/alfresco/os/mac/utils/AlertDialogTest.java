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

package org.alfresco.os.mac.utils;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Unit test for validation of AlertDialog class
 * 
 */
public class AlertDialogTest
{

    private AlertDialog alert;
    private File folder = new File(System.getProperty("user.home"), "Documents/AlertPresent");

    @AfterClass
    private void tearDown()
    {
        folder.delete();
        // this is the TMP folder file that cannot be renamed on <testIsAlertPresent> due to duplication
        // we will delete this in order to have a clean env, on the next run
        folder = new File(System.getProperty("user.home"), "Documents/untitled folder");
        folder.delete();
    }

    @Test(groups = { "MacOnly" })
    public void testAlertDialog()
    {
        try
        {
            alert = new AlertDialog();
        }
        catch (Exception e)
        {
            Assert.fail("Could not initalize a new AlertDialog class: " + e.getStackTrace());
        }
    }

    @Test(groups = { "MacOnly" })
    public void testIsAlertPresent()
    {
        ProcessBuilder pb = new ProcessBuilder(new String[] { "open", new File(System.getProperty("user.home"), "Documents").getPath() });
        try
        {
            pb.start(); // this will just open the Finder Window on Documents folder
        }
        catch (IOException e)
        {
            Assert.fail("Could not open Finder Window on Documents: " + e.getStackTrace());
        }
        folder.mkdir(); // make dir

        alert = new AlertDialog();
        alert.getLdtp().activateWindow("Documents");
        alert.getLdtp().generateKeyEvent("<shift><command>n");
        alert.getLdtp().generateKeyEvent(folder.getName());
        alert.getLdtp().generateKeyEvent("<enter>"); // this should open the alert
        alert = new AlertDialog();
        Assert.assertTrue(alert.isAlertPresent(), "Alert is opened");
    }

    @Test(dependsOnMethods = { "testIsAlertPresent" }, groups = { "MacOnly" })
    public void testCloseAlert()
    {
        try
        {
            alert.closeAlert();
        }
        catch (Exception e)
        {
            Assert.fail("Could not close AlertDialog: " + e.getStackTrace());
        }
    }
}

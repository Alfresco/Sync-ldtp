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

package org.alfresco.os.win.app;

import java.io.File;

import org.alfresco.os.win.Application;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.LdtpExecutionError;

/**
 * This class has all the method involved in using the actions in Notepad application 2013
 * 
 * @author Subashni Prasanna
 * @author Paul Brodner
 */
public class Notepad extends Application
{

    String applicationWindowName = "Notepad";

    public Notepad()
    {
        setApplicationName("notepad.exe");
        // set the root path of the Finder Window to the current user Documents folder
        setApplicationPath("");
        // each finder has the window name set to the current folder name
        setWaitWindow("Notepad");
    }

    /**
     * Edit a content in notepad
     */
    public void edit(String data)
    {
        getLdtp().enterString("txt0", data);
    }

    /**
     * Edit a content in notepad
     */
    public void appendData(String data)
    {
        getLdtp().appendText("txt0", data);
    }

    /**
     * Save As notepad in a particular location
     * 
     * @throws Exception
     */
    public void saveAs(File destinationFile) throws Exception
    {
        getLdtp().doubleClick("File");
        getLdtp().click("Save As");
        waitForWindow("Save As");
        getLdtp().enterString("txtFilename", destinationFile.getPath());
        getLdtp().click("btnSave");
        LdtpUtils.waitToLoopTime(1);
    }

    /**
     * Close the notepad application after the save
     */
    public void close(File fileName) throws LdtpExecutionError
    {
        focus(fileName);
        getLdtp().doubleClick("File");
        getLdtp().click("Exit");
    }

    /**
     * Ctrl - S and save the file
     */
    public void save()
    {
        getLdtp().keyPress("<ctrl>S");
        getLdtp().keyRelease("<ctrl>S");
    }

    /**
     * Focus Notepad - bring to top, based on filename parameter
     * 
     * @param filename
     */
    public void focus(File partialFileName)
    {
        String winName = partialFileName.getName();
        try
        {
            waitForApplicationWindow(winName, true);
            String fullName = LdtpUtils.getFullWindowList(getLdtp(), partialFileName.getName());
            getLdtp().activateWindow(fullName);
        }
        catch (Exception e)
        {
            logger.error("Could not find Notepad file:" + winName, e);
        }
    }
}

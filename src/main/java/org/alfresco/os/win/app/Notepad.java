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
 * This class has all the method involved in using the actions in Notepad application 2013
 * 
 * @author sprasanna
 */

package org.alfresco.os.win.app;

import java.io.File;
import java.io.IOException;

import org.alfresco.os.win.Application;

import com.cobra.ldtp.LdtpExecutionError;

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
    public void editNotepad(String data)
    {
        getLdtp().enterString("txt0", data);
    }

    /**
     * Edit a content in notepad
     */
    public void appendTextToNotepad(String data)
    {
        getLdtp().appendText("txt0", data);
    }

    /**
     * Save As notepad in a particular location
     * 
     * @throws IOException
     * @throws LdtpExecutionError
     */
    public void saveAsNotpad(File destinationFile) throws LdtpExecutionError, IOException
    {
        getLdtp().doubleClick("File");
        getLdtp().click("Save As");
        getLdtp().activateWindow("Save As");
        getLdtp().enterString("txtFilename", destinationFile.getPath());
        getLdtp().click("btnSave");
    }

    /**
     * Save a notepad in a particular location
     * 
     * @throws IOException
     * @throws LdtpExecutionError
     */
    public void saveNotpadOnClose() throws LdtpExecutionError, IOException
    {
        getLdtp().click("Save");
    }

    /**
     * Close the notepad application after the save
     */
    public void closeNotepad(String fileName) throws LdtpExecutionError
    {
        setNotepadWindow(fileName);
        getLdtp().doubleClick("File");
        getLdtp().click("Exit");
    }

    /**
     * Ctrl - S and save the file
     */
    public void ctrlSSave()
    {
        getLdtp().keyPress("<ctrl>S");
        getLdtp().keyRelease("<ctrl>S");
    }

    /**
     * Private method to find the note pad window
     */
    public void setNotepadWindow(String windowNameToFind)
    {
        try
        {
            waitForWindow(windowNameToFind);
        }
        catch (Exception e)
        {
            logger.error("Could not SET Notepad Window to: " + windowNameToFind, e);
        }
    }
}

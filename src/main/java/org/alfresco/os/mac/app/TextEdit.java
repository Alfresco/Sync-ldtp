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
import java.io.IOException;

import org.alfresco.os.mac.Editor;
import org.alfresco.os.mac.utils.AppleScript;

import com.cobra.ldtp.LdtpExecutionError;

/**
 * This class will handle operations related to TextEditor application on MAC operating system.
 * 
 * @task QA-1108
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class TextEdit extends Editor
{
    /**
     * @param version
     * @param application 'Microsoft Word.app' or 'Microsoft Outlook.app'
     * @return Ldtp
     * @throws LdtpExecutionError
     * @throws IOException
     */
    public TextEdit()
    {
        setApplicationPath("/Applications/TextEdit.app");
        setApplicationName("TextEdit");
        setWaitWindow("Untitled");// appTextEdit
        setFileName("Untitled");
    }

    /*
     * (non-Javadoc)
     * @see org.alfresco.os.mac.EditorAbstract#save(java.lang.String)
     */
    public void save(String location)
    {
        logger.info("Save document to: " + location);
        // paul.brodner: "/" char cannot be added to TextEditor via LDTP
        // we used an alternative: with AppleScript
        goToLocation(location);
        waitForFileOnDisk(new File(location));
    }

    /**
     * Helper for saving file using File object
     * 
     * @param file
     */
    public void save(File file)
    {
        save(file.getPath());
    }

    /**
     * Using the AppleScript commands, we can activate and add proper input values
     * This will input "/" - so Go to Folder dialog will appear
     * will add the location string and save the file
     * 
     * @param folder
     */
    private void goToLocation(String location)
    {
        AppleScript appleScript = getAppleScript();
        appleScript.clean();
        appleScript.addCommandScript("tell app \"TextEdit\" to activate");
        appleScript.addCommandScript("delay 2");
        appleScript.addCommandScript("tell application \"System Events\"");
        appleScript.addCommandScript("keystroke \"s\" using {command down}");
        appleScript.addCommandScript("delay 1.5");
        appleScript.addCommandScript("keystroke \"a\" using {command down}");
        appleScript.addCommandScript("delay 0.5");
        appleScript.addCommandScript("keystroke \"a\" using {command down}");
        appleScript.addCommandScript("delay 1");
        appleScript.addCommandScript("keystroke \"" + location + "\"");
        appleScript.addCommandScript("delay 1");
        appleScript.addCommandScript("keystroke return");
        appleScript.addCommandScript("delay 1");
        appleScript.addCommandScript("keystroke return");
        appleScript.addCommandScript("end tell");
        appleScript.run();
    }

    /*
     * (non-Javadoc)
     * @see org.alfresco.os.mac.EditorAbstract#saveAndClose()
     */
    public void saveAndClose()
    {
        logger.info("Save and close current file.");
        getLdtp().generateKeyEvent("<command>s");
        close(getFileName());
    }

    public void saveAs(File file)
    {
        logger.info("Save file As: " + file.getPath());
        getLdtp().generateKeyEvent("<command><shift>s");
        save(file.getPath());
    }

    /**
     * Create a new File locally
     * 
     * @param file
     */
    public void createFile(File file)
    {
        logger.info("Create file: " + file.getPath());
        openApplication();
        saveAs(file);
        exitApplication();
    }
}

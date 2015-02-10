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

import org.alfresco.os.mac.EditorAbstract;
import org.alfresco.os.mac.utils.AppleScript;

import com.cobra.ldtp.LdtpExecutionError;

/**
 * This class will handle operations related to TextEditor application on MAC operating system.
 * 
 * @task QA-1108
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class TextEdit extends EditorAbstract
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
        // paul.brodner: "/" char cannot be added to TextEditor via LDTP
        // we used an alternative: with AppleScript
        goToLocation(location);
        focus();
        getLdtp().generateKeyEvent("<command>s");
        waitForFileOnDisk(location);
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
        logger.info("Using Location: " + location);
        AppleScript appleScript = getAppleScript();
        appleScript.clean();
        appleScript.addCommandScript("tell app \"TextEdit\" to activate");
        appleScript.addCommandScript("delay 2");
        appleScript.addCommandScript("tell application \"System Events\"");
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
        getLdtp().generateKeyEvent("<command>s");
        closeFile(getFileName());
    }

    public void saveAs(File file)
    {
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
        openApplication();
        saveAs(file);
        exitApplication();
    }
}

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

import java.io.IOException;
import java.util.Arrays;

import org.alfresco.os.mac.Application;
import org.alfresco.os.mac.Editor;
import org.alfresco.os.mac.utils.AppleMenuBar.Menu;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;

/**
 * Handle general MAC Office actions used between multiple versions of Office suite (i.e. Office 2011, Office 2010, etc.).
 * 
 * @author Paul Brodner
 */
public class MicrosoftOfficeBase extends Editor
{
    private MicrosoftDocumentConnection mdc;

    /**
     * @param version
     * @param application 'Microsoft Word.app' or 'Microsoft Outlook.app'
     * @return Ldtp
     * @throws LdtpExecutionError
     * @throws IOException
     */
    public MicrosoftOfficeBase(String version, String application, String name) throws Exception
    {
        setApplicationPath("/Applications/Microsoft Office " + version.toString() + "/" + application);
        setApplicationName(name);

        // for all Office application it seams appMicrosoftAUDaemon is used
        setWaitWindow("appMicrosoftAUDaemon");
        setMdc(new MicrosoftDocumentConnection(version));
        setApplicationVersion(version);
    }

    /**
     * Open a file from url passed
     * 
     * @param url
     */
    public void openURLByFile(String url)
    {
        getAppleMenu().activate();
        getAppleMenu().select(Menu.FILE);
        getAppleMenu().open(Menu.OPEN_URL);
        logger.debug("Open Office document from URL: " + url);
        getLdtp().enterString(url);
        getLdtp().generateKeyEvent("<enter>");
    }

    /**
     * Use the MAC Shortcut keys in order to open the URL file
     * 
     * @param url
     */
    public void openURL(String url)
    {
        getLdtp().generateKeyEvent("<command><shift>o");
        logger.debug("Open Office document from URL: " + url);
        getLdtp().enterString(url);
        getLdtp().generateKeyEvent("<enter>");
    }

    /*
     * (non-Javadoc)
     * @see org.alfresco.utilities.ApplicationAbstract#focus()
     * Bring the App on top
     */
    public void focus()
    {
        focus(getFileName());
    }

    public MicrosoftDocumentConnection getMDC()
    {
        return mdc;
    }

    public void setMdc(MicrosoftDocumentConnection mdc)
    {
        this.mdc = mdc;
    }

    /**
     * Return the state of the Office filename
     * 
     * @param filename
     * @return boolean value
     */
    public boolean isFileInReadOnlyMode(String filename)
    {
        try
        {
            waitForWindow(getWaitWindow());
        }
        catch (Exception e)
        {
            // no need for handling this exception
        }
        return Arrays.asList(getLdtp().getWindowList()).contains("frm" + filename + "(Read-Only)");
    }

    /**
     * Return the state of the Office filename
     * 
     * @param filename
     * @return boolean value
     */
    public boolean isFileInEditMode(String filename)
    {
        try
        {
            waitForWindow(getWaitWindow());
        }
        catch (Exception e)
        {
            // no need to handle this exception, function will always return a boolean value bellow
        }
        return Arrays.asList(getLdtp().getWindowList()).contains("frm" + filename);
    }

    @Override
    protected Application openApplication(String[] command) throws Exception
    {
        handleCrash();
        runProcess(command);
        waitForWindow(getWaitWindow());
        setLdtp(new Ldtp(getFileName()));
        return this;
    }

    public void addCredentials(String username, String password) throws Exception
    {
        if (waitForWindow("frmLogin") != null)
        {
            getLdtp().generateKeyEvent(username);
            getLdtp().generateKeyEvent("<tab>");
            getLdtp().generateKeyEvent(password);
            getLdtp().generateKeyEvent("<enter>");
        }
    }

    public void save(String location) throws Exception
    {
        close(getFileName());
        LdtpUtils.waitToLoopTime(2);
        getLdtp().generateKeyEvent("<enter>");
        LdtpUtils.waitToLoopTime(2);
        getLdtp().generateKeyEvent(location);
        getLdtp().generateKeyEvent("<enter>");
    }

    public void exitApplication()
    {
        try
        {
            logger.debug("Exiting Application and all processes: " + getApplicationName());
            handleCrash();
            destroyProcesses();
            killProcess();
        }
        catch (Exception e)
        {
            logger.error("Exception on exitApplication: " + e.getMessage());
        }
    }
}

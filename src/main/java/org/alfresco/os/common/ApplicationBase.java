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
package org.alfresco.os.common;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.alfresco.utilities.LdtpUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;

/**
 * Abstract class that will cover the main functionalities of Windows/MAC applications
 */
public abstract class ApplicationBase
{
    private Ldtp ldtp;

    private static Logger logger = Logger.getLogger(ApplicationBase.class);
    protected String applicationPath;
    protected String applicationName;
    protected String applicationVersion;
    private boolean useDefinedWindowFullName = false; //this is the window full name that we will find on {@link waitForApplicationWindow }

    public String getApplicationVersion()
    {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion)
    {
        this.applicationVersion = applicationVersion;
    }

    protected Properties properties = null;
    protected String waitWindow;

    private static final String START_DELIMITER = "${";
    private static final String END_DELIMITER = "}";
    private ArrayList<Process> processes = new ArrayList<Process>();

    public abstract void exitApplication();

    public abstract ApplicationBase openApplication() throws Exception;

    public abstract void closeWindow();

    public abstract void killProcess();

    protected abstract Ldtp initializeLdtp(); // add methods for initialising the LDTP class

    /**
     * Open the application, based on command passed
     * This will also wait until the application is opened
     *
     * @param command
     * @return
     * @throws Exception
     */
    protected ApplicationBase openApplication(String[] command) throws Exception
    {
        LdtpUtils.logInfo("Opening Application: " + Arrays.asList(command));
        runProcess(command);
        //waitForApplicationWindow(getWaitWindow(), true);
        return this;
    }

    /**
     * @return the Application name
     */
    public String getApplicationName()
    {
        return applicationName;
    }

    /**
     * This is the WaitWindow of any application.
     * This window string will tell us when the application is loaded.
     *
     * @return
     */
    public String getWaitWindow()
    {
        return waitWindow;
    }

    public ArrayList<Process> getProcesses()
    {
        return processes;
    }

    public void setProcess(Process processe)
    {
        this.processes.add(processe);
    }

    /**
     * This will initialize only once the Properties object
     * You can get the value of a key property
     *
     * @return String
     * @author Paul Brodner
     */
    public String getProperty(String key)
    {
        if (properties == null)
        {
            properties = new Properties();
            URL propertyURI = this.getClass().getClassLoader().getResource(LdtpUtils.PROPERTIES_FILE);
            if (propertyURI == null)
            {
                logger.error("Could not find property file: " + LdtpUtils.PROPERTIES_FILE);
                return null;
            }
            File propertiesFile = new File(propertyURI.getPath());

            if (!propertiesFile.exists())
            {
                logger.error("Propertie file: " + propertiesFile.getPath() + " does NOT exists");
                return null;
            }

            try
            {
                properties.load(this.getClass().getClassLoader().getResourceAsStream(LdtpUtils.PROPERTIES_FILE));
            }
            catch (IOException e)
            {
                logger.error("Could NOT READ Properties file: " + e.getMessage());
            }
        }
        String value = properties.getProperty(key);

        if (value != null)
        {
            int startIndex = 0;
            int endIndex = 0;
            while ((startIndex = value.indexOf(START_DELIMITER, endIndex)) >= 0 && (endIndex = value.indexOf(END_DELIMITER, startIndex)) >= 0)
            {
                String variableName = value.substring(startIndex + START_DELIMITER.length(), endIndex);
                // now call getProperty recursively to have this looked up
                String variableValue = null;

                if (!variableName.equals(key))
                {
                    // only recurse if the variable does not equal our own original key
                    variableValue = this.getProperty(variableName);
                }

                if (variableValue == null)
                {
                    // when unable to find the variable value, just return it as the variable name
                    variableValue = START_DELIMITER + variableName + END_DELIMITER;
                }
                value = value.replace(START_DELIMITER + variableName + END_DELIMITER, variableValue);
            }
        }
        return value;
    }

    /**
     * This will a specific process based on command array list passed
     * It's best to use this alternative rather than Runtime.getRuntime().exec
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @author Paul Brodner
     */
    public Process runProcess(String... command) throws Exception
    {
        Process process = LdtpUtils.runProcess(command);
        setProcess(process);
        return process;
    }

    /**
     * @author Paul Brodner
     * Destroyed the processes opened
     */
    protected void destroyProcesses()
    {

        try
        {
            for (Iterator<Process> iterator = getProcesses().iterator(); iterator.hasNext(); )
            {
                Process process = iterator.next();
                logger.debug("Destroy process: " + process.toString());
                process.destroy();
            }
        }
        catch (Exception e)
        {
            logger.error("Error on destroy Process: " + e.getMessage());
        }
    }

    /**
     * This method will initialize or reuse the same LDTP object for interacting with GUI app
     *
     * @return Ldtp
     */
    public Ldtp getLdtp()
    {
        if (ldtp == null)
        {
            try
            {
                ldtp = initializeLdtp();
                return ldtp;
            }
            catch (Exception e)
            {
                logger.error("Error Initializing LDTP: " + e.getMessage());
            }
            ldtp = new Ldtp("dummy");
            LdtpUtils.logDebug("Initialized LDTP with default wait window: " + getWaitWindow());
        }
        return ldtp;
    }

    protected void setLdtp(Ldtp ldtp)
    {
        this.ldtp = ldtp;
    }

    /**
     * @return all open applications as String Array
     * @author Paul Brodner
     */
    public String[] getOpenApplications()
    {
        return getLdtp().getAppList();
    }

    public void focus()
    {
        logger.info("Focusing: " + getWaitWindow());
        getLdtp().setWindowName(getWaitWindow());
        getLdtp().activateWindow(getWaitWindow());
    }

    public void minimize()
    {
        focus();
        getLdtp().click("btnminimizebutton");
    }

    /**
     * Wait for a Application main window
     * If you want to wait to specific dialogs, use waitForWindow
     *
     * @param windowName
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @author Paul Brodner
     */
    public Ldtp waitForApplicationWindow(String windowName, boolean defineGetLDTP)
    {
        Ldtp _ldtp = initializeLdtp();
        int retries = 0;
        windowName = windowName.replaceAll("\\*", "");
        // here we will wait until the window is visible
        LdtpUtils.logInfo("WaitForApplicationWindow '" + windowName + "' between all windows...");
        while (retries <= LdtpUtils.RETRY_COUNT)
        {
            String[] windowList = _ldtp.getWindowList();
            for (String window : windowList)
            {
                LdtpUtils.logInfo(String.format("Window [%s] expected, but found: %s. Waiting...", windowName, window));
                if (window.contains(windowName))
                {
                    if (isDefinedWindowFullName())
                    {
                        return _ldtp;
                    }
                    _ldtp = new Ldtp(window);
                    if (defineGetLDTP)
                    {
                        setWaitWindow(window);
                        setLdtp(_ldtp);
                        return getLdtp();
                    }
                    return _ldtp;
                }
            }
            LdtpUtils.waitToLoopTime(1);
            retries += 1;
        }
        return null;
    }

    /**
     * Wait for a Window
     *
     * @param windowName
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @author Paul Brodner
     */
    public Ldtp waitForWindow(String windowName) throws Exception
    {
        return waitForApplicationWindow(windowName, false);
    }

    /**
     * Wait until this <windowName> is closed
     *
     * @param windowName
     * @throws InterruptedException
     * @throws IOException
     */
    public void waitUntilWindowIsClosed(String windowName) throws Exception
    {
        int retries = 0;
        Ldtp _ldtp = initializeLdtp();
        while (retries <= 3)
        {
            String[] windowList = _ldtp.getWindowList();
            for (String window : windowList)
            {
                LdtpUtils.logDebug(String.format("Window [%s] expected, but found: %s. Waiting...", windowName, window));

                if (!window.contains(windowName))
                {
                    return;
                }
            }
            Thread.sleep(1000);
            retries += 1;
        }
    }

    /**
     * Check if a windows identified by <windowName> is opened or not
     *
     * @param windowName
     * @return
     */
    public boolean isWindowOpened(String windowName)
    {
        return LdtpUtils.isWindowOpened(getLdtp(), windowName);
    }

    /**
     * Check if a button is enabled or not
     *
     * @param buttonName
     * @return boolean value
     */
    protected boolean isBtnEnabled(String buttonName)
    {
        return getLdtp().stateEnabled(buttonName) == 1;
    }

    /**
     * @return the Application path
     */
    public String getApplicationPath()
    {
        return applicationPath;
    }

    /**
     * Used mainly for wait process: waiting until the specified windows is opened
     *
     * @param waitWindow
     * @author Paul Brodner
     */
    protected void setWaitWindow(String waitWindow)
    {
        this.waitWindow = waitWindow;
    }

    /**
     * Define the application name
     *
     * @param applicationName
     * @author Paul Brodner
     */
    protected void setApplicationName(String applicationName)
    {
        this.applicationName = applicationName;
    }

    /**
     * Add the Application full path
     *
     * @param applicationPath
     * @author Paul Brodner
     */
    protected void setApplicationPath(String applicationPath)
    {
        this.applicationPath = applicationPath;
    }

    /**
     * Click on a Button only when is enabled
     *
     * @param name
     * @author Paul Brodner
     */
    protected void clickButton(String name)
    {
        int btnEnabled = 0;
        int cnt = 0;
        while (btnEnabled == 0 && cnt < LdtpUtils.RETRY_COUNT)
        {
            LdtpUtils.logDebug("Wait until we can " + name + " the File...");
            getLdtp().waitTime(1);
            btnEnabled = getLdtp().stateEnabled(name);
            cnt++;
        }
        getLdtp().click(name);
    }

    /**
     * @return the useWindowFullName
     */
    public boolean isDefinedWindowFullName()
    {
        return useDefinedWindowFullName;
    }

    /**
     * @param useWindowFullName the useWindowFullName to set
     */
    public void setUseDefinedWindowFullName(boolean useWindowFullName)
    {
        this.useDefinedWindowFullName = useWindowFullName;
    }

    public void pasteString(String value)
    {
        StringSelection stringSelection = new StringSelection(value);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        if (SystemUtils.IS_OS_WINDOWS)
        {
            getLdtp().generateKeyEvent("<ctrl>v");
        }
        else
        {
            getLdtp().generateKeyEvent("<command>v");
        }
    }
}

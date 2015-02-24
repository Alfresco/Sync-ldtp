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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.alfresco.utilities.LdtpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cobra.ldtp.Ldtp;

/**
 * Abstract class that will cover the main functionalities of Windows/MAC applications
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public abstract class ApplicationBase
{
    private Ldtp ldtp;

    protected static Log logger = onThisClass();
    protected String applicationPath;
    protected String applicationName;
    protected String applicationVersion;

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
        LdtpUtils.logInfo("Opening Application: " + command.toString());
        runProcess(command);
        LdtpUtils.waitToLoopTime(2);
        waitForApplicationWindow(getWaitWindow(), true);
        return this;
    }

    /**
     * This will return the logger of the caller
     * 
     * @return logger of the class name
     */
    protected static Log onThisClass()
    {
        StackTraceElement thisCaller = Thread.currentThread().getStackTrace()[2];
        return LogFactory.getLog(thisCaller.getClassName());
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
     * @author Paul Brodner
     * @return String
     */
    public String getProperty(String key)
    {
        if (properties == null)
        {
            properties = new Properties();

            File propertiesFile = new File(this.getClass().getClassLoader().getResourceAsStream(LdtpUtils.PROPERTIES_FILE).toString());

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
     * @author Paul Brodner
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public Process runProcess(String... command)
    {
        Process process = LdtpUtils.runProcess(command);
        setProcess(process);
        return process;
    }

    /**
     * @author Paul Brodner
     *         Destroyed the processes opened
     */
    protected void destroyProcesses()
    {

        try
        {
            for (Iterator<Process> iterator = getProcesses().iterator(); iterator.hasNext();)
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
     * @author Paul Brodner
     * @return all open applications as String Array
     */
    public String[] getOpenApplications()
    {
        return getLdtp().getAppList();
    }

    public void focus()
    {
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
     * @author Paul Brodner
     * @param windowName
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public Ldtp waitForApplicationWindow(String windowName, boolean defineGetLDTP) throws Exception
    {
        Ldtp _ldtp = initializeLdtp();
        int retries = 0;
        // here we will wait until the window is visible
        LdtpUtils.logInfo("WaitForApplicationWindow '" + windowName + "' between all windows...");
        while (retries <= LdtpUtils.RETRY_COUNT)
        {
            String[] windowList = _ldtp.getWindowList();
            for (String window : windowList)
            {
                LdtpUtils.logDebug(String.format("Window [%s] expected, but found: %s. Waiting...", windowName, window));

                if (window.contains(windowName))
                {
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
     * @author Paul Brodner
     * @param windowName
     * @return
     * @throws InterruptedException
     * @throws IOException
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
        String[] windows = null;
        boolean isOpened = false;
        windows = getLdtp().getWindowList();
        windowName = windowName.toLowerCase();
        for (int i = 0; i < windows.length; i++)
        {
            if (windows[i].toLowerCase().contains(windowName))
            {
                isOpened = true;
                break;
            }
        }
        return isOpened;
    }

    /**
     * Check if a button is enabled or not
     * 
     * @param buttonName
     * @return boolean value
     */
    public boolean isBtnEnabled(String buttonName)
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
     * @author Paul Brodner
     * @param waitWindow
     */
    protected void setWaitWindow(String waitWindow)
    {
        this.waitWindow = waitWindow;
    }

    /**
     * Define the application name
     * 
     * @author Paul Brodner
     * @param applicationName
     */
    protected void setApplicationName(String applicationName)
    {
        this.applicationName = applicationName;
    }

    /**
     * Add the Application full path
     * 
     * @author Paul Brodner
     * @param applicationPath
     */
    protected void setApplicationPath(String applicationPath)
    {
        this.applicationPath = applicationPath;
    }

    /**
     * Click on a Button only when is enabled
     * 
     * @author Paul Brodner
     * @param name
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
}

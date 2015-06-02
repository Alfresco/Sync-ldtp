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

package org.alfresco.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;
import com.google.common.io.Files;

/**
 * LDTP Utility class.
 * This class should have only static methods added.
 * 
 * @author Subashni Prasanna
 * @author Paul Brodner
 */
public class LdtpUtils
{
    private static Log logger = LogFactory.getLog(LdtpUtils.class);
    private static boolean isInfoEnabled = logger.isInfoEnabled();
    private static boolean isDebugEnabled = logger.isDebugEnabled();

    // CONSTANTS
    public static final int RETRY_COUNT = 15;
    public static final String PROPERTIES_FILE = "alfresco-ldtp.properties";

    /**
     * Show info message
     * 
     * @param message
     */
    public static void logInfo(String message)
    {
        if (isInfoEnabled)
        {
            logger.info(message);
        }
    }

    /**
     * Show Debug message
     * 
     * @param message
     */
    public static void logDebug(String message)
    {
        if (isDebugEnabled)
        {
            logger.info(message);
        }
    }

    /**
     * This will kill a process based on windowsName - works on linux/mac OS
     * Fow windows application, just pass the exe name of the application as <windowName> e.g. "notepad.exe"
     * 
     * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
     * @param windowName
     */
    public static void killProcessByWindowName(String windowName)
    {

        if (SystemUtils.IS_OS_MAC)
        {
            executeOnUnix("kill `ps ax | grep \"" + windowName + "\" | awk '{print $1}'`");
        }

        if (SystemUtils.IS_OS_WINDOWS)
        {
            execute(new String[] { "taskkill", "/F", "/IM", windowName });
        }
    }

    /**
     * check if File exists
     * 
     * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
     * @param filePath
     * @return boolean
     */
    public static boolean isFilePresent(String filePath)
    {
        return new File(filePath).exists();
    }

    /**
     * Check if Directory exists
     * 
     * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
     * @param directoryPath
     * @return
     */
    public static boolean isDirectoryPresent(String directoryPath)
    {
        return isFilePresent(directoryPath);
    }

    /**
     * Wait for an object
     * 
     * @param ldtp
     * @param objectName
     */
    public static void waitForObject(Ldtp ldtp, String objectName)
    {
        int counter = 0;
        int exists = 0;

        while (counter < RETRY_COUNT)
        {
            exists = ldtp.objectExist(objectName);
            if (exists == 1)
                break;
            else
            {
                counter++;
                try
                {
                    ldtp.wait(1);
                }
                catch (InterruptedException e)
                {
                }
            }
        }
    }
    
    /**
     * Wait for a partial ldtp object
     * Example:
     * if the object searched is something like: lblFailedtoconnecttoserverhttp
     * then you can use just the partial name to wait for it: lblFailed
     * 
     * @param ldtp
     * @param partialObjectName
     */
    public static void waitForPartialObject(Ldtp ldtp, String partialObjectName)
    {
        int counter = 0;
        logger.info("Waiting for partial object: " + partialObjectName);
        
        while (counter < RETRY_COUNT)
        {
            if (getFullObjectList(ldtp, partialObjectName) !="")
                break;
            else
            {
                counter++;
                try
                {
                    ldtp.wait(1);
                }
                catch (Exception e)
                {
                }
            }
        }
    }

    /**
     * Execute any Terminal commands
     * 
     * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
     * @param command
     * @return
     */
    public static String executeOnUnix(String command)
    {
        logInfo("Run Command: " + command);

        StringBuilder sb = new StringBuilder();
        String[] commands = new String[] { "/bin/sh", "-c", command };
        try
        {
            Process proc = new ProcessBuilder(commands).start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null)
            {
                sb.append(s);
                sb.append("\n");
            }

            while ((s = stdError.readLine()) != null)
            {
                sb.append(s);
                sb.append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Execute a command
     * 
     * @example: execute(new String[] { "killall", getApplicationName() }) for MAC
     * @param command
     */
    public static void execute(String[] command)
    {
        try
        {
            Runtime.getRuntime().exec(command);
        }
        catch (IOException e)
        {
            logger.error("Could not execute command", e.getCause());
        }
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
    public static Process runProcess(String... command)
    {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process;
        try
        {
            process = pb.start();
            LdtpUtils.logDebug("Running Process:" + command.toString());
            return process;
        }
        catch (IOException e)
        {
            logger.error("Could not run process", e.getCause());
        }
        return null;
    }

/**
     * This method will transform the input string to a recognized output LDAP command.
     * Example:
     * inputs like <img src="aaa"> cannot be processed by LDAP enterString method, so we need to pre-process it
     * transforming '<' into '<shift>.' for example and so on.
     * @author Paul Brodner
     * @param input
     * @return
     */
    public static String toLdapString(String input)
    {
        String results = "";
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (c == '<')
            {
                results += "<shift>,";
            }
            else if (c == '>')
            {
                results += "<shift>.";
            }
            else
            {
                results += c;
            }
        }

        return results;
    }

    /**
     * We will wait until the <seconds> are passed from current run
     * 
     * @param seconds
     */
    public static void waitToLoopTime(int seconds)
    {
        logInfo("Waiting (in loops) for: " + seconds + " second(s).");
        long currentTime;
        long endTime;
        currentTime = System.currentTimeMillis();
        do
        {
            endTime = System.currentTimeMillis();
        }
        while (endTime - currentTime < (seconds * 1000));
    }

    public static String getOS()
    {
        return System.getProperty("os.name");
    }

    public static File getHomeFolder()
    {
        return new File(System.getProperty("user.home"));
    }

    public static File getDocumentsFolder()
    {
        if (SystemUtils.IS_OS_MAC)
        {
            return new File(getHomeFolder(), "Documents");
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            return new File(getHomeFolder(), "My Documents");
        }
        return null;
    }

    /**
     * TODO add getTrashFolderLocation for Windows
     */
    public static File getTrashFolderLocation()
    {
        if (SystemUtils.IS_OS_MAC)
        {
            return new File(getHomeFolder(), "Trash");
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            return new File(getHomeFolder(), "TBD");
        }
        return null;
    }

    /**
     * Return a folder that was deleted and exist in Trash
     * 
     * @param folder
     * @return
     */
    public static File getFolderFromTrash(String folder)
    {
        return getFileFromTrash(folder);
    }

    /**
     * Return a file that was deleted and exist in Trash folder
     * 
     * @param file
     * @return
     */
    public static File getFileFromTrash(String file)
    {
        return new File(getTrashFolderLocation().getPath(), file);
    }

    /**
     * This until a file exists on disc, or the RETRY_COUNT is reached
     * 
     * @param filePath
     */
    public static void waitUntilFileExistsOnDisk(File filePath)
    {
        int retries = 1;
        while (retries <= LdtpUtils.RETRY_COUNT && !filePath.exists())
        {
            retries++;
            waitToLoopTime(2);
        }
    }

    /**
     * Check if process identified by <processName> is currently running
     * 
     * @param processName
     * @return
     */
    public static boolean isProcessRunning(String processName)
    {
        processName = processName.toLowerCase();
        Process p = null;
        try
        {
            if (SystemUtils.IS_OS_MAC)
            {
                p = Runtime.getRuntime().exec("ps -ef");
            }
            else if (SystemUtils.IS_OS_WINDOWS)
            {
                p = Runtime.getRuntime().exec(new String[] { "cmd", "/c", "tasklist" });
            }
            InputStream inputStream = p.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null)
            {
                if (line.toLowerCase().contains(processName))
                    return true;
            }
            inputStream.close();
            inputStreamReader.close();
            bufferReader.close();
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
        return false;
    }

    /**
     * Wait until process is running, or the RETRY_COUNT is reached
     * 
     * @param processName
     */
    public static void waitUntilProcessIsRunning(String processName)
    {
        boolean isRunning = false;
        int retry = 0;
        waitToLoopTime(1);
        while (!isRunning && retry <= RETRY_COUNT)
        {
            retry++;
            waitToLoopTime(1);
            isRunning = isProcessRunning(processName);
        }
    }

    /*
     * Return a File using a random name
     * The path will point to current user documents folder
     */
    public static File getRandomFileName(String extension)
    {
        return new File(LdtpUtils.getDocumentsFolder(), System.currentTimeMillis() + "." + extension);
    }

    /*
     * Return a copy of <resourceFileName> that should exist on Resource
     * The copied file is save on current user's Documents folder
     */
    public static File getNewRandomFileFromResource(String resourceFileName)
    {
        URL fileURL = Thread.currentThread().getContextClassLoader().getResource(resourceFileName);
        if (fileURL == null)
        {
            logger.error("No resource file with name: " + resourceFileName + " was found in RESOURCES folder.");
            return null;
        }
        File tmpFile = new File(fileURL.getPath());
        File randomFile = getRandomFileName(Files.getFileExtension(resourceFileName));
        try
        {
            Files.copy(tmpFile, randomFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return randomFile;
    }

    /**
     * Return the full name of the window list based on a partial value
     * 
     * @param ldtp
     * @param partialWindowList
     * @return
     */
    public static String getFullWindowList(Ldtp ldtp, String partialWindowList)
    {
        int retries = 0;
        while (retries <= LdtpUtils.RETRY_COUNT)
        {
            String[] windowList = ldtp.getWindowList();
            for (String window : windowList)
            {
                if (window.toLowerCase().contains(partialWindowList.toLowerCase()))
                {
                    return window;
                }
            }
            waitToLoopTime(1);
            retries += 1;
        }
        return null;
    }

    /**
     * Return the full name of the LDTP object
     * 
     * @param ldtp
     * @param partialObjectName
     * @return
     */
    public static String getFullObjectList(Ldtp ldtp, String partialObjectName)
    {
        String fullObjectName = "";
        String[] allObjectsWindow = ldtp.getObjectList();
        partialObjectName = partialObjectName.toLowerCase().replace(".", "");

        for (String objectWindow : allObjectsWindow)
        {
            if (objectWindow.substring(3).toLowerCase().contains(partialObjectName))
            {
                fullObjectName = objectWindow;
                return fullObjectName;
            }
        }
        return fullObjectName;
    }

    /**
     * Waits for an object to have a certain value
     *
     * @param ldtp
     * @param objectName
     * @param valueToWait
     */
    public static void waitObjectHasValue(Ldtp ldtp, String objectName, String valueToWait)
    {
        int waitInSeconds = 2;
        int counter = 0;
        while (counter < LdtpUtils.RETRY_COUNT)
        {
            String fileNameContent = ldtp.getTextValue(objectName);
            if (fileNameContent.equals(valueToWait))
                break;
            else
            {
                ldtp.waitTime(waitInSeconds);
                waitInSeconds = (waitInSeconds * 2);
            }
        }
    }

    /**
     * Capture screenshot
     * @return
     */
    public static File getScreenShot()
    {
        File screen = null;
        try
        {
            Ldtp ldtp = new Ldtp("*");
            screen = new File(ldtp.imageCapture());
        }
        catch (LdtpExecutionError e)
        {
            logger.error(e);
        }
        return screen;
    }
}

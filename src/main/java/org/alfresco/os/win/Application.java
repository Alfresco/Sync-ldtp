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

package org.alfresco.os.win;

import org.alfresco.os.common.ApplicationBase;
import org.alfresco.utilities.LdtpUtils;
import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Abstract class that will cover only Windows based application
 *
 */
public class Application extends ApplicationBase
{
	private static Logger logger = Logger.getLogger(Application.class);

    @Override
    public void exitApplication()
    {
        killProcess();
        LdtpUtils.waitToLoopTime(2);
    }

    @Override
    public void killProcess()
    {
    	LdtpUtils.killAllApplicationsByExeName(getApplicationName());
    }

    @Override
    protected Ldtp initializeLdtp()
    {
        Ldtp ldtp = null;
        try
        {
            ldtp = new Ldtp(getWaitWindow());
        }
        catch (LdtpExecutionError e) // it seem LDTP is not initialisez so we need to run a python script on MAC
        {
            logger.error("Could not instantiate LDTP on Windows. Please run CorbaWinLDTP.exe as administrator first", e);
        }
        return ldtp;
    }

    @Override
    public ApplicationBase openApplication() throws Exception
    {
        try
        {
            return openApplication(new String[] { getApplicationName() });
        }
        catch (Exception e)
        {
            logger.error("Could not open Application " + getApplicationName() + "Error: " + e);
        }
        return this;
    }

    public void closeWindow()
    {
        logger.info("Closing the window, clicking the 'Close' button");
        getLdtp().click("Close");
    }

    /**
     * Enum for all the application that can be used in windows
     * @author sprasanna
     *
     */
    public enum type
    {
        WORD("Microsoft Word Document"),
        EXCEL("Microsoft Excel Worksheet"),
        POWERPOINT("Microsoft PowerPoint Presentation"),
        OUTLOOK("Outlook"),
        FOLDER("Folder"),
        TEXTFILE("Text Document"),
        NOTEPAD("Notepad");

        private String application;

        private type(String type)
        {
            application = type;
        }

        public String getType()
        {
            return application;
        }
    }
    /*
     * Maximize the window
     */
    public void maximize()
    {
        String idMaximize = "btnMaximize";
        if (getLdtp().objectExist(idMaximize)==1)
        {
            clickButton(idMaximize);
        }
    }
}

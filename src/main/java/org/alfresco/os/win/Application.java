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

import com.cobra.ldtp.Ldtp;
import com.cobra.ldtp.LdtpExecutionError;

/**
 * Abstract class that will cover only Windows based application
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class Application extends ApplicationBase
{

    /**
     * Enums to hold all the applications name.
     * 
     * @author Subashni Prasanna
     */
    public enum OfficeApplication
    {
        WORD("Microsoft Word Document", "Word", "WINWORD.EXE"), 
        EXCEL("Microsoft Excel Worksheet", "Excel", "EXCEL.EXE"), 
        POWERPOINT(
                "Microsoft PowerPoint Presentation",
                "PowerPoint",
                "POWERPNT.EXE"), 
        OUTLOOK("Outlook", "Outlook", "OUTLOOK.exe");

        private String application;
        private String waitWindow;
        private String exeName;

        private OfficeApplication(String type, String waitWindow, String exeName)
        {
            this.application = type;
            this.waitWindow = waitWindow;
            this.exeName = exeName;
        }

        public String getName()
        {
            return application;
        }

        public String getWaitWindow()
        {
            return waitWindow;
        }

        public String getExeName()
        {
            return exeName;
        }
    }

    @Override
    public void exitApplication()
    {
        killProcess();

    }

    @Override
    public void killProcess()
    {
        LdtpUtils.execute(new String[] { "taskkill", "/IM", getApplicationName() });

    }

    @Override
    protected Ldtp initializeLdtp()
    {
        Ldtp ldtp = null;
        try
        {
            ldtp = new Ldtp("a");
        }
        catch (LdtpExecutionError e) // it seem LDTP is not initialisez so we need to run a python script on MAC
        {
            logger.error("Could not instantiate LDTP on Windows. Please run CorbaWinLDTP.exe as administrator first", e);
        }
        return ldtp;
    }

    @Override
    public ApplicationBase openApplication()
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

}

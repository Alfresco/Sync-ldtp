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

package org.alfresco.os.mac;

import org.alfresco.os.ApplicationBaseAbstract;
import org.alfresco.os.mac.utils.AppleScript;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.Ldtp;

/**
 * Abstract class that will cover only Windows based application
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public abstract class ApplicationAbstract extends ApplicationBaseAbstract
{
    public abstract void exitApplication();

    private AppleScript appleScript;

    public AppleScript getAppleScript()
    {
        if (appleScript == null)
            appleScript = new AppleScript();

        return appleScript;
    }

    @Override
    public void killProcess()
    {
        LdtpUtils.logDebug("Kill Application process: " + getApplicationName());
        LdtpUtils.execute(new String[] { "killall", getApplicationName() });
        killPython();
    }

    public void killPython()
    {
        LdtpUtils.logDebug("Kill Python process: Python");
        LdtpUtils.executeOnUnix("kill `ps ax | grep \"python\" | awk '{print $1}'`");
    }

    @Override
    protected Ldtp initializeLdtp()
    {
        Ldtp ldtp = null;
        try
        {
            ldtp = new Ldtp(getWaitWindow());
        }
        catch (Exception e) // it seem LDTP is not initialisez so we need to run a python script on MAC
        {
            runProcess("python", "-i", this.getClass().getClassLoader().getResource("startLdtp.py").getPath());
            LdtpUtils.waitUntilProcessIsRunning("python");
            ldtp = new Ldtp(getWaitWindow());
        }
        return ldtp;
    }

    public ApplicationAbstract openApplication()
    {
        try
        {
            openApplication(new String[] { "open", getApplicationPath() });
        }
        catch (Exception e)
        {
            logger.error("Could not open Application: " + getApplicationName() + " " + e.getStackTrace());
        }
        return this;
    }

}

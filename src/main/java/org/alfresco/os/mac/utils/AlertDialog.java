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

package org.alfresco.os.mac.utils;

import org.alfresco.os.mac.Application;
import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.Ldtp;

/**
 * This class will handle all alerts from Finder Explorer.
 * 
 * @task QA-1107
 * @author Paul Brodner
 */
public class AlertDialog extends Application
{

    private Ldtp alert;
    private String identifier;

    public AlertDialog()
    {
        identifier = "frm0";
        setWaitWindow(identifier);
    }

    /**
     * Check if alert is opened in Finder
     * Example: the alert window when you want to create a new folder with the same name
     * 
     * @return boolean value
     */
    public boolean isAlertPresent()
    {
        return isWindowOpened("0");
    }

    public Ldtp dialog()
    {
        alert = new Ldtp(identifier);
        return alert;
    }

    /**
     * close Alert dialog
     */
    public void closeAlert()
    {
        if (isAlertPresent())
        {
            LdtpUtils.logInfo("Alert Dialog is opened. Close Alert.");
            dialog().click("btnOK");
        }
    }

    @Override
    public void exitApplication()
    {
        closeAlert();
    }
}

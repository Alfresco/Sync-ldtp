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

import org.alfresco.os.mac.Application;

/**
 * Handles AlfrescoDesktopSync tool on MAC system
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class DesktopSync extends Application
{
    public DesktopSync()
    {
        setApplicationName("AlfrescoDesktopSyncClient");

        // set the root path of the Finder Window to the current user Documents folder
        setApplicationPath("/Applications/AlfrescoDesktopSyncClient.app");

        // each finder has the window name set to the current folder name
        setWaitWindow("AlfrescoDesktopSyncClient");
    }

    /**
     * Try to Sync the folder based on the
     */
    public void synchNow()
    {
        getLdtp().click("mnu0");
        getLdtp().click("mnuSyncNow!");
    }

    @Override
    public void exitApplication()
    {
        killProcess();
    }
}

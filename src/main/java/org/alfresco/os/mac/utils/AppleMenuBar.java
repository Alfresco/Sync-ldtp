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

import org.alfresco.utilities.LdtpUtils;

import com.cobra.ldtp.Ldtp;

/**
 * Class that will handle the AppleMenu bar. Useful for accessing Apple specific menus.
 * 
 * @task QA-1107
 * @author Paul Brodner
 */
public class AppleMenuBar
{
    private Ldtp ldtp;

    public enum Menu
    {
        FILE(2, "right"), OPEN_URL(4, "down"), SAVE(7, "down");

        private String orientation;
        private int location;

        /**
         * Name of the File from Apple menu
         * Location is the 'index' of the menu (x times we need to press right key in order to open this)
         * 
         * @param name
         * @param location
         */
        private Menu(int location, String orientation)
        {
            this.orientation = orientation;
            this.location = location;
        }
    }

    public AppleMenuBar()
    {
        ldtp = new Ldtp("");
    }

    public AppleMenuBar(Ldtp demo)
    {
        ldtp = demo;
    }

    /**
     * Just click on the Apple Logo from top Menu
     * 
     * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
     */
    public void activate()
    {
        LdtpUtils.logDebug("Activate AppleMenuBar");
        ldtp.click("mnumenubar");
        ldtp.click("Apple");
    }

    /**
     * This will navigate (via send keys) on the Apple menus. Pay attention to focus first the menu
     * 
     * @param menu
     */
    public void select(Menu menu)
    {
        LdtpUtils.logDebug("Select Menu: " + menu.name());
        for (int i = 1; i <= menu.location; i++)
        {
            ldtp.generateKeyEvent(String.format("<%s>", menu.orientation));
        }
    }

    /**
     * This will actually open a specific menu
     * 
     * @param menu
     */
    public void open(Menu menu)
    {
        select(menu);
        LdtpUtils.logDebug("Open Menu: " + menu.name());
        ldtp.generateKeyEvent("<enter>");
    }
}

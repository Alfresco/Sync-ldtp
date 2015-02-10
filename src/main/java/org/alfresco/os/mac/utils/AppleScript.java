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

import java.util.ArrayList;

import org.alfresco.utilities.LdtpUtils;

/**
 * This class is a wrapper over OSA Scripting - Apple Script.
 * https://developer.apple.com/library/mac/documentation/Darwin/Reference/ManPages/man1/osascript.1.html
 * Some LDTP actions will not work on all GUI based application, so we will this built in tool in order to perform simple operations.
 * AppleScript will not replace the LDTP implementation, will help LDTP to perform differed tasks.
 * 
 * @task QA-1124
 * @author Paul Brodner
 */
public class AppleScript
{
    private ArrayList<String> commandLine = new ArrayList<String>();

    /**
     * add command strings to <commandLine>
     * Each statement will be added using -e flag, so osascript will take this as a multi-line script.
     * 
     * @param command
     */
    public void addCommandScript(String command)
    {
        commandLine.add("-e");
        commandLine.add(command);
    }

    /**
     * Run the script
     */
    public void run()
    {
        commandLine.add(0, "osascript");
        String[] command = new String[commandLine.size()];
        command = commandLine.toArray(command);
        LdtpUtils.runProcess(command);
    }

    public void clean()
    {
        commandLine.clear();
    }

    public ArrayList<String> getCommandLines()
    {
        return commandLine;
    }
}

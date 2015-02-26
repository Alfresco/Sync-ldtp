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

package org.alfresco.os.win.app.office;

import java.io.IOException;

import com.cobra.ldtp.LdtpExecutionError;

/**
 * This class has all the method involved in using the actions in office 2010
 * 
 * @author Subashni Prasanna
 */
public class MicrosoftOffice2013 extends MicrosoftOfficeBase
{
    public String OFFICE_PATH = "C:\\Program Files\\Microsoft Office\\Office15";
    

    public MicrosoftOffice2013(VersionDetails officeApplication)
    {
        super(officeApplication, "2013");
    }

    /**
     * Method to up check the start up window
     * 
     * @throws IOException
     * @throws LdtpExecutionError
     */
    public void unCheckStartUp(String path) throws LdtpExecutionError, IOException
    {
       
        getLdtp().click(applicationDetails.getBlankDoc());
        
        getLdtp().click("File Tab");
        getLdtp().click("Options");
        getLdtp().activateWindow("Excel Options");
        if (getLdtp().check("Show the Start screen when this application starts") == 1)
        {
            getLdtp().unCheck("Show the Start screen when this application starts");
        }
        getLdtp().click("OK");
        getLdtp().closeWindow(getWaitWindow());
    }
}

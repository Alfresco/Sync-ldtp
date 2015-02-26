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


/**
 * This class has all the method involved in using the actions in office 2010
 * 
 * @author Subashni Prasanna
 */
public class MicrosoftOffice2010 extends MicrosoftOfficeBase
{
    public String OFFICE_PATH = "C:\\Program Files\\Microsoft Office\\Office14";
   
    public MicrosoftOffice2010(VersionDetails officeApplication)
    {
        super(officeApplication, "2010");
    }
}

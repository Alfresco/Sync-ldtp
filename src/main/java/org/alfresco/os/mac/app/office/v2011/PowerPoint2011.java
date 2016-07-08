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
package org.alfresco.os.mac.app.office.v2011;

import org.alfresco.os.mac.app.office.MicrosoftOfficeBase;

/**
 * Microsoft Office PowerPoint 2011 GUI - implementation.
 * 
 */
public class PowerPoint2011 extends MicrosoftOfficeBase
{
    // please take in consideration to check "Don't show this when opening Power Point" option manually
    public PowerPoint2011() throws Exception
    {
        super("2011", "Microsoft PowerPoint.app", "Microsoft PowerPoint");

        setFileName("Presentation1"); // this is the default filename that will be opened when a new PowerPoint document is created
    }
}

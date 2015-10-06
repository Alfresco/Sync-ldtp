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


package org.alfresco.os.common.gui;

import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;

/**
 * @author Paul Brodner
 *
 */
public class Button extends Element
{
    final Logger logger = Logger.getLogger(Button.class);

    public Button(Ldtp parent, String identificator)
    {
        super(parent, identificator);
    }

    public void click()
    {
        logger.info("Clicking button identified by: " + getIdentificator());
        getLdtp().click(getIdentificator());
    }

    @Override
    public String getStatus()
    {
        return ((isEnabled() == true) ? "Enabled" : "Disabled");
    }

}

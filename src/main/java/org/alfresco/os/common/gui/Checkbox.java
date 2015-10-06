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

import com.cobra.ldtp.Ldtp;

/**
 * @author Paul Brodner
 *
 */
public class Checkbox extends Element
{
    public Checkbox(Ldtp parent, String identificator)
    {
        super(parent, identificator);
    }

    public boolean isChecked()
    {
        return getLdtp().verifyCheck(getIdentificator()) == 1;
    }

    public boolean isUnchecked()
    {
        return getLdtp().verifyCheck(getIdentificator()) == 0;
    }

    public void check()
    {
        getLdtp().check(getIdentificator());
    }

    public void uncheck()
    {
        getLdtp().unCheck(getIdentificator());
    }

    @Override
    public String getStatus()
    {
        return ((isChecked() == true) ? "Checked" : "UnChecked");
    }
}

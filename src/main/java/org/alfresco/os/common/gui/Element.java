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
public abstract class Element
{
    private String identificator;
    private Ldtp ldtp;

    public abstract String getStatus();

    public Element(Ldtp parent, String identificator)
    {
        setIdentificator(identificator);
        setLdtp(parent);
    }

    public String getIdentificator()
    {
        return identificator;
    }

    private void setIdentificator(String identificator)
    {
        this.identificator = identificator;
    }

    public boolean isEnabled()
    {
        return getLdtp().stateEnabled(getIdentificator()) == 1;
    }

    public boolean isDisabled()
    {
        return  getLdtp().stateEnabled(getIdentificator()) == 0;
    }
   
    protected Ldtp getLdtp()
    {
        return ldtp;
    }

    private void setLdtp(Ldtp ldtp)
    {
        this.ldtp = ldtp;
    }
}

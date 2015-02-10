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

package org.alfresco.exceptions;

/**
 * Custom Exception class
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class ExceptionBase extends Exception
{
    private static final long serialVersionUID = -8500644323000193749L;

    public ExceptionBase()
    {
    }

    public ExceptionBase(String message)
    {
        super(message);
    }

    public ExceptionBase(Throwable cause)
    {
        super(cause);
    }

    public ExceptionBase(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ExceptionBase(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

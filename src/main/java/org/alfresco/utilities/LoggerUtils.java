package org.alfresco.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils
{
    /**
     * Dynamically get the class logger
     * 
     * @return
     */
    public static Logger getLogger()
    {
        StackTraceElement myCaller = Thread.currentThread().getStackTrace()[2];
        return  LoggerFactory.getLogger(myCaller.getClassName());
    }
}

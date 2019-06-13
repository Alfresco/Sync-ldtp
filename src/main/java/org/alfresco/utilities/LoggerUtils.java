package org.alfresco.utilities;

import org.apache.log4j.Logger;

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
        return Logger.getLogger(myCaller.getClassName());
    }
}

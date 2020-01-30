package org.alfresco.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        return LogManager.getLogger(myCaller.getClassName());
    }
}

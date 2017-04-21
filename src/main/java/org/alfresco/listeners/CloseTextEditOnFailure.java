package org.alfresco.listeners;

import org.alfresco.utilities.LdtpUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CloseTextEditOnFailure extends TestListenerAdapter
{
    @Override
    public void onTestFailure(ITestResult tr)
    {
        if(LdtpUtils.isProcessRunning("TextEdit"))
        {
        LdtpUtils.execute(new String[] { "killall", "TextEdit" });
        super.onTestFailure(tr);
        }
    }
    
}

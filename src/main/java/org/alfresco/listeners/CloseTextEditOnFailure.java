package org.alfresco.listeners;

import org.alfresco.utilities.LdtpUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CloseTextEditOnFailure extends TestListenerAdapter
{

    @Override
    public void onTestFailure(ITestResult tr)
    {
        LdtpUtils.killProcessByWindowName("TextEdit");
        super.onTestFailure(tr);
    }
    
}

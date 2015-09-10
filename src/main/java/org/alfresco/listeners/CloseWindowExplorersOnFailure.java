package org.alfresco.listeners;

import org.alfresco.os.win.app.WindowsExplorer;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CloseWindowExplorersOnFailure extends TestListenerAdapter
{

    @Override
    public void onTestFailure(ITestResult tr)
    {
        new WindowsExplorer().closeAllWindowForms();
        super.onTestFailure(tr);
    }
}

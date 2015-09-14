package org.alfresco.listeners;

import org.alfresco.os.win.app.misc.Dialog;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CloseDialogsOnFailure extends TestListenerAdapter
{

    @Override
    public void onTestFailure(ITestResult tr)
    {
        new Dialog().closeAllDialogs();
        super.onTestFailure(tr);
    }
}

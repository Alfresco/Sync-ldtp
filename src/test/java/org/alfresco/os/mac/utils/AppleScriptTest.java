package org.alfresco.os.mac.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test AppleScript class.
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class AppleScriptTest
{

    private AppleScript app = new AppleScript();

    @Test(groups = { "MacOnly" })
    public void testAddCommandScript()
    {
        app.clean();
        app.addCommandScript("test");
        Assert.assertEquals(app.getCommandLines().size(), 2);
    }

    @Test(groups = { "MacOnly" })
    public void testRun()
    {
        // this should open Calculator file
        app.clean();
        app.addCommandScript("tell app \"Finder\"");
        app.addCommandScript("open (POSIX file \"/Applications/Calculator.app\")");
        app.addCommandScript("end tell");
        app.run();
        AlertDialog alert = new AlertDialog();
        boolean isCalculatorOpen = alert.isWindowOpened("Calculator");
        Assert.assertTrue(isCalculatorOpen, "Calculator was successfuly opened via AppleScript");
        alert.getLdtp().closeWindow("Calculator");
    }

    @Test(groups = { "MacOnly" })
    public void testClean()
    {
        app.clean();
        Assert.assertEquals(app.getCommandLines().size(), 0, "AppleScript clean method");
    }

}

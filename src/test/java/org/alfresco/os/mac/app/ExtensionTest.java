package org.alfresco.os.mac.app;

import org.testng.annotations.Test;

public class ExtensionTest
{
    MacExtensions ext = new MacExtensions();
    
    @Test
    public void testExtension()
    {
        ext.openExtension();
        ext.closeExtension();
    }
}

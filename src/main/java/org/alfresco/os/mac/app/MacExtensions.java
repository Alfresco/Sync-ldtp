package org.alfresco.os.mac.app;

import org.alfresco.os.mac.utils.KeyboardShortcut;

import com.cobra.ldtp.Ldtp;

public class MacExtensions extends KeyboardShortcut
{
    /**
     * load extension
     * @throws Exception 
     * 
     */
    
    public MacExtensions() 
    {
        Ldtp ldtp = new Ldtp("*");
        setLdtp(ldtp);
        cmdSpotlight();
        try
        {
        setLdtp(waitForWindow("Spolight*"));
        }
        catch (Exception e)
        {
            
        }
    }

    public void openExtension()
    {
        getLdtp().generateKeyEvent("extensions");
        getLdtp().generateKeyEvent("<enter>");
        getLdtp().setWindowName("Extensions");
    }

    public void closeExtension()
    {
        getLdtp().click("btnclosebutton");
    }
}

package org.alfresco.os.win.app.misc.dialog;

import org.alfresco.os.win.app.misc.Dialog;

import com.cobra.ldtp.Ldtp;

/**
 * The Delete File windows based dialog
 * 
 * @author Paul Brodner
 */
public class DeleteFile extends Dialog
{

    public DeleteFile()
    {
        setLdtp(new Ldtp("Delete File"));
    }

    /**
     * Checking "Do this for all current items" checkbox if exists
     */
    public void checkAllCurrentItems()
    {
        if (getLdtp().objectExist("Do this for all current items") == 1)
            getLdtp().check("Do this for all current items");
    }

    /**
     * Clicking Yes on Delete File dialog
     */
    public void clickYes()
    {
        getLdtp().click("Yes");
    }

    /**
     * Clicking Cancel on Delete File dialog
     */
    public void clickCancel()
    {
        getLdtp().click("Cancel");
    }
}

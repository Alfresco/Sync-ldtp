package org.alfresco.os.common;

import org.alfresco.utilities.LoggerUtils;
import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Generic GUI object that is using LDTP
 * 
 */
public abstract class GuiObject
{
    protected static Logger logger = LoggerUtils.getLogger();
    private Ldtp ldtp;

    public Ldtp getLdtp()
    {
        return ldtp;
    }

    public void setLdtp(Ldtp ldtp)
    {
        this.ldtp = ldtp;
    }

    /**
     * @return true if this Dialog exists
     */
    public boolean isDisplayed()
    {
        return getLdtp().guiExist() == 1;
    }

    /**
     * Return the String label value of a label
     * 
     * @param label
     * @return
     */
    protected String getLabelStringValue(String label)
    {
        return getLdtp().getObjectProperty(label, "label");
    }

    public void pasteString(String value)
    {
        StringSelection stringSelection = new StringSelection(value);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        getLdtp().generateKeyEvent("<ctrl>v");
    }
}

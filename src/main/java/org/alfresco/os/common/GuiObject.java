package org.alfresco.os.common;

import com.cobra.ldtp.Ldtp;
import org.alfresco.os.win.Application;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Generic GUI object that is using LDTP
 *
 */
public abstract class GuiObject
{
    public static Logger logger = LoggerFactory.getLogger(GuiObject.class);
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
        if(SystemUtils.IS_OS_MAC)
        {
            getLdtp().generateKeyEvent("<command>v");
        }
        else
        {
            getLdtp().generateKeyEvent("<ctrl>v");
        }
    }

    public void clearTextValue(String textField)
    {
        getLdtp().deleteText(textField, 0);
    }
}

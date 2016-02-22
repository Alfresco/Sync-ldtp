package org.alfresco.os.common;

import com.cobra.ldtp.Ldtp;

/**
 * 
 * Generic GUI object that is using LDTP
 * @author pbrodner
 *
 */
public abstract class GuiObject
{
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
}

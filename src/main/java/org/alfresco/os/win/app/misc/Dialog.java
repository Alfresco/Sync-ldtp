package org.alfresco.os.win.app.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.alfresco.utilities.LdtpUtils;
import org.apache.log4j.Logger;

import com.cobra.ldtp.Ldtp;

/**
 * Map a simple Dialog window
 * 
 * @author Paul Brodner
 */
public class Dialog
{
    private Ldtp ldtp;

    protected static Logger logger = Logger.getLogger(Dialog.class);

    public Dialog()
    {
        setLdtp(new Ldtp(" "));
    }

    public void closeAllDialogs()
    {
        logger.info("Try to close All Dialogs opened");
        ArrayList<String> arrDialogs = getOpenedDialogs();
        Collections.reverse(arrDialogs);

        for (Iterator<String> iterator = arrDialogs.iterator(); iterator.hasNext();)
        {
            String dialog = (String) iterator.next();
            logger.info("Found  Dialog [" + dialog + "] and try to close it sending ESC key!");
            getLdtp().generateKeyEvent("<esc>");
        }
    }

    /**
     * @return ArrayList of all dlg* opened
     */
    private ArrayList<String> getOpenedDialogs()
    {
        String[] windowsList = getLdtp().getWindowList();
        ArrayList<String> arrDialogs = new ArrayList<String>();
        for (String window : windowsList)
        {
            if (window.startsWith("dlg"))
            {
                Ldtp info = new Ldtp(window);
                if (!LdtpUtils.isApplicationObject(info))
                {
                    arrDialogs.add(window);
                }
            }
        }
        return arrDialogs;
    }

    public Ldtp getLdtp()
    {
        return ldtp;
    }

    public void setLdtp(Ldtp ldtp)
    {
        this.ldtp = ldtp;
    }
    
    public void closeDialog(){
        getLdtp().click("Close");
    }
}

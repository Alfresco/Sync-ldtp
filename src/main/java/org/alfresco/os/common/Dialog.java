package org.alfresco.os.common;

import com.cobra.ldtp.Ldtp;
import org.alfresco.utilities.LdtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Map a simple Dialog window
 * 
 */
public class Dialog extends GuiObject
{   
    private String dialogName;
    private String cancelButton = "btnCancel";
    private String closeMacButton = "btnclosebutton";
    private String closeButton = "Close";

    public static Logger logger = LoggerFactory.getLogger(Dialog.class);

    public Dialog(String dialogName)
    {
        setDialogName(dialogName);
        setLdtp(new Ldtp(dialogName));
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
            getLdtp().activateWindow(dialog);
            getLdtp().generateKeyEvent("<esc>");
            logger.info("Pressing 'ESC' key on [" + dialog + "]");
        }
    }

    /**
     * @return ArrayList of all dlg* opened
     */
    protected ArrayList<String> getOpenedDialogs()
    {
        String[] windowsList = getLdtp().getWindowList();
        ArrayList<String> arrDialogs = new ArrayList<String>();
        for (String window : windowsList)
        {
            if (window.startsWith("dlg") || window.startsWith("frm"))
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

    public void closeDialog()
    {
        closeDialog(closeButton);
    }

    public void closeMacDialog()
    {
        closeDialog(closeMacButton);
    }

    public void closeDialog(String btnName)
    {
        getLdtp().waitTillGuiExist(btnName);
        getLdtp().click(btnName);
    }

    public void clickCancel()
    {
        getLdtp().click(cancelButton);
        logger.info("Clicking 'Cancel' on  '" + getDialogName() + "' dialog");
    }

    public void waitForDialogToAppear()
    {
        getLdtp().waitTillGuiExist();
    }

    public void waitForDialogToDisappear()
    {
        getLdtp().waitTillGuiNotExist();
    }

    public String getDialogName()
    {
        return dialogName;
    }

    public void setDialogName(String dialogName)
    {
        this.dialogName = dialogName;
    }

    public void focus()
    {
        logger.info("Grab focus to: " + getDialogName());
        getLdtp().setWindowName(getDialogName());
        getLdtp().waitTillGuiExist();
        getLdtp().activateWindow(getDialogName());
    }

    public void arrowsNavigate(String arrow, int times)
    {
        int i = 0;
        while(i < times)
        {
            getLdtp().generateKeyEvent(arrow);
            i++;
        }
    }

    public String macGetValueControlFromTable(String tableObject, String labelObject)
    {
        String propertyLabel = "lbl" + labelObject;
        getLdtp().waitTillGuiExist(propertyLabel);
        String[] table = getLdtp().getObjectProperty(tableObject, "children").split(" ");
        List<String> propertyRows = Arrays.stream(table).filter(
                row -> row.startsWith("tblctablerow")).collect(Collectors.toList());
        for(int i = 0; i < propertyRows.size(); i++)
        {
            String[] firstCell = getLdtp().getObjectProperty(propertyRows.get(i), "children").split(" ");
            if(getLdtp().getObjectProperty(firstCell[0], "children")
                    .equals(propertyLabel.replace(" ", "")))
            {
                return getLdtp().getObjectProperty(firstCell[1], "children").split( " ")[0];
            }
        }
        return "";
    }
}

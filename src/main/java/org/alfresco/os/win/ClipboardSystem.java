package org.alfresco.os.win;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Handle Clipboard actions
 * 
 * @author Paul Brodner
 */
public class ClipboardSystem
{

    public static void setString(String value)
    {
        Clipboard c = defaultClipboard();
        c.setContents(new StringSelection(value), null);
    }

    public static String getString() throws UnsupportedFlavorException, IOException
    {
        Clipboard c = defaultClipboard();
        return (String) c.getData(DataFlavor.stringFlavor);
    }

    private static Clipboard defaultClipboard()
    {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }
}

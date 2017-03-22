package org.alfresco.os.mac.utils;

import org.alfresco.os.mac.Application;

/**
 * Implements the Keyboard shortcuts
 * 
 * @author <a href="mailto:paulbrodner@gmail.com">Paul Brodner</a>
 */
public class KeyboardShortcut extends Application
{

    @Override
    public void exitApplication()
    {
        //nothing to do here
    }

    /**
     * Go up in enclosing folder based on shortcut keys
     */
    public void goToEnclosingFolder()
    {
        getLdtp().generateKeyEvent("<command><up>");
    }

    /**
     * Go back in enclosing folder based on shortcut keys
     */
    public void goBack()
    {
        getLdtp().generateKeyEvent("<command>[");
    }

    /**
     * Go forward in enclosing folder based on shortcut keys
     */
    public void goForward()
    {
        getLdtp().generateKeyEvent("<command>]");
    }

    /**
     * Open a folder, file or any other application using the shortcut keys.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void goActivate()
    {
        getLdtp().generateKeyEvent("<command><down>");
    }

    /**
     * Copy command.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void cmdCopy()
    {
        getLdtp().generateKeyEvent("<command>c");
    }

    /**
     * Paste command.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void cmdPaste()
    {
        getLdtp().generateKeyEvent("<command>v");
    }

    /**
     * Move command.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void cmdMove()
    {
        getLdtp().generateKeyEvent("<command><alt>v");
    }

    /**
     * Delete command.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void cmdDelete()
    {
        getLdtp().generateKeyEvent("<command><bksp>");
    }

    /**
     * Undo command.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void cmdUndo()
    {
        getLdtp().generateKeyEvent("<command>z");
    }

    /**
     * Select all command.
     * <a href='http://ldtp.freedesktop.org/user-doc/dd/da2/a00192.html'>LDTP KeyPress</a>
     */
    public void cmdAll()
    {
        getLdtp().generateKeyEvent("<command>a"); // we want also to rewrite the extension
    }
   /**
    * open spot search    
    */
    public void cmdSpotlight()
    {
        getLdtp().generateKeyEvent("<command><space>");
    }
}

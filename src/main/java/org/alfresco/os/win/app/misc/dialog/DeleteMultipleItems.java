/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */

package org.alfresco.os.win.app.misc.dialog;

import org.alfresco.os.common.Dialog;

/**
 * Delete Multiple Items dialog
 * 
 * @author Paul Brodner
 */
public class DeleteMultipleItems extends Dialog
{
    
    public DeleteMultipleItems()
    {       
        super("Delete Multiple Items");
    }
    
    public void clickYes(){       
        getLdtp().click("Yes");
        logger.info("Clicking 'Yes' on '" + getDialogName() + "' Items dialog");
    }
    
    public void clickNo(){        
        getLdtp().click("No");
        logger.info("Clicking 'No' on '" + getDialogName() + "' Items dialog");
    }

   
}

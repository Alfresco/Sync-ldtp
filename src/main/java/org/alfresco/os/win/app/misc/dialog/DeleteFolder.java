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

import org.alfresco.os.common.gui.Button;
import org.alfresco.os.win.app.misc.Dialog;

import com.cobra.ldtp.Ldtp;

/**
 * 
 * The Delete Folder dialog
 * @author Paul Brodner
 *
 */
public class DeleteFolder extends Dialog
{
    public DeleteFolder(){
        setLdtp(new Ldtp("Delete Folder"));
    }

    public Button yes(){
       return new Button(getLdtp(), "Yes");
    }
    
    public Button no(){
        return new Button(getLdtp(), "No");
     }
}

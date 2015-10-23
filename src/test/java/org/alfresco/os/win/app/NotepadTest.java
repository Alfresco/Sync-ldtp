/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
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
package org.alfresco.os.win.app;

import java.io.File;

import org.alfresco.os.AbstractTestClass;
import org.alfresco.utilities.LdtpUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NotepadTest  extends AbstractTestClass {
	Notepad notepad = new Notepad();
	File testOne;

	@AfterClass
	public void tearDown() {
		super.tearDown();
		notepad.exitApplication();
	}

	@BeforeMethod
	private void setupFile() {
		testOne = LdtpUtils.getRandomFileName("txt");
		randomFiles.add(testOne);
	}
	
	@AfterMethod
	public void cleanuo(){
		notepad.exitApplication();
	}

	@Test
	public void testEdit() throws Exception {
		notepad.openApplication();
		notepad.edit("1st line");
		notepad.close();
	}

	@Test()
	public void testSaveAsWithOverwrite() throws Exception {
		testOne.createNewFile();
		notepad.openApplication();
		notepad.saveAsWithOverwrite(testOne);
		notepad.close(testOne);

		Assert.assertTrue(testOne.exists(), testOne.getPath() + " created");
	}

	@Test
	public void testSave() throws Exception {
		notepad.openApplication();
		notepad.saveAs(testOne);
		notepad.focus(testOne);
		notepad.appendData("data!@#$%^&*()WERTYUIO");
		notepad.save();
		notepad.close();
	}

	@Test
	public void testSaveAsWithShortcutKeys() throws Exception {
		notepad.openApplication();
		notepad.saveAsWithShortcutKeys(testOne);
		notepad.close(testOne);
		
		Assert.assertTrue(testOne.exists(),"File exists on disk");
	}

	@Test
	public void testCreateFile() throws Exception {		
		notepad.createFile(testOne);
		Assert.assertTrue(testOne.exists(),"FIle exist");
	}

	@Test
	public void testGetNotepadText() {
		String data = "data";
		notepad.openApplication();
		notepad.appendData(data);
		
		Assert.assertEquals(notepad.getNotepadText(), data);
		notepad.exitApplication();
	}
}

package org.alfresco.os;

import java.io.File;
import java.util.ArrayList;

import org.alfresco.os.mac.app.office.v2011.Excel2011;
import org.alfresco.utilities.LdtpUtils;
import org.testng.annotations.AfterClass;

/**
 * Abstract Test Util class, used on MAC unit tests
 * 
 * @author Paul Brodner
 */
public class AbstractTestClass
{
    protected File tmpFile;
    protected Excel2011 excel;
    protected ArrayList<File> randomFiles = new ArrayList<File>();

    @AfterClass
    public void tearDown()
    {
        deleteAllRandomGeneratedFiles();
    }

    /**
     * Return a copy of <filename> using a random name,save in documents folder of current user
     * 
     * @param filename
     * @return
     */
    protected File getRandomTestFile(String filename)
    {
        File randomFile = LdtpUtils.getNewRandomFileFromResource(filename);
        randomFiles.add(randomFile);
        return randomFile;
    }

    /**
     * This will delete all generated files based on <getRandomTestFile> method
     */
    protected void deleteAllRandomGeneratedFiles()
    {
        for (int i = 0; i < randomFiles.size(); i++)
        {
            randomFiles.get(i).delete();
        }
    }
}
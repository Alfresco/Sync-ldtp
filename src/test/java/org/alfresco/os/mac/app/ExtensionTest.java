package org.alfresco.os.mac.app;

import org.testng.annotations.Test;

import com.cobra.ldtp.Ldtp;

public class ExtensionTest
{
//{
//  //  MacExtensions ext = new MacExtensions();
//    
//   // @Test
//    public void testExtension()
//    {
//        ext.openExtension();
//        ext.closeExtension();
//    }

    @Test
  public void appleMenu() throws Exception 
    {
        Ldtp ldtp = new Ldtp("*");
//        String[] object2= ldtp.getWindowList();
//        for(String each: object2)
//         {
//            System.out.println("list " + each);
//           }
        
       ldtp.setWindowName("appAlfrescoDesktopSync");
      ldtp.click("mnu0");
    Thread.sleep(3000);
     ldtp.generateKeyEvent("<command>d");
       ldtp.setWindowName("frmAlfrescoDesktopSync");
       ldtp.activateWindow("frmAlfrescoDesktopSync");
       String[] object2= ldtp.getObjectList();
       for(String each: object2)
      {
       System.out.println("list " + each);
      }
       ldtp.click("btnCONFLICTS");
    ldtp.mouseRightClick("initialShareFile-dI5iG.txt");
    ldtp.click("btnRESOLVE");
    
  String[] object1= ldtp.getObjectList();
  for(String each: object1)
      {
          System.out.println("list " + each);
      }
//     ldtp.click("btnRESOLVE");
    }
   
       //ldtp.grabFocus("frmWindow");
      //    ldtp.click("btnbutton1");
        //  Thread.sleep(3000);
        //  String[] object1= ldtp.getAllItem("*menu*");
        // for(String each: object1)
         // {
          //   System.out.println("list " + each);
            //}
   

    @Test
    public void aboutDialog() throws Exception 
    {
        Ldtp ldtp = new Ldtp("*");
        ldtp.setWindowName("appAlfrescoDesktopSync");
       ldtp.click("mnu0");
     Thread.sleep(3000);
      ldtp.generateKeyEvent("<command>d");
        ldtp.setWindowName("frmWindow");
        ldtp.activateWindow("frmWindow");
        //ldtp.grabFocus("frmWindow");
           ldtp.click("btnbutton1");
           ldtp.generateKeyEvent("<down>");
           ldtp.generateKeyEvent("<down>");
           ldtp.generateKeyEvent("<down>");
           ldtp.generateKeyEvent("<down>");
           ldtp.generateKeyEvent("<down>");
           ldtp.generateKeyEvent("<enter>");
           Thread.sleep(3000);
           ldtp.setWindowName("frmAbout");
      ldtp.activateWindow("frmAbout");
        String[] object1= ldtp.getObjectList();
        for(String each: object1)
         {
            System.out.println("list " + each);
           }
    }
    
    @Test
    public void test() throws Exception 
    {
        Ldtp ldtp = new Ldtp("*");
        ldtp.setWindowName("frmAlfrescoDesktopSync");
     //   ldtp.activateWindow("frmAlfrescoDesktopSync");
        String[] object1= ldtp.getObjectList();
        for(String each: object1)
         {
            System.out.println("list " + each);
           }
       // ldtp.activateWindow("frmConflict");
       //ldtp.generateKeyEvent("<tab>")   ;
        
    }

    @Test
    public void windowList() throws Exception
    {
        Ldtp ldtp = new Ldtp("*");
        String[] object1= ldtp.getWindowList();
        for(String each: object1)
         {
            System.out.println("list " + each);
           }
        ldtp.setWindowName("frm0");
        String[] object2= ldtp.getObjectList();
        for(String each: object2)
         {
            System.out.println("list " + each);
           }
    }
}

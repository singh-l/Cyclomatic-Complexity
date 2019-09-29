package com.accenture.cc.servic;

public class TestClass
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    
    
    String codeLine = "   END LOOP;";
    if (codeLine.matches(".*LOOP") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]LOOP"))
    
       {

         
         System.out.println(codeLine+"1");

       }
      
    //new CSFileParserClass().parseAndGenerateClassBo("E:\\C#\\C#\\common\\LayoutAwarePage.cs");

    /*
     * if(codeLine.matches(" else[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]")) {
     * System.out.println("Match found");
     * }
     *             this._pageKey = "Page-" + this.Frame.BackStackDepth;

            if (e.NavigationMode == NavigationMode.New)

     */
  }
}

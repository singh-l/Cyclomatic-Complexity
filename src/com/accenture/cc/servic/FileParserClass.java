package com.accenture.cc.servic;

import java.io.*;
import com.accenture.cc.domain.ClassBO;

public class FileParserClass
{

  boolean doWhileFlag = false;
  int     index       = 0, multicommentCount = 0, ifCount = 0, ifElseCount = 0, doWhileCount = 0, whileCount = 0,
          forCount = 0, switchCount = 0, finallyBlockcount = 0, catchCount = 0, conditionalANDCount = 0,
          conditionalORCount = 0, unvCount = 0, bitWiseAndCount = 0, bitWiseOrCount = 0, bitWiseXorCount = 0;

  public ClassBO parseAndGenerateClassBo(String path)
  {
    ClassBO classBo = new ClassBO();

    BufferedReader br = null;

    String fileName = "";
    boolean multilineComment = false;

    // int index = 0, multicommentCount = 0;

    try
    {

      String sCurrentLine;

      File fn = new File(path);
      fileName = fn.getName();

      fileName = fileName.substring(0, fileName.indexOf("."));

      br = new BufferedReader(new FileReader(path));

      int multIndex = 0;

      while ((sCurrentLine = br.readLine()) != null)
      {

        if (!multilineComment)
        {
          if (sCurrentLine.contains("/*"))
          {
            multilineComment = true;

            multIndex = sCurrentLine.indexOf("/*");

            if (multIndex >= 0)
            {
              sCurrentLine = sCurrentLine.substring(0, multIndex);
            }

            collectTokens(sCurrentLine);
          }
        }

        if (multilineComment)
        {
          if (sCurrentLine.contains("*/"))
          {
            multilineComment = false;

            multIndex = sCurrentLine.indexOf("*/");

            if (multIndex >= 0)
            {
              sCurrentLine = sCurrentLine.substring(multIndex + 2, sCurrentLine.length());
            }

            collectTokens(sCurrentLine);
          }
        }

        if (!multilineComment && !sCurrentLine.contains("*/")) collectTokens(sCurrentLine);

      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (br != null) br.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }

    }

    if (unvCount > 0)
    {
      classBo.setClassName(fileName);
      populateBo(classBo);
      return classBo;
    }
    return null;
  }

  public void collectTokens(String codeLine)
  {

    StringBuffer buf = new StringBuffer("");
    boolean stringFlag = false;
    index = codeLine.indexOf("//");

    if (index >= 0)
    {
      codeLine = codeLine.substring(0, index);
    }


    // Check for String

    index = codeLine.indexOf("\"");

    if (index > 0)
    {
      for (int i = 0; i < codeLine.length(); i++)
      {

        if (codeLine.charAt(i) == '\"')
        {
          stringFlag = !stringFlag;
          if (i > 0)
          {
            if (codeLine.charAt(i - 1) == '\\')
            {
              stringFlag = !stringFlag;
            }
          }
        }
        if (!stringFlag && codeLine.charAt(i) != '"')
        {
          buf.append(codeLine.charAt(i));
        }
      }

      codeLine = buf.toString();

    }
    if (codeLine != null && !codeLine.equals(""))
    {
     // System.out.println(codeLine);
      if (codeLine.matches(".*if\\s+\\(.*") || codeLine.matches(".*if\\(.*"))
      {

        ifCount++;
        unvCount++;
        // System.out.println(codeLine);

      }

      if ((codeLine.matches(".*else\\s+\\{.*") || codeLine.matches(".*else\\{.*") || codeLine.matches(".*else.*") || codeLine
              .matches(".*else\\s+.*")) &&
          (!codeLine.matches(".*else if\\s+\\(.*") && !codeLine.matches(".*else if\\(.*")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]else[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ifElseCount++;
        unvCount++;
        ifCount--;
        // System.out.println(codeLine);

      }
      else if (codeLine.equals("else"))
      {
        ifElseCount++;
        unvCount++;
        ifCount--;
      }
      else if (codeLine.matches("else\\s+"))
      {
        ifElseCount++;
        unvCount++;
        ifCount--;
      }
      else if (codeLine.matches("\\s+else"))
      {
        ifElseCount++;
        unvCount++;
        ifCount--;
      }

      // do while count

      if ((codeLine.matches(".*do\\s+\\{.*") || codeLine.matches(".*do\\{.*") || codeLine.matches(".*do.*")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]do[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        doWhileCount++;
        unvCount++;
        doWhileFlag = true;
      }
      else if (codeLine.equals("do"))
      {
        doWhileCount++;
        unvCount++;
        doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+do"))
      {

        doWhileCount++;
        unvCount++;
        doWhileFlag = true;
      }
      else if (codeLine.matches("do\\s+"))
      {

        doWhileCount++;
        unvCount++;
        doWhileFlag = true;
      }

      if ((codeLine.matches(".*while\\s+\\(.*") || codeLine.matches(".*while\\(.*")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]while[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        if (doWhileFlag)
        {
          doWhileFlag = false;
        }
        else
        {
          whileCount++;
          unvCount++;
        }

      }

      if ((codeLine.matches(".*for\\s+\\(.*") || codeLine.matches(".*for\\(.*")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]for[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        forCount++;
        unvCount++;

      }

      if ((codeLine.matches(".*switch\\s+\\(.*") || codeLine.matches(".*switch\\(.*")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]switch[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        switchCount++;
        unvCount++;

      }

      if ((codeLine.matches(".*finally\\s+\\{.*") || codeLine.matches(".*finally\\{.*")) ||
          codeLine.matches(".*finally.*") &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]switch[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        finallyBlockcount++;
        unvCount++;

      }
      else if (codeLine.equals("finally"))
      {

        finallyBlockcount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+finally"))
      {

      }
      else if (codeLine.matches("finally\\s+"))
      {
        finallyBlockcount++;
        unvCount++;
      }

      if ((codeLine.matches(".*catch\\s+\\(.*") || codeLine.matches(".*catch\\(.*")) || codeLine.matches(".*catch.*") &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]catch[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        catchCount++;
        unvCount++;

      }

      if (codeLine.matches(".*&.*") && !codeLine.matches(".*&&.*"))
      {

        bitWiseAndCount++;
        unvCount++;

      }

      if (codeLine.matches(".*\\|.*") && !codeLine.matches(".*\\|\\|.*"))
      {

        bitWiseOrCount++;
        unvCount++;

      }

      if (codeLine.matches(".*\\^.*"))
      {

        bitWiseXorCount++;
        unvCount++;

      }



      if (codeLine.matches(".*&&.*"))
      {

        conditionalANDCount++;
        unvCount++;

      }

      if (codeLine.matches(".*\\|\\|.*"))
      {

        conditionalORCount++;
        unvCount++;

      }



    }
  }

  public ClassBO populateBo(ClassBO cls)
  {

    cls.setIfCount(ifCount);
    cls.setIfElseCount(ifElseCount);
    cls.setDoWhileCount(doWhileCount);
    cls.setWhileCount(whileCount);
    cls.setForCount(forCount);
    cls.setSwitchCount(switchCount);
    cls.setFinallyCount(finallyBlockcount);
    cls.setCatchCount(catchCount);
    cls.setConditionalAndCount(conditionalANDCount);
    cls.setConditionalOrCount(conditionalORCount);
    cls.setBitWiseAndCount(bitWiseAndCount);
    cls.setBitWiseOrCount(bitWiseOrCount);
    cls.setBitwiseXor(bitWiseXorCount);
    return cls;

  }
}

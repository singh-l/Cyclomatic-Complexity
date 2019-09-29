package com.accenture.cc.servic;

import java.io.*;
import com.accenture.cc.domain.ClassBO;

public class VbFileParserClass
{

  boolean doWhileFlag = false;
  int     index       = 0, multicommentCount = 0, ifCount = 0, ifElseCount = 0, doWhileCount = 0, whileCount = 0,
          forCount = 0, switchCount = 0, finallyBlockcount = 0, catchCount = 0, conditionalANDCount = 0,
          conditionalORCount = 0, unvCount = 0, bitWiseAndCount = 0, bitWiseOrCount = 0, bitWiseXorCount = 0,
          loopUntilCount = 0, loopWhilecount = 0, doUntilCount = 0, forEachCount = 0;

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
    index = codeLine.indexOf("'");

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
            if (codeLine.charAt(i - 1) == '\"')
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
      if (((codeLine.matches(".*If.*") && !codeLine.matches(".*End If.*") && !codeLine.matches(".*End If") &&
            !codeLine.matches("End If.*") && !codeLine.matches("End If") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]If[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) || codeLine.matches("If")))
      {

        ifCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("If.*") && codeLine.matches("If[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*If") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]If") &&
               !codeLine.matches(".*End If") && !codeLine.matches("End If"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*ElseIf.*") || codeLine.matches("ElseIf") &&
               codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]ElseIf[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        ifCount++;
        unvCount++;
        // System.out.println(codeLine);
      }
      else if (codeLine.matches(".*ElseIf") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]ElseIf"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches("ElseIf.*") && codeLine.matches("ElseIf[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        ifCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*Else.*")) &&
           (!codeLine.matches(".*Elseif.*") && !codeLine.matches(".*Elseif") && !codeLine.matches("Elseif.*") && !codeLine
                   .matches("Elseif")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Else[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Else"))
      {

        ifElseCount++;
        unvCount++;
        ifCount--;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("Else.*") && !codeLine.matches("Elseif.*") && !codeLine.matches("Elseif") &&
               codeLine.matches("ElseIf[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        ifElseCount++;
        unvCount++;
        ifCount--;
      }
      else if (codeLine.matches(".*Else") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]ElseIf"))
      {

        ifElseCount++;
        unvCount++;
        ifCount--;
      }

      else if (codeLine.matches("\\s+Else") || codeLine.matches("Else\\s+") || codeLine.matches("\\s+Else\\s+"))
      {

        ifElseCount++;
        unvCount++;
        ifCount--;
        // doWhileFlag = true;
      }


      // do while count

      if (((codeLine.matches(".*Do While.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Do While[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Do While"))
      {

        doWhileCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches(".*Do While") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Do While"))
      {

        doWhileCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("Do While.*") && codeLine.matches("Do While[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        doWhileCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+Do While") || codeLine.matches("Do While\\s+") ||
               codeLine.matches("\\s+Do While\\s+"))
      {

        doWhileCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*Do Until.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Do Until[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Do Until"))
      {

        doUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches(".*Do Until") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Do Until"))
      {

        doUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("Do Until.*") && codeLine.matches("Do Until[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        doUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+Do Until") || codeLine.matches("Do Until\\s+") ||
               codeLine.matches("\\s+Do Until\\s+"))
      {

        doUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*Loop While.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Loop While[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Loop While"))
      {

        loopWhilecount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches(".*Loop While") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Loop While"))
      {

        loopWhilecount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("Loop While.*") && codeLine.matches("Loop While[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        loopWhilecount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+Loop While") || codeLine.matches("Loop While\\s+") ||
               codeLine.matches("\\s+Loop While\\s+"))
      {

        loopWhilecount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*Loop Until.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Loop Until[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Loop Until"))
      {

        loopUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches(".*Loop Until") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Loop Until"))
      {

        loopUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("Loop Until.*") && codeLine.matches("Loop Until[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        loopUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+Loop Until") || codeLine.matches("Loop Until\\s+") ||
               codeLine.matches("\\s+Loop Until\\s+"))
      {

        loopUntilCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*End While.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]End While[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("End While"))
      {

        whileCount++;
        unvCount++;

      }
      else if (codeLine.matches(".*End While") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]End While"))
      {

        whileCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("End While.*") && codeLine.matches("End While[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        whileCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+End While") || codeLine.matches("End While\\s+") ||
               codeLine.matches("\\s+End While\\s+"))
      {

        whileCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*For.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]For[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) || codeLine.matches("For"))
      {

        forCount++;
        unvCount++;

      }
      else if (codeLine.matches(".*For") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]For"))
      {

        forCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("For.*") && codeLine.matches("For[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        forCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+For") || codeLine.matches("For\\s+") || codeLine.matches("\\s+For\\s+"))
      {

        forCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*For Each.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]For Each[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("For Each"))
      {

        forEachCount++;
        unvCount++;

      }
      else if (codeLine.matches(".*For Each") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]For Each"))
      {

        forEachCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("For Each.*") && codeLine.matches("For Each[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        forEachCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+For Each") || codeLine.matches("For Each\\s+") ||
               codeLine.matches("\\s+For Each\\s+"))
      {

        forEachCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*Select Case.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Select Case[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Select Case"))
      {

        switchCount++;
        unvCount++;

      }
      else if (codeLine.matches(".*Select Case") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Select Case"))
      {

        switchCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("Select Case.*") && codeLine.matches("Select Case[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {

        switchCount++;
        unvCount++;
        // doWhileFlag = true;
      }
      else if (codeLine.matches("\\s+Select Case") || codeLine.matches("Select Case\\s+") ||
               codeLine.matches("\\s+Select Case\\s+"))
      {

        switchCount++;
        unvCount++;
        // doWhileFlag = true;
      }

      if (((codeLine.matches(".*Finally.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Finally[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Finally"))
      {

        finallyBlockcount++;
        unvCount++;

      }
      else if (codeLine.matches(".*Finally") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Finally"))
      {

        finallyBlockcount++;
        unvCount++;
      }
      else if (codeLine.matches("Finally.*") && codeLine.matches("Finally[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        finallyBlockcount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+Finally") || codeLine.matches("Finally\\s+") ||
               codeLine.matches("\\s+Finally\\s+"))
      {

        finallyBlockcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*Catch.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Catch[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("Catch"))
      {

        catchCount++;
        unvCount++;

      }
      else if (codeLine.matches(".*Catch") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Catch"))
      {
        catchCount++;
        unvCount++;
      }
      else if (codeLine.matches("Catch.*") && codeLine.matches("Catch[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        catchCount++;
        unvCount++;
      }

      else if (codeLine.matches("\\s+Catch") || codeLine.matches("Catch\\s+") || codeLine.matches("\\s+Catch\\s+"))
      {
        catchCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*And.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]And[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) || codeLine.matches("And"))
      {

        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*And") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]And"))
      {
        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches("And.*") && codeLine.matches("And[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+And") || codeLine.matches("And\\s+") || codeLine.matches("\\s+And\\s+"))
      {
        conditionalANDCount++;
        unvCount++;
      }


      if (((codeLine.matches("Or")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Or[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches(".*Or.*"))
      {

        conditionalORCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*Or") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]Or"))
      {
        conditionalORCount++;
        unvCount++;
      }
      else if (codeLine.matches("Or.*") && codeLine.matches("Or[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        conditionalORCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+Or") || codeLine.matches("Or\\s+") || codeLine.matches("\\s+Or\\s+"))
      {
        conditionalORCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*AndAlso.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]AndAlso[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("AndAlso"))
      {

        bitWiseAndCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*AndAlso") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]AndAlso"))
      {
        bitWiseAndCount++;
        unvCount++;
      }
      else if (codeLine.matches("AndAlso.*") && codeLine.matches("AndAlso[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        bitWiseAndCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+AndAlso"))
      {
        bitWiseAndCount++;
        unvCount++;
      }
      else if (codeLine.matches("AndAlso\\s+"))
      {
        bitWiseAndCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*OrElse.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]OrElse[^a-zA-Z0-9&_$%@#!^+-/*�����].*")) ||
          codeLine.matches("OrElse"))
      {

        bitWiseOrCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*OrElse") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*�����]OrElse"))
      {
        bitWiseOrCount++;
        unvCount++;
      }
      else if (codeLine.matches("OrElse.*") && codeLine.matches("OrElse[^a-zA-Z0-9&_$%@#!^+-/*�����].*"))
      {
        bitWiseOrCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+OrElse"))
      {
        bitWiseOrCount++;
        unvCount++;
      }
      else if (codeLine.matches("OrElse\\s+"))
      {
        bitWiseOrCount++;
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
    cls.setDoUntilcount(doUntilCount);
    cls.setLoopUntilCount(loopUntilCount);
    cls.setLoopWhilecount(loopWhilecount);
    cls.setForCount(forCount);
    cls.setForEachcount(forEachCount);
    cls.setSwitchCount(switchCount);
    cls.setFinallyCount(finallyBlockcount);
    cls.setCatchCount(catchCount);
    cls.setConditionalAndCount(conditionalANDCount);
    cls.setConditionalOrCount(conditionalORCount);
    cls.setBitWiseAndCount(bitWiseAndCount);
    cls.setBitWiseOrCount(bitWiseOrCount);
    // cls.setBitwiseXor(bitWiseXorCount);
    return cls;

  }
}

package com.accenture.cc.servic;

import java.io.*;
import com.accenture.cc.domain.ClassBO;

public class PLSQLFileParserClass
{

  boolean doWhileFlag = false;
  int     index       = 0, multicommentCount = 0, ifCount = 0, whileCount = 0, forCount = 0, switchCount = 0,
          catchCount = 0, conditionalANDCount = 0, unvCount = 0, decodeCount = 0, whenCount = 0, nonInnerCount = 0,
          nvl2Count = 0, nvlCount = 0, gotoCount = 0, loopCount = 0, setOperatorCount = 0, comparisonOperatorcount = 0,
          arithmaticOperatorcount = 0, analticalfunctioncount = 0, dynamicSqlCount = 0, ddlstatementcount = 0,
          autoTransactionCount = 0;


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
    index = codeLine.indexOf("--");

    if (index >= 0)
    {
      codeLine = codeLine.substring(0, index);
    }


    // Check for String

    index = codeLine.indexOf("'");

    if (index > 0)
    {
      for (int i = 0; i < codeLine.length(); i++)
      {

        if (codeLine.charAt(i) == '\'')
        {
          stringFlag = !stringFlag;
          if (i > 0)
          {
            if (codeLine.charAt(i - 1) == '\'')
            {
              stringFlag = !stringFlag;
            }
          }
        }
        if (!stringFlag && codeLine.charAt(i) != '\'')
        {
          buf.append(codeLine.charAt(i));
        }
      }

      codeLine = buf.toString();

    }
    if (codeLine != null && !codeLine.equals(""))
    {

      codeLine = codeLine.toUpperCase();
      // System.out.println(codeLine);
      if (((codeLine.matches(".*IF.*")) && !codeLine.matches(".*END IF.*") && !codeLine.matches("END IF") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]IF[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("IF"))
      {

        ifCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if ((codeLine.matches(".*ELSIF.*") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ELSIF[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
               codeLine.matches("ELSIF"))
      {
        ifCount++;
        unvCount++;

      }
      else if (codeLine.matches("IF.*") && codeLine.matches("IF[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*") &&
               !codeLine.matches("END IF.*"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*IF") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]IF") &&
               !codeLine.matches(".*END IF"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches("ELSIF.*") && codeLine.matches("ELSIF[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {
        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*ELSIF") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ELSIF"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+ELSIF") || codeLine.matches("ELSIF\\s+") || codeLine.matches("\\s+ELSIF\\s+"))
      {

        ifCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+IF") || codeLine.matches("IF\\s+") || codeLine.matches("\\s+IF\\s+"))
      {

        ifCount++;
        unvCount++;
      }

      if ((codeLine.matches(".*DECODE.*") || codeLine.matches("DECODE")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]DECODE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        decodeCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("DECODE.*") && codeLine.matches("DECODE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {
        decodeCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*DECODE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]DECODE"))
      {

        decodeCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+DECODE") || codeLine.matches("DECODE\\s+") || codeLine.matches("\\s+DECODE\\s+"))
      {

        decodeCount++;
        unvCount++;
      }

      if ((codeLine.matches(".*WHEN.*") || codeLine.matches("WHEN")) &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]WHEN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        whenCount++;
        unvCount++;
        // System.out.println(codeLine);
      }
      else if (codeLine.matches("WHEN.*") && codeLine.matches("WHEN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        whenCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*WHEN") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]WHEN"))
      {

        whenCount++;
        unvCount++;
      }

      else if (codeLine.matches("\\s+WHEN") || codeLine.matches("WHEN\\s+") || codeLine.matches("\\s+WHEN\\s+"))
      {

        whenCount++;
        unvCount++;
      }


      if ((codeLine.matches(".*JOIN.*") || codeLine.matches("JOIN")) && !codeLine.matches(".*INNER.*") &&
          codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]JOIN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        nonInnerCount++;
        unvCount++;

      }
      else if (codeLine.matches("JOIN.*") && codeLine.matches("JOIN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        nonInnerCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*JOIN") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]JOIN"))
      {

        nonInnerCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+JOIN") || codeLine.matches("JOIN\\s+") || codeLine.matches("\\s+JOIN\\s+"))
      {

        nonInnerCount++;
        unvCount++;
      }


      if ((codeLine.matches(".*NVL2.*") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]NVL2[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("NVL2"))
      {

        nvl2Count++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("NVL2.*") && codeLine.matches("NVL2[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        nvl2Count++;
        unvCount++;
      }
      else if (codeLine.matches(".*NVL2") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]NVL2"))
      {

        nvl2Count++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+NVL2") || codeLine.matches("NVL2\\s+") || codeLine.matches("\\s+NVL2\\s+"))
      {

        nvl2Count++;
        unvCount++;
      }

      if ((codeLine.matches(".*NVL.*") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]NVL[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("NVL"))
      {

        nvlCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("NVL.*") && codeLine.matches("NVL[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        nvlCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*NVL") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]NVL"))
      {

        nvlCount++;
        unvCount++;
      }

      else if (codeLine.matches("\\s+NVL") || codeLine.matches("NVL\\s+") || codeLine.matches("\\s+NVL\\s+"))
      {

        nvlCount++;
        unvCount++;
      }

      if ((codeLine.matches(".*WHILE.*") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]WHILE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("WHILE"))
      {

        whileCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("WHILE.*") && codeLine.matches("WHILE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        whileCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*WHILE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]WHILE"))
      {

        whileCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+WHILE") || codeLine.matches("WHILE\\s+") || codeLine.matches("\\s+WHILE\\s+"))
      {

        whileCount++;
        unvCount++;
      }

      if ((codeLine.matches(".*GOTO.*") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]GOTO[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("GOTO"))
      {

        gotoCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("GOTO.*") && codeLine.matches("GOTO[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        gotoCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*GOTO") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]GOTO"))
      {

        gotoCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+GOTO") || codeLine.matches("GOTO\\s+") || codeLine.matches("\\s+GOTO\\s+"))
      {

        gotoCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*LOOP.*")) && !codeLine.matches(".*END LOOP.*") && !codeLine.matches(".*END LOOP") &&
           !codeLine.matches("END LOOP.*") && !codeLine.matches("END LOOP") && !codeLine.matches(".*FOR.*") &&
           !codeLine.matches(".*FOR") && !codeLine.matches("FOR.*") && !codeLine.matches("FOR") &&
           !codeLine.matches(".*WHILE.*") && !codeLine.matches(".*WHILE") && !codeLine.matches("WHILE.*") &&
           !codeLine.matches("WHILE") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]LOOP[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("LOOP"))
      {

        loopCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("LOOP.*") && codeLine.matches("LOOP[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        loopCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*LOOP") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]LOOP"))
      {

        loopCount++;
        unvCount++;
      }
      else if (codeLine.matches("\\s+LOOP") || codeLine.matches("LOOP\\s+") || codeLine.matches("\\s+LOOP\\s+"))
      {

        loopCount++;
        unvCount++;
      }


      if ((codeLine.matches(".*FOR.*") && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]FOR[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("FOR"))
      {

        forCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("FOR.*") && codeLine.matches("FOR[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        forCount++;
        unvCount++;

      }

      else if (codeLine.matches(".*FOR") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]FOR"))
      {

        forCount++;
        unvCount++;
      }

      else if (codeLine.matches("\\s+FOR") || codeLine.matches("FOR\\s+") || codeLine.matches("\\s+FOR\\s+"))
      {

        forCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*UNION.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]UNION[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("UNION"))
      {

        setOperatorCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("UNION.*") && codeLine.matches("UNION[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        setOperatorCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*UNION") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]UNION"))
      {
        setOperatorCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+UNION") || codeLine.matches("UNION\\s+") || codeLine.matches("\\s+UNION\\s+"))
      {

        setOperatorCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*INTERSECT.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]INTERSECT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("INTERSECT"))
      {

        setOperatorCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("INTERSECT.*") && codeLine.matches("INTERSECT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        setOperatorCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*INTERSECT") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]INTERSECT"))
      {
        setOperatorCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+INTERSECT") || codeLine.matches("INTERSECT\\s+") ||
               codeLine.matches("\\s+INTERSECT\\s+"))
      {

        setOperatorCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*INTERSECT.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]INTERSECT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("INTERSECT"))
      {

        setOperatorCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("INTERSECT.*") && codeLine.matches("INTERSECT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        setOperatorCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*INTERSECT") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]INTERSECT"))
      {
        setOperatorCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+INTERSECT") || codeLine.matches("INTERSECT\\s+") ||
               codeLine.matches("\\s+INTERSECT\\s+"))
      {

        setOperatorCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*MINUS.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]MINUS[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("MINUS"))
      {

        setOperatorCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("MINUS.*") && codeLine.matches("MINUS[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        setOperatorCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*MINUS") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]MINUS"))
      {
        setOperatorCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+MINUS") || codeLine.matches("MINUS\\s+") || codeLine.matches("\\s+MINUS\\s+"))
      {

        setOperatorCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*LIKE.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]LIKE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("LIKE"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("LIKE.*") && codeLine.matches("LIKE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*LIKE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]LIKE"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+LIKE") || codeLine.matches("LIKE\\s+") || codeLine.matches("\\s+LIKE\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*BETWEEN.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]BETWEEN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("BETWEEN"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("BETWEEN.*") && codeLine.matches("BETWEEN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*BETWEEN") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]BETWEEN"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+BETWEEN") || codeLine.matches("BETWEEN\\s+") ||
               codeLine.matches("\\s+BETWEEN\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*ANY.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ANY[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("ANY"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("ANY.*") && codeLine.matches("ANY[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*ANY") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ANY"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+ANY") || codeLine.matches("ANY\\s+") || codeLine.matches("\\s+ANY\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*SOME.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]SOME[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("SOME"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("SOME.*") && codeLine.matches("SOME[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*SOME") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]SOME"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+SOME") || codeLine.matches("SOME\\s+") || codeLine.matches("\\s+SOME\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*EXISTS.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]EXISTS[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("EXISTS"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("EXISTS.*") && codeLine.matches("EXISTS[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*EXISTS") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]EXISTS"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+EXISTS") || codeLine.matches("EXISTS\\s+") || codeLine.matches("\\s+EXISTS\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*IN.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]IN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("IN"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("IN.*") && codeLine.matches("IN[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*IN") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]IN"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+IN") || codeLine.matches("IN\\s+") || codeLine.matches("\\s+IN\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*IS NULL.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]IS NULL[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("IS NULL"))
      {

        comparisonOperatorcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("IS NULL.*") && codeLine.matches("IS NULL[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*IS NULL") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]IS NULL"))
      {
        comparisonOperatorcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+IS NULL") || codeLine.matches("IS NULL\\s+") ||
               codeLine.matches("\\s+IS NULL\\s+"))
      {

        comparisonOperatorcount++;
        unvCount++;
      }

      if (codeLine.contains("<") || codeLine.contains("<") || codeLine.contains("=") || codeLine.contains("!"))
      {
        comparisonOperatorcount++;
        unvCount++;
      }

      if (codeLine.contains("+") || codeLine.contains("-") || codeLine.contains("/") || codeLine.contains("*"))
      {
        arithmaticOperatorcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*AND.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]AND[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("AND"))
      {

        conditionalANDCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("AND.*") && codeLine.matches("AND[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*AND") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]AND"))
      {
        conditionalANDCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+AND") || codeLine.matches("AND\\s+") || codeLine.matches("\\s+AND\\s+"))
      {

        conditionalANDCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*OR.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]OR[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("OR"))
      {

        conditionalANDCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("OR.*") && codeLine.matches("OR[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*OR") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]OR"))
      {
        conditionalANDCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+OR") || codeLine.matches("OR\\s+") || codeLine.matches("\\s+OR\\s+"))
      {

        conditionalANDCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*NOT.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]NOT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) || codeLine.matches("NOT"))
      {

        conditionalANDCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("NOT.*") && codeLine.matches("NOT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*NOT") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]NOT"))
      {
        conditionalANDCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+NOT") || codeLine.matches("NOT\\s+") || codeLine.matches("\\s+NOT\\s+"))
      {

        conditionalANDCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*OVER.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]OVER[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("OVER"))
      {

        conditionalANDCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("OVER.*") && codeLine.matches("OVER[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        conditionalANDCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*OVER") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]OVER"))
      {
        conditionalANDCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+OVER") || codeLine.matches("OVER\\s+") || codeLine.matches("\\s+OVER\\s+"))
      {

        conditionalANDCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*EXECUTE IMMEDIATE.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]EXECUTE IMMEDIATE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("EXECUTE IMMEDIATE"))
      {

        conditionalANDCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("EXECUTE IMMEDIATE.*") &&
               codeLine.matches("EXECUTE IMMEDIATE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        dynamicSqlCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*EXECUTE IMMEDIATE") &&
               codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]EXECUTE IMMEDIATE"))
      {
        dynamicSqlCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+EXECUTE IMMEDIATE") || codeLine.matches("EXECUTE IMMEDIATE\\s+") ||
               codeLine.matches("\\s+EXECUTE IMMEDIATE\\s+"))
      {

        dynamicSqlCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*OPEN FOR.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]OPEN FOR[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("OPEN FOR"))
      {

        dynamicSqlCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("OPEN FOR.*") && codeLine.matches("OPEN FOR[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        dynamicSqlCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*OPEN FOR") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]OPEN FOR"))
      {
        dynamicSqlCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+OPEN FOR") || codeLine.matches("OPEN FOR\\s+") ||
               codeLine.matches("\\s+OPEN FOR\\s+"))
      {

        dynamicSqlCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*FETCH.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]FETCH[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("FETCH"))
      {

        dynamicSqlCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("FETCH.*") && codeLine.matches("FETCH[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        dynamicSqlCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*FETCH") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]FETCH"))
      {
        dynamicSqlCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+FETCH") || codeLine.matches("FETCH\\s+") || codeLine.matches("\\s+FETCH\\s+"))
      {

        dynamicSqlCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*CLOSE.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]CLOSE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("CLOSE"))
      {

        dynamicSqlCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("CLOSE.*") && codeLine.matches("CLOSE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        dynamicSqlCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*CLOSE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]CLOSE"))
      {
        dynamicSqlCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+CLOSE") || codeLine.matches("CLOSE\\s+") || codeLine.matches("\\s+CLOSE\\s+"))
      {

        dynamicSqlCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*CREATE.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]CREATE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("CREATE"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("CREATE.*") && codeLine.matches("CREATE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*CREATE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]CREATE"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+CREATE") || codeLine.matches("CREATE\\s+") || codeLine.matches("\\s+CREATE\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*ALTER.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ALTER[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("ALTER"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("ALTER.*") && codeLine.matches("ALTER[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*ALTER") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ALTER"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+ALTER") || codeLine.matches("ALTER\\s+") || codeLine.matches("\\s+ALTER\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*DROP.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]DROP[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("DROP"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("DROP.*") && codeLine.matches("DROP[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*DROP") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]DROP"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+DROP") || codeLine.matches("DROP\\s+") || codeLine.matches("\\s+DROP\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*GRANT.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]GRANT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("GRANT"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("GRANT.*") && codeLine.matches("GRANT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*GRANT") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]GRANT"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+GRANT") || codeLine.matches("GRANT\\s+") || codeLine.matches("\\s+GRANT\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*REVOKE.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]REVOKE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("REVOKE"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("REVOKE.*") && codeLine.matches("REVOKE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*REVOKE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]REVOKE"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+REVOKE") || codeLine.matches("REVOKE\\s+") || codeLine.matches("\\s+REVOKE\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*ANALYZE.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ANALYZE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("ANALYZE"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("ANALYZE.*") && codeLine.matches("ANALYZE[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*ANALYZE") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]ANALYZE"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+ANALYZE") || codeLine.matches("ANALYZE\\s+") ||
               codeLine.matches("\\s+ANALYZE\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*AUDIT.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]AUDIT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("AUDIT"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("AUDIT.*") && codeLine.matches("AUDIT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*AUDIT") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]AUDIT"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+AUDIT") || codeLine.matches("AUDIT\\s+") || codeLine.matches("\\s+AUDIT\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*COMMENT.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]COMMENT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("COMMENT"))
      {

        ddlstatementcount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("COMMENT.*") && codeLine.matches("COMMENT[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        ddlstatementcount++;
        unvCount++;
      }
      else if (codeLine.matches(".*COMMENT") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]COMMENT"))
      {
        ddlstatementcount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+COMMENT") || codeLine.matches("COMMENT\\s+") ||
               codeLine.matches("\\s+COMMENT\\s+"))
      {

        ddlstatementcount++;
        unvCount++;
      }

      if (((codeLine.matches(".*AUTONOMOUS_TRANSACTION.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]AUTONOMOUS_TRANSACTION[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("AUTONOMOUS_TRANSACTION"))
      {

        autoTransactionCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("AUTONOMOUS_TRANSACTION.*") &&
               codeLine.matches("AUTONOMOUS_TRANSACTION[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        autoTransactionCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*AUTONOMOUS_TRANSACTION") &&
               codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]AUTONOMOUS_TRANSACTION"))
      {
        autoTransactionCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+AUTONOMOUS_TRANSACTION") || codeLine.matches("AUTONOMOUS_TRANSACTION\\s+") ||
               codeLine.matches("\\s+AUTONOMOUS_TRANSACTION\\s+"))
      {

        autoTransactionCount++;
        unvCount++;
      }

      if (((codeLine.matches(".*EXCEPTION.*")) && codeLine
              .matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]EXCEPTION[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*")) ||
          codeLine.matches("EXCEPTION"))
      {

        catchCount++;
        unvCount++;
        // System.out.println(codeLine);

      }
      else if (codeLine.matches("EXCEPTION.*") && codeLine.matches("EXCEPTION[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ].*"))
      {

        catchCount++;
        unvCount++;
      }
      else if (codeLine.matches(".*EXCEPTION") && codeLine.matches(".*[^a-zA-Z0-9&_$%@#!^+-/*öüÄÖÜ]EXCEPTION"))
      {
        catchCount++;
        unvCount++;

      }
      else if (codeLine.matches("\\s+EXCEPTION") || codeLine.matches("EXCEPTION\\s+") ||
               codeLine.matches("\\s+EXCEPTION\\s+"))
      {

        catchCount++;
        unvCount++;
      }

    }
  }

  public ClassBO populateBo(ClassBO cls)
  {

    cls.setIfCount(ifCount);
    cls.setDecodeCount(decodeCount);
    cls.setSwitchCount(whenCount);
    cls.setNonInnerCount(nonInnerCount);
    cls.setNvl2Count(nvl2Count);
    cls.setNvlCount(nvlCount);
    cls.setWhileCount(whileCount);
    cls.setGotocount(gotoCount);
    cls.setLoopUntilCount(loopCount);
    cls.setForCount(forCount);
    cls.setSetOprCount(setOperatorCount);
    cls.setComparOprCount(comparisonOperatorcount);
    cls.setArithOprCount(arithmaticOperatorcount);
    cls.setConditionalAndCount(conditionalANDCount);
    cls.setAnalyticalFunctcount(analticalfunctioncount);
    cls.setDynamicSqlCount(dynamicSqlCount);
    cls.setDdlcount(ddlstatementcount);
    cls.setAutoTransCount(autoTransactionCount);
    cls.setCatchCount(catchCount);
    return cls;

  }
}

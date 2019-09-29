package com.accenture.cc.servic;

import java.io.File;
import java.util.*;
import com.accenture.cc.domain.ClassBO;

public class CCGeneratorService
{

  List<ClassBO>     programList = new ArrayList<ClassBO>();

  ClassBO           clsbo       = new ClassBO();
  String            releaseName;
  String            moduleName;

  VbFileParserClass VBparser    = null;
  CFileParserClass Cparser     = null;
  CSFileParserClass CSparser    = null;
  FileParserClass parser = null;
  PLSQLFileParserClass plSqlParser = null;
  

  public List<ClassBO> getCCInformation(String path, String releaseName, String moduleName, String projectType)
  {
    this.releaseName = releaseName;
    this.moduleName = moduleName;

    if (projectType.equalsIgnoreCase("java"))
    {
      transverseDir(new File(path));
      //System.out.println("IN JAVA");
    }

    else if (projectType.equalsIgnoreCase("vb"))
    {
      transverseDirVB(new File(path));
      
    }
    else if (projectType.equalsIgnoreCase("cs"))
    {

      transverseDirCS(new File(path));
     
    }
    else if (projectType.equalsIgnoreCase("c"))
    {
      transverseDirC(new File(path));
      
    }
    else if(projectType.equalsIgnoreCase("plsql"))  {
      System.out.println("INPLSQL");
      transverseDirPlSql(new File(path));
      
    }

    return programList;
  }


  public void transverseDir(File node)
  {

   
    String extension = "";

    int i = node.getAbsoluteFile().toString().lastIndexOf('.');
    if (i > 0)
    {
      extension = node.getAbsoluteFile().toString().substring(i + 1);
    }

    if (extension.equalsIgnoreCase("java"))
    {
      // System.out.println(node.getAbsoluteFile());
      parser = new FileParserClass();
      clsbo = parser.parseAndGenerateClassBo(node.getAbsoluteFile().toString());
      
      if (clsbo != null)
      {

        clsbo.calculateParameters();

        if (clsbo.getTestCasesCount() > 0)
        {
          clsbo.setReleaseName(releaseName);
          clsbo.setModuleName(moduleName);
          programList.add(clsbo);
        }
        
      }
      
    }

    if (node.isDirectory())
    {
      String[] subNote = node.list();
      for (String filename : subNote)
      {
        transverseDir(new File(node, filename));
      }
    }

  }

  public void transverseDirVB(File node)
  {

    // clsbo = new ClassBO();
    String extension = "";

    // System.out.println("File : "+node.getAbsoluteFile().toString());
    int i = node.getAbsoluteFile().toString().lastIndexOf('.');
    if (i > 0)
    {
      extension = node.getAbsoluteFile().toString().substring(i + 1);
    }

    // System.out.println("EXT : "+extension);
    if (extension.equalsIgnoreCase("vb") || extension.equalsIgnoreCase("vbs"))
    {
      //System.out.println(node.getAbsoluteFile());
      VBparser    = new VbFileParserClass();
      clsbo = VBparser.parseAndGenerateClassBo(node.getAbsoluteFile().toString());



      if (clsbo != null)
      {

        clsbo.calculateParametersForVB();

        if (clsbo.getTestCasesCount() > 0)
        {
          clsbo.setReleaseName(releaseName);
          clsbo.setModuleName(moduleName);
          programList.add(clsbo);
        }
      }
    }


    if (node.isDirectory())
    {
      String[] subNote = node.list();
      for (String filename : subNote)
      {
        transverseDirVB(new File(node, filename));
      }
    }

  }

  public void transverseDirCS(File node)
  {

    clsbo = new ClassBO();
    String extension = "";

    int i = node.getAbsoluteFile().toString().lastIndexOf('.');
    if (i > 0)
    {
      extension = node.getAbsoluteFile().toString().substring(i + 1);
    }

    if (extension.equalsIgnoreCase("cs"))
    {
      //System.out.println(node.getAbsoluteFile());

      CSparser    = new CSFileParserClass();
      clsbo = CSparser.parseAndGenerateClassBo(node.getAbsoluteFile().toString());



      if (clsbo != null)
      {

        clsbo.calculateParametersCS();

        if (clsbo.getTestCasesCount() > 0)
        {
          clsbo.setReleaseName(releaseName);
          clsbo.setModuleName(moduleName);
          programList.add(clsbo);
        }
      }

    }

    if (node.isDirectory())
    {
      String[] subNote = node.list();
      for (String filename : subNote)
      {
        transverseDirCS(new File(node, filename));
      }
    }

  }

  public void transverseDirC(File node)
  {

    clsbo = new ClassBO();
    String extension = "";

    int i = node.getAbsoluteFile().toString().lastIndexOf('.');
    if (i > 0)
    {
      extension = node.getAbsoluteFile().toString().substring(i + 1);
    }

    if (extension.equalsIgnoreCase("c"))
    {
      // System.out.println(node.getAbsoluteFile());    
      Cparser     = new CFileParserClass();
      clsbo = Cparser.parseAndGenerateClassBo(node.getAbsoluteFile().toString());



      if (clsbo != null)
      {

        clsbo.calculateParametersC();

        if (clsbo.getTestCasesCount() > 0)
        {
          clsbo.setReleaseName(releaseName);
          clsbo.setModuleName(moduleName);
          programList.add(clsbo);
        }
      }
    }


    if (node.isDirectory())
    {
      String[] subNote = node.list();
      for (String filename : subNote)
      {
        transverseDirC(new File(node, filename));
      }
    }

  }
  
  public void transverseDirPlSql(File node)
  {

    clsbo = new ClassBO();
    String extension = "";

    int i = node.getAbsoluteFile().toString().lastIndexOf('.');
    if (i > 0)
    {
      extension = node.getAbsoluteFile().toString().substring(i + 1);
    }

    if (extension.equalsIgnoreCase("sql"))
    {
      System.out.println(node.getAbsoluteFile());
      
      plSqlParser = new PLSQLFileParserClass();
      clsbo = plSqlParser.parseAndGenerateClassBo(node.getAbsoluteFile().toString());



      if (clsbo != null)
      {

        clsbo.calculateParametersPlSql();

        if (clsbo.getTestCasesCount() > 0)
        {
          clsbo.setReleaseName(releaseName);
          clsbo.setModuleName(moduleName);
          programList.add(clsbo);
        }
      }
    }


    if (node.isDirectory())
    {
      String[] subNote = node.list();
      for (String filename : subNote)
      {
        transverseDirPlSql(new File(node, filename));
      }
    }

  }
}

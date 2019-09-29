package com.accenture.cc.controller;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.accenture.cc.domain.ClassBO;
import com.accenture.cc.servic.*;

/**
 * Servlet implementation class CyclomaticComplexityExcelDoc
 */
@WebServlet("/CyclomaticComplexityExcelDoc")
public class CyclomaticComplexityExcelDoc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CyclomaticComplexityExcelDoc() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  
	  String projectPath = request.getParameter("folderLoc");
    String releaseName = request.getParameter("rlsName");
    String moduleName = request.getParameter("modName");
    String projectType = request.getParameter("projectType");
    boolean errorFlag = false;
   

    CCGeneratorService service = new CCGeneratorService();
    List<ClassBO> classInfo = new ArrayList<ClassBO>();
    
    try
    {
      classInfo = service.getCCInformation(projectPath, releaseName, moduleName, projectType);

      Collections.sort(classInfo, new SortingHelperStringComp());
    }
    catch (Exception e)
    {
      // TODO: handle exception
     
      e.printStackTrace();
      errorFlag = true;
    }
    
    if(errorFlag) {
     //Go TO Error Page
      RequestDispatcher rd = request.getRequestDispatcher("errorPage.html");
      rd.forward(request, response);
    }
    else  {
      try
      {
        System.out.println("SIZE : "+classInfo.size());
        request.setAttribute("classInfoList", classInfo);
        
        if(projectType.equalsIgnoreCase("java"))  {
        RequestDispatcher rd = request.getRequestDispatcher("ccView.jsp");
        rd.forward(request, response);
        }
        
        else if (projectType.equalsIgnoreCase("vb"))
        {

          RequestDispatcher rd = request.getRequestDispatcher("VbccView.jsp");
          rd.forward(request, response);
        }
        else if (projectType.equalsIgnoreCase("cs"))
        {

          RequestDispatcher rd = request.getRequestDispatcher("CsccView.jsp");
          rd.forward(request, response);
        }
        else if (projectType.equalsIgnoreCase("c"))
        {

          RequestDispatcher rd = request.getRequestDispatcher("CccView.jsp");
          rd.forward(request, response);
        }
        else if (projectType.equalsIgnoreCase("plsql"))
        {

          RequestDispatcher rd = request.getRequestDispatcher("PlSqlccView.jsp");
          rd.forward(request, response);
        }
        
      }
      catch (Exception e)
      {
        // TODO: handle exception
        RequestDispatcher rd = request.getRequestDispatcher("errorPage.html");
        rd.forward(request, response);
      }
   
    }
	}

}

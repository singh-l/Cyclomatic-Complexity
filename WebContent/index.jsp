<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>

<script type="text/javascript">
  function validate() {
var val = document.getElementById("folderLoc").value;
var ind = val.lastIndexOf("\\");
var fold = val.substring(0,ind);
    alert(fold);
  }
</script>
</head>
<body>
  <h4 align="center">Welcome To Ciclomatic Complexity Generation
    Wizard</h4>
  <br>
  <br>
  <hr>

  <form name="ccProjectForm" id="ccForm" action="CCRequestHandller" method="post">
    <table align="center" cellpadding="5" cellspacing="5" border="1">
      <tr>
        <td>Choose Project Root Directory :</td>
        <td><input type="text" id="folderLoc" name="folderLoc"></td>
      </tr>
      
      <tr>
        <td>Release Name :</td>
        <td><input type="text" id="rlsName" name="rlsName"></td>
      </tr>
      
      <tr>
        <td>Module Name : </td>
        <td><input type="text" id="modName" name="modName"></td>
      </tr>

      <tr>
        <td colspan="2" align="center"><input type="submit"
          value="Generate CC" ></td>
      </tr>


    </table>
  </form>
</body>
</html>
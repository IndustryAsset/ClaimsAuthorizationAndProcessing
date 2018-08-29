<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="javascript/buttonDisable.js"></script>
<title>User Test Model</title>

<script>
$(document).ready(function(){
       $("#result").hide();
       $("#result1").hide();
    });

</script>
<c:if test="${resultpage == 'yes' and isjava == 'yes'}">
		<script>
		$(document).ready(function(){
			$("#result").show();
		 });
		</script>
</c:if>
<c:if test="${resultpage == 'yes' and isjava == 'no'}">
		<script>
		$(document).ready(function(){
			$("#result1").show();
		 });
		</script>
	</c:if>	

<style>
 #headerContainer {
	-moz-box-shadow: inset 0px 1px 0px 0px #97c4fe;
	-webkit-box-shadow: inset 0px 1px 0px 0px #97c4fe;
	box-shadow: inset 0px 1px 0px 0px #97c4fe;
	background-position: center;-webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
	/* background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #064d81
		), color-stop(1, #064d81));
	background: -moz-linear-gradient(top, #064d81 5%, #064d81 100%);
	background: -webkit-linear-gradient(top, #064d81 5%, #064d81 100%);
	background: -o-linear-gradient(top, #064d81 5%, #064d81 100%);
	background: -ms-linear-gradient(top, #064d81 5%, #064d81 100%);
	background: linear-gradient(to bottom, #064d81 5%, #064d81 100%); */
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#064d81',
		endColorstr='#064d81', GradientType=0);
	background-color: #064d81;
	-moz-border-radius: 6px;
	-webkit-border-radius: 6px;
	border-radius: 6px;
	border: 1px solid #337fed;
	display: block;
	color: #ffffff;
	font-family: Arial;
	font-size: 15px;
	font-weight: bold;
	/* padding: 6px 24px; */
	text-decoration: none;
	text-shadow: 0px 1px 0px #1570cd;
	margin: 0px;
} 
</style>
</head>
<body>
<div id="headerContainer">
	<table width="100%">
		<tr>
			<td width="33%" style="vertical-align: middle;"><img height="60"
				src="css/images/accenture-blue.jpg" id="logo" /></td>
			<td width="33%" align="center" style="font-size: 24px"><b>Claims
					AI Prediction Engine</b></td>
			<!-- <td width="33%" align="right">
				<table>
					<tr>
						<td align="right" style="font-size: 14px">Employee Id: </td>
						<td align="left" style="font-weight:800;"></td>
					</tr>
					<tr>
						<td align="right" style="font-size: 14px">Role: </td>
						<td align="left" style="font-weight:800;"></td>
					</tr>
					
					
				</table>
			</td> -->
			<%-- <td width="33%" align="right">
			<c:url value="/j_spring_security_logout" var="logoutUrl" />
					
	--%>
	
			<td width="33%" align="right"><a href="logout.htm"> <img src="css/images/logout.jpg"
					 style="width: 30px; height: 28px; border: 0;">
			</a></td>
		</tr>
	</table>

</div>
<div class="box" style=" position: absolute;width: 1000px;height: 580px;background-image: url(images/robotichand.jpg);width:99%;-webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;">
  <center>
	<form name="evaluate" action="evaluateResults.xls" method="post"
		enctype="multipart/form-data">
		<h3 align="center" style="color:#064d81">USER TEST MODEL</h3><br><br>
		<strong style="font-size: 20px;">Select Excel File:
		<br><br> <input id="file" name="file"
			type="file">
			 <br> </br></strong>
			<!--  <label><b>Language</b> : </label><input type="radio" name="language" value="python" checked> Python
  										<input type="radio" name="language" value="java"> Java<br><br> -->
			  <input id="submit"
			type="submit" value="Test" >

	</form>
	</center><br>
	</div>
	<%-- <div id="result">
		<div style="float: left; width: 20%">
			<Table border="1">
				<tr>
					<th>Actual Class</th>
					<th>Predicted Class</th>
				</tr>
				<c:forEach items="${results}" var="result">
					<c:set var="data" value="${fn:split(result, ',')}" />
					<tr>
						<td><c:out value="${data[0]}" /></td>
						<td><c:out value="${data[1]}" /></td>
					</tr>
				</c:forEach>
			</Table>
		</div>
		<div style="float: right; width: 70%">
			<Table border="1">
				<c:forEach var="eval" items="${evaluationResult}">
					<tr>
						<td>${eval.key}</td>
						<td>${eval.value}</td>
					</tr>

				</c:forEach>
			</Table>
		</div>
	</div>
	
</div>	 --%>
	
	<%-- <div id="result1">
		<div style="float: left; width: 20%">
			<Table border="1">
				<tr>
					<th>Billing National Provider Identification Number</th>
					<th>Insured Policy Number for Subscriber</th>
					<th>Subscriber State</th>
					<th>Subscriber Postal Code</th>
					<th>Subscriber Birth Date</th>
					<th>Payer Identification</th>
					<th>Patient State</th>
					<th>Patient Zip Code</th>
					<th>Patients Birth Date</th>
					<th>Facility Type Code</th>
					<th>Claim Transaction Type</th>
					<th>Statement From and Statement Through Date</th>
					<th>Principal Diagnosis Code</th>
					<th>Admitting Diagnosis Code</th>
					<th>Attending Provider NPI</th>
					<th>Rendering Provider NPI</th>
					<th>Reffering provider NPI</th>
					<th>Revenue Code</th>
					<th>CPT Procedure Code</th>
					<th>Procedure Modifier 1</th>
					<th>Procedure Modifier 2</th>
					<th>Line Item Charge Amount</th>
					<th>Service Unit Count</th>
					<th>Service Date</th>
					<th>Service Facility Provider ID</th>
				</tr>
				<c:forEach items="${ListOfClaims}" var="Claims">
					<tr>
						<c:forEach items="${Claims}" var="Claim">

							<td><c:out value="${Claim}" /></td>
						</c:forEach>
					</tr>

				</c:forEach>
			</Table>
		</div>
	</div> --%>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Prepare Training Data</title>
	<script  type="text/javascript" src = "javascript/buttonDisable.js"></script>
	<script type="text/javascript" src = "javascript/excelFileExtensionValidation.js"></script>
	<c:if test="${message == 'successUpload'}">
		<script>
			alert("Excel File Saved Successfully");
		</script>
	</c:if>
	<c:if test="${message == 'successConversion'}">
		<script>
			alert("Excel File Converted to CSV Successfully");
		</script>
	</c:if>
	<%-- <c:if test="${message == 'deletedSuccessfully'}">
		<script>
			alert("Excel File deleted Successfully");
		</script>
	</c:if> --%>
	<c:if test="${message == 'CannotDeleteExcel'}">
		<script>
			alert("Selected Excel file cannot be deleted as it has CSV dependency");
		</script>
	</c:if>
	

<script>
       $(document).ready(function(){
    	   $(document).ajaxSend(function(event, request, settings) {
    			  $('#loading-indicator').show();
    			});

    			$(document).ajaxComplete(function(event, request, settings) {
    			  $('#loading-indicator').hide();
    			}); 
    	   document.getElementById("submit").style.color = "black";
    		//document.getElementById("submit").style.background = "#000";
    		document.getElementById("submit").style.background = null;
    		document.getElementById("submit").style.opacity = ".5";
    		$('#file').change(function() {
    			if ($("#file").val() != '') {
    				$('#submit').attr('disabled', false);
    				document.getElementById("submit").style.color = "black";
    				document.getElementById("submit").style.background = null;
    				document.getElementById("submit").style.opacity = null;
    			} else {
    				$('#submit').attr('disabled', true);
    				document.getElementById("submit").style.color = "black";
    				//document.getElementById("submit").style.background = "#000";
    				document.getElementById("submit").style.background = null;
    				//document.getElementById("submit").style.opacity = ".4";
    			}
    		});
    	   $('#excelTable').DataTable();
    	    $('#excelTable').on('click', '.convert', function(){
    		   butId = $(this).attr('id');
               var lastChar = butId.substr(7);
               var selectId = "select_"+lastChar;
               var language = 'Python';
               var id= $("#"+butId).val();
               var init = [];
           
			init[0]=id;
           
               var id2 =JSON.stringify(init);
             
               $.ajax({
                   type: "POST",
                   url: "convertToCsv.htm",
                  dataType: 'text',
       			   data:{excelId:id2,
       				language : language
       			},
                   success: function (result) {
                	   $("#div1")
						.fadeTo(1000, 100)
						.slideUp(
								350,
								function() {
									$("#div1")
											.slideUp(
													350);
								});
				$("#div1").show();
                   },
                   error: function (result) {
                	   $("#div2")
   					.fadeTo(1000, 100)
   					.slideUp(
   							350,
   							function() {
   								$("#div2")
   										.slideUp(
   												350);
   							});
   			$("#div2").show();
                   }
               });
    		}); 
    	   
    	
    		
    	    	   $("#btn_convert").click(function() {
    	    		
    	    			var inputs = $(".denied");
						var init = [];
						for (var i = 0; i < inputs.length; i++) {
							init.push($(inputs[i]).val());
						}
						var id = JSON.stringify(init);
					
						var language = 'Python';
						//alert(id);
						
						 $.ajax({
			                   type: "POST",
			                   url: "convertToCsv.htm",
			                  dataType: 'text',
			       			   data:{excelId:id,
			       				language : language
			       			},
			                   success: function (result) {
			                	   $("#div1")
									.fadeTo(1000, 100)
									.slideUp(
											350,
											function() {
												$("#div1")
														.slideUp(
																350);
											});
							$("#div1").show();
			                   },
			                   error: function (result) {
			                	   $("#div2")
									.fadeTo(1000, 100)
									.slideUp(
											350,
											function() {
												$("#div2")
														.slideUp(
																350);
											});
							$("#div2").show();
			                   }
			               });
    	    		 
    	    	   });
});
       
</script>
 
</head>	
<body >
<h1>Prepare Training Data</h1><br>
<div>
<center><img src="images/ajax-loader (1).gif" id="loading-indicator" style="display:none" /></center>

</div> 
<div id="div1" class="alert alert-success" style="display: none;">
						<strong>Convertion successfull for the file</strong>
					</div> 
					
<div id="div2" class="alert alert-warning" style="display: none;">
						<strong>Convertion unsuccessfull for the file</strong>
					</div> 

	<form action="uploadExcel.htm" method="post" enctype="multipart/form-data">
		<label>Select Excel File :</label>
	
		<input id = "file" name="file" type="file" multiple="multiple"> <br> <br> 
		<!-- <input type="radio" name="modeltype" value="General"> Main Model<br>
			<input type="radio" name="modeltype" value="Pend"> Pend Model<br>
               <input type="radio" name="modeltype" value="Reject"> Reject Model<br> -->
		<input id = "submit" type="submit" value="Upload File" disabled  >

	</form> 
	<br>
	<br>
	<table class="display jqueryDataTable" id="excelTable">
		<thead>
			<tr>
				<th>#</th>
				<th>File Name</th>
				<th>Data Count</th>
				<th>Attribute Count</th>
				<th>Download</th>
				<th>Python</th>
			 	<th>Convert to CSV</th> 
				<th>Delete</th>
			</tr>
		</thead>
		<tbody style="color: Black">
			<c:forEach items="${excelFiles}" var = "excelFiles" varStatus = "loop">
			<tr>
				<td scope="row"><c:out value = "${loop.count }"></c:out></td>
				<td><c:out value = "${excelFiles.fileName}"/></td>
				<td><c:out value = "${excelFiles.rowcount}"/></td> 
				<td><c:out value = "${excelFiles.colCount}"/></td> 
				<td>
					<a href="downloadExcel.htm?id=${excelFiles.id}">
						Download
  					</a>
				</td> 
				<td>
				<%-- <select  id="select_${loop.count}">
				      <option>Select Language</option>
					  <option value="python">Python</option>
					  <option value="java">Java</option>
				</select> --%>
				
				</td>
			
				 <td><Button class="convert" id = "button_${loop.count}" value="${excelFiles.id}">Convert</Button></td> 
				
				<td>
					<a href="deleteExcel.htm?id=${excelFiles.id}">
					<img src="images/delete.png" alt="delete" style="width:30px;height:28px;border:0;"></a>
				</td>
				<%-- <td><input type="hidden" id="excelid_${loop.count}" value="${excelFiles.id}"></td> --%>
	<%-- 			<td><input type ="hidden" name ="excelid" value ="${excelFiles.id}"></td> --%>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:forEach items="${excelFiles}" var = "excelFiles" varStatus = "loop">
	<input type ="hidden" name ="excelid" value ="${excelFiles.id}" class="denied">
	</c:forEach>
	<center>
	<Button style="color: black" class="convert" id="btn_convert" class="btn btn-primary">Convert All Files</Button>
	</center>
</body>
</html>

 

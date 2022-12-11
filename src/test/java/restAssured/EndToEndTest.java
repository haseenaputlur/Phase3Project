package restAssured;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	Response response;
	String baseURI="http://localhost:3000";
	
	
	@Test
	public void test1() {
		
		System.out.println("Get All Employees");		
		response = GetAllEmployee();
		Assert.assertEquals(200, response.getStatusCode());
		JsonPath jpath = response.jsonPath();
		String name=jpath.get("name");
		int id= jpath.get("id");
		
		System.out.println("Get Single Employee");	
		
		response=GetSingleEmp(id);
		Assert.assertEquals(200, response.getStatusCode());
		String ResponseBody = response.getBody().asString();
		//Assert.assertTrue(responseBody.contains("Jhon"));
		JsonPath jpath1 = response.jsonPath();
	    //Assert.assertEquals(name.get(id).equals("Jhon"));
		
		
		
		System.out.println("Create new Employee");		
		response=CreateEmp("Harsh" , "5000");
		Assert.assertEquals(201, response.getStatusCode());
		
		
		System.out.println("Update Employee");		
		response=UpdateEmp(id,"Sam" , "7000");
		Assert.assertEquals(200, response.getStatusCode());
		
		
		System.out.println("Delete Employee");
		response=DeleteEmp(28);
		Assert.assertEquals(200, response.getStatusCode());
		
		System.out.println("Validate statuscode for created employee");
		response=GetSingleEmp(28);
		Assert.assertEquals(404, response.getStatusCode());
		
		System.out.println("Validate Deleted Employee");
		response=GetAllEmployee();		
		//Assert.assertTrue(ResponseBody.contains("Harsh"));
		//JsonPath jpath = response.jsonPath();
		List<String> names = jpath.get("name");
		System.out.println("The name is :" + names.get(1));
		
		Assert.assertEquals(names.get(0), "Jhon");
	
		
		
	}
	

	public Response GetAllEmployee() {
		RestAssured.baseURI = this.baseURI;	
		RequestSpecification request = RestAssured.given();
		response = request.get("employees");
		String ResponseBody = response.getBody().asString();

		System.out.println(ResponseBody);
		
		return response;
		
	}

	public Response GetSingleEmp(int empID) {
		RestAssured.baseURI = this.baseURI;	
		RequestSpecification request = RestAssured.given();
		Response response = request.param("id" , empID).get("employees");
		System.out.println(response.getBody().asString());
		
		return response;
		

	}
	
	public Response CreateEmp(String name, String Salary) {
		
		RestAssured.baseURI = this.baseURI;	
		RequestSpecification request = RestAssured.given();
		
		Map<String, Object> MapObj=new HashMap<String,Object>();
		MapObj.put("name", name);
		MapObj.put("Salary", Salary);
		
		Response response=request
		                  .contentType(ContentType.JSON)
		                  .accept(ContentType.JSON)
		                  .body(MapObj)
		                  .post("employees/create");
		
		System.out.println(response.getBody().asString());
		
		return response;
		
		
	}
	
	public Response UpdateEmp(int empID, String name, String Salary) {
		
		RestAssured.baseURI = this.baseURI;	
		RequestSpecification request = RestAssured.given();
		
		Map<String, Object> MapObj=new HashMap<String,Object>();
		MapObj.put("name", name);
		MapObj.put("salary", Salary);
		Response response=request
				   .contentType(ContentType.JSON)
                  .accept(ContentType.JSON)
                  .body(MapObj)
                  .put("employees"+empID);
		
       System.out.println(response.getBody().asString());
		
		return response;
		
	}
	
	public Response DeleteEmp(int empID) {
		
		RestAssured.baseURI = this.baseURI;	
		RequestSpecification request = RestAssured.given();
		
		Response response = request.delete("employees"+empID);

		System.out.println(response.getBody().asString());
		
		return response;
		
	}
}
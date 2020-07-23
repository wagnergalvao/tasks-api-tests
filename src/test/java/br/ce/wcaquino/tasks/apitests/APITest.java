package br.ce.wcaquino.tasks.apitests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	public static DateTimeFormatter task = DateTimeFormatter.ofPattern("A EEEE");
	public static DateTimeFormatter dueDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8081/tasks-backend";
	}

	@Test
	public void deveRetornarTarefas() {
		RestAssured
		.given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
		;
	}

	@Test
	public void deveAdicionarUmaTarefaComSucesso() {
		LocalDateTime dataTarefa = LocalDateTime.now();
		String body = "{\"task\":\"Teste via API " + dataTarefa.format(task)
			+ "\",\"dueDate\":\"" + dataTarefa.format(dueDate)+ "\"}";
		RestAssured
		.given()
			.body(body)
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
		;
	}
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		LocalDateTime dataTarefa = LocalDateTime.now().minusDays(1);
		String body = "{\"task\":\"Teste via API " + dataTarefa.format(task)
			+ "\",\"dueDate\":\"" + dataTarefa.format(dueDate)+ "\"}";
		RestAssured
		.given()
			.body(body)
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}

}

/**
 * Copyright (C) 2010-2016 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.rest.resource;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.structr.rest.common.StructrRestTest;

/**
 *
 *
 */
public class SchemaResourceTest extends StructrRestTest {

	@Test
	public void testCustomSchema0() {

		createEntity("/schema_node", "{ \"name\": \"TestType0\", \"_foo\": \"String\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].type", equalTo("String"))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].declaringClass", equalTo("TestType0"))

			.when()
				.get("/_schema/TestType0/ui");

	}

	@Test
	public void testCustomSchema1() {

		createEntity("/schema_node", "{ \"name\": \"TestType1\", \"_foo\": \"fooDb|String\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].type", equalTo("String"))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("fooDb"))
				.body("result[-1].declaringClass", equalTo("TestType1"))

			.when()
				.get("/_schema/TestType1/ui");

	}

	@Test
	public void testCustomSchema2() {

		createEntity("/schema_node", "{ \"name\": \"TestType2\", \"_foo\": \"+String\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].type", equalTo("String"))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType2/ui");

	}

	@Test
	public void testCustomSchema3() {

		createEntity("/schema_node", "{ \"name\": \"TestType3\", \"_foo\": \"String!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].unique", equalTo(true))

			.when()
				.get("/_schema/TestType3/ui");

	}

	@Test
	public void testCustomSchema4() {

		createEntity("/schema_node", "{ \"name\": \"TestType4\", \"_foo\": \"+String!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].unique", equalTo(true))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType4/ui");

	}

	@Test
	public void testCustomSchema5() {

		createEntity("/schema_node", "{ \"name\": \"TestType5\", \"_foo\": \"String(bar)\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].format", equalTo("bar"))

			.when()
				.get("/_schema/TestType5/ui");

	}

	@Test
	public void testCustomSchema6() {

		createEntity("/schema_node", "{ \"name\": \"TestType6\", \"_foo\": \"String!(bar)\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].unique", equalTo(true))
				.body("result[-1].format", equalTo("bar"))

			.when()
				.get("/_schema/TestType6/ui");

	}

	@Test
	public void testCustomSchema7() {

		createEntity("/schema_node", "{ \"name\": \"TestType7\", \"_foo\": \"String[text/html]\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))

			.when()
				.get("/_schema/TestType7/ui");

	}

	@Test
	public void testCustomSchema8() {

		createEntity("/schema_node", "{ \"name\": \"TestType8\", \"_foo\": \"String[text/html]!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].unique", equalTo(true))

			.when()
				.get("/_schema/TestType8/ui");

	}

	@Test
	public void testCustomSchema9() {

		createEntity("/schema_node", "{ \"name\": \"TestType9\", \"_foo\": \"+String[text/html]!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType9/ui");

	}


	@Test
	public void testCustomSchema10() {

		createEntity("/schema_node", "{ \"name\": \"TestType10\", \"_foo\": \"+String[text/html]!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType10/ui");

	}

	@Test
	public void testCustomSchema11() {

		createEntity("/schema_node", "{ \"name\": \"TestType11\", \"_foo\": \"+String[text/html]!([a-f0-9]{32}):xyz\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("[a-f0-9]{32}"))
				.body("result[-1].defaultValue", equalTo("xyz"))

			.when()
				.get("/_schema/TestType11/ui");

	}

	@Test
	public void testCustomSchema12() {

		createEntity("/schema_node", "{ \"name\": \"TestType12\", \"_foo\": \"+Date!(yyyy-MM-dd)\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].type", equalTo("Date"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("yyyy-MM-dd"))

			.when()
				.get("/_schema/TestType12/ui");

	}

	@Test
	public void testCustomSchema13() {

		createEntity("/schema_node", "{ \"name\": \"TestType13\", \"_foo\": \"fooDb|+String[text/html]!([a-f0-9]{32}):xyz\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("fooDb"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("[a-f0-9]{32}"))
				.body("result[-1].defaultValue", equalTo("xyz"))

			.when()
				.get("/_schema/TestType13/ui");

	}

	@Test
	public void testCustomSchema14() {

		createEntity("/schema_node", "{ \"name\": \"TestType14\", \"_foo\": \"fooDb|+String[text/html]!(multi-line):xyz\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("fooDb"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("multi-line"))
				.body("result[-1].defaultValue", equalTo("xyz"))

			.when()
				.get("/_schema/TestType14/ui");

	}

	@Test
	public void testCustomSchema15() {

		createEntity("/schema_node", "{ \"name\": \"TestType15\", \"_foo\": \"fooDb|+String[text/html]!(some-format with | pipe in it):xyz\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("fooDb"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("some-format with | pipe in it"))
				.body("result[-1].defaultValue", equalTo("xyz"))

			.when()
				.get("/_schema/TestType15/ui");

	}


	@Test
	public void testCustomSchema16() {

		createEntity("/schema_node", "{ \"name\": \"TestType16\", \"_foo\": \"+String[text/html]!(some-format with no pipe in it):xyz\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("some-format with no pipe in it"))
				.body("result[-1].defaultValue", equalTo("xyz"))

			.when()
				.get("/_schema/TestType16/ui");

	}

	@Test
	public void testCustomSchema17() {

		createEntity("/schema_node", "{ \"name\": \"TestType17\", \"_foo\": \"+String[text/html]!(some-format with a | pipe in it):xyz\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].contentType", equalTo("text/html"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("some-format with a | pipe in it"))
				.body("result[-1].defaultValue", equalTo("xyz"))

			.when()
				.get("/_schema/TestType17/ui");

	}

	@Test
	public void testCustomSchema18() {

		createEntity("/schema_node", "{ \"name\": \"TestType18\", \"_foo\": \"Foo|+Date!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Date"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType18/ui");

	}

	@Test
	public void testCustomSchema19() {

		createEntity("/schema_node", "{ \"name\": \"TestType19\", \"_foo\": \"Foo|+Date!(yyyy-MM-dd)\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Date"))
				.body("result[-1].notNull", equalTo(true))
				.body("result[-1].format", equalTo("yyyy-MM-dd"))

			.when()
				.get("/_schema/TestType19/ui");

	}

	@Test
	public void testCustomSchema20() {

		createEntity("/schema_node", "{ \"name\": \"TestType20\", \"_foo\": \"Foo|+Boolean!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Boolean"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType20/ui");

	}

	@Test
	public void testCustomSchema21() {

		createEntity("/schema_node", "{ \"name\": \"TestType21\", \"_foo\": \"Foo|+Double!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Double"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType21/ui");

	}

	@Test
	public void testCustomSchema22() {

		createEntity("/schema_node", "{ \"name\": \"TestType22\", \"_foo\": \"+Enum(a,b,c)!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("foo"))
				.body("result[-1].type", equalTo("Enum"))
				.body("result[-1].format", equalTo("a,b,c"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType22/ui");

	}

	@Test
	public void testCustomSchema23() {

		createEntity("/schema_node", "{ \"name\": \"TestType23\", \"_foo\": \"Foo|+Enum(a,b,c)!\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Enum"))
				.body("result[-1].format", equalTo("a,b,c"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType23/ui");

	}

	@Test
	public void testCustomSchema24() {

		createEntity("/schema_node", "{ \"name\": \"TestType24\", \"_foo\": \"Foo|+Enum!(a,b,c):b\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Enum"))
				.body("result[-1].format", equalTo("a,b,c"))
				.body("result[-1].defaultValue", equalTo("b"))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType24/ui");

	}

	@Test
	public void testCustomSchema25() {

		createEntity("/schema_node", "{ \"name\": \"TestType25\", \"_foo\": \"Foo|+Boolean!:true\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Boolean"))
				.body("result[-1].defaultValue", equalTo(true))
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType25/ui");

	}

	@Test
	public void testCustomSchema26() {

		createEntity("/schema_node", "{ \"name\": \"TestType26\", \"_foo\": \"Foo|+Double!:12.34\" }");

		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(200))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result",	      hasSize(14))
				.body("result_count", equalTo(14))
				.body("result[-1].jsonName", equalTo("foo"))
				.body("result[-1].dbName", equalTo("Foo"))
				.body("result[-1].type", equalTo("Double"))
				.body("result[-1].defaultValue", equalTo(12.34f)) // The restassured lib parses floating-point numbers to Float
				.body("result[-1].notNull", equalTo(true))

			.when()
				.get("/_schema/TestType26/ui");

	}

	@Test
	public void testSchemaMethodExecution() {

		createEntity("/SchemaNode", "{ name: Test, __public: \"name, type\", ___test: \"find('Test')\" }");
		createEntity("Test", "{ name: Test }");


		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result_count", equalTo(1))
				.body("result.type", equalTo("Test"))
				.body("result.name", equalTo("Test"))

			.when()
				.post("/Test/test");
	}

	@Test
	public void testInheritedSchemaMethodExecution() {

		createEntity("/SchemaNode", "{ name: TestBase, ___test: \"find('Test')\" }");
		createEntity("/SchemaNode", "{ name: Test, __public: \"name, type\", extendsClass: \"org.structr.dynamic.TestBase\" }");
		createEntity("Test", "{ name: Test }");


		RestAssured

			.given()
				.contentType("application/json; charset=UTF-8")
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(201))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(400))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(404))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(422))
				.filter(ResponseLoggingFilter.logResponseIfStatusCodeIs(500))

			.expect()
				.statusCode(200)

				.body("result_count", equalTo(1))
				.body("result.type", equalTo("Test"))
				.body("result.name", equalTo("Test"))

			.when()
				.post("/Test/test");
	}
}

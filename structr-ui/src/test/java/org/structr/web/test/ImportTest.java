/**
 * Copyright (C) 2010-2016 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.web.test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.structr.common.error.FrameworkException;
import org.structr.core.entity.SchemaNode;
import org.structr.core.entity.SchemaProperty;
import org.structr.core.graph.Tx;
import org.structr.schema.importer.GraphGistImporter;
import org.structr.web.common.StructrUiTest;

/**
 *
 *
 */
public class ImportTest extends StructrUiTest {

	private static final Logger logger = LoggerFactory.getLogger(ImportTest.class.getName());

	@Test
	public void testImportAndSchemaAnalyzer() {

		final GraphGistImporter importer = app.command(GraphGistImporter.class);
		final String source =
			"== Test setup\n" +
			"\n" +
			"[source, cypher]\n" +
			"----\n" +
			"CREATE (c:Company { name: 'Company 1', comp_id: '12345', string_name: 'company1', year: 2013, month: 6, day: 7, status: 'test'})\n" +
			"CREATE (p:Company { name: 'Company 2'})\n" +
			"----\n";

		final List<String> sourceLines = importer.extractSources(new ByteArrayInputStream(source.getBytes(Charset.forName("utf-8"))));

		// import (uses Neo4j transaction)
		importer.importCypher(sourceLines);
		importer.analyzeSchema();

		try (final Tx tx = app.tx()) {

			final SchemaNode schemaNode           = app.nodeQuery(SchemaNode.class).andName("Company").getFirst();
			final List<SchemaProperty> properties = schemaNode.getProperty(SchemaNode.schemaProperties);
			final Map<String, SchemaProperty> map = new HashMap<>();

			for (final SchemaProperty prop : properties) {
				map.put(prop.getProperty(SchemaProperty.name), prop);
			}

			assertNotNull("A schema node with name 'Company' should have been created: ", schemaNode);

			assertEquals("Company schema node should have a 'name' property with value 'String': ",        "String", map.get("name").getPropertyType().name());
			assertEquals("Company schema node should have a 'comp_id' property with value 'String': ",     "String", map.get("comp_id").getPropertyType().name());
			assertEquals("Company schema node should have a 'string_name' property with value 'String': ", "String", map.get("string_name").getPropertyType().name());
			assertEquals("Company schema node should have a 'year' property with value 'Long': ",          "Long",   map.get("year").getPropertyType().name());
			assertEquals("Company schema node should have a 'month' property with value 'Long': ",         "Long",   map.get("month").getPropertyType().name());
			assertEquals("Company schema node should have a 'day' property with value 'Long': ",           "Long",   map.get("day").getPropertyType().name());
			assertEquals("Company schema node should have a 'status' property with value 'String': ",      "String", map.get("status").getPropertyType().name());

			tx.success();

		} catch (FrameworkException fex) {

			logger.warn("", fex);
			fail("Unexpected exception.");
		}


	}
}


/*
*/

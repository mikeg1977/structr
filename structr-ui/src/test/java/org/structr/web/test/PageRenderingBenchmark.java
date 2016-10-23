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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import static junit.framework.TestCase.assertTrue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.structr.common.error.FrameworkException;
import org.structr.core.graph.Tx;
import org.structr.web.common.StructrUiTest;
import org.structr.web.entity.dom.Page;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

//~--- classes ----------------------------------------------------------------
/**
 * Create a simple test page and test performance
 *
 *
 */
public class PageRenderingBenchmark extends StructrUiTest {

	private static final Logger logger = LoggerFactory.getLogger(PageRenderingBenchmark.class.getName());

	@Test
	public void test01PagePerformance() {

		final String pageName = "page-01";
		final String pageTitle = "Page Title";
		final String bodyText = "Body Text";

		final String h1ClassAttr = "heading";
		final String divClassAttr = "main";

		Page page = null;
		Element html = null;
		Element head = null;
		Element body = null;
		Element title = null;
		Element h1 = null;
		Element div = null;

		Text titleText = null;
		Text heading = null;
		Text bodyContent = null;

		try (final Tx tx = app.tx()) {

			page = Page.createNewPage(securityContext, pageName);

			if (page != null) {

				html = page.createElement("html");
				head = page.createElement("head");
				body = page.createElement("body");
				title = page.createElement("title");
				h1 = page.createElement("h1");
				div = page.createElement("div");

				titleText = page.createTextNode(pageTitle);
				heading = page.createTextNode(pageTitle);
				bodyContent = page.createTextNode(bodyText);

				makePublic(page, html, head, body, title, h1, div, titleText, heading, bodyContent);

				tx.success();
			}

		} catch (FrameworkException ex) {

			logger.warn("", ex);

			logger.error(ex.toString());
			fail("Unexpected exception");

		}

		try (final Tx tx = app.tx()) {
			// add HTML element to page
			page.appendChild(html);

			// add HEAD and BODY elements to HTML
			html.appendChild(head);
			html.appendChild(body);

			// add TITLE element to HEAD
			head.appendChild(title);

			// add H1 element to BODY
			body.appendChild(h1);
			h1.setAttribute("class", h1ClassAttr);

			// add DIV element
			body.appendChild(div);
			div.setAttribute("class", divClassAttr);

			// add text nodes
			title.appendChild(titleText);
			h1.appendChild(heading);
			div.appendChild(bodyContent);

			tx.success();

		} catch (Exception ex) {

			logger.warn("", ex);

			logger.error(ex.toString());
			fail("Unexpected Exception");
		}

		assertTrue(page != null);
		assertTrue(page instanceof Page);

		try (final Tx tx = app.tx()) {

			Document doc = null;


			// Warm-up caches and JVM
			for (long i = 0; i < 50000; i++) {
				doc = Jsoup.connect(baseUri + pageName).get();
			}

			final long max = 1000;



			long t0 = System.currentTimeMillis();

			for (long i = 0; i < max; i++) {
				doc = Jsoup.connect(baseUri).get();
			}

			long t1 = System.currentTimeMillis();

			DecimalFormat decimalFormat = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
			Double time                 = (t1 - t0) / 1000.0;
			Double rate                 = max / ((t1 - t0) / 1000.0);

			logger.info("------> Time to render {} the base URI: {} seconds ({} per s)", new Object[] { max, decimalFormat.format(time), decimalFormat.format(rate) });

			assertFalse(doc.select("html").isEmpty());
			assertFalse(doc.select("html > head").isEmpty());
			assertFalse(doc.select("html > head > title").isEmpty());
			assertFalse(doc.select("html > body").isEmpty());

			assertEquals(doc.select("html > head > title").first().text(), pageTitle);

			Elements h1Elements = doc.select("html > body > h1");
			assertFalse(h1Elements.isEmpty());
			assertEquals(h1Elements.first().text(), pageTitle);
			assertEquals(h1Elements.first().attr("class"), h1ClassAttr);

			Elements divElements = doc.select("html > body > div");
			assertFalse(divElements.isEmpty());
			assertEquals(divElements.first().text(), bodyText);
			assertEquals(divElements.first().attr("class"), divClassAttr);



			t0 = System.currentTimeMillis();

			for (long i = 0; i < max; i++) {
				doc = Jsoup.connect(baseUri + pageName).get();
			}

			t1 = System.currentTimeMillis();

			time                 = (t1 - t0) / 1000.0;
			rate                 = max / ((t1 - t0) / 1000.0);

			logger.info("------> Time to render {} the test page by name: {} seconds ({} per s)", new Object[] { max, decimalFormat.format(time), decimalFormat.format(rate) });


			assertFalse(doc.select("html").isEmpty());
			assertFalse(doc.select("html > head").isEmpty());
			assertFalse(doc.select("html > head > title").isEmpty());
			assertFalse(doc.select("html > body").isEmpty());

			assertEquals(doc.select("html > head > title").first().text(), pageTitle);

			h1Elements = doc.select("html > body > h1");
			assertFalse(h1Elements.isEmpty());
			assertEquals(h1Elements.first().text(), pageTitle);
			assertEquals(h1Elements.first().attr("class"), h1ClassAttr);

			divElements = doc.select("html > body > div");
			assertFalse(divElements.isEmpty());
			assertEquals(divElements.first().text(), bodyText);
			assertEquals(divElements.first().attr("class"), divClassAttr);

			tx.success();

		} catch (Exception ex) {

			logger.warn("", ex);

			logger.error(ex.toString());
			fail("Unexpected Exception");

		}

	}

}

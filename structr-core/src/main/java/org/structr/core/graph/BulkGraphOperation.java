/**
 * Copyright (C) 2010-2014 Morgner UG (haftungsbeschränkt)
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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.core.graph;

import org.structr.common.SecurityContext;
import org.structr.common.error.FrameworkException;
import org.structr.core.GraphObject;

/**
 * Encapsulates a bulk graph operation.
 * 
 * @author Christian Morgner
 */
public interface BulkGraphOperation<T extends GraphObject> {
	
	public void handleGraphObject(SecurityContext securityContext, T obj) throws FrameworkException;
	public void handleThrowable(SecurityContext securityContext, Throwable t, T currentObject);
	public void handleTransactionFailure(SecurityContext securityContext, Throwable t);
}

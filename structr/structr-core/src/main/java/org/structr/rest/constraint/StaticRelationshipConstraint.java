/*
 *  Copyright (C) 2011 Axel Morgner
 * 
 *  This file is part of structr <http://structr.org>.
 * 
 *  structr is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  structr is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with structr.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.structr.rest.constraint;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;
import org.structr.core.GraphObject;
import org.structr.core.entity.DirectedRelationship;
import org.structr.core.entity.StructrRelationship;
import org.structr.rest.exception.IllegalPathException;
import org.structr.rest.exception.PathException;
import org.structr.core.EntityContext;
import org.structr.core.Services;
import org.structr.core.entity.AbstractNode;
import org.structr.core.node.CreateRelationshipCommand;
import org.structr.core.node.StructrTransaction;
import org.structr.core.node.TransactionCommand;
import org.structr.rest.RestMethodResult;
import org.structr.rest.VetoableGraphObjectListener;
import org.structr.rest.wrapper.PropertySet;

/**
 *
 * @author Christian Morgner
 */
public class StaticRelationshipConstraint extends FilterableConstraint {

	TypedIdConstraint typedIdConstraint = null;
	TypeConstraint typeConstraint = null;

	public StaticRelationshipConstraint(TypedIdConstraint typedIdConstraint, TypeConstraint typeConstraint) {
		this.securityContext = typeConstraint.securityContext;
		this.typedIdConstraint = typedIdConstraint;
		this.typeConstraint = typeConstraint;
	}

	@Override
	public List<GraphObject> doGet(List<VetoableGraphObjectListener> listeners) throws PathException {

		List<GraphObject> results = typedIdConstraint.doGet(listeners);
		if(results != null) {

			// get source and target type from previous constraints
			String sourceType = typedIdConstraint.getTypeConstraint().getType();
			String targetType = typeConstraint.getType();

			// fetch static relationship definition
			DirectedRelationship staticRel = EntityContext.getRelation(sourceType, targetType);
			if(staticRel != null) {

				LinkedList<GraphObject> transformedResults = new LinkedList<GraphObject>();
				for(GraphObject obj : results) {

					List<StructrRelationship> rels = obj.getRelationships(staticRel.getRelType(), staticRel.getDirection());
					if(staticRel.getDirection().equals(Direction.INCOMING)) {

						for(StructrRelationship rel : rels) {
							transformedResults.add(rel.getStartNode());
						}

					} else {

						for(StructrRelationship rel : rels) {
							transformedResults.add(rel.getEndNode());
						}
					}
				}
				
				// return related nodes
				return transformedResults;
			}
		}

		throw new IllegalPathException();
	}

	@Override
	public RestMethodResult doPost(PropertySet propertySet, List<VetoableGraphObjectListener> listeners) throws Throwable {

		final AbstractNode sourceNode = typedIdConstraint.getIdConstraint().getNode();
		final AbstractNode newNode = typeConstraint.createNode(propertySet);
		final DirectedRelationship rel = EntityContext.getRelation(sourceNode.getClass(), newNode.getClass());

		if(sourceNode != null && newNode != null && rel != null) {

			final RelationshipType relType = rel.getRelType();
			final Direction direction = rel.getDirection();

			// create transaction closure
			StructrTransaction transaction = new StructrTransaction() {

				@Override
				public Object execute() throws Throwable {
					if(direction.equals(Direction.OUTGOING)) {
						return Services.command(securityContext, CreateRelationshipCommand.class).execute(sourceNode, newNode, relType);
					} else {
						return Services.command(securityContext, CreateRelationshipCommand.class).execute(newNode, sourceNode, relType);
					}
				}
			};

			Services.command(securityContext, TransactionCommand.class).execute(transaction);
			if(transaction.getCause() != null) {
				throw transaction.getCause();
			}

			// TODO: set location header
			RestMethodResult result = new RestMethodResult(HttpServletResponse.SC_CREATED);
			// FIXME: result.addHeader("Location", buildCreatedURI(request, newNode.getType(), newNode.getId()));
			return result;
		}

		throw new IllegalPathException();
	}

	@Override
	public RestMethodResult doHead() throws Throwable {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public RestMethodResult doOptions() throws Throwable {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean checkAndConfigure(String part, HttpServletRequest request) {
		return false;
	}

	@Override
	public ResourceConstraint tryCombineWith(ResourceConstraint next) throws PathException {
		return super.tryCombineWith(next);
	}

	public TypedIdConstraint getTypedIdConstraint() {
		return typedIdConstraint;
	}

	public TypeConstraint getTypeConstraint() {
		return typeConstraint;
	}
}

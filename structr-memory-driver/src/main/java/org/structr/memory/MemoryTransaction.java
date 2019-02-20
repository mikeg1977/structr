/**
 * Copyright (C) 2010-2018 Structr GmbH
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
package org.structr.memory;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.structr.api.Transaction;
import org.structr.api.util.Iterables;

/**
 *
 * @author Christian Morgner
 */
public class MemoryTransaction implements Transaction {

	private static final AtomicLong idCounter = new AtomicLong();

	private final Map<MemoryIdentity, MemoryRelationship> createdRelationships = new LinkedHashMap<>();
	private final Map<MemoryIdentity, MemoryNode> createdNodes                 = new LinkedHashMap<>();
	private final Set<MemoryEntity> modifiedEntities                           = new LinkedHashSet<>();
	private final Set<MemoryIdentity> deletedRelationships                     = new LinkedHashSet<>();
	private final Set<MemoryIdentity> deletedNodes                             = new LinkedHashSet<>();
	private final long transactionId                                           = idCounter.incrementAndGet();
	private MemoryDatabaseService db                                           = null;
	private boolean success                                                    = false;

	public MemoryTransaction(final MemoryDatabaseService db) {
		this.db = db;
	}

	@Override
	public void failure() {
	}

	@Override
	public void success() {
		success = true;
	}

	@Override
	public long getTransactionId() {
		return transactionId;
	}

	@Override
	public void close() {

		if (success) {

			for (final MemoryEntity entity : modifiedEntities) {

				entity.commit(transactionId);
			}

			db.commitTransaction(createdNodes, createdRelationships, deletedNodes, deletedRelationships);

		} else {

			for (final MemoryEntity entity : modifiedEntities) {

				entity.rollback(transactionId);
			}

			db.rollbackTransaction();
		}

		createdNodes.values().stream().forEach(n -> n.unlock());
		createdRelationships.values().stream().forEach(r -> r.unlock());
	}

	public void create(final MemoryNode newNode) {
		createdNodes.put(newNode.getIdentity(), newNode);
	}

	public void create(final MemoryRelationship newRelationship) {

		// check for duplicate relationships
		if (Iterables.first(Iterables.filter(r -> r.isEqualTo(newRelationship), getRelationships())) != null) {
			throw new RuntimeException("Relationship already exists.");
		}

		createdRelationships.put(newRelationship.getIdentity(), newRelationship);
	}

	public void modify(final MemoryEntity entity) {
		modifiedEntities.add(entity);
	}

	public void delete(final MemoryNode toDelete) {
		deletedNodes.add(toDelete.getIdentity());
	}

	public void delete(final MemoryRelationship toDelete) {
		deletedRelationships.add(toDelete.getIdentity());
	}

	// ----- package-private methods -----
	Iterable<MemoryNode> getNodes() {

		final List<Iterable<MemoryNode>> sources = new LinkedList<>();

		sources.add(createdNodes.values());
		sources.add(db.getNodes());

		// return union of new and existing nodes, filtered for deleted nodes
		return Iterables.filter(n -> !deletedNodes.contains(n.getIdentity()), Iterables.flatten(sources));
	}

	Iterable<MemoryRelationship> getRelationships() {

		final List<Iterable<MemoryRelationship>> sources = new LinkedList<>();

		sources.add(createdRelationships.values());
		sources.add(db.getRelationships());

		// return union of new and existing nodes
		return Iterables.filter(r -> !deletedRelationships.contains(r.getIdentity()), Iterables.flatten(sources));
	}

	MemoryNode getNodeById(final MemoryIdentity id) {

		// deleted, dont return value
		if (deletedNodes.contains(id)) {
			return null;
		}

		MemoryNode candidate = createdNodes.get(id);
		if (candidate != null) {

			return candidate;
		}

		candidate = db.getNodeFromRepository(id);
		if (candidate != null) {

			return candidate;
		}

		return null;
	}

	MemoryRelationship getRelationshipById(final MemoryIdentity id) {

		// deleted, dont return value
		if (deletedRelationships.contains(id)) {
			return null;
		}

		MemoryRelationship candidate = createdRelationships.get(id);
		if (candidate != null) {

			return candidate;
		}

		candidate = db.getRelationshipFromRepository(id);
		if (candidate != null) {

			return candidate;
		}

		return null;
	}

	boolean isDeleted(final MemoryIdentity id) {
		return deletedNodes.contains(id) || deletedRelationships.contains(id);
	}

	boolean exists(final MemoryIdentity id) {
		return createdNodes.containsKey(id) || createdRelationships.containsKey(id) || db.exists(id);
	}
}
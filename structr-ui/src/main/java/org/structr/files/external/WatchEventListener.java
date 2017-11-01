/**
 * Copyright (C) 2010-2017 Structr GmbH
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
package org.structr.files.external;

import java.nio.file.Path;
import org.structr.common.error.FrameworkException;

/**
 * Listener iterface for directory watch events.
 */
public interface WatchEventListener {

	/**
	 * Called when an existing file was discovered during
	 * initialization.
	 *
	 * @param context the parent folder
	 * @param path the newly discovered file
	 */
	boolean onDiscover(final Path root, final Path context, final Path path) throws FrameworkException;

	/**
	 * Called when a new file is created in one of the
	 * directories watched by the watch service.
	 *
	 * @param context the parent folder
	 * @param path the newly created file
	 */
	boolean onCreate(final Path root, final Path context, final Path path) throws FrameworkException;

	/**
	 * Called when an existing file is modified in one of
	 * the directories watched by the watch service.
	 *
	 * @param context the parent folder
	 * @param path the modified file
	 */
	boolean onModify(final Path root, final Path context, final Path path) throws FrameworkException;

	/**
	 * Called when a file is deleted in one of the
	 * directories watched by the watch service.
	 *
	 * @param context the parent folder
	 * @param path the deleted file
	 */
	boolean onDelete(final Path root, final Path context, final Path path) throws FrameworkException;
}

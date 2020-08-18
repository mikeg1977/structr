/*
 * Copyright (C) 2010-2020 Structr GmbH
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
package org.structr.bpmn.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.mozilla.javascript.NativeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.structr.common.SecurityContext;
import org.structr.common.error.ErrorBuffer;
import org.structr.common.error.FrameworkException;
import org.structr.core.Export;
import org.structr.core.app.StructrApp;
import org.structr.core.entity.AbstractNode;
import org.structr.core.property.BooleanProperty;
import org.structr.core.property.EndNode;
import org.structr.core.property.ISO8601DateProperty;
import org.structr.core.property.Property;
import org.structr.core.property.PropertyKey;
import org.structr.core.property.StartNode;
import org.structr.core.property.StringProperty;

/**
 *
 */

public abstract class BPMNProcessStep<T> extends AbstractNode {

	private static final Logger logger                         = LoggerFactory.getLogger(BPMNProcessStep.class);
	public static final Property<BPMNProcessStep> nextStep     = new EndNode<>("nextStep", BPMNProcessStepNext.class);
	public static final Property<BPMNProcessStep> previousStep = new StartNode<>("previousStep", BPMNProcessStepNext.class);
	public static final Property<Boolean> isFinished           = new BooleanProperty("isFinished").indexed();
	public static final Property<Boolean> isManual             = new BooleanProperty("isManual").indexed();
	public static final Property<Boolean> isSuspended          = new BooleanProperty("isSuspended").indexed().hint("This flag can be used to manually suspend a process.");
	public static final Property<Date> dueDate                 = new ISO8601DateProperty("dueDate").indexed();
	public static final Property<String> statusText            = new StringProperty("statusText");

	private NativeObject internalState = null;

	public abstract T execute(final Map<String, Object> context);

	public String getStatusText() {

		final String statusFromDatabase = getProperty(statusText);
		if (statusFromDatabase != null) {

			return statusFromDatabase;
		}

		return "Unknown";
	}

	public boolean next(final T t) {

		try {

			final BPMNProcessStep nextStep = getNextStep(t);
			if (nextStep != null) {

				setProperty(BPMNProcessStep.nextStep, nextStep);

				return true;
			}

		} catch (FrameworkException fex) {
			logger.warn("Unable to assign next step for {} ({}): {}", getUuid(), getClass().getSimpleName(), fex.getMessage());
		}

		return false;
	}

	public BPMNProcessStep getNextStep(final Object data) {

		try {

			final PropertyKey nextKey = StructrApp.getConfiguration().getPropertyKeyForJSONName(getClass(), "next", false);
			if (nextKey != null) {

				final Class<BPMNProcessStep> nextType = nextKey.relatedType();
				if (nextType != null) {

					return StructrApp.getInstance(securityContext).create(nextType);
				}
			}

		} catch (FrameworkException fex) {
			logger.warn("Unable to determine next step for {} ({}): {}", getUuid(), getClass().getSimpleName(), fex.getMessage());
		}

		return null;
	}

	public boolean isSuspended() {
		return getProperty(isSuspended);
	}

	public boolean isFinished() {
		return getProperty(isFinished);
	}

	public void finish() {

		try {

			setProperty(isFinished, true);

		} catch (FrameworkException fex) {
			logger.warn("Unable to finish step for {} ({}): {}", getUuid(), getClass().getSimpleName(), fex.getMessage());
		}
	}

	public void suspend() {

		try {

			setProperty(isSuspended, true);

		} catch (FrameworkException fex) {
			logger.warn("Unable to suspend step for {} ({}): {}", getUuid(), getClass().getSimpleName(), fex.getMessage());
		}
	}

	public Object canBeExecuted(final SecurityContext securityContext, final Map<String, Object> parameters) throws FrameworkException {
		return true;
	}

	public boolean canBeExecuted() {

		try {

			final Object value = canBeExecuted(securityContext, new LinkedHashMap<>());
			if (value != null && value instanceof Boolean) {

				return (Boolean)value;
			}

			logger.warn("Return value of canBeExecuted() method must be of type boolean");

		} catch (FrameworkException fex) {
			logger.warn("Unable to execute canBeExecuted() method for {} ({}): {}", getUuid(), getClass().getSimpleName(), fex.getMessage());
		}

		return false;
	}

	@Override
	public void onCreation(final SecurityContext securityContext, final ErrorBuffer errorBuffer) throws FrameworkException {

		if (this instanceof BPMNProcess) {

			if (this instanceof BPMNInactive) {

				throw new FrameworkException(422, "BPMN process is disabled.");
			}


		} else {

			if (Boolean.TRUE.equals(securityContext.getAttribute("BPMNService"))) {

				// BPMNService is allowed to create instances of intermediate steps, do nothing

			} else {

				throw new FrameworkException(422, "BPMN process must be started with a BPMNStart instance.");
			}
		}
	}

	@Export
	public Map<String, Object> processData(final String optionalMode) {

		String mode = optionalMode;
		if (mode == null) {

			mode = "newest";
		}

		// This method iterates over all steps from this one to the start step and
		// collects all property values that are registered in the BPMN view,
		// according to the "mode" setting.

		final Map<String, Object> values = new LinkedHashMap<>();
		BPMNProcessStep current          = this;
		BPMNProcessStep prev             = null;
		int count                        = 0;

		do {

			prev = current.getProperty(previousStep);
			if (prev != null) {

				current = prev;
			}

			// prevent endless loop
			if (count++ > 10000) {
				logger.warn("Process step chain is longer than 10000, this is most likely an error. Aborting.");
				break;
			}

			if (current != null) {

				// collect properties from BPMN view
				final Set<PropertyKey> bpmnView = StructrApp.getConfiguration().getPropertySet(current.getClass(), "bpmn");
				if (bpmnView != null) {

					for (final PropertyKey key : bpmnView) {

						switch (mode) {
							case "newest":
								{
									// first (newest) value must be returned, since we're iterating backwards
									if (!values.containsKey(key.jsonName())) {

										final Object value = current.getProperty(key);
										if (value != null) {

											values.put(key.jsonName(), value);
										}
									}
								}
								break;

							case "oldest":
								{
									// easiest: older values overwrite newer values
									final Object value = current.getProperty(key);
									if (value != null) {

										values.put(key.jsonName(), value);
									}
								}
								break;

							case "all":
								{
									List<Object> list = (List)values.get(key.jsonName());
									if (list == null) {

										list = new LinkedList<>();
										values.put(key.jsonName(), list);
									}

									final Object value = current.getProperty(key);
									if (value != null) {

										list.add(value);
									}
								}

								break;
						}
					}
				}
			}

		} while (prev != null);

		return values;
	}

	@Export
	public Map<String, Object> getState(final SecurityContext securityContext) throws FrameworkException {
		return internalState;
	}

	public void loadState() {

		if (internalState == null) {

			internalState = new NativeObject();

			final BPMNProcess process = findProcess();
			if (process != null) {

				final Gson gson                = new GsonBuilder().create();
				final Map<String, Object> data = gson.fromJson(process.getProperty(BPMNProcess.state), Map.class);

				if (data != null) {

					for (final Entry<String, Object> entry : data.entrySet()) {

						internalState.put(entry.getKey(), internalState, entry.getValue());
					}
				}
			}
		}
	}

	public void saveState() {

		try {

			final BPMNProcess process = findProcess();
			if (process != null) {

				final Gson gson   = new GsonBuilder().create();
				final String data = gson.toJson(internalState);

				if (data != null) {

					process.setProperty(BPMNProcess.state, data);
				}
			}

		} catch (FrameworkException fex) {

			logger.warn("Unable to store state for {} ({}): {}", getUuid(), getClass().getSimpleName(), fex.getMessage());
		}
	}

	// ----- private methods -----
	private BPMNProcess findProcess() {

		BPMNProcessStep current = this;
		BPMNProcessStep prev    = null;
		int count                        = 0;

		do {

			prev = current.getProperty(previousStep);
			if (prev != null) {

				current = prev;
			}

			// prevent endless loop
			if (count++ > 10000) {
				logger.warn("Process step chain is longer than 10000, this is most likely an error. Aborting.");
				break;
			}

		} while (prev != null);

		if (current instanceof BPMNProcess) {

			return (BPMNProcess)current;
		}

		logger.warn("Unable to find process head, first element in chain is {} ({})", current.getClass().getSimpleName(), current.getUuid());

		return null;
	}
}

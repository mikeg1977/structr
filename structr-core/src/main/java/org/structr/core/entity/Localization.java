package org.structr.core.entity;

import org.structr.common.PropertyView;
import org.structr.common.View;
import org.structr.core.property.Property;
import org.structr.core.property.StringProperty;

/**
 *
 * @author Christian Morgner
 */
public class Localization extends AbstractNode {

	public static final Property<String> localizedName = new StringProperty("localizedName").indexed();
	public static final Property<String> domain        = new StringProperty("domain").indexed();

	public static final View defaultView = new View(Localization.class, PropertyView.Public,
		domain, name, localizedName
	);

	public static final View uiView = new View(Localization.class, PropertyView.Ui,
		domain, name, localizedName
	);
}

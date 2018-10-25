/**
 * Copyright (c) 2016, 2018 EclipseSource Services GmbH, Christian W. Damus, and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Martin Fleck (EclipseSource) - Initial API and implementation
 *   Christian W. Damus - adapted for UML Light
 */
package org.eclipse.papyrus.umllight.ui.simplification.internal.navigator;

import org.eclipse.ui.internal.navigator.resources.actions.OpenActionProvider;

/**
 * <p>
 * An Action Provider that provides the <em>Open</em> and <em>Open
 * With&hellip;</em> actions in the <em>Project Explorer</em>.
 * </p>
 * <p>
 * In the UML Light context, these actions would be provided by the action
 * provider of JDT, which overrides the action provider of the navigator plugin.
 * However, by hiding the JDT contributions through activities, we also lose the
 * action provider. This class restores the lost actions.
 * </p>
 */
@SuppressWarnings("restriction")
public class ProjectExplorerOpenActionProvider extends OpenActionProvider {

	/**
	 * Initializes me.
	 */
	public ProjectExplorerOpenActionProvider() {
		super();
	}

}
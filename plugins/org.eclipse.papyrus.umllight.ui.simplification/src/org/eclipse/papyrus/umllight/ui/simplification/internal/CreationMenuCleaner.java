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
 *   Christian W. Damus - adaptation for UML Light
 */
package org.eclipse.papyrus.umllight.ui.simplification.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.newchild.CreationMenuRegistry;
import org.eclipse.papyrus.infra.newchild.elementcreationmenumodel.Folder;

/**
 * Cleans the creation menu.
 */
public final class CreationMenuCleaner {

	private static final String UML_NEW_CHILD_MENU = "/resource/UML.creationmenumodel"; //$NON-NLS-1$
	private static final String UML_NEW_RELATIONSHIP_MENU = "/resource/UMLEdges.creationmenumodel"; //$NON-NLS-1$

	private static final List<String> DEACTIVATED_CHILD_MENUS = Arrays.asList(//
			UML_NEW_CHILD_MENU, //
			UML_NEW_RELATIONSHIP_MENU //
	);

	/**
	 * Not instantiable by clients.
	 */
	private CreationMenuCleaner() {
		super();
	}

	/**
	 * Cleans the creation menu.
	 * 
	 * @return status of the clean operation, with details of any problems
	 */
	public static IStatus clean() {
		Set<String> toDisable = new HashSet<>(DEACTIVATED_CHILD_MENUS);

		CreationMenuRegistry registry = CreationMenuRegistry.getInstance();
		for (Folder folder : registry.getRootFolder()) {
			for (String childMenuPath : DEACTIVATED_CHILD_MENUS) {
				if (folder.eResource().getURI().toString().endsWith(childMenuPath)) {
					toDisable.remove(childMenuPath);

					if (registry.getCreationMenuVisibility(folder)) {
						registry.setCreationMenuVisibility(folder, false);
					}
				}
			}
		}

		if (!toDisable.isEmpty()) {
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					NLS.bind("Some new-child menus not disabled: {0}", toDisable));
		}

		return Status.OK_STATUS;
	}
}
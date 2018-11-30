/*****************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.core.internal.provider;

import java.util.function.Consumer;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.papyrus.infra.gmfdiag.common.editpolicies.DefaultCreationEditPolicy;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.PackageEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateEditPart;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Edit-policy provider for the <em>UML Light</em> sequence diagram.
 */
public class StateMachineEditPolicyProvider extends AbstractUMLLightEditPolicyProvider {

	/**
	 * Initializes me.
	 */
	public StateMachineEditPolicyProvider() {
		super(PackageEditPart.MODEL_ID);
	}

	protected Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> createEditPolicyMap() {
		Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> result = ArrayListMultimap.create(1, 1);

		result.put(StateEditPart.class,
				ep -> ep.installEditPolicy(EditPolicyRoles.CREATION_ROLE, new DefaultCreationEditPolicy()));

		return result;
	}

}

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
import org.eclipse.papyrus.infra.gmfdiag.common.editpolicies.PapyrusConnectionHandleEditPolicy;
import org.eclipse.papyrus.infra.gmfdiag.common.editpolicies.PapyrusPopupBarEditPolicy;
import org.eclipse.papyrus.uml.diagram.sequence.edit.parts.AbstractMessageEditPart;
import org.eclipse.papyrus.uml.diagram.sequence.edit.parts.CInteractionInteractionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.sequence.edit.parts.SequenceDiagramEditPart;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Edit-policy provider for the <em>UML Light</em> sequence diagram.
 */
public class SequenceEditPolicyProvider extends AbstractUMLLightEditPolicyProvider {

	/**
	 * Initializes me.
	 */
	public SequenceEditPolicyProvider() {
		super(SequenceDiagramEditPart.MODEL_ID);
	}

	protected Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> createEditPolicyMap() {
		Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> result = ArrayListMultimap.create(1, 1);

		result.put(CInteractionInteractionCompartmentEditPart.class,
				ep -> ep.installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE, new PapyrusPopupBarEditPolicy()));
		result.put(AbstractMessageEditPart.class, ep -> ep.installEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE,
				new PapyrusConnectionHandleEditPolicy()));

		return result;
	}
}

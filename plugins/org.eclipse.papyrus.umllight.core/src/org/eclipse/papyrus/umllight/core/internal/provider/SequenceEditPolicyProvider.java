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

import static org.eclipse.papyrus.umllight.core.internal.UMLLightArchitecture.isUMLLight;

import java.util.Collection;
import java.util.function.Consumer;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.CreateEditPoliciesOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider;
import org.eclipse.gmf.runtime.notation.View;
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
public class SequenceEditPolicyProvider extends AbstractProvider implements IEditPolicyProvider {

	private final Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> editPolicies;

	/**
	 * Initializes me.
	 */
	public SequenceEditPolicyProvider() {
		super();

		editPolicies = createEditPolicyMap();
	}

	@Override
	public boolean provides(IOperation operation) {
		CreateEditPoliciesOperation epOperation = (CreateEditPoliciesOperation) operation;
		EditPart editPart = epOperation.getEditPart();
		if (!(editPart instanceof GraphicalEditPart) && !(editPart instanceof ConnectionEditPart)) {
			return false;
		}
		View notationView = (View) editPart.getModel();
		String diagramType = notationView.getDiagram().getType();
		return SequenceDiagramEditPart.MODEL_ID.equals(diagramType) && isUMLLight(notationView);
	}

	@Override
	public void createEditPolicies(EditPart editPart) {
		Collection<Consumer<? super EditPart>> processors = getProcessors(editPart.getClass());
		processors.forEach(p -> p.accept(editPart));
	}

	private Collection<Consumer<? super EditPart>> getProcessors(Class<? extends EditPart> class_) {
		if (!editPolicies.containsKey(class_) && EditPart.class.isAssignableFrom(class_.getSuperclass())) {
			// Look up superclass
			editPolicies.putAll(class_, getProcessors(class_.getSuperclass().asSubclass(EditPart.class)));
		}

		return editPolicies.get(class_);
	}

	private static Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> createEditPolicyMap() {
		Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> result = ArrayListMultimap.create(1, 1);

		result.put(CInteractionInteractionCompartmentEditPart.class,
				ep -> ep.installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE, new PapyrusPopupBarEditPolicy()));
		result.put(AbstractMessageEditPart.class, ep -> ep.installEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE,
				new PapyrusConnectionHandleEditPolicy()));

		return result;
	}
}

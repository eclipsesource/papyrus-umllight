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
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.CreateEditPoliciesOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import com.google.common.collect.Multimap;

/**
 * Abstract edit-policy provider for the <em>UML Light</em> diagrams.
 */
public abstract class AbstractUMLLightEditPolicyProvider extends AbstractProvider implements IEditPolicyProvider {

	private final String diagramID;
	private final Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> editPolicies;

	/**
	 * Initializes me with the ID of the diagram that I handle.
	 * 
	 * @param diagramID the notation type of the {@link Diagram} that I handle
	 */
	public AbstractUMLLightEditPolicyProvider(String diagramID) {
		super();

		this.diagramID = diagramID;
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
		return diagramID.equals(diagramType) && isUMLLight(notationView);
	}

	@Override
	public void createEditPolicies(EditPart editPart) {
		Collection<Consumer<? super EditPart>> processors = getProcessors(editPart.getClass());
		processors.forEach(p -> p.accept(editPart));
	}

	protected Collection<Consumer<? super EditPart>> getProcessors(Class<? extends EditPart> class_) {
		if (!editPolicies.containsKey(class_) && EditPart.class.isAssignableFrom(class_.getSuperclass())) {
			// Look up superclass
			editPolicies.putAll(class_, getProcessors(class_.getSuperclass().asSubclass(EditPart.class)));
		}

		return editPolicies.get(class_);
	}

	protected abstract Multimap<Class<? extends EditPart>, Consumer<? super EditPart>> createEditPolicyMap();
}

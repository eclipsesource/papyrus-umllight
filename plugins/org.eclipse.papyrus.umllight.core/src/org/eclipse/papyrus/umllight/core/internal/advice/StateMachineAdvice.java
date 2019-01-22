/*****************************************************************************
 * Copyright (c) 2018, 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.core.internal.advice;

import static org.eclipse.papyrus.uml.service.types.utils.ElementUtil.isTypeOf;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * Edit-helper advice for configuration and editing of {@link StateMachine}s.
 */
public class StateMachineAdvice extends AbstractNewChildAdvice {

	private final RegionElisionHelper region = new RegionElisionHelper();

	/**
	 * Initializes me.
	 */
	public StateMachineAdvice() {
		super();
	}

	@Override
	protected ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		return region.getBeforeEditContextCommand(request);
	}

	@Override
	protected void configureCreateRequest(CreateElementRequest request) {
		region.configureCreateRequest(request);
	}

	@Override
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		return new UMLSwitch<ICommand>() {
			@Override
			public ICommand caseStateMachine(StateMachine object) {
				return getAfterConfigureStateMachineCommand(request, object);
			}
		}.doSwitch(request.getElementToConfigure());
	}

	protected ICommand getAfterConfigureStateMachineCommand(ConfigureRequest request, StateMachine stateMachine) {

		return new AbstractTransactionalCommand(request.getEditingDomain(), "Create Region", null) {

			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				if (stateMachine.getRegions().isEmpty()) {
					stateMachine.createRegion("region");
				}
				return CommandResult.newOKCommandResult(stateMachine);
			}
		};
	}

	@Override
	protected boolean approveCreateRequest(CreateElementRequest request, EReference containment) {
		// We don't support composite states
		return (containment != UMLPackage.Literals.STATE__REGION)
				// We don't support constraints in state machines in UML Light
				&& !isTypeOf(UMLElementTypes.CONSTRAINT, request.getElementType());
	}

}

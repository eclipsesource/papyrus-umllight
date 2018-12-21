/*****************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.umllight.ui.simplification.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.papyrus.umllight.ui.simplification.internal.ContextConfigurator.Context;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.papyrus.umllight.ui.simplification"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * Initializes me.
	 */
	public Activator() {
		super();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		configureCreationMenus();
		ContextConfigurator.disableContext(Context.UML);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Obtains the shared instance.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private void configureCreationMenus() {
		// Do this on the UI thread because that's the context in which the creation
		// menu registry is generally accessed
		new UIJob("Initializing creation menus") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				return CreationMenuCleaner.clean();
			}
		}.schedule();
	}
	
	// 
	  // Nested types 
	  // 
	 
	  /** 
	   * An early startup hook that cleans the creation menu registry disables unwanted contexts. 
	   */ 
	  public static class Startup implements IStartup { 
	    @Override 
	    public void earlyStartup() { 
	      // Nothing really to do but kick the activator 
	      Activator.getDefault(); 
	    } 
	  }
}

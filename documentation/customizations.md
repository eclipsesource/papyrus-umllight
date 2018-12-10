# Papyrus UML Light Customizations

This document describes in detail the customizations applied to the _Papyrus UML_
framework to implement the _UML Light_ dialect as inspired by the _[OMG Certified UML
Professional 2™ (OCUP 2™): Foundation Level][ocup2]_ subset of the UML semantics and
diagrams.

Customizations are of three kinds:

- contributions (often in the form of models) to Papyrus extension points
- contributions to Eclipse Platform extension points, not particular Papyrus-specific,
and extension points of other Eclipse projects supporting Papyrus such as GMF
- unsupported work-arounds for extensibility considerations not provided for by
either Papyrus or the Eclipse Platform

These extension mechanisms will be dealt with severally in some detail.

[ocup2]: https://www.omg.org/ocup-2/coveragemap-found.htm

## Papyrus Extensions

The primary mechanism for specialization of _Papyrus_, especially as it pertains to
the implementation of a derivative language of UML, is the extension points provided
by the _Papyrus UML_ platform.  These are always preferred where possible.

### Architecture Model

Definition of a modeling language (custom DSML or subset of another language) begins
with an _Architecture Model_ for that language.  For _UML Light_, this is the
[UMLLight.architecture][archmodel] resource in the `org.eclipse.papyrus.umllight.core`
bundle.  This model defines the identity of the _UML Light_ language and, critically,
the diagram types (_representation kinds_) that visualize models in that language.
So, many of the other extensions flow from these, either directly by reference from
the architecture model or indirectly by their activation in the context of that
architecture.

For each representation kind in the language, the architecture model provides

- a name, unique identifier, icon, and brief description
- an implementation ID, which in the case of _UML Light_ is always the implementation
ID of the _Papyrus UML_ diagram editor on which the specialized diagram is based
- the command class that creates the diagram. Following the implementation ID, this is
the _Papyrus UML_ creation command class for the corresponding base diagram

The architecture model itself is contributed on the
`org.eclipse.papyrus.infra.architecture.models` extension point in the `plugin.xml`.

[archmodel]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/architecture/UMLLight.architecture

### Palette Definition Models

Each diagram type (_representation kind_) in the _UML Light_ architecture model
references a palette definition model that lays out the tools in the diagram editor's
palette.  For example, the [UMLLightClassDiagram.paletteconfiguration][classpal] resource
is essentially a subset of the _Papyrus UML_ palette for the class diagram, presenting
only tools for the subset of class modeling concepts in the _UML Light_ dialect.

[classpal]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/palette/UMLLightClassDiagram.paletteconfiguration

### Diagram Assistant Models

A close corollary of the palette definition model is the diagram assistant model.  Unlike
the former, this model is not reference by the architecture model.  Instead, the
association with the _UML Light_ language is made in two parts:

- contribution of the assistant model (if different to what _Papyrus UML_ provides) on
the `org.eclipse.papyrus.infra.gmfdiag.assistant.modelProviders` extension point
- in the representation kind definition in the architecture model, _assistant rules_
referencing individual element-types by ID to either include or exclude them in the
diagram editor. It is significant that these rules identify the specific _visual element
types_ (hinted with the notation view type) and not the more general semantic types

Custom assistant models are only required where for some reason it is not sufficient
just to reuse assistants defined in _Papyrus UML_ and filter them in the representation
kind definition in the architecture model.  So, for example, the _UML Light_ state machine
diagram re-uses the _Papyrus UML_ assistants for that diagram as they are, with rules
in the architecture model and other extensions described below to restrict which
assistants are presented in the diagram.  However, _UML Light_ does contribute an
[UMLLightUseCase.assistants][usecaseasst] model to replace the _classifier as subject_
tool provided by _Papyrus UML_ with a more specific _class as subject_ tool for the
restricted semantics of the use case diagram.

[usecaseasst]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/assistant/UMLLightUseCase.assistants

### Creation Menu Models

In the _Papyrus UML_ platform, the **New Child** and **New Relationship** context menus
in the **Model Explorer** view are defined by models contributed on the
`org.eclipse.papyrus.infra.newchild` extension point.  There is no connection between
this extension point and the _architecture model_.  This has two important consequences
for applications such as _UML Light_ in defining custom modeling languages:

- the architecture model cannot define inclusion/exclusion rules for the creation menus
as it does for modeling assistants to re-use or filter the menus defined by _Papyrus UML_
- enablement of creation menus is under the control of the user via a preference page
(_Papyrus ⟶ New Child Menu_)

Following the convention established by _Papyrus UML_, the _UML Light_ product defines
separate menus for the **New Child** menu ([UMLLightNewChild.creationmenumodel][newchild])
and for the **New Relationship** menu
([UMLLightNewRelationship.creationmenumodel][newrel]).  An unsupported work-around (see
below) is employed to avoid redundancy of these menus with the full Papyrus menus.

[newchild]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/creationmenu/UMLLightNewChild.creationmenumodel
[newrel]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/creationmenu/UMLLightNewRelationship.creationmenumodel

### Element Type Configuration Models

Some applications extending _Papyrus UML_ will have to define custom element types (as
implemented in GMF) for their language, possibly in addition to reusing those provided
by Papyrus already.  Such is the case for _UML Light_ because, although it is strictly
a subset of the UML, in order to support the peculiar restrictions required by the light
diagrams, it is necessary to define new element types that _Papyrus UML_ does not need.

Following the precedent set by Papyrus, new element types are partitioned into
_semantic types_ in the [UMLLight.elementypesconfigurations][types] model and _visual
types_ (encapsulating hints for notation view types) in the
[UMLLightDI.elementtypesconfigurations][ditypes] model.  The visual types include a
reinterpretation of the concept of "subject" for the light use case diagram that is
more constrained than in _Papyrus UML_.  The semantic types include specific types for
each of the kinds of `Pseudostate` supported in _UML Light_ state machines in order to
present them individually in the **New Child** menu (whereas in _Papyrus UML_ they are
all unified in the single pseudostate type).

Another common use case for the element-types configuration model is the contribution of
_edit-helper advice_ in the application's architecture context.  The _UML Light_
architecture makes use of this in the semantic types configuration, defining several
advices that (in _UML Light_ models only, via the architecture context) constrain the
kinds of elements that can be created in various other elements.  This effectively
narrows the options presented to the user in

- modeling assistants (reducing the need for custom assistant models)
- **New Child** menu (for which filters otherwise can be cumbersome)
- creation of new elements in the diagram by palette tools that are applicable only
in some containers
- drag-and-drop from the **Model Explorer** view into the diagram

[types]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/types/UMLLight.elementtypesconfigurations
[ditypes]:https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/types/UMLLightDI.elementtypesconfigurations

### CSS Styles

The final link from the architecture model description of a _representation kind_ is
its reference (by URL) to a CSS style resource to provide a custom visual styling of
the diagram.  For all of the _UML Light_ diagrams, the [umllight_style.css][cssfile]
defines a consistent theme with pleasing blue colours, soft corners, and drop shadows.

[cssfile]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.core/resource/style/umllight_style.css

## Papyrus Substrate Extensions

Many of the _Papyrus UML_ extensions points are built on top of its own substrate,
especially a number of cases where Papyrus plugs in a generic implementation of some
GMF extension point and either delegates the behaviour of those extensions to further
extensions or the generic extension is configured by means of a model.  The latter case
is exemplified by the _element types configuration models_, _diagram assistant models_,
and _palette configuration models_.  Most diagram editor customizations that might be
implemented on GMF have such Papyrus layers that are preferred.  However, there are
some lower-level (Eclipse Platform) extension points that are relevant to Papyrus
customization and are employed for _UML Light_.

### UI Capabilities (Activities)

The technologies that _Papyrus UML_ is built on, such as Xtext and QVTo in particular,
pull in dependencies such as JDT and PDE that are distractions for modellers using _UML
Light_.  Accordingly, the `org.eclipse.papyrus.umllight.ui.simplification`
[bundle][activities] defines a number of _capabilities_ that are disabled by default, for
the purpose of reducing the amount of Eclipse functionality that is presented to the user.
This includes low-level concerns such as EMF editors and code generation, tangentially
related concerns such as QVTo and OCL development, and essentially unrelated concerns such
as Java and plug-in development.  Also included in this scheme is an _Advanced Papyrus
Functionality_ capability that by default hides _Papyrus Toolsmith_ concerns such as
properties view customization, architecture/viewpoints tooling, and table definition.

The `org.eclipse.papyrus.umllight.rcp` [bundle][rcp] contributes the **Capabilities** preference
page that gives users access to all of these capabilities in case they are needed for
advanced use cases.

One unintended consequence of the filtering out of all JDT capabilities is that the
**Project Explorer** view's **Open** and **Open With…** menu is actually provided by JDT,
but users of Papyrus-based modelling products unsurprisingly depend on them.  To that
end, the `org.eclipse.papyrus.umllight.ui.simplification` bundle itself re-contributes
these actions via the [`ProjectExplorerOpenActionprovider`][openactions] class on the
`org.eclipse.ui.navigator.navigatorContent` extension point.

[activities]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.ui.simplification/plugin.xml
[rcp]: https://github.com/eclipsesource/papyrus-umllight/blob/master/releng/org.eclipse.papyrus.umllight.rcp/plugin.xml
[openactions]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.ui.simplification/src/org/eclipse/papyrus/umllight/ui/simplification/internal/navigator/ProjectExplorerOpenActionProvider.java

### Perspective Extensions

Also in the `org.eclipse.papyrus.umllight.ui.simplification` bundle are perspective
extension for the _Papyrus_ perspective that hide menu items and toolbar items not needed
in the _UML Light_ context, such as those related to tables and launching programs.

### New Wizards

Related to perspective extensions is the registration of custom _New Model_ and _New
Project_ wizards for the _UML Light_ language, and suppression of their analogues from
_Papyrus UML_.  The `org.eclipse.papyrus.umllight.ui` bundle registers custom wizards
of these two kinds that

- omit the architecture context selection, because it's implicitly _UML Light_
- simplify the representation kinds page by omitting the template/transformation
selection and profile application widgets

thereby significantly streamlining the creation of models.  Perspective extensions are
employed to make these new wizards prominent as well as to hide the _Papyrus UML_ wizards
that they are intended to supplant.

### Edit-policy Providers

Two of the more fundamental extension points in GMF are the _edit-part providers_ and the
_edit-policy providers_, which are used by the platform to contribute the editing behavior
in every diagram.  Unlike some other GMF extension points, these are not recast by Papyrus
using models, although the _Papyrus UML_ diagrams do employ _GMF Tooling_ models to
generate these (to a large extent).

In the case of the _UML Light_ language, the subsetting of the editing behavior of the
diagrams in a few instances requires overrides at the edit-policy level.  For example,
in the state machine diagram to disable the creation of composite states by using the
`State` palette tool to create a state within an existing state, which is implemented in
an edit-policy that entirely by-passes the GMF _edit-helper_ (element types) framework.
To that end, the `org.eclipse.papyrus.umllight.core` bundle contributes a GMF edit-policy
provider for the state machine diagram.  It also plugs in an edit-policy provider for
the sequence diagram to add the diagram assistant policies to shapes in the diagram on
which the _Papyrus UML_ implementation of the diagram does not install them.

## Unsupported Work-arounds

The final category of customization extensions, and the mechanisms of last resort, are
extensions that modify the behavior of the platform in unsupported ways.  These are
unsupported because they depend on internal/private details of the implementations that
they alter or because the platform does not expect those implementations to be changed,
and so may become unstable, perhaps in future updates.

### Start-up Extensions

The definition of custom **New Child** and **New Relationship** menus in the explorer
leads to a problem of apparent duplication of these menus:  one with a large variety of
options and one much lighter menu with the _UML Light_-appropriate options.  The solution
to this that was pioneered by the [_Papyrus for Information Modeling_][p4im] project is
a start-up extension that [disables the default creation menus][menucleaner] in the
preferences.

The start-up extension triggers activation of the _UML Light_ UI bundle, but at least it
can generally be expected to run before the user is able to interact in any way with
the **Model Explorer** view and with open models.

[p4im]: https://wiki.eclipse.org/Papyrus_for_Information_Modeling/Customization_Guide
[menucleaner]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.ui/src/org/eclipse/papyrus/umllight/ui/internal/newchild/CreationMenuCleaner.java

### Equinox Transform Hooks

Possibly the most invasive (and potentially dangerous) extension mechanism is the
[_Equinox Transform Hook_][equinox].  This can be used to transform any resource deployed
in the OSGi framework, but in the context of _Papyrus UML_ product customization, the most
interesting is the registration of XSLT transformations for `plugin.xml` resources.

The `org.eclipse.papyrus.umllight.ui.simplification.transforms` bundle registers a single
simple [XSLT transformation][tablexslt] targeting a specific extension manifest in Papyrus
to remove the contribution of the **New Table** menu in the **Model Explorer** view at the
level of the Eclipse extension registry.  This is a last resort to work around the failure
of other supported filtering mechanisms (capabilities and perspective extensions) that
should be able to support the suppression of this menu but do not.

[equinox]: https://wiki.eclipse.org/Equinox_Transforms
[tablexslt]: https://github.com/eclipsesource/papyrus-umllight/blob/master/plugins/org.eclipse.papyrus.umllight.ui.simplification.transforms/transforms/nattable.xslt

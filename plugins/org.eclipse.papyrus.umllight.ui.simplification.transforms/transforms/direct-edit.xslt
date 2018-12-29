 <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
     <!-- Add a constraint to the direct editor to apply only to UML models -->
     <xsl:template match="DirectEditor">
         <xsl:copy>
             <xsl:attribute name="additionalConstraint">org.eclipse.papyrus.umllight.ui/org.eclipse.papyrus.umllight.ui.internal.directedit.IsNotUMLLightConstraint</xsl:attribute>
             <xsl:apply-templates select="node()|@*"/>
         </xsl:copy>
     </xsl:template>
     
     <!-- Everything else -->
     <xsl:template match="node()|@*">
         <xsl:copy>
             <xsl:apply-templates select="node()|@*"/>
         </xsl:copy>
     </xsl:template>
 </xsl:stylesheet>

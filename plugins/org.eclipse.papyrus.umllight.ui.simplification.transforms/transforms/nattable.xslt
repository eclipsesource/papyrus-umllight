 <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
     <!-- Suppress the contribution of the 'New Table' menu in the Model Explorer -->
     <xsl:template match="extension[@id='papyrus.table.menuspapyrus.table.menu']">
     </xsl:template>
     
     <!-- Everything else -->
     <xsl:template match="node()|@*">
         <xsl:copy>
             <xsl:apply-templates select="node()|@*"/>
         </xsl:copy>
     </xsl:template>
 </xsl:stylesheet>

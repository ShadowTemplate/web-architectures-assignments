<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="ISO-8859-1"/>
<xsl:strip-space elements="*"/>

<xsl:template match="TAXONOMY/AREA/UNITS/UNIT">
    <!-- wrap unit name into "" to avoid problem with csv -->
    <xsl:text>"</xsl:text>
    <xsl:value-of select="UNIT_NAME"/>
    <xsl:text>",</xsl:text>
    <xsl:value-of select="@TYPE" />
    <xsl:text>,</xsl:text>
    <xsl:value-of select="TIME"/>
    <!-- print line feed -->
    <xsl:text>&#xa;</xsl:text>
</xsl:template>

<!-- discard other areas fields -->
<xsl:template match="TAXONOMY/AREA/AREA_NAME"/>
<xsl:template match="TAXONOMY/AREA/SHORT_NAME"/>
<xsl:template match="TAXONOMY/AREA/DESCRIPTION"/>

</xsl:stylesheet>

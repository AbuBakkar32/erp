<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">

	<xsl:template match="landpersonreport">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<xsl:variable name="pageid" select="generate-id()" />

			<!-- PAGE SETUP -->
			<fo:layout-master-set>
				<!-- PAGE MASTER -->
				<fo:simple-page-master master-name="A4" page-height="29.02cm" page-width="21cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="25mm" margin-left="0mm" margin-right="0mm" margin-bottom="5mm" />
					<fo:region-before region-name="header-first" extent="25mm"/>
					<fo:region-after region-name="footer-pagenumber" extent="4.5mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="A4-rest" page-height="29.02cm" page-width="21cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="0mm" margin-left="0mm" margin-right="0mm" margin-bottom="5mm" />
					<fo:region-after region-name="footer-pagenumber" extent="4.5mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="A4-last" page-height="29.02cm" page-width="21cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="0mm" margin-left="0mm" margin-right="0mm" margin-bottom="5mm" />
					<fo:region-after region-name="footer-pagenumber" extent="4.5mm"/>
				</fo:simple-page-master>

				<!-- PAGE SEQUENCE DECLARATION -->
				<fo:page-sequence-master master-name="document">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference page-position="first" master-reference="A4" />
						<fo:conditional-page-master-reference page-position="rest" master-reference="A4-rest" />
						<fo:conditional-page-master-reference page-position="last" master-reference="A4-last" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<!-- PAGE SEQUENCE -->
			<fo:page-sequence master-reference="document">

				<!-- PAGE HEADER (STATIC CONTENT) -->
				<fo:static-content flow-name="header-first">
					<fo:block-container height="18mm" width="18mm" right="0mm" position="absolute">
						<fo:block>
							<xsl:variable name="imagepath" select="reportLogo" />
							<fo:external-graphic padding="0" margin="0" space-start="0" space-end="0" pause-before="0" pause-after="0" content-height="18mm" content-width="18mm" scaling="non-uniform" src="url('resources/bussinesslogo/{$imagepath}')"/>
						</fo:block>
					</fo:block-container>

					<fo:block-container width="100%" border-bottom ="1px solid #000000" >
						<fo:block text-align="center" font-size="20px" font-weight="bold">
							<xsl:value-of select="businessName"/>
						</fo:block>
						<fo:block text-align="center" font-size="9px" margin-top="1px" font-style="italic">
							<xsl:value-of select="businessAddress"/>
						</fo:block>
						<fo:block text-align="center" font-size="12px" font-weight="bold" margin-top="4px">
							<xsl:value-of select="reportName"/>
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- FOOTER PAGE NUMBER -->
				<fo:static-content flow-name="footer-pagenumber">
					<fo:block-container position="absolute" width="40%">
						<fo:block text-align="left" font-size="8px">
							Page <fo:page-number/> of <fo:page-number-citation ref-id="{$pageid}"/>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="40%" width="20%">
						<fo:block text-align="center" font-size="8px">
							<xsl:value-of select="copyrightText"/>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="60%" width="40%">
						<fo:block text-align="right" font-size="8px">
							Printed Date : <xsl:value-of select="printDate"/>
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- BODY CONTENT -->
 				<fo:flow flow-name="xsl-region-body">
					<fo:block-container width="100%" right="0mm" margin-top="1px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="15%" />
								<fo:table-column column-width="2%" />
								<fo:table-column column-width="83%" />

								<fo:table-body>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Person Id</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xperson"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Person Name</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xname"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Father Name</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xfname"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Mother Name</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xmname"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Occupation</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xoccupation"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Contact Number</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xcontact"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Email</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xemail"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Phone Number</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xphone"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Present Address</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xmadd"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Permanent Address</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xpadd"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Date</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="fromDate"/></fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>

					<xsl:if test="educations/education">
					<fo:block-container width="100%" right="0mm" margin-top="10px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="10%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="6"><fo:block text-align="center">Education Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Year</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Exam</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Institude</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Session</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Major</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Result</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(educations/education)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="4">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="educations/education">
										<xsl:apply-templates select="educations/education"/>
									</xsl:if>
								</fo:table-body>

							</fo:table>
						</fo:block>
					</fo:block-container>
					</xsl:if>

					<xsl:if test="experiences/experience">
					<fo:block-container width="100%" right="0mm" margin-top="20px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="5"><fo:block text-align="center">Experience Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Title</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Designation</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Experience Type</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Type</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Duration(Year)</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(experiences/experience)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="4">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="experiences/experience">
										<xsl:apply-templates select="experiences/experience"/>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					</xsl:if>

					<xsl:if test="documents/document">
					<fo:block-container width="100%" right="0mm" margin-top="20px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="33%" />
								<fo:table-column column-width="33%" />
								<fo:table-column column-width="34%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="3"><fo:block text-align="center">Document Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Document ID</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Document Type</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Document</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(documents/document)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="4">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="documents/document">
										<xsl:apply-templates select="documents/document"/>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					</xsl:if>

					<fo:block id="{$pageid}" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- Education table template -->
	<xsl:template match="educations/education">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xyear"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xexam"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xinstitude"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xsession"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xmajor"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xresult"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
	
	<!-- Experience table template -->
	<xsl:template match="experiences/experience">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xname"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xdesignation"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xtypetrn"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xtype"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xduration"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Document table template -->
	<xsl:template match="documents/document">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xdoc"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xdoctype"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xnameold"/></fo:block>
			</fo:table-cell>
			</fo:table-row>
	</xsl:template>


	<!-- stylesheets -->
	<xsl:attribute-set name="border.full">
		<xsl:attribute name="border">1px solid #000000</xsl:attribute>
		<xsl:attribute name="float">left</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table.font.size">
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="dealer.table.td">
		<xsl:attribute name="padding-top">2px</xsl:attribute>
		<xsl:attribute name="padding-bottom">2px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="client.table.th">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
		<xsl:attribute name="background-color">#DDDDDD</xsl:attribute>
		<xsl:attribute name="border">1px solid #000000</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="client.table.tf">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="border">1pt solid #000000</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="client.table.td">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="border">1pt solid #000000</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="client.table.td.noborder">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="category.table.td">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>
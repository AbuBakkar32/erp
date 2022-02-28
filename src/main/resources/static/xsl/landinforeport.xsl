<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">

	<xsl:template match="landinforeport">
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
						<fo:block text-align="left" font-size="7px" font-weight="bold"  margin-top="2px" padding-bottom="5px">
							Date: <xsl:value-of select="fromDate"/>
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
					<fo:block-container width="50%" left="0%" margin-top="10px" margin-bottom="60px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
								<fo:table-column column-width="25%" />
								<fo:table-column column-width="2%" />
								<fo:table-column column-width="73%" />

								<fo:table-body>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land ID</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xland"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Name</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlname"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Block</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xblock"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Road</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xroad"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandqty"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Unit</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandunit"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Deduction for Road(%)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlanddedroad"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Deduction for Others(%)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlanddedother"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Amt.(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandqtyd"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Amt.(Katha)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandqtyk"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Total Deduction Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xtotded"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Remaining Land Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xtotrem"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>River Side Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xriversideqty"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>River Side Unit</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xriversideunit"/></fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>

					<fo:block-container width="50%" left="50%" top="10px" position="absolute">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="25%" />
								<fo:table-column column-width="2%" />
								<fo:table-column column-width="73%" />

								<fo:table-body>
									
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Member/Director</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xcus"/>-<xsl:value-of select="xmemname"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Land Ref.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandparent"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Previous Land Note</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xnote2"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Status</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xstatus"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Record Date</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="fromDate"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Price of Deducted Land</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xtotdedprice"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Total Receivable Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xamtar"/></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:if test="xamtrv &gt; 0">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Total Received Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xamtrv"/></fo:block></fo:table-cell>
									</fo:table-row>
									</xsl:if>
									<xsl:if test="xamtrv &lt; 0">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Total Received Amt.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>(<xsl:value-of select="xamtrv * -1"/>)</fo:block></fo:table-cell>
									</fo:table-row>
									</xsl:if>
									<xsl:if test="(xamtar + xamtrv) &lt; 0">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Net Receivable=</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>(<xsl:value-of select="(xamtar + xamtrv) * -1"/>)</fo:block></fo:table-cell>
									</fo:table-row>
									</xsl:if>
									<xsl:if test="(xamtar + xamtrv) &gt; 0">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Net Receivable:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xamtar + xamtrv"/></fo:block></fo:table-cell>
									</fo:table-row>
									</xsl:if>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Remarks</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xnote"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row text-align="right" font-size="20px" font-weight="bold">
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Survey Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Gross Land Amt.(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandqtydsg"/></fo:block></fo:table-cell>
									</fo:table-row>
									
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Gross Deviation(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<xsl:if test="xlandqtydsg &gt; 0">
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="format-number(xlandqtyd - xlandqtydsg,'#.00')"/></fo:block></fo:table-cell>
										</xsl:if>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Net Land Amt.(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xlandqtydsn"/></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Net Deviation Amt.(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<xsl:if test="xlandqtydsn &gt; 0">
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="format-number(xtotrem -xlandqtydsn,'#.00')"/></fo:block></fo:table-cell>
										</xsl:if>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Survey By</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<xsl:if test="xsurveyor">
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xsurveyor"/>-<xsl:value-of select="surveyorName"/></fo:block></fo:table-cell>
										</xsl:if>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>Survey Date</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.td.noborder"><fo:block><xsl:value-of select="xdatesrv"/></fo:block></fo:table-cell>
									</fo:table-row>
									</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>

					<xsl:if test="owners/owner">
					<fo:block-container width="100%" right="0mm" margin-top="20px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="35%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />
								<fo:table-column column-width="20%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="5"><fo:block text-align="center">Owner Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>SL</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Person</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Owner Type</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Land Amount</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Unit</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(owners/owner)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="4">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="owners/owner">
										<xsl:apply-templates select="owners/owner"/>
									</xsl:if>
								</fo:table-body>

							</fo:table>
						</fo:block>
					</fo:block-container>
					</xsl:if>


					<xsl:if test="dags/dag">
					<fo:block-container width="100%" right="0mm" margin-top="20px" >
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="19%" />
								<fo:table-column column-width="19%" />
								<fo:table-column column-width="19%" />
								<fo:table-column column-width="19%" />
								<fo:table-column column-width="19%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="6"><fo:block text-align="center">Dag Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>SL</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Dag Type</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Dag No.</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>In/Out</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Land Amount</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Unit</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(dags/dag)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="5">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="dags/dag">
										<xsl:apply-templates select="dags/dag"/>
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
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="31%" />
								<fo:table-column column-width="30%" />
								<fo:table-column column-width="34%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="4"><fo:block text-align="center">Document Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>SL</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Document ID</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Document Type</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Document</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(documents/document)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="3">
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

					<xsl:if test="(activities/activity)">
					<fo:block-container width="100%" right="0mm" margin-top="20px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="25%" />
								<fo:table-column column-width="15%" />
								<fo:table-column column-width="15%" />
								<fo:table-column column-width="15%" />
								<fo:table-column column-width="25%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="6"><fo:block text-align="center">Activities Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>SL</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Person</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Date</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Place</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Priority</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Comment</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(activities/activity)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="6">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="activities/activity">
										<xsl:apply-templates select="activities/activity"/>
									</xsl:if>
								</fo:table-body>

							</fo:table>
						</fo:block>
					</fo:block-container>
					</xsl:if>

					<xsl:if test="surveys/survey">
					<fo:block-container width="100%" right="0mm" margin-top="20px">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="18%" />
								<fo:table-column column-width="13%" />
								<fo:table-column column-width="12%" />
								<fo:table-column column-width="12%" />
								<fo:table-column column-width="30%" />
								<fo:table-column column-width="10%" />

								<fo:table-header>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="7"><fo:block text-align="center">Survey Details</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>SL</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Survey By</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Survey Date</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Gross Land Amt.(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Net Land Amt.(Decimal)</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Survey Details</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Status</fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>

								<fo:table-body>
									<xsl:if test="not(surveys/survey)">
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="4">
												<fo:block>No Records</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="surveys/survey">
										<xsl:apply-templates select="surveys/survey"/>
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

	<!-- Owner table template -->
	<xsl:template match="owners/owner">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:number/>. <xsl/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xperson"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xtype"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xqty"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xunit"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Dag table template -->
	<xsl:template match="dags/dag">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:number/>. <xsl/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xdagtype"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xdagnum"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xtype"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xqty"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xunit"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Document table template -->
	<xsl:template match="documents/document">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:number/>. <xsl/></fo:block>
			</fo:table-cell>
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

	<!-- Activities table template -->
	<xsl:template match="activities/activity">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:number/>. <xsl/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xperson"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="formatedDate"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xplace"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xtype"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xnote"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Survey table template -->
	<xsl:template match="surveys/survey">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:number/>. <xsl/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xsurveyor"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="formatedDate"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xlandqtydsg"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xlandqtydsn"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xnote"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xstatus"/></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>


	<!-- Item table template -->
	<xsl:template match="batchdetails/batchdetail">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="5">
				<fo:block></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block><xsl:value-of select="xitem"/> - <xsl:value-of select="xdesc"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block><xsl:value-of select="xqtyreq"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block><xsl:value-of select="xunit"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block><xsl:value-of select="xtype"/></fo:block>
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
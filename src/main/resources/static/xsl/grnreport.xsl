<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">

	<xsl:template match="grnreport">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<xsl:variable name="pageid" select="generate-id()" />

			<!-- PAGE SETUP -->
			<fo:layout-master-set>
				<!-- PAGE MASTER -->
				<fo:simple-page-master master-name="A4" page-height="29.02cm" page-width="21cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="18mm" margin-left="0mm" margin-right="0mm" margin-bottom="5mm" />
					<fo:region-before region-name="header-first" extent="18mm"/>
					<fo:region-after region-name="footer-pagenumber" extent="4.5mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="A4-rest" page-height="21cm" page-width="29.02cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="0mm" margin-left="0mm" margin-right="0mm" margin-bottom="5mm" />
					<fo:region-after region-name="footer-pagenumber" extent="4.5mm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="A4-last" page-height="21cm" page-width="29.02cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
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

					<fo:block-container width="100%" border-bottom ="1pt solid #000000" >
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
					<fo:block-container position="absolute" width="30%">
						<fo:block text-align="left" font-size="8px">
							Page <fo:page-number/> of <fo:page-number-citation ref-id="{$pageid}"/>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="30%" width="40%">
						<fo:block text-align="center" font-size="8px">
							<xsl:value-of select="copyrightText"/>
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="70%" width="30%">
						<fo:block text-align="right" font-size="8px">
							Printed Date : <xsl:value-of select="printDate"/>
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- BODY CONTENT -->
 				<fo:flow flow-name="xsl-region-body">
					<fo:block-container width="100%" margin-top="0px" right="0mm">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="100%" />

								<fo:table-body>
									<xsl:apply-templates select="grnorders/grnorder"/>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<fo:block id="{$pageid}" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- Category table template -->
	<xsl:template match="grnorders/grnorder">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="category.table.td">
				<fo:block-container width="50%" left="0%" margin-top="10px" margin-bottom="30px">
					<fo:block font-size="8px" padding-top="10px" padding-bottom="5px" text-align="left">
						<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
							<fo:table-column column-width="25%" />
							<fo:table-column column-width="2%" />
							<fo:table-column column-width="73%" />
	
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell><fo:block font-weight="bold">GRN Number</fo:block></fo:table-cell>
									<fo:table-cell><fo:block font-weight="bold">:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="orderNumber"/></fo:block></fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>Date</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block><xsl:value-of select="date"/></fo:block></fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>Supplier</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<xsl:if test="supplier">
										<fo:table-cell><fo:block><xsl:value-of select="supplier"/> - <xsl:value-of select="supplierName"/></fo:block></fo:table-cell>
									</xsl:if>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>Project/Business</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<xsl:if test="xhwh">
										<fo:table-cell><fo:block><xsl:value-of select="xhwh"/>-<xsl:value-of select="projectName"/></fo:block></fo:table-cell>
									</xsl:if>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>Store/Site</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<xsl:if test="warehouse">
										<fo:table-cell><fo:block><xsl:value-of select="warehouse"/>-<xsl:value-of select="storename"/></fo:block></fo:table-cell>
									</xsl:if>
								</fo:table-row>
								<xsl:if test="xtype='Other'">
								<fo:table-row>
									<fo:table-cell><fo:block>GRN By</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<xsl:if test="xpreparer">
										<fo:table-cell><fo:block><xsl:value-of select="xpreparer"/>-<xsl:value-of select="xpreparername"/></fo:block></fo:table-cell>
									</xsl:if>
								</fo:table-row>
								</xsl:if>
								<fo:table-row>
									<fo:table-cell><fo:block>Supplier Bill No.</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block><xsl:value-of select="xinvnum"/></fo:block></fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:block-container>
				<fo:block-container width="50%" left="50%" top="10px" margin-bottom="30px" position="absolute">
					<fo:block font-size="8px" padding-top="10px" padding-bottom="5px" text-align="left">
						<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
							<fo:table-column column-width="25%"/>
							<fo:table-column column-width="2%" />
							<fo:table-column column-width="73%" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell><fo:block>Voucher No.</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<xsl:if test="xvoucher">
										<fo:table-cell><fo:block><xsl:value-of select="xvoucher"/></fo:block></fo:table-cell>
									</xsl:if>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>Purchase Type</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block><xsl:value-of select="xtype"/></fo:block></fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>Total Amount</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block><xsl:value-of select="totalAmount"/></fo:block></fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell><fo:block>GRN Status</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block><xsl:value-of select="xstatusgrn"/></fo:block></fo:table-cell>
								</fo:table-row>
								<xsl:if test="poNumber">
									<fo:table-row>
										<fo:table-cell><fo:block>PO Number</fo:block></fo:table-cell>
										<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block><xsl:value-of select="poNumber"/></fo:block></fo:table-cell>
									</fo:table-row>
								</xsl:if>
								<fo:table-row>
									<fo:table-cell><fo:block>Note</fo:block></fo:table-cell>
									<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
									<fo:table-cell><fo:block><xsl:value-of select="xnote"/></fo:block></fo:table-cell>
								</fo:table-row>
								
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:block-container>

				<!-- Item table -->
				<fo:block>
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
						<fo:table-column column-width="5%"/>
						<fo:table-column column-width="20%"/>
						<fo:table-column column-width="10%"/>
						<fo:table-column column-width="7%"/>
						<fo:table-column column-width="5%"/>
						<fo:table-column column-width="11%"/>
						<fo:table-column column-width="7%"/>
						<fo:table-column column-width="11%"/>
						<fo:table-column column-width="12%"/>
						<fo:table-column column-width="12%"/>

						<!-- Table header -->
						<fo:table-header xsl:use-attribute-sets="table.font.size">
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="client.table.th">
									<fo:block>Row</fo:block>
								</fo:table-cell> 
								<fo:table-cell xsl:use-attribute-sets="client.table.th">
									<fo:block>Item</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th">
									<fo:block>Purpose</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Qty.</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Rate</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center">
									<fo:block>Purchase Unit</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center">
									<fo:block>Vat (%)</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>CF(Purchase)</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Note</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Line Amount</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>

						<!-- table body -->
						<fo:table-body>
							<xsl:if test="items/item">
								<xsl:apply-templates select="items/item"/>
							</xsl:if>
							<xsl:if test="not(items/item)">
								<fo:table-row>
									<fo:table-cell number-columns-spanned="8" xsl:use-attribute-sets="client.table.td">
										<fo:block>No Items found</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</xsl:if>
							
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right" font-weight="bold" number-columns-spanned="9">
									<fo:block>Total</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right" font-weight="bold" >
									<fo:block><xsl:value-of select="totalAmount"/></fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>

				<fo:block font-size="8px" padding-top="10px" padding-bottom="5px" text-align="left">
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
						<fo:table-column column-width="20%"/>
						<fo:table-column column-width="2%"/>
						<fo:table-column column-width="78%"/>

						<fo:table-body>
							<fo:table-row>
								<fo:table-cell><fo:block font-weight="bold">Total Line Amount in Words</fo:block></fo:table-cell>
								<fo:table-cell font-weight="bold"><fo:block>:</fo:block></fo:table-cell>
								<fo:table-cell  text-align="left" font-weight="bold">
									<fo:block><xsl:value-of select="spellword"/> <xsl:if test="xprimeword"> and </xsl:if><xsl:value-of select="xprimeword"/> Taka Only </fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>

			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Item table template -->
	<xsl:template match="items/item">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block>
					<xsl:number/>. <xsl/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block>
					<xsl:value-of select="itemCode"/>-<xsl:value-of select="itemName"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block>
					<xsl:value-of select="xpurpose"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="itemQty"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="itemRate"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemUnit"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="xvatamt"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="xcfpur"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="xnote"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="itemTotalAmount"/>
				</fo:block>
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
		<xsl:attribute name="background-color">#DDDDDD</xsl:attribute>
		<xsl:attribute name="border">1pt solid #000000</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
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
	<xsl:attribute-set name="category.table.td">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>
<?xml version="1.0" encoding="UTF-8"?>
<!--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-->
<!--!                                                              !-->
<!--!    File Name    : RM0301.xsl                                 !-->
<!--!    Description  : Purchase Order Report                      !-->
<!--!    Author       : Zubayer Ahamed                             !-->
<!--!    Date         : 08-APR-2021                                !-->
<!--!    Copyright    : Copyright (c) ASL, 2021                    !-->
<!--!                                                              !-->
<!--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-->

<xsl:stylesheet 
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">

	<xsl:template match="RM0301">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<xsl:variable name="pageid" select="generate-id()" />

			<!-- PAGE SETUP -->
			<fo:layout-master-set>
				<!-- PAGE MASTER -->
				<fo:simple-page-master master-name="A4" page-height="21cm" page-width="29.02cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="25mm" margin-left="0mm" margin-right="0mm" margin-bottom="5mm" />
					<fo:region-before region-name="header-first" extent="25mm"/>
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
						<fo:block text-align="left" font-size="7px" font-weight="bold"  margin-top="2px" padding-bottom="5px">
							Date: <xsl:value-of select="fromDate"/> to <xsl:value-of select="toDate"/>
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
					<fo:block-container width="100%" right="0mm">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse" margin-top="5px">
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="12%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="13%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />

								<fo:table-body>
									<xsl:if test="suppliers/supplier">
										<xsl:apply-templates select="suppliers/supplier"/>
									</xsl:if>
									<xsl:if test="not(suppliers/supplier)">
										<fo:table-row xsl:use-attribute-sets="supplier.group.th.tr">
											<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="11"><fo:block text-align="right" font-weight="bold">No records</fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>

					<fo:block-container width="100%" right="0mm">
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse" margin-top="5px">
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="12%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="10%" />
								<fo:table-column column-width="13%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />
								<fo:table-column column-width="8%" />

								<fo:table-body>
									<fo:table-row xsl:use-attribute-sets="supplier.group.th.tr">
										<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="6" number-rows-spanned="2"><fo:block text-align="right">Grand Total</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Ordered</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Purchased</fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right"></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="center"></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Amount</fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row xsl:use-attribute-sets="supplier.group.th.tr">
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalQtyOrder"/></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalQtyPurchased"/></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block></fo:block></fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalAmount"/></fo:block></fo:table-cell>
									</fo:table-row>
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
	<xsl:template match="suppliers/supplier">
		<!-- SUPPLIER INFO -->
		<fo:table-row xsl:use-attribute-sets="supplier.group.th.tr">
			<fo:table-cell xsl:use-attribute-sets="supplier.group.th.tr.tc">
				<fo:block><xsl:value-of select="supplierCode"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="supplier.group.th.tr.tc">
				<fo:block><xsl:value-of select="supplierName"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="supplier.group.th.tr.tc" number-columns-spanned="9">
				<fo:block><xsl:value-of select="supplierAddress"/></fo:block>
			</fo:table-cell>
		</fo:table-row>

		<!-- ORDER INFO -->
		<fo:table-row xsl:use-attribute-sets="supplier.group.td.tr">
			<fo:table-cell xsl:use-attribute-sets="supplier.group.td.tr.tc" number-columns-spanned="11">
				<fo:block>
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="12%" />
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="5%" />
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="13%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />

						<fo:table-body>
							<xsl:if test="orders/order">
								<xsl:apply-templates select="orders/order"/>
							</xsl:if>
							<xsl:if test="not(orders/order)">
								<fo:table-row xsl:use-attribute-sets="supplier.group.th.tr">
									<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="11"><fo:block text-align="right" font-weight="bold">No records</fo:block></fo:table-cell>
								</fo:table-row>
							</xsl:if>
						</fo:table-body>
					</fo:table>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row xsl:use-attribute-sets="supplier.group.td.tr.total">
			<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="6"><fo:block text-align="right" font-weight="bold">Supplier Total</fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalQtyOrder"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalQtyPurchased"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalAmount"/></fo:block></fo:table-cell>
		</fo:table-row>
		<fo:table-row xsl:use-attribute-sets="supplier.group.td.tr.divider">
			<fo:table-cell number-columns-spanned="11"><fo:block></fo:block></fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Orders table template -->
	<xsl:template match="orders/order">
		<fo:table-row xsl:use-attribute-sets="table.tr.left-border">
			<fo:table-cell xsl:use-attribute-sets="table.tr.left-border.td">
				<fo:block font-weight="bold"><xsl:value-of select="orderNumber"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="table.tr.left-border.td">
				<fo:block font-weight="bold"><xsl:value-of select="orderDate"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="table.tr.left-border.td">
				<fo:block font-weight="bold"><xsl:value-of select="status"/></fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="8">
				<fo:block text-align="center">Items</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row>
			<fo:table-cell number-columns-spanned="11">
				<fo:block>
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="12%" />
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="5%" />
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="13%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="8%" />

						<fo:table-header>
							<fo:table-row xsl:use-attribute-sets="table.tr.left-border">
								<fo:table-cell><fo:block></fo:block></fo:table-cell>
								<fo:table-cell><fo:block></fo:block></fo:table-cell>
								<fo:table-cell><fo:block></fo:block></fo:table-cell>
	
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="center">SL</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Code</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block>Name</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Ordered</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Purchased</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Rate</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="center">Unit</fo:block></fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th"><fo:block text-align="right">Amount</fo:block></fo:table-cell>
							</fo:table-row>
						</fo:table-header>
	
						<fo:table-body>
							<xsl:if test="items/item">
								<xsl:apply-templates select="items/item"/>
							</xsl:if>
							<xsl:if test="not(items/item)">
								<fo:table-row xsl:use-attribute-sets="supplier.group.th.tr">
									<fo:table-cell xsl:use-attribute-sets="client.table.th" number-columns-spanned="11"><fo:block text-align="right" font-weight="bold">No records</fo:block></fo:table-cell>
								</fo:table-row>
							</xsl:if>
							
						</fo:table-body>
					</fo:table>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row xsl:use-attribute-sets="supplier.group.td.tr.total">
			<fo:table-cell xsl:use-attribute-sets="client.table.td" number-columns-spanned="6"><fo:block text-align="right" font-weight="bold">Order Total</fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalQtyOrder"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalQtyPurchased"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right" font-weight="bold"><xsl:value-of select="totalAmount"/></fo:block></fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Items table -->
	<xsl:template match="items/item">
		<fo:table-row xsl:use-attribute-sets="table.tr.left-border">
			<fo:table-cell><fo:block></fo:block></fo:table-cell>
			<fo:table-cell><fo:block></fo:block></fo:table-cell>
			<fo:table-cell><fo:block></fo:block></fo:table-cell>

			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="center"><xsl:value-of select="sl"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block><xsl:value-of select="itemCode"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block><xsl:value-of select="itemName"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right"><xsl:value-of select="qtyOrder"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right"><xsl:value-of select="qtyPurchased"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right"><xsl:value-of select="rate"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="center"><xsl:value-of select="unit"/></fo:block></fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td"><fo:block text-align="right"><xsl:value-of select="amount"/></fo:block></fo:table-cell>
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
	<xsl:attribute-set name="category.table.td">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="supplier.group.th.tr">
		<xsl:attribute name="border">1px solid #000000</xsl:attribute>
		<xsl:attribute name="background-color">#DDDDDD</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="supplier.group.th.tr.tc">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="supplier.group.td.tr">
	</xsl:attribute-set>
	<xsl:attribute-set name="supplier.group.td.tr.divider">
		<xsl:attribute name="height">15px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="supplier.group.td.tr.total">
		<xsl:attribute name="background-color">#eee</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="supplier.group.td.tr.tc">
	</xsl:attribute-set>
	<xsl:attribute-set name="table.tr.left-border">
		<xsl:attribute name="border-left">1px solid #000</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table.tr.left-border.td">
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">

	<xsl:template match="chalanreportupdate">
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

					<fo:block-container width="100%" margin-top="15px" border-bottom ="2pt solid #000000" >
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
							Date: <xsl:value-of select="fromDate"/> To <xsl:value-of select="toDate"/>
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
					<fo:block-container width="100%" margin-top="20px" right="0mm">
					<fo:block-container width="50%" margin-top="20px" right="0mm" padding-top="2mm">
						<fo:block font-weight="bold" font-size="10px" text-align="left">
							Chalan: <xsl:value-of select="chalanNumber"/>
						</fo:block>
						<fo:block font-weight="bold" font-size="10px" text-align="left">
							Chalan Date: <xsl:value-of select="chalanDate"/>
						</fo:block>
					</fo:block-container>
						<fo:block>
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
								<fo:table-column column-width="100%" />
										
								<fo:table-body>
									<xsl:apply-templates select="salesorders/salesorder"/>
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
      <xsl:template match="salesorders/salesorder">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="category.table.td" margin-top="10px">
				
				<fo:block font-weight="bold" font-size="8px" padding-top="30px" padding-bottom="5px" text-align="left">
					Sales Order: <xsl:value-of select="orderNumber"/>
				</fo:block>
				<fo:block font-weight="bold" font-size="8px" padding-bottom="5px" text-align="left">
					Customer: <xsl:value-of select="customer"/>
				</fo:block>
				<fo:block font-weight="bold" font-size="8px" padding-bottom="5px" text-align="left">
					Address: <xsl:value-of select="customerAddress"/>
				</fo:block>
				
				<!-- Item table -->
				<fo:block>
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
						<fo:table-column column-width="15%"/>
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="10%" />
						<fo:table-column column-width="10%"/>
						<fo:table-column column-width="10%"/>
						<fo:table-column column-width="10%"/>
						
						<!-- Table header -->
						<fo:table-header xsl:use-attribute-sets="table.font.size" font-weight="bold">
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Code</fo:block>
								</fo:table-cell> 
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>Item Name</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Qty</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Unit</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Category</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Group</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Rate</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center" background-color="gray" color="white">
									<fo:block>item Total Amount</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>

					<!-- table body -->
						<fo:table-body>
							<xsl:apply-templates select="items/item"/>
						</fo:table-body>
					</fo:table>
				</fo:block>
				<fo:block-container width="100%" margin-top="10px" margin-left="0px">
						<fo:block font-weight="bold" padding-top="8px" font-size="10px" text-align="right" margin-right="110px">Grand Total</fo:block>
						<fo:block padding-top="8px">
							<fo:table table-layout="fixed" width="100%" border-collapse="collapse" xsl:use-attribute-sets="table.font.size">
								<fo:table-column column-width="80%" />
								<fo:table-column column-width="5%" />
								<fo:table-column column-width="15%" />

								<fo:table-body>
									<fo:table-row>
										<fo:table-cell text-align="right">
											<fo:block font-weight="bold">Total Amount</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left" margin-left="10px">
											<fo:block>:</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left">
											<fo:block color="black"><xsl:value-of select="totalAmount"/></fo:block>
										</fo:table-cell>
									</fo:table-row>

									<fo:table-row>
										<fo:table-cell text-align="right">
											<fo:block font-weight="bold">Vat Amount</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left" margin-left="10px">
											<fo:block>:</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left">
											<fo:block><xsl:value-of select="vatAmount"/></fo:block>
										</fo:table-cell>
									</fo:table-row>
									
								    <fo:table-row>
										<fo:table-cell text-align="right">
											<fo:block font-weight="bold">Discount Amount</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left" margin-left="10px">
											<fo:block>:</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left">
											<fo:block><xsl:value-of select="discountAmount"/></fo:block>
										</fo:table-cell>
									</fo:table-row>
									
									 <fo:table-row>
										<fo:table-cell text-align="right">
											<fo:block font-weight="bold">Grand Total Amount</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left" margin-left="10px">
											<fo:block>:</fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="left">
											<fo:block><xsl:value-of select="grandTotalAmount"/></fo:block>
										</fo:table-cell>
									</fo:table-row>

								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Item table template -->
	<xsl:template match="items/item">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemCode"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemName"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemQty"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemUnit"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemCategory"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemGroup"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemRate"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
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
	</xsl:attribute-set>
	<xsl:attribute-set name="category.table.td">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>
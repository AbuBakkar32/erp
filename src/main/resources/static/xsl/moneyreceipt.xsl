<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">

	<xsl:template match="moneyreceipt">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<xsl:variable name="pageid" select="generate-id()" />

			<!-- PAGE SETUP -->
			<fo:layout-master-set>
				<!-- PAGE MASTER -->
				<fo:simple-page-master master-name="A4" page-height="29.02cm" page-width="21cm" margin-top="6mm" margin-bottom="6mm" margin-left="6mm" margin-right="6mm">
					<fo:region-body margin-top="0mm" margin-left="0mm" margin-right="0mm" margin-bottom="0mm" />
					<fo:region-before region-name="header-first" extent="0mm"/>
					<fo:region-after region-name="footer-pagenumber" extent="0mm"/>
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
					<fo:block-container>
						<fo:block></fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- FOOTER PAGE NUMBER -->
				<fo:static-content flow-name="footer-pagenumber">
					<fo:block-container position="absolute">
						<fo:block text-align="left" font-size="8px">
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- BODY CONTENT -->
 				<fo:flow flow-name="xsl-region-body">
					<fo:block-container height="50%" position="absolute"  border-bottom="1px solid black">

						<fo:block-container height="18mm" width="18mm" right="0mm" position="absolute">
							<fo:block>
								<xsl:variable name="imagepath" select="reportLogo" />
								<fo:external-graphic padding="0" margin="0" space-start="0" space-end="0" pause-before="0" pause-after="0" content-height="18mm" content-width="18mm" scaling="non-uniform" src="url('resources/bussinesslogo/{$imagepath}')"/>
							</fo:block>
						</fo:block-container>
	
						<fo:block-container width="100%" position="absolute" height="100px">
							<fo:block font-size="20px" font-weight="bold">
								<xsl:value-of select="businessName"/>
							</fo:block>
							<fo:block font-size="9px" margin-top="1px" font-style="italic">
								<xsl:value-of select="businessAddress"/>
							</fo:block>
							<fo:block font-size="9px" margin-top="1px" font-style="italic">
								Phone : <xsl:value-of select="phone"/>
								<xsl:if test="fax">
									, Fax: <xsl:value-of select="fax"/>
								</xsl:if>
							</fo:block>
							<fo:block text-align="center" font-size="12px" font-weight="bold" margin-top="20px">
								<xsl:value-of select="reportName"/> (Office Copy)
							</fo:block>
							<fo:block text-align="right" font-size="7px" margin-top="2px" padding-bottom="5px">
								Date: <xsl:value-of select="xdate"/>
							</fo:block>
						</fo:block-container>
	
						<fo:block-container width="100%" height="180px" top="95px" right="0mm" position="absolute">
							<fo:block>
								<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="2%" />
									<fo:table-column column-width="68%" />
	
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>M.R. Number</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xvoucher"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td" number-columns-spanned="3"><fo:block font-weight="bold">Received with thanks from : </fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Customer ID</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xcus"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Customer Name</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xorg"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Address</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xmadd"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Amount</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block font-weight="bold">TK <xsl:value-of select="xprime"/> /=</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>The Amount of TAKA (In Word)</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="spellword"/> <xsl:if test="xprimeword"> and </xsl:if><xsl:value-of select="xprimeword"/> Taka Only </fo:block></fo:table-cell>
										</fo:table-row>
										
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
	
						<fo:block-container width="40%" top="340px" left="0%" padding-top="5px" position="absolute" border-top="1px solid black">
							<fo:block>For <xsl:value-of select="businessName"/></fo:block>
						</fo:block-container>

						<fo:block-container width="50%" top="300px" left="50%" position="absolute">
							<fo:block>
								<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="2%" />
									<fo:table-column column-width="68%" />
									
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Payment Type</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xpaymenttype"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Reference</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xref"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Bank</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xbank"/> - <xsl:value-of select="bankname"/></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>

						<fo:block-container width="100%" top="365px" left="0%" position="absolute">
							<fo:block text-align="left" font-size="8px">
								Printed Date : <xsl:value-of select="printDate"/>
							</fo:block>
						</fo:block-container>

					</fo:block-container>

					<fo:block-container top="52%" height="50%" position="absolute">

						<fo:block-container height="18mm" width="18mm" right="0mm" position="absolute">
							<fo:block>
								<xsl:variable name="imagepath" select="reportLogo" />
								<fo:external-graphic padding="0" margin="0" space-start="0" space-end="0" pause-before="0" pause-after="0" content-height="18mm" content-width="18mm" scaling="non-uniform" src="url('resources/bussinesslogo/{$imagepath}')"/>
							</fo:block>
						</fo:block-container>
	
						<fo:block-container width="100%" position="absolute" height="100px">
							<fo:block font-size="20px" font-weight="bold">
								<xsl:value-of select="businessName"/>
							</fo:block>
							<fo:block font-size="9px" margin-top="1px" font-style="italic">
								<xsl:value-of select="businessAddress"/>
							</fo:block>
							<fo:block font-size="9px" margin-top="1px" font-style="italic">
								Phone : <xsl:value-of select="phone"/>
								<xsl:if test="fax">
									, Fax: <xsl:value-of select="fax"/>
								</xsl:if>
							</fo:block>
							<fo:block text-align="center" font-size="12px" font-weight="bold" margin-top="20px">
								<xsl:value-of select="reportName"/> (Customer Copy)
							</fo:block>
							<fo:block text-align="right" font-size="7px" margin-top="2px" padding-bottom="5px">
								Date: <xsl:value-of select="xdate"/>
							</fo:block>
						</fo:block-container>
	
						<fo:block-container width="100%" height="180px" top="95px" right="0mm" position="absolute">
							<fo:block>
								<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="2%" />
									<fo:table-column column-width="68%" />
	
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>M.R. Number</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xvoucher"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td" number-columns-spanned="3"><fo:block font-weight="bold">Received with thanks from : </fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Customer ID</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xcus"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Customer Name</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xorg"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Address</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xmadd"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Amount</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block font-weight="bold">TK <xsl:value-of select="xprime"/> /=</fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>The Amount of TAKA (In Word)</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="spellword"/> <xsl:if test="xprimeword"> and </xsl:if><xsl:value-of select="xprimeword"/> Taka Only </fo:block></fo:table-cell>
										</fo:table-row>
										
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>
	
						<fo:block-container width="40%" top="340px" left="0%" padding-top="5px" position="absolute" border-top="1px solid black">
							<fo:block>Customer Signature</fo:block>
						</fo:block-container>

						<fo:block-container width="50%" top="300px" left="50%" position="absolute">
							<fo:block>
								<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="2%" />
									<fo:table-column column-width="68%" />
									
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Payment Type</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xpaymenttype"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Reference</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xref"/></fo:block></fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>Bank</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block>:</fo:block></fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="category.table.td"><fo:block><xsl:value-of select="xbank"/> - <xsl:value-of select="bankname"/></fo:block></fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:block-container>

						<fo:block-container width="100%" top="365px" left="0%" position="absolute">
							<fo:block text-align="left" font-size="8px">
								Printed Date : <xsl:value-of select="printDate"/>
							</fo:block>
						</fo:block-container>

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

				<fo:block  font-size="8px" padding-top="30px" padding-bottom="5px" text-align="left">
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
						<fo:table-column column-width="15%"/>
						<fo:table-column column-width="2%" />
						<fo:table-column column-width="83%" />

						<fo:table-body>
							<fo:table-row>
								<fo:table-cell><fo:block>Sales Number</fo:block></fo:table-cell>
								<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
								<fo:table-cell><fo:block><xsl:value-of select="orderNumber"/></fo:block></fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell><fo:block>Customer</fo:block></fo:table-cell>
								<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
								<fo:table-cell><fo:block><xsl:value-of select="reqBranch"/> - <xsl:value-of select="customer"/></fo:block></fo:table-cell>
							</fo:table-row>
							<fo:table-row>
								<fo:table-cell><fo:block>Date</fo:block></fo:table-cell>
								<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
								<fo:table-cell><fo:block><xsl:value-of select="date"/></fo:block></fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>

				<!-- Item table -->
				<fo:block>
					<fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
						<fo:table-column column-width="20%"/>
						<fo:table-column column-width="35%" />
						<fo:table-column column-width="8%" />
						<fo:table-column column-width="15%" />
						<fo:table-column column-width="8%"/>
						<fo:table-column column-width="14%"/>

						<!-- Table header -->
						<fo:table-header xsl:use-attribute-sets="table.font.size" font-weight="bold">
							<fo:table-row>
								<fo:table-cell xsl:use-attribute-sets="client.table.th">
									<fo:block>Item Code</fo:block>
								</fo:table-cell> 
								<fo:table-cell xsl:use-attribute-sets="client.table.th">
									<fo:block>Item Name</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Qty</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="center">
									<fo:block>Unit</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Rate</fo:block>
								</fo:table-cell>
								<fo:table-cell xsl:use-attribute-sets="client.table.th" text-align="right">
									<fo:block>Total Amount</fo:block>
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
						</fo:table-body>
					</fo:table>
				</fo:block>

				<fo:block-container width="100%" margin-top="10px" margin-left="0px">
					<fo:block padding-top="8px">
						<fo:table table-layout="fixed" width="100%" border-collapse="collapse" xsl:use-attribute-sets="table.font.size">
							<fo:table-column column-width="80%" />
							<fo:table-column column-width="5%" />
							<fo:table-column column-width="10%" />

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

								<!--<fo:table-row>
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
								</fo:table-row>-->

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
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block>
					<xsl:value-of select="itemCode"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td">
				<fo:block>
					<xsl:value-of select="itemName"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="itemQty"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="center">
				<fo:block>
					<xsl:value-of select="itemUnit"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="client.table.td" text-align="right">
				<fo:block>
					<xsl:value-of select="itemRate"/>
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
		<xsl:attribute name="font-size">12px</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="border">1pt solid #000000</xsl:attribute>
		<xsl:attribute name="padding-left">5px</xsl:attribute>
		<xsl:attribute name="padding-right">5px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="category.table.td">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="padding">5px</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>
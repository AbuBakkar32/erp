package com.asl.service.report.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.model.report.RM0301;
import com.asl.model.report.RM0301Item;
import com.asl.model.report.RM0301PurchaseOrder;
import com.asl.model.report.RM0301Report;
import com.asl.model.report.RM0301Supplier;
import com.asl.service.PoordService;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0611Service")
public class RM0611ServiceImpl extends AbstractReportService {

	@Autowired
	private PoordService poordService;
	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		options.add(new DropdownOption("Open", "Open"));
		options.add(new DropdownOption("Confirmed", "Confirmed"));
		options.add(new DropdownOption("GRN Created", "GRN Created"));
		options.add(new DropdownOption("Full Received", "Full Receipt"));
		
		List<Xcodes> statusList = xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE);
		List<DropdownOption> wh = new ArrayList<>();
		wh.add(new DropdownOption("", "-- Select --"));
		statusList.stream().forEach(x -> wh.add(new DropdownOption(x.getXcode(), x.getXcode() + "-" + x.getXlong())));
		
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// fwh
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "From Warehouse", wh, "", false));
		
		// twh
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "To Warehouse", wh, "", false));

		// Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Status", options, "", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {

		String fdate = (String) reportParams.get("FDATE");
		String tdate = (String) reportParams.get("TDATE");
		String xcus = (String) reportParams.get("XCUS");
		String xstatuspor = (String) reportParams.get("XSTATUSPOR");
		String xitem = (String) reportParams.get("XITEM");
		
		SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yyyy"); 		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd"); 		
		fdate = sdf2.format(sdf3.parse(fdate)); 		
		tdate = sdf2.format(sdf3.parse(tdate));

		List<RM0301> list = poordService.getRM0301(fdate, tdate, xcus, xstatuspor, xitem);
		if(list == null || list.isEmpty()) return new byte[0];

		RM0301Report report = new RM0301Report();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Purchase Order");
		report.setFromDate(SDF.format(sdf2.parse(fdate)));
		report.setToDate(SDF.format(sdf2.parse(tdate)));
		report.setPrintDate(SDF.format(new Date()));
		report.setCopyrightText("");
		report.setTotalQtyOrder(BigDecimal.ZERO);
		report.setTotalQtyPurchased(BigDecimal.ZERO);
		report.setTotalAmount(BigDecimal.ZERO);

		list.sort(Comparator.comparing(RM0301::getXcus)
				.thenComparing(Comparator.comparing(RM0301::getXpornum))
				.thenComparing(Comparator.comparing(RM0301::getXitem)));


		Map<String, RM0301Supplier> supMap = new HashMap<>();
		Map<String, RM0301PurchaseOrder> orderMap = new HashMap<>();
		int itemsl = 1;
		for(RM0301 data : list) {

			if(supMap.get(data.getXcus()) != null) {
				RM0301Supplier sup = supMap.get(data.getXcus());

				if(orderMap.get(data.getXpornum()) != null) {
					RM0301PurchaseOrder order = orderMap.get(data.getXpornum());

					RM0301Item item = new RM0301Item();
					item.setSl(itemsl);
					item.setItemCode(data.getXitem());
					item.setItemName(data.getXdesc());
					item.setQtyOrder(data.getXqtyord() != null ? data.getXqtyord() : BigDecimal.ZERO);
					item.setQtyPurchased(data.getXqtygrn() != null ? data.getXqtygrn() : BigDecimal.ZERO);
					item.setRate(data.getXrate() != null ? data.getXrate() : BigDecimal.ZERO);
					item.setUnit(data.getXunitpur());
					item.setAmount(data.getXlineamt() != null ? data.getXlineamt() : BigDecimal.ZERO);
					itemsl++;
					order.getItems().add(item);

					order.setTotalQtyOrder(order.getTotalQtyOrder().add(item.getQtyOrder()));
					order.setTotalQtyPurchased(order.getTotalQtyPurchased().add(item.getQtyPurchased()));
					order.setTotalAmount(order.getTotalAmount().add(item.getAmount()));
				} else {
					RM0301PurchaseOrder order = new RM0301PurchaseOrder();
					order.setOrderNumber(data.getXpornum());
					order.setOrderDate(SDF.format(data.getXdate()));
					order.setStatus(data.getXstatuspor());

					itemsl = 1;
					RM0301Item item = new RM0301Item();
					item.setSl(itemsl);
					item.setItemCode(data.getXitem());
					item.setItemName(data.getXdesc());
					item.setQtyOrder(data.getXqtyord() != null ? data.getXqtyord() : BigDecimal.ZERO);
					item.setQtyPurchased(data.getXqtygrn() != null ? data.getXqtygrn() : BigDecimal.ZERO);
					item.setRate(data.getXrate() != null ? data.getXrate() : BigDecimal.ZERO);
					item.setUnit(data.getXunitpur());
					item.setAmount(data.getXlineamt() != null ? data.getXlineamt() : BigDecimal.ZERO);
					itemsl++;
					order.getItems().add(item);

					order.setTotalQtyOrder(item.getQtyOrder());
					order.setTotalQtyPurchased(item.getQtyPurchased());
					order.setTotalAmount(item.getAmount());

					orderMap.put(order.getOrderNumber(), order);
				}

				RM0301PurchaseOrder order = orderMap.get(data.getXpornum());
				//sup.getOrders().add(order);
				if(sup.getOm().get(order.getOrderNumber()) == null) {
					sup.getOm().put(order.getOrderNumber(), order);
				}

			} else {
				RM0301Supplier sup = new RM0301Supplier();
				sup.setSupplierCode(data.getXcus());
				sup.setSupplierName(data.getXorg());
				sup.setSupplierAddress(data.getXmadd());

				if(orderMap.get(data.getXpornum()) != null) {
					RM0301PurchaseOrder order = orderMap.get(data.getXpornum());

					RM0301Item item = new RM0301Item();
					item.setSl(itemsl);
					item.setItemCode(data.getXitem());
					item.setItemName(data.getXdesc());
					item.setQtyOrder(data.getXqtyord() != null ? data.getXqtyord() : BigDecimal.ZERO);
					item.setQtyPurchased(data.getXqtygrn() != null ? data.getXqtygrn() : BigDecimal.ZERO);
					item.setRate(data.getXrate() != null ? data.getXrate() : BigDecimal.ZERO);
					item.setUnit(data.getXunitpur());
					item.setAmount(data.getXlineamt() != null ? data.getXlineamt() : BigDecimal.ZERO);
					itemsl++;
					order.getItems().add(item);

					order.setTotalQtyOrder(order.getTotalQtyOrder().add(item.getQtyOrder()));
					order.setTotalQtyPurchased(order.getTotalQtyPurchased().add(item.getQtyPurchased()));
					order.setTotalAmount(order.getTotalAmount().add(item.getAmount()));
				} else {
					RM0301PurchaseOrder order = new RM0301PurchaseOrder();
					order.setOrderNumber(data.getXpornum());
					order.setOrderDate(SDF.format(data.getXdate()));
					order.setStatus(data.getXstatuspor());

					itemsl = 1;
					RM0301Item item = new RM0301Item();
					item.setSl(itemsl);
					item.setItemCode(data.getXitem());
					item.setItemName(data.getXdesc());
					item.setQtyOrder(data.getXqtyord() != null ? data.getXqtyord() : BigDecimal.ZERO);
					item.setQtyPurchased(data.getXqtygrn() != null ? data.getXqtygrn() : BigDecimal.ZERO);
					item.setRate(data.getXrate() != null ? data.getXrate() : BigDecimal.ZERO);
					item.setUnit(data.getXunitpur());
					item.setAmount(data.getXlineamt() != null ? data.getXlineamt() : BigDecimal.ZERO);
					itemsl++;
					order.getItems().add(item);

					order.setTotalQtyOrder(item.getQtyOrder());
					order.setTotalQtyPurchased(item.getQtyPurchased());
					order.setTotalAmount(item.getAmount());

					orderMap.put(order.getOrderNumber(), order);
				}

				RM0301PurchaseOrder order = orderMap.get(data.getXpornum());
				//sup.getOrders().add(order);
				sup.getOm().put(order.getOrderNumber(), order);

				sup.setTotalQtyOrder(BigDecimal.ZERO);
				sup.setTotalQtyPurchased(BigDecimal.ZERO);
				sup.setTotalAmount(BigDecimal.ZERO);

				supMap.put(sup.getSupplierCode(), sup);
			}

		}

		supMap.entrySet().stream().forEach(s -> {
			s.getValue().getOm().entrySet().stream().forEach(l -> {
				s.getValue().getOrders().add(l.getValue());

				s.getValue().setTotalQtyOrder(s.getValue().getTotalQtyOrder().add(l.getValue().getTotalQtyOrder()));
				s.getValue().setTotalQtyPurchased(s.getValue().getTotalQtyPurchased().add(l.getValue().getTotalQtyPurchased()));
				s.getValue().setTotalAmount(s.getValue().getTotalAmount().add(l.getValue().getTotalAmount()));
			});

			report.getSuppliers().add(s.getValue());
			report.setTotalQtyOrder(report.getTotalQtyOrder().add(s.getValue().getTotalQtyOrder()));
			report.setTotalQtyPurchased(report.getTotalQtyPurchased().add(s.getValue().getTotalQtyPurchased()));
			report.setTotalAmount(report.getTotalAmount().add(s.getValue().getTotalAmount()));
		});

		String xml = printingService.parseXMLString(report);
		if (StringUtils.isBlank(xml))
			return new byte[0];

		Document doc = printingService.getDomSourceForXML(xml);
		if (doc == null)
			return new byte[0];

		ByteArrayOutputStream baos = printingService.transfromToPDFBytes(doc, templatePath, ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
		if (baos == null)
			return new byte[0];

		return baos.toByteArray();
	}

}

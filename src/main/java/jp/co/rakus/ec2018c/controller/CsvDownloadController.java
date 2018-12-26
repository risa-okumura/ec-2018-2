package jp.co.rakus.ec2018c.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import jp.co.rakus.ec2018c.domain.Item;
import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.OrderItem;
import jp.co.rakus.ec2018c.service.OrderService;

@Controller
@Transactional
@RequestMapping("/")
public class CsvDownloadController {

	@Autowired
	private OrderService service;
	//これを使用.
	@RequestMapping("/download")
	public void download(HttpServletResponse response) throws UnsupportedEncodingException {

		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + ";charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename = \"test.csv\"");

		try (PrintWriter pw = response.getWriter()) {
			List<Order> orderList = service.findAll();
			System.out.println(orderList.size());
			StringBuilder outputString = new StringBuilder();
			
			//CSVのヘッダーを設定
			outputString.append("order_number");
			outputString.append(",");
			outputString.append("delivery_name");
			outputString.append(",");
			outputString.append("delivery_email");
			outputString.append(",");
			outputString.append("delivery_zip_code");
			outputString.append(",");
			outputString.append("delivery_address");
			outputString.append(",");
			outputString.append("delivery_tel");
			outputString.append(",");
			outputString.append("order_item_name");
			outputString.append(",");
			outputString.append("order_item_price");
			outputString.append(",");
			outputString.append("order_item_quantity");
			outputString.append(",");
			outputString.append("order_item_subtotal");
			outputString.append(",");
			outputString.append("order_totalprice");
			outputString.append(",");
			outputString.append("order_status");
			outputString.append("\r\n");
			
			for (int i = 0; i < orderList.size(); i++) {

				if (orderList.get(i).getOrderItemList().size() == 0) {

					outputString.append(commonOrder(orderList, i));
					outputString.append(",");
					outputString.append(orderStatusName(orderList, i));
					outputString.append("\r\n");
					outputString.append(commonOrder(orderList, i));
					outputString.append(",");
					outputString.append(orderStatusName(orderList, i));
					outputString.append("\r\n");

				} else {
					for (OrderItem orderItem : orderList.get(i).getOrderItemList()) {

						outputString.append(commonOrder(orderList, i));
						outputString.append(",");
						outputString.append(orderItem.getItem().getName());
						outputString.append(",");

						if (orderItem.getSize().equals('M')) {
							outputString.append(orderItem.getItem().getPriceM());
						} else {
							outputString.append(orderItem.getItem().getPriceL());
						}
						
						outputString.append(",");
						outputString.append(orderItem.getQuantity());
						outputString.append(",");
						outputString.append(orderItem.getSubTotal());
						outputString.append(",");
						outputString.append(orderList.get(i).getTotalPrice());
						outputString.append(",");
						outputString.append(orderStatusName(orderList, i));
						outputString.append("\r\n");

					}

				}
			}
			System.out.println(outputString.toString());
			pw.print(outputString.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// CSVファイルに出力する共通情報.
	private String commonOrder(List<Order> orderList, int i) {
		StringBuilder commonOrderStr = new StringBuilder();
		commonOrderStr.append(orderList.get(i).getId());
		commonOrderStr.append(",");
		commonOrderStr.append(orderList.get(i).getDestinationName());
		commonOrderStr.append(",");
		commonOrderStr.append(orderList.get(i).getDestinationEmail());
		commonOrderStr.append(",");
		commonOrderStr.append(orderList.get(i).getDestinationZipcode());
		commonOrderStr.append(",");
		commonOrderStr.append(orderList.get(i).getDestinationAddress());
		commonOrderStr.append(",");
		commonOrderStr.append(orderList.get(i).getDestinationTel());
		return commonOrderStr.toString();
	}

	// 注文ステータスの別称を指定.
	private String orderStatusName(List<Order> orderList, int i) {
		String statusName = null;
		if (orderList.get(i).getStatus() == 1) {
			statusName = "未入金";
		} else if (orderList.get(i).getStatus() == 2) {
			statusName = "入金済";
		} else if (orderList.get(i).getStatus() == 3) {
			statusName = "発送済";
		}
		return statusName;
	}

	// 1つのクラスに依存するDBの情報のみCSV化できる.
	public String getCsvtext() throws JsonProcessingException {
		CsvMapper mapper = new CsvMapper();
		mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
		CsvSchema schema = mapper.schemaFor(Order.class).withHeader();
		CsvSchema schema2 = mapper.schemaFor(OrderItem.class).withHeader();

		List<Order> orderList = service.findAll();
		List<OrderItem> orderItemList = new ArrayList<>();
		List<OrderItem> newOrderItemList = new ArrayList<>();
		OrderItem newOrderItem = new OrderItem();
		Item item = new Item();

		if (orderItemList.size() != 0) {
			for (int i = 0; i < orderList.size(); i++) {
				orderItemList = orderList.get(i).getOrderItemList();

				for (OrderItem orderItem : orderItemList) {
					newOrderItem = new OrderItem();
					newOrderItem.setId(orderItem.getId());
					newOrderItem.setOrderId(orderItem.getOrderId());
					newOrderItem.setQuantity(orderItem.getQuantity());
					newOrderItem.setSize(orderItem.getSize());
					// newOrderItem.setItem(orderItem.getItem());
					newOrderItemList.add(newOrderItem);
				}
				System.out.println(newOrderItemList.size());

			}
		}
		return mapper.writer(schema2).writeValueAsString(newOrderItemList);

		// return mapper.writer(schema).writeValueAsString(orderList);

	}

	@RequestMapping("/toDownload")
	public String toDownload() {
		return "csv-download";
	}

	@RequestMapping("/csvDownload")
	public ResponseEntity<byte[]> csvDownload() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "text/csv; charset=MS932");
		headers.setContentDispositionFormData("filename", "test.csv");
		return new ResponseEntity<>(getCsvtext().getBytes("MS932"), headers, HttpStatus.OK);

	}

	// 自分のPCにのみCSV出力可能.
	@RequestMapping("/testCsvDownload")
	public String testCsvDownload() {

		service.findAllCsv();

		return "redirect:/toDownload";

	}

}

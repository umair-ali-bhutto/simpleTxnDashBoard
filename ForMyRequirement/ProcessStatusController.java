package com.ag.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ag.config.TxnSafUtilLogger;
import com.ag.entity.AuditLog;
import com.ag.service.AuditLogService;
import com.ag.service.UserLoginService;
import com.ag.util.UtilAccess;

@RestController
public class ProcessStatusController {

	@Autowired
	AuditLogService auditLogService;

	@Autowired
	UserLoginService userLoginService;

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/process/getStatus", produces = MediaType.TEXT_HTML_VALUE)
	public String performStatusCheckHTML(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		TxnSafUtilLogger.logInfo("ProcessStatusController REQUEST RECIEVED");

		StringBuilder html = new StringBuilder();

		JSONObject response = new JSONObject();
		String clientIp = httpServletRequest.getRemoteAddr();
		String ipAddress = UtilAccess.getClientIp(httpServletRequest);

		try {

			html.append(
					"<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>Transaction Dashboard</title><style>body {margin: 0;font-family: Arial, sans-serif;background: linear-gradient(135deg, #cfe9ff, #ffffff, #d9d9ff);min-height: 100vh;padding: 20px;} h1 {font-size: 2.5rem;font-weight: 800;text-align: center;background: linear-gradient(to right, #2563eb, #9333ea, #06b6d4);-webkit-background-clip: text;-webkit-text-fill-color: transparent;text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.15);margin-bottom: 40px;} .glass {background: rgba(255, 255, 255, 0.25);backdrop-filter: blur(16px) saturate(180%);-webkit-backdrop-filter: blur(16px) saturate(180%);border: 1px solid rgba(255, 255, 255, 0.2);border-radius: 16px;box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);transition: all 0.3s ease;} .glass:hover {border-color: #60a5fa;background: rgba(255, 255, 255, 0.6);} .grid {display: grid;grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));gap: 20px;margin-bottom: 30px;} .kpi-card {padding: 20px;text-align: center;animation: fadeSlideIn 1s ease forwards;opacity: 0;transform: translateY(20px) scale(0.95);} @keyframes fadeSlideIn {to {opacity: 1;transform: translateY(0) scale(1);}} .kpi-card h2 {font-size: 14px;color: #555;margin-bottom: 10px;} .kpi-card p {font-size: 24px;font-weight: bold;color: #2563eb;} .kpi-card.purple p {color: #9333ea;} table {width: 100%;border-collapse: collapse;font-size: 14px;} thead {background: linear-gradient(to right, #2563eb, #9333ea, #4338ca);color: #fff;text-transform: uppercase;} th,td {padding: 12px 16px;text-align: left;} tbody tr {opacity: 0;animation: fadeGlow 0.6s ease forwards;} tbody tr:nth-child(1) {animation-delay: 0.1s;} tbody tr:nth-child(2) {animation-delay: 0.2s;} tbody tr:nth-child(3) {animation-delay: 0.3s;} tbody tr:nth-child(4) {animation-delay: 0.4s;} tbody tr:nth-child(5) {animation-delay: 0.5s;} tbody tr:nth-child(6) {animation-delay: 0.6s;} tbody tr:nth-child(7) {animation-delay: 0.7s;} tbody tr:nth-child(8) {animation-delay: 0.8s;} tbody tr:nth-child(9) {animation-delay: 0.9s;} tbody tr:nth-child(10) {animation-delay: 1s;} @keyframes fadeGlow {from {opacity: 0;transform: translateY(10px);}to {opacity: 1;transform: translateY(0);}} tbody tr:hover {background: rgba(59, 130, 246, 0.08);transition: background 0.2s ease;} .search-bar {padding: 10px 14px;border-radius: 30px;border: 1px solid #ccc;width: 100%;max-width: 300px;outline: none;box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);} .controls {display: flex;gap: 10px;} button {padding: 10px 16px;border: none;border-radius: 20px;color: #fff;font-size: 14px;cursor: pointer;box-shadow: 0 3px 6px rgba(0, 0, 0, 0.15);transition: background 0.2s ease;} .btn-excel {background: #22c55e;} .btn-excel:hover {background: #16a34a;} .btn-pdf {background: #ef4444;} .btn-pdf:hover {background: #dc2626;} .table-card {opacity: 0;transform: translateY(200px) scale(0.5);animation: fadeSlideIn 1s ease forwards;margin-top: 20px;overflow-x: auto;} .table-card>div {padding: 16px;display: flex;justify-content: space-between;align-items: center;flex-wrap: wrap;gap: 10px;border-bottom: 1px solid #ddd;}</style></head><body><h1 id=\"dashboardTitle\">✨ Dashboard</h1><div class=\"grid\"><div class=\"glass kpi-card\"><h2>Total Transactions Pending</h2><p id=\"kpiTotal\">0</p></div><div class=\"glass kpi-card purple\"><h2>Total Transactions Processed Today</h2><p id=\"kpiTotalCompleted\">0</p></div><div class=\"glass kpi-card purple\"><h2>Total Transactions Processed Yesterday</h2><p id=\"kpiTotalPreviousCompleted\">0</p></div></div><div class=\"glass table-card\"><h4 style=\"padding-left: 1%;margin-bottom: 0px;\">Pending Transactions Status</h4><div><input type=\"text\" id=\"searchInput\" placeholder=\"🔍 Search transactions...\" class=\"search-bar\" onkeyup=\"filterTable('txnTable', this)\" title=\"Search transactions\"><div class=\"controls\"><button class=\"btn-excel\" onclick=\"exportTableToExcel('txnTable', 'Pending-Transactions')\">⬇ Excel</button></div></div><table id=\"txnTable\"><thead><tr><th>Transaction Type</th><th>End Point</th><th>Total</th><th>Min ID</th><th>Max ID</th><th>Min Entry Date</th><th>Max Entry Date</th></tr></thead><tbody>");

			List<Object[]> fetchCurrentStatusList = fetchCurrentStatus();
			TxnSafUtilLogger
					.logInfo("ProcessStatusController fetchCurrentStatus List Size: " + fetchCurrentStatusList.size());
			for (Object[] obj : fetchCurrentStatusList) {
				html.append("<tr>");
				for (Object val : obj) {
					html.append("<td>").append(val == null ? "" : val.toString()).append("</td>");
				}
				html.append("</tr>");
			}

			html.append(
					"</tbody></table></div><div class=\"grid\"><div class=\"glass table-card\"><h4 style=\"padding-left: 3%;margin-bottom: 0px;\">Today's Status</h4><div><input type=\"text\" id=\"searchInputCompleted\" placeholder=\"🔍 Search transactions...\" class=\"search-bar\" onkeyup=\"filterTable('txnTableCompleted', this)\" title=\"Search transactions\"><div class=\"controls\"><button class=\"btn-excel\" onclick=\"exportTableToExcel('txnTableCompleted', 'Completed-Transactions')\">⬇Excel</button></div></div><table id=\"txnTableCompleted\"><thead><tr><th>Transaction Status</th><th>Total</th></tr></thead><tbody>");

			List<Object[]> fetchCompletedRecordsList = fetchCompletedRecords();
			TxnSafUtilLogger.logInfo(
					"ProcessStatusController fetchCompletedRecords List Size: " + fetchCompletedRecordsList.size());
			for (Object[] obj : fetchCompletedRecordsList) {
				html.append("<tr>");
				for (Object val : obj) {
					html.append("<td>").append(val == null ? "" : val.toString()).append("</td>");
				}
				html.append("</tr>");
			}

			html.append(
					"</tbody></table></div><div class=\"glass table-card\"><h4 style=\"padding-left: 3%;margin-bottom: 0px;\">Yesterday's Status</h4><div><input type=\"text\" id=\"searchInputPreviousCompleted\" placeholder=\"🔍 Search transactions...\" class=\"search-bar\" onkeyup=\"filterTable('txnTablePreviousCompleted', this)\" title=\"Search transactions\"><div class=\"controls\"><button class=\"btn-excel\" onclick=\"exportTableToExcel('txnTablePreviousCompleted', 'Completed-Yesterday-Transactions')\">⬇Excel</button></div></div><table id=\"txnTablePreviousCompleted\"><thead><tr><th>Transaction Status</th><th>Total</th></tr></thead><tbody>");

			List<Object[]> fetchCompletedRecordsForPreviousDayList = fetchCompletedRecordsForPreviousDay();
			TxnSafUtilLogger.logInfo("ProcessStatusController fetchCompletedRecordsForPreviousDay List Size: "
					+ fetchCompletedRecordsForPreviousDayList.size());
			for (Object[] obj : fetchCompletedRecordsForPreviousDayList) {
				html.append("<tr>");
				for (Object val : obj) {
					html.append("<td>").append(val == null ? "" : val.toString()).append("</td>");
				}
				html.append("</tr>");
			}

			html.append(
					"</tbody></table></div></div><script>function setDateTime() {  const now = new Date();  const formatted = now.toLocaleString(\"en-US\", {    weekday: \"long\",    year: \"numeric\",    month: \"short\",    day: \"2-digit\",    hour: \"2-digit\",    minute: \"2-digit\"  });  document.getElementById(\"dashboardTitle\").innerHTML =    `✨ Dashboard <br><span style=\"font-size:16px; font-weight:normal;\">${formatted}</span>`;}setDateTime();function updateKPIs() {let total = 0;let totalCompleted = 0;let totalPreviousCompleted = 0;document.querySelectorAll(\"#txnTable tbody tr\").forEach(r => {  if (r.style.display === \"none\") return;  total += parseFloat(r.cells[2].innerText.replace(/,/g, \"\")) || 0;});document.querySelectorAll(\"#txnTableCompleted tbody tr\").forEach(r => {  if (r.style.display === \"none\") return;  totalCompleted += parseFloat(r.cells[1].innerText.replace(/,/g, \"\")) || 0;});document.querySelectorAll(\"#txnTablePreviousCompleted tbody tr\").forEach(r => {  if (r.style.display === \"none\") return;  totalPreviousCompleted += parseFloat(r.cells[1].innerText.replace(/,/g, \"\")) || 0;});document.getElementById(\"kpiTotal\").innerText = total || 0;document.getElementById(\"kpiTotalCompleted\").innerText = totalCompleted || 0; document.getElementById(\"kpiTotalPreviousCompleted\").innerText = totalPreviousCompleted || 0;}updateKPIs();function filterTable(tableName, input) {const filter = input.value.toLowerCase();const rows = document.querySelectorAll(\"#\" + tableName + \" tbody tr\");rows.forEach(row => {const text = row.innerText.toLowerCase();row.style.display = text.includes(filter) ? \"\" : \"none\";});} function exportTableToExcel(tableID, filename = '') {const table = document.getElementById(tableID);const html = table.outerHTML.replace(/ /g, '%20');filename = filename ? filename + '.xls' : 'excel_data.xls';const a = document.createElement(\"a\");a.href = 'data:application/vnd.ms-excel,' + html;a.download = filename;a.click();}</script></body></html>");

			response.put("code", "0000");
			response.put("message", "Success");
			response.put("fetchCurrentStatusListSize", fetchCurrentStatusList.size());
			response.put("fetchCompletedRecordsListSize", fetchCompletedRecordsList.size());
			response.put("fetchCompletedRecordsForPreviousDayListSize", fetchCompletedRecordsForPreviousDayList.size());
		} catch (Exception e) {

			TxnSafUtilLogger.logError("Exception ", e);
			response.put("code", "9999");
			response.put("message", "Something Went Wrong");
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		} finally {
			AuditLog adt = new AuditLog();
			adt.setUserId("0");
			adt.setEntryDate(new Timestamp(new Date().getTime()));
			adt.setRequest("N/A");
			adt.setResponse("");
			adt.setRequestMode("TXN_SAF_UTIL");
			adt.setRequestIp(ipAddress + " || " + clientIp);
			adt.setTxnType("getStatus");
			adt.setMid("N/A");
			adt.setTid("N/A");
			adt.setSerialNum("N/A");
			adt.setCorpId("N/A");
			auditLogService.insertAuditLog(adt);

			TxnSafUtilLogger.logInfo("ProcessStatusController RESPONSE: " + response.toString());
		}

		return html.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> fetchCurrentStatus() {
		List<Object[]> lst = new ArrayList<Object[]>();
		try {
			String queryString = "select txn_type as 'Transaction Type', end_point as 'End Point', format(count(*),'#,0') as Total,  min(id) as MIN_ID, max(id) as MAX_ID, min(entry_date) as MIN_ENTRY_DATE, max(entry_date) as MAX_ENTRY_DATE from TXN_SAF where IS_ACTIVE='1' group by txn_type, end_point order by MIN(ID)";
			TxnSafUtilLogger.logInfo("ProcessStatusController fetchCurrentStatus Query : " + queryString);
			Query query = entityManager.createNativeQuery(queryString);
			lst = query.getResultList();
		} catch (NoResultException nre) {
		} catch (Exception e) {
			TxnSafUtilLogger.logError("Exception", e);
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> fetchCompletedRecords() {
		List<Object[]> lst = new ArrayList<Object[]>();
		try {
			String queryString = "select STATUS , COUNT(*) as total from TXN_SAF_HIST where CONVERT(varchar,UPD_DATE,23) = CONVERT(varchar,GETDATE(),23) group by STATUS";
			TxnSafUtilLogger.logInfo("ProcessStatusController fetchCompletedRecords Query: " + queryString);
			Query query = entityManager.createNativeQuery(queryString);
			lst = query.getResultList();
		} catch (NoResultException nre) {
		} catch (Exception e) {
			TxnSafUtilLogger.logError("Exception", e);
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> fetchCompletedRecordsForPreviousDay() {
		List<Object[]> lst = new ArrayList<Object[]>();
		try {
			String queryString = "select STATUS , COUNT(*) as total from TXN_SAF_HIST where CONVERT(varchar,UPD_DATE,23) = CONVERT(varchar,GETDATE()-1,23) group by STATUS";
			TxnSafUtilLogger
					.logInfo("ProcessStatusController fetchCompletedRecordsForPreviousDay Query: " + queryString);
			Query query = entityManager.createNativeQuery(queryString);
			lst = query.getResultList();
		} catch (NoResultException nre) {
		} catch (Exception e) {
			TxnSafUtilLogger.logError("Exception", e);
		}
		return lst;
	}

}

////////////// OLD
//@SuppressWarnings("unchecked")
//@GetMapping(value = "/process/getStatus", produces = MediaType.APPLICATION_JSON_VALUE)
//public JSONObject performStatusCheck(HttpServletRequest httpServletRequest,
//		HttpServletResponse httpServletResponse) {
//
//	JSONObject response = new JSONObject();
//	String clientIp = httpServletRequest.getRemoteAddr();
//	String ipAddress = UtilAccess.getClientIp(httpServletRequest);
//
//	TxnSafUtilLogger.logInfo("ProcessStatusController REQUEST RECIEVED");
//
//	try {
//		List<Object[]> lst = fetchCurrentStatus();
//		TxnSafUtilLogger.logInfo("ProcessStatusController List Size: " + lst.size());
//		if (lst.size() != 0) {
//			JSONObject data = new JSONObject();
//
//			int i = 1;
//			for (Object[] obj : lst) {
//
//				String currentDate = Objects.isNull(obj[0]) ? "" : obj[0].toString();
//				String transactionType = Objects.isNull(obj[1]) ? "" : obj[1].toString();
//				String endPoint = Objects.isNull(obj[2]) ? "" : obj[2].toString();
//				String total = Objects.isNull(obj[3]) ? "" : obj[3].toString();
//				String minId = Objects.isNull(obj[4]) ? "" : obj[4].toString();
//				String maxId = Objects.isNull(obj[5]) ? "" : obj[5].toString();
//				String minEntryDate = Objects.isNull(obj[6]) ? "" : obj[6].toString();
//				String maxEntryDate = Objects.isNull(obj[7]) ? "" : obj[7].toString();
//
//				data.put(i,
//						"currentDate: " + currentDate + " | transactionType: " + transactionType + " | endPoint: "
//								+ endPoint + " | total: " + total + " | minId: " + minId + " | maxId: " + maxId
//								+ " | minEntryDate: " + minEntryDate + " | maxEntryDate: " + maxEntryDate);
//				i++;
//			}
//
//			response.put("code", "0000");
//			response.put("message", "Success");
//			response.put("data", data);
//		} else {
//			response.put("code", "0002");
//			response.put("message", "No Record Found");
//			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//		}
//	} catch (Exception e) {
//		TxnSafUtilLogger.logError("Exception ", e);
//		response.put("code", "9999");
//		response.put("message", "Something Went Wrong");
//		httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//	} finally {
//
//		AuditLog adt = new AuditLog();
//		adt.setUserId("0");
//		adt.setEntryDate(new Timestamp(new Date().getTime()));
//		adt.setRequest("N/A");
//		adt.setResponse(response.toString());
//		adt.setRequestMode("TXN_SAF_UTIL");
//		adt.setRequestIp(ipAddress + " || " + clientIp);
//		adt.setTxnType("getStatus");
//		adt.setMid("N/A");
//		adt.setTid("N/A");
//		adt.setSerialNum("N/A");
//		adt.setCorpId("N/A");
//		auditLogService.insertAuditLog(adt);
//
//		TxnSafUtilLogger.logInfo("ProcessStatusController RESPONSE: " + response.toString());
//
//	}
//	return response;
//}

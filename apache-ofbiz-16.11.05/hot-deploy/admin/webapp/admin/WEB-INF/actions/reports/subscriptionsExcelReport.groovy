import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil
import com.autopatt.common.utils.ExcelUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook

import java.sql.Timestamp

String statusParam = null == parameters.status ? "ACTIVE" : parameters.status
String tenantIdParam = null == parameters.tenantId ? "ALL" : parameters.tenantId
String planIdParam = null == parameters.planId ? "ALL" : parameters.planId

context.status = statusParam
context.tenantId = tenantIdParam
context.planId = planIdParam

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp"), false);

def subscriptions = new ArrayList();
def tenants = new ArrayList();
if (UtilValidate.isNotEmpty(tenantOrgParties)) {
    for (GenericValue tenantOrg : tenantOrgParties) {
        Map<String, Object> tenantEntry = UtilMisc.toMap();
        def tenantId = tenantOrg.tenantId;
        tenantEntry.put("tenantId", tenantId);
        tenantEntry.put("tenantOrgPartyId", tenantOrg.orgPartyId);
        tenants.add(tenantEntry);
        if (!"ALL".equals(tenantIdParam) && !tenantIdParam.equals(tenantId)) {
            continue;
        }
        resp = dispatcher.runSync("getSubscriptions",
                UtilMisc.<String, Object> toMap("orgPartyId", tenantOrg.orgPartyId, "status", statusParam, "productId", planIdParam, "userLogin", userLogin));

        if (ServiceUtil.isSuccess(resp)) {
            for (Map subscriptionEntry : resp.get("subscriptions")) {
                subscriptionEntry.put("tenantId", tenantId)
                subscriptionEntry.put("orgPartyId", tenantOrg.orgPartyId)
                subscriptions.add(subscriptionEntry)
            }
        }
    }
}

context.subscriptions = subscriptions;

/**
 *
 * export subscriptions report excel
 */
def reportFileName = "SubscriptionsReport"

// Report Title/Info
def organizationName = "AutoPatt"
List reportTitle = ['Subscriptions Report', organizationName]

// Report Body (contains groups -- each group have header/records/summary sections)
List reportGroups = []
Map reportGroupMap = [:]

// Header - columns
Map reportGroupHeader = ['slNo'       : ["description": '#'],
                         'customer'   : ["description": "Customer"],
                         'plan'       : ["description": "Plan"],
                         'dateCreated': ["description": "Date Created"],
                         'validFrom'  : ["description": "Valid From"],
                         'validTill'  : ["description": "Valid Till"],
                         'status'     : ["description": "Status"]
]

// Records ( possible to group them )
List<LinkedHashMap<String, String>> subscriptionsMapsList = new ArrayList<>();
int i = 0;
for (Map<String, Object> subscriptionEntry : subscriptions) {
    Timestamp fromDate = ((java.sql.Timestamp) subscriptionEntry.get("fromDate"))
    Timestamp createdDate = ((java.sql.Timestamp) subscriptionEntry.get("createdDate"))
    Timestamp thruDate = ((java.sql.Timestamp) subscriptionEntry.get("thruDate"))
    def productId = subscriptionEntry.get("productId")
    GenericValue product = delegator.findOne("Product", false, "productId", productId)
    def usersMap = ['slNo'       : ++i,
                    'customer'   : subscriptionEntry.get("tenantId"),
                    "plan"       : productId + "-" + product.get("productName"),
                    "dateCreated": createdDate ? createdDate.dateString : "NA",
                    "validFrom"  : fromDate ? fromDate.dateString : "NA",
                    "validTill"  : thruDate ? thruDate.dateString : "NA",
                    "status"     : subscriptionEntry.get("status")];
    subscriptionsMapsList.add(usersMap);
}
List reportGroupRecords = [subscriptionsMapsList];

reportGroupMap.reportGroupHeader = reportGroupHeader
reportGroupMap.reportGroupRecords = reportGroupRecords
reportGroupMap.reportGroupSummary = [:]
reportGroups.add(reportGroupMap)

// Invoke service to generate excel
def serviceResponse = dispatcher.runSync("generateExcel",
        ["sheetName"   : "Subscription Report",
         "reportTitle" : reportTitle,
         "reportGroups": reportGroups,
         userLogin     : userLogin])

HSSFWorkbook wb = serviceResponse.hssWorkbook
ExcelUtils.flushExcelToResponse(reportFileName, wb, response)
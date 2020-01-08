import com.autopatt.common.utils.ExcelUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil

import java.sql.Timestamp

String statusParam = null == parameters.status ? "ALL" : parameters.status
String tenantIdParam = null == parameters.tenantId ? "ALL" : parameters.tenantId

context.status = statusParam
context.tenantId = tenantIdParam

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp"), false);

def users = new ArrayList();
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

        getTenantUsersResp = dispatcher.runSync("getTenantUsers", ["userLogin": userLogin, "tenantId": tenantId]);
        if (!ServiceUtil.isSuccess(getTenantUsersResp)) {
            continue;
        }
        for (Map<String, Object> userEntry : getTenantUsersResp.get("users")) {
            userEntry.put("tenantId", tenantId)
            userEntry.put("orgPartyId", tenantOrg.orgPartyId)
            if ("ALL".equals(statusParam) || statusParam.equals(userEntry.get("userStatus"))) {
                users.add(userEntry);
            }
        }
    }
}

/**
 *
 * export users report excel
 */
def reportFileName = "UsersReport"

// Report Title/Info
def organizationName = "AutoPatt"
List reportTitle = ['Users Report', organizationName]

// Report Body (contains groups -- each group have header/records/summary sections)
List reportGroups = []
Map reportGroupMap = [:]

// Header - columns
Map reportGroupHeader = ['slNo'       : ["description": '#'],
                         'customer'   : ["description": "Customer"],
                         'name'       : ["description": "Name"],
                         'email'      : ["description": "Email"],
                         'dateCreated': ["description": "Date Created"],
                         'role'       : ["description": "Role"],
                         'status'     : ["description": "Status"]
]

// Records ( possible to group them )
List<LinkedHashMap<String, String>> userMapsList = new ArrayList<>();
int i=0;
for (Map<String, Object> userEntry : users) {
    GenericValue userLoginEntry = userEntry.get("userLogin");
    Timestamp createdAt = userLoginEntry.getTimestamp("createdStamp");
    def usersMap = ['slNo'       : ++i,
                    'customer'   : userEntry.get("tenantId"),
                    "name"       : userEntry.get("partyName"),
                    "email"      : userEntry.get("userLoginId"),
                    "dateCreated": createdAt.dateString,
                    "role"       : userEntry.get("roleName"),
                    "status"     : userEntry.get("userStatus")];
    userMapsList.add(usersMap);
}
List reportGroupRecords = [userMapsList];

reportGroupMap.reportGroupHeader = reportGroupHeader
reportGroupMap.reportGroupRecords = reportGroupRecords
reportGroupMap.reportGroupSummary = [:]
reportGroups.add(reportGroupMap)

// Invoke service to generate excel
def serviceResponse = dispatcher.runSync("generateExcel",
        ["sheetName"   : "Users Report",
         "reportTitle" : reportTitle,
         "reportGroups": reportGroups,
         userLogin     : userLogin])

HSSFWorkbook wb = serviceResponse.hssWorkbook
ExcelUtils.flushExcelToResponse(reportFileName, wb, response)

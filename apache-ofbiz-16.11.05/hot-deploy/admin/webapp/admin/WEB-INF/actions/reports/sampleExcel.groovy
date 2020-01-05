import com.autopatt.common.utils.ExcelUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook

/**
 *
 * Sample groovy with example of export excel report
 */

def reportFileName = "SampleReport"

// Report Title/Info
def organizationName = "AutoPatt"
List reportTitle = ['Some Title for Report', organizationName]


// Report Body (contains groups -- each group have header/records/summary sections)
List reportGroups = []
Map reportGroupMap = [:]

// Header - columns
Map reportGroupHeader = ['productId':  ["description": 'Product #'],
                         'productName': ["description" : "Product Name"],
                        'productCost':["description":"Cost", "headerFormat":"","cellFormat": '$* #,0.00;[Red]$* (#,0.00);$* -', "align": "right"]
                ]

// Records ( possible to group them )
List reportGroupRecords = [
        [
                ['productId':"P1", 'productName':"Product 1", "productCost":"100"],
                ['productId':"P2", 'productName':"Product 2", "productCost":"200"],
                ['productId':"P3", 'productName':"Product 3", "productCost":"300"]
        ]
        ];

def totalCost = 600;
// Report Summary
List reportTotalSummary =[
        ['productId': ["description":"Total"],
         'productCost': ["description": totalCost]
        ]
];

reportGroupMap.reportGroupHeader = reportGroupHeader
reportGroupMap.reportGroupRecords = reportGroupRecords
reportGroupMap.reportGroupSummary = [:]
reportGroups.add(reportGroupMap)

// Invoke service to generate excel
def serviceResponse = dispatcher.runSync("generateExcel",
        ["sheetName":"Sample Report",
         "reportTitle": reportTitle,
         //"reportMetaInfo": reportMetaInfo,
         "reportGroups":reportGroups,
         "reportTotalSummary": reportTotalSummary,
         //"reportSummary":reportSummary,
         userLogin: userLogin])

HSSFWorkbook wb = serviceResponse.hssWorkbook
ExcelUtils.flushExcelToResponse(reportFileName, wb, response)

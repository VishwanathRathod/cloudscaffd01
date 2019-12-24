import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue

products = delegator.findByAnd("Product", UtilMisc.toMap("productTypeId", "SUBSCRIPTION_PLAN"), null, false);

List<Map> plansList = new ArrayList();
for(GenericValue product : products) {
    Map entry = UtilMisc.toMap("productId", product.productId);
    entry.put("productName", product.productName);

    def productPrices = product.getRelated("ProductPrice", null, null,false);
    if(UtilValidate.isNotEmpty(productPrices)) {
        def priceGv = productPrices.get(0)
        entry.put("productPrice", priceGv.price)
    }

    def productAttributes = product.getRelated("ProductAttribute", null, null,false);
    if(UtilValidate.isNotEmpty(productAttributes)) {
        for(GenericValue productAttr: productAttributes){
             entry.put(productAttr.attrName, productAttr.attrValue)
        }
    }
    plansList.add(entry);
}
context.plans = plansList;
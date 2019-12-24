import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue

planId = request.getParameter("planId")
context.planId = planId

Map inputs = UtilMisc.toMap("productId", planId)
product = delegator.findOne("Product", inputs, false)
context.product = product

def productPrices = product.getRelated("ProductPrice", null, null,false);
if(UtilValidate.isNotEmpty(productPrices)) {
    def priceGv = productPrices.get(0)
    context.priceGv = priceGv
}

def productAttributes = product.getRelated("ProductAttribute", null, null,false);

if(UtilValidate.isNotEmpty(productAttributes)) {
    for(GenericValue productAttr: productAttributes){
        context.put(productAttr.attrName, productAttr.attrValue)
    }
}
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate

productId = request.getParameter("productId")
context.productId = productId

Map inputs = UtilMisc.toMap("productId", productId)
product = delegator.findOne("Product", inputs, false)
context.product = product

def productPrices = product.getRelated("ProductPrice", null, null,false);
if(UtilValidate.isNotEmpty(productPrices)) {
    def priceGv = productPrices.get(0)
    context.priceGv = priceGv
}


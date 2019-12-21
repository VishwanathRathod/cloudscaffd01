package com.autopatt.admin.events;
import org.apache.ofbiz.base.conversion.NumberConverters;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import com.autopatt.portal.events.UserMgmtEvents;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductMgmtEvents {

    public final static String module = UserMgmtEvents.class.getName();
    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String updateProduct(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String productName = request.getParameter("productName");
        String productId = request.getParameter("productId");
        String priceStr = request.getParameter("price");
        BigDecimal price= BigDecimal.valueOf(1.0);
        try{
            price = new BigDecimal(priceStr);
        System.out.println(price+"insert try catch");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(price+"insert try catch");
            request.setAttribute("_ERROR_MESSAGE_", "Product Price should not contain Letters.");
        }
        System.out.println(price+"insert try catch");

        Map<String, Object> inputs = UtilMisc.toMap("productId", productId);
        try {
            GenericValue product = delegator.findOne("Product", inputs , false);
            product.set("productName",productName);
            delegator.store(product);

            List<GenericValue> productPrices = product.getRelated("ProductPrice", null,null,false);
            if(UtilValidate.isNotEmpty(productPrices)) {
                GenericValue priceGv = productPrices.get(0);
                priceGv.set("price", price);
                delegator.store(priceGv);
            }

        } catch (GenericEntityException e) {
            e.printStackTrace();
            request.setAttribute("_ERROR_MESSAGE_", "Unable to update the Product details.");
            return ERROR;
        }
        request.setAttribute("updateSuccess","Y");
        request.setAttribute("_EVENT_MESSAGE_", "Product price updated successfully.");
        request.setAttribute("_EVENT_MESSAGE_", "Product details updated successfully.");

        return SUCCESS;
    }

}

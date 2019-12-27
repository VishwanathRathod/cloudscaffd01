package com.autopatt.admin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.service.ServiceUtil;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class JWTHelper {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    private static Properties PORTAL_PROPERTIES = UtilProperties.getProperties("admin.properties");
    public final static String module = JWTHelper.class.getName();

    public static Map<String, Object> generateJWTToken(String subject, String id) {
        String secretKey = PORTAL_PROPERTIES.getProperty("reset.password.token.jwt.secret.key", "AUTOPATT");
        String expiryStr = PORTAL_PROPERTIES.getProperty("reset.password.token.expiry", "60");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMinutes = new Long(expiryStr);
            Date exp = new Date(nowMillis + 60000 * expMinutes);

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder();
            builder.setId(id);
            builder.setIssuedAt(now);
            builder.setSubject(subject);
            builder.setExpiration(exp);
            builder.signWith(signatureAlgorithm, signingKey);
            resultMap.put("token", builder.compact());
            System.out.println(resultMap.get("token"));
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to generate token");
        }
    }

    public static Map<String, Object> parseJWTToken(String token) {
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        try {
            String secretKey = PORTAL_PROPERTIES.getProperty("reset.password.token.jwt.secret.key", "AUTOPATT");
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            if (UtilDateTime.nowTimestamp().after(UtilDateTime.toTimestamp(expiration))) {
                return ServiceUtil.returnFailure("Token has been expired");
            }
            resultMap.put("USERNAME", claims.getId());
            resultMap.put("userTenantId", claims.getSubject());
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to decrypt token");
        }
    }
}

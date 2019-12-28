package com.autopatt.common.utils;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JWTHelper {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    private static Properties PORTAL_PROPERTIES = UtilProperties.getProperties("admin.properties");
    public final static String module = JWTHelper.class.getName();

    public static String generateJWTToken(Map<String, String> inputParams) throws Exception {
        String secretKey = PORTAL_PROPERTIES.getProperty("reset.password.token.jwt.secret.key", "AUTOPATT");
        String expiryStr = PORTAL_PROPERTIES.getProperty("reset.password.token.expiry", "60");
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMinutes = new Long(expiryStr);
            Date exp = new Date(nowMillis + 60000 * expMinutes);

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder();
            builder.setId(inputParams.get("id"));
            builder.setIssuedAt(now);
            builder.setSubject(inputParams.get("subject"));
            builder.setExpiration(exp);
            builder.signWith(signatureAlgorithm, signingKey);
            String token = builder.compact();
            return token;
        } catch (Exception e) {
            Debug.logError(e, module);
            throw new Exception("Failed to generate token");
        }
    }

    public static Map<String, String> parseJWTToken(String token) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        try {
            String secretKey = PORTAL_PROPERTIES.getProperty("reset.password.token.jwt.secret.key", "AUTOPATT");
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            if (UtilDateTime.nowTimestamp().after(UtilDateTime.toTimestamp(expiration))) {
                throw new Exception("Token has been expired");
            }
            resultMap.put("id", claims.getId());
            resultMap.put("subject", claims.getSubject());
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            throw new Exception("Failed to decrypt token");
        }
    }
}

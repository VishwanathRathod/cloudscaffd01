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
    private static String SECRET_KEY = PORTAL_PROPERTIES.getProperty("reset.password.token.jwt.secret.key", "APSECRET@987");
    private static String DEFAULT_JWT_EXPIRY_IN_MINS = PORTAL_PROPERTIES.getProperty("reset.password.token.expiry", "1440");

    public final static String module = JWTHelper.class.getName();

    public static String generateJWTToken(Map<String, Object> inputParams) throws Exception {
        long defaultExpireMins = new Long(DEFAULT_JWT_EXPIRY_IN_MINS);
        return generateJWTToken(inputParams, defaultExpireMins);
    }

    public static String generateJWTToken(Map<String, Object> inputParams, Long expireInMins) throws Exception {
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMinutes = new Long(expireInMins);
            Date exp = new Date(nowMillis + 60000 * expMinutes);

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder();
            builder.addClaims(inputParams);
            builder.setIssuedAt(now);
            builder.setExpiration(exp);
            builder.signWith(signatureAlgorithm, signingKey);
            return builder.compact();
        } catch (Exception e) {
            Debug.logError(e, module);
            throw new Exception("Failed to generate token");
        }
    }

    public static Map<String, Object> parseJWTToken(String token) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            if (UtilDateTime.nowTimestamp().after(UtilDateTime.toTimestamp(expiration))) {
                throw new Exception("Token has been expired");
            }
            resultMap.put("id", claims.getId());
            resultMap.put("subject", claims.getSubject());
            resultMap.putAll(claims);
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            throw new Exception("Failed to decrypt token");
        }
    }
}

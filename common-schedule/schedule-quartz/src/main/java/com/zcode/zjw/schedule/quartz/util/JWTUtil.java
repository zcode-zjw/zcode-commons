package com.zcode.zjw.schedule.quartz.util;

import io.jsonwebtoken.*;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author zhangjiwei
 * @since 2022年03月23日
 */
public class JWTUtil {
    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);
    /* token的默认生存时间30分钟 */
    static final long DEFAULT_ALIVE_TERM = 30 * 60 * 1000L;

    /**
     * 获取token,并设置token有效时间，单位s
     *
     * @param uid
     * @param exp
     * @return
     */
    public static String getToken(String uid, long exp) {
        long endTime = System.currentTimeMillis() + exp * 1000;
        return Jwts.builder().setSubject(uid).setExpiration(new Date(endTime))
                .signWith(SignatureAlgorithm.RS512, RSAUtil.getPublicKey()).compact();
    }

    public static JWTResult checkToken(String token) {
        if (null == token) {
            return new JWTResult(false, null, "未授权请求", HttpStatus.SC_UNAUTHORIZED);
        }
        try {
            Claims claims =
                    Jwts.parser().setSigningKey(RSAUtil.getPublicKey()).parseClaimsJws(token).getBody();
            return new JWTResult(true, claims.getSubject(), "合法请求", HttpStatus.SC_OK);
        } catch (ExpiredJwtException e) {
            return new JWTResult(false, null, "token已过期", HttpStatus.SC_PAYMENT_REQUIRED);
        } catch (UnsupportedJwtException e) {
            return new JWTResult(false, null, "不支持的媒体类型", HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
        } catch (MalformedJwtException e) {
            return new JWTResult(false, null, "非法请求", HttpStatus.SC_FORBIDDEN);
        } catch (SignatureException e) {
            return new JWTResult(false, null, "非法请求", HttpStatus.SC_FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new JWTResult(false, null, "非法请求", HttpStatus.SC_FORBIDDEN);
        }
    }

    /**
     * 获取token,默认token有效时间30分钟
     *
     * @param uid
     * @return
     */
    public static String getToken(String uid) {
        long endTime = System.currentTimeMillis() + DEFAULT_ALIVE_TERM;
        // 这里用私钥加密，做签名
        return Jwts.builder().setSubject(uid).setExpiration(new Date(endTime))
                .signWith(SignatureAlgorithm.RS512, RSAUtil.getPrivateKey()).compact();
    }


    public static class JWTResult {
        private boolean status;
        private String uid;
        private String message;
        private int code;

        public JWTResult() {
        }

        public JWTResult(boolean status, String uid, String message, int code) {
            this.status = status;
            this.uid = uid;
            this.message = message;
            this.code = code;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "JWTResult{" + "status=" + status + ", uid='" + uid + '\'' + ", message='" + message
                    + '\'' + ", code=" + code + '}';
        }
    }
}

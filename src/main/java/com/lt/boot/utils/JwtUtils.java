package com.lt.boot.utils;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * @description: JWT 工具类
 * @author: ~Teng~
 * @date: 2024/1/27 14:45
 */
public class JwtUtils {
    /**
     * Token 请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * TOKEN的有效期 2个小时 2h _表示支持的语法 可读性更强
     */
    private static final int TOKEN_TIME_OUT = 2 * 3_600;

    /**
     * 加密KEY
     */
    private static final String TOKEN_ENCRYPTION_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";

    /**
     * 最小刷新间隔(S)
     */
    private static final int REFRESH_TIME = 300;

    /**
     * 生成token
     *
     * @param id 用户id
     * @return token
     */
    public static String getToken(Long id) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("userId", id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                // 签发时间
                .setId(UUID.randomUUID().toString()).setIssuedAt(new Date(currentTime))
                // 说明
                .setSubject("system")
                // 签发者信息
                .setIssuer("muziteng")
                // 接收用户
                .setAudience("boot")
                // 数据压缩方式
                .compressWith(CompressionCodecs.GZIP)
                // 加密方式 加密算法，密钥
                .signWith(SignatureAlgorithm.HS512, generalKey())
                // 过期时间戳
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))
                // 用户有效信息
                .addClaims(claimMaps)
                .compact();
    }

    /**
     * 获取 token 中的 claims 信息
     */
    private static Jws<Claims> getJws(String token) {
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
    }

    /**
     * 获取 payload body信息
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    /**
     * 获取 header body信息
     */
    public static JwsHeader getHeaderBody(String token) {
        return getJws(token).getHeader();
    }

    /**
     * 是否过期
     *
     * @return -1：有效，0：有效，1：过期，2：过期
     */
    public static int verifyToken(Claims claims) {
        if (claims == null) {
            return 1;
        }
        try {
            claims.getExpiration().before(new Date());
            // 需要自动刷新TOKEN
            if ((claims.getExpiration().getTime() - System.currentTimeMillis()) > REFRESH_TIME * 1000) {
                return -1;
            } else {
                return 0;
            }
        } catch (ExpiredJwtException ex) {
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 由字符串生成加密 key
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(TOKEN_ENCRYPTION_KEY.getBytes());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static void main(String[] args) {
        // 生成token
        String token = JwtUtils.getToken(1L);
        System.out.println(token);
        try {
            // 获取 payload 信息
            Claims claimsBody = JwtUtils.getClaimsBody(token);
            // 判断是否过期
            int res = JwtUtils.verifyToken(claimsBody);
            if (res < 1) {
                Integer id = (Integer) claimsBody.get("userId");
                System.out.println("token 解析成功，userId = " + id);
            } else {
                System.out.println("token 已过期");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("token 解析失败");
        }
    }
}

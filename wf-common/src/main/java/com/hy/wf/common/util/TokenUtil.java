package com.hy.wf.common.util;

import com.hy.wf.common.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: hy-wf
 * @description: jwt工具
 * @author: jt
 * @create: 2019-01-04 15:34
 **/
@Component
public class TokenUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access_expire}")
    private long accessExpire;
    @Value("${jwt.refresh_expire}")
    private long refreshExpire;
    @Value("${jwt.header}")
    private String header;


    /**
     * 生成jwt token
	 */
    public String generateToken(String uid,String type) {
        Date nowDate = new Date();
        // 过期时间
        Date expireDate;
        if(type.equals(Constant.REFRESH_TOKEN)){
            expireDate= new Date(nowDate.getTime() + refreshExpire * 1000);
        }else {
            expireDate = new Date(nowDate.getTime() + accessExpire * 1000);
        }
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(uid).setIssuedAt(nowDate).setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析token
     */
    public String pareToken(String token) {
        String uid = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = getClaimByToken(token);
            if (claims != null) {
                if(!isTokenExpired(claims.getExpiration())){
                    uid = claims.getSubject();
                }
            }
        }
        return uid;
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }


//    public static void main(String[] args) {
//        TokenUtil tokenUtil = new TokenUtil();
////        String token =  tokenUtil.generateToken("10303");
////        String token1 =  tokenUtil.generateToken("10303");
//        String uid = tokenUtil.pareToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDMwMyIsImlhdCI6MTU0ODA1OTQzNSwiZXhwIjoxNTQ4MDYwNDM1fQ.X_pjBY1YKqRr581MB8hQnvwSIMiK0qxkl4Iw4Mfo-YBLsMyrXSlcBVP9RgX2GoQXlak1U-F9wWNkdkm6lzcwHw");
//        System.out.println(uid);
//    }
}

package edu.nju.mook.sys.utils;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于jwt token的生成和解密
 */
public class JWTUtils {

    public final static String BEARER="Bearer ";

    private static String secrect;

    private static Long expire;


    public static void setSecrect(String secrect) {
        JWTUtils.secrect = secrect;
    }

    public static void setExpire(Long expire) {
        JWTUtils.expire = expire;
    }

    /**
     * 生成jwt token
     *
     * @param subject     代表用户名
     * @param authorities 代表用户的权限集合
     * @return
     */
    public static String generateJWT(String subject, Collection<? extends GrantedAuthority> authorities) {

        //jdk 8 Stream api写法
        String authStr = authorities.stream().map(GrantedAuthority.class::cast)  //map将原有的Object集合转换成GrantedAuthority集合
                .map(GrantedAuthority::getAuthority) //通过GrantedAuthority的getAuthority方法将集合转换成String集合
                .collect(Collectors.joining(","));

        //创建jwt的构造器对象JWTClaimsSet
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.subject(subject) //将用户名设为subject
                .issuer("mook") //将项目名称设置为发布者
                .expirationTime(new Date(new Date().getTime() + expire)) //设置过期时间
                .claim("roles", authStr);  //声明自定义的载荷：保存角色信息转为JSON对象 {roles:"admin,user// "}

        //通过构造器生成JWT对象
        JWTClaimsSet claimsSet = builder.build();

        //设置签名
        //创建签名对象
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        //调用sign方法进行签名
        try {
            signedJWT.sign(new MACSigner(secrect));
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        //返回token以字符串形式
        return signedJWT.serialize();
    }

    /**
     * 从request中获取token并且进行解析，获得用户名，以及权限，并且将这些信息封装成Spring-security能够识别的
     * Authentication对象。
     * @param request
     * @return
     */
    public static Authentication extractJWT(HttpServletRequest request){
        //从request中获得请求头为Authroization的token字符串： "bearer sjfkljklrngioejgireltjeritorejtioet"
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if(StringUtils.isNotBlank(authHeader)){
            //如果不为空，对token字符串进行去bearer
            token = authHeader.substring(BEARER.length());
            //解密
            try {
                //将字符串token反序列化为jwt对象
                SignedJWT signedJWT = SignedJWT.parse(token);

                //获得jwt的过期时间，判断是否超过当前的系统时间。
                boolean isAfter = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());

                //检查你的签名是否合法
                boolean isVerify = signedJWT.verify(new MACVerifier(secrect));


                //判断jwt是否过期并且是否合法，然后再进行解密
                if(isAfter && isVerify){
                    //提取其中的关于user的信息 username和authroity
                    String subject;
                    String authStr;
                    List<GrantedAuthority> authorities;

                    //获取subject
                    subject = signedJWT.getJWTClaimsSet().getSubject();

                    //获得authority的字符串形式
                    authStr = (String) signedJWT.getJWTClaimsSet().getClaim("roles");

                    //将authStr转换成GrantedAuthority集合
                    authorities = Stream.of(authStr.split(",")) //通过of方法将字符串数组转换成流
                          .filter(StringUtils::isNotBlank) //排除空字符串
                          .map(SimpleGrantedAuthority::new)  //转换成GrantedAuthority结合
                          .collect(Collectors.toList());

                    //将username和authorities 组成Authentication对象 UsernamePasswordAuthenticationToken
                    return new UsernamePasswordAuthenticationToken(subject,null,authorities);
                }
            } catch (ParseException | JOSEException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}

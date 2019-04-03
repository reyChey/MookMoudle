package edu.nju.mook.sys.security;

import edu.nju.mook.sys.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录之后验证是否有token，并且解析token，并包装成authentication对象供spring security进行权限的判断
 * 1. 从request中获取请求头authroization的值，转换成token
 * 2.将token转换成authentication对象
 * 3.将authentication对象交给spring security的环境进行权限的验证
 * 4.如果验证通过，就认为是一个合法的请求，予以放行。
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 具体的token转换任务
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获得header值
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        //判断是否为空，或者不是以Bearer 开头，为空代表没有token，没有bearer代表token不合法，没有token就直接交给下一个filter去处理
        if(StringUtils.isBlank(header) || !header.startsWith(JWTUtils.BEARER)){
            chain.doFilter(request,response);
            return ;
        }

        //如果有token，并且合法，就将token转换成Authentication对象
        Authentication authentication = JWTUtils.extractJWT(request);

        //将authentication对象交给springsecurity的环境，供其进行验证
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //再一次生成token，并且将token发送给前台
        response.addHeader(HttpHeaders.AUTHORIZATION,JWTUtils.generateJWT(authentication.getName(),authentication.getAuthorities()));

        chain.doFilter(request,response);

    }
}

package edu.nju.mook.sys.config;

import edu.nju.mook.sys.security.JWTAuthenticationFilter;
import edu.nju.mook.sys.security.JWTLoginFilter;
import edu.nju.mook.sys.security.WorkerService;
import edu.nju.mook.sys.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class AppConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") //那些请求需要跨域
                .allowedOrigins("*") //代表允许所有的地址和端口对本服务进行访问
                .allowedHeaders("token")  //允许请求有哪些自定义的请求头
                .exposedHeaders("token")  //返回的响应头
                .allowCredentials(true) //是否允许密钥
                .allowedMethods("DELETE","POST","GET","PUT") //允许请求方法
                .maxAge(3600); //请求超时时间1小时，以秒为单位
    }

    @Bean
    @Lazy(false) //不允许懒加载
    public int readJWTConfig(){
        JWTUtils.setExpire(env.getProperty("JWT.expire",Long.class,6000000L));
        JWTUtils.setSecrect(env.getProperty("JWT.secrect"));
        return 1;//1代表加载优先级最高
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager());
        //设置登录请求地址
        jwtLoginFilter.setFilterProcessesUrl("/api/signIn");
        /*jwtLoginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\"}");
        });
*/
        http.cors() //允许跨域
                .and()  //and代表上一个配置完了后回到父节点继续进行配置
                .httpBasic().disable() //不允许进行httpbasic的验证
                .csrf().disable() //关闭csrf,防止dos攻击
                .authorizeRequests() //设置不同路径的权限验证规则
                .antMatchers("/api/signUp").permitAll() //对/api/signUp进行放心
                .anyRequest().authenticated() //除以上放行的路径必须都进行权限验证
                .and().addFilter(jwtLoginFilter) // 第一个filter用于登录权限空指
                .addFilter(new JWTAuthenticationFilter(authenticationManager())); //第二个filter用于验证登录后用户的具体权限
        // 两个filter都不需要实现验证逻辑，而只是对jwt的序列化和饭反序列化
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置workservice
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //构造AuthenticationManager，设置manager的UserDetailService的实现以及密码加密器的实现
        builder.userDetailsService(workerService).passwordEncoder(passwordEncoder());
    }
}

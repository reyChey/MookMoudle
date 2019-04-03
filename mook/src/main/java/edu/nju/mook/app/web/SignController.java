package edu.nju.mook.app.web;

import edu.nju.mook.sys.security.Worker;
import edu.nju.mook.sys.security.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册用户信息
     * @param
     * @return
     */
    @PostMapping("/api/signUp")
    public Worker signUp(@RequestBody Worker worker){
        //存储注册信息
        //把明文密码加密成字符串
        worker.setPassword(passwordEncoder.encode(worker.getPassword()));
        workerService.save(worker);
        return worker;
    }

    @PostMapping("/api/signIn")
    public String signInSuccessHandler(){
        return "{\"status\":\"success\"}";
    }
}

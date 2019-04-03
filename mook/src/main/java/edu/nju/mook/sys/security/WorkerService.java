package edu.nju.mook.sys.security;

import edu.nju.mook.app.dao.WorkerMapper;
import edu.nju.mook.sys.base.BaseService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * spring security 服务，通过该类和username查询对应的用户信息
 *
 */
@Service
public class WorkerService extends BaseService<Worker, WorkerMapper> implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Worker worker = new Worker();
        worker.setUsername(username);
        Worker worker1 = this.dao.find(worker);
        if(worker1.getPassword()!= null){
            return worker1;
        }else
            return null;
    }
}

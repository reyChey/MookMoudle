package edu.nju.mook.app.service;

import edu.nju.mook.app.dao.MemberMapper;
import edu.nju.mook.app.model.Member;
import edu.nju.mook.sys.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends BaseService<Member, MemberMapper> {
}

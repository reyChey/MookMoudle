package edu.nju.mook.app.web;

import com.github.pagehelper.PageInfo;
import edu.nju.mook.app.model.Member;
import edu.nju.mook.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("")
    public PageInfo<Member> page(Member condition, Integer pageNum, Integer pageSize){
        return memberService.pageInfo(condition,pageNum,pageSize);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id){
        memberService.delete(id);
    }

    @PostMapping("save")
    public void save(@RequestBody Member member){
        memberService.save(member);
    }
}

package edu.nju.mook.sys.base;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseService<T extends BaseModel,D extends BaseDao<T>> {

    @Autowired
    protected D dao;

    public T findById(Long id){
        return dao.findById(id);
    }

    public T find(T t){
        return dao.find(t);
    }

    public List<T> findList(T t){
        return dao.findList(t);
    }


    public List<T> findAll(){
        return dao.findAll();
    }

    public void delete(Long id){
        dao.delete(id);
    }

    public void delete(T t){
        dao.delete(t);
    }

    public void save(T t){
        if(t.getId()!=null){
            dao.update(t);
        }else{
            dao.insert(t);
        }
    }

    public PageInfo<T> pageInfo(T t, Integer pageNum, Integer pageSize){
        //采用静态方法来进行分页；
        PageHelper.startPage(pageNum,pageSize);
        //调用具体的查询方法
        List<T> list = dao.findList(t);
        //直接new PageInfo

        return new PageInfo<>(list);
    }
}

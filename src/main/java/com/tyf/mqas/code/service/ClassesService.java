package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.code.dao.ClassesRepository;
import com.tyf.mqas.code.dao.KnowledgeRepository;
import com.tyf.mqas.code.entity.Classes;
import com.tyf.mqas.code.entity.Knowledge;
import com.tyf.mqas.code.entity.TreeTable;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Transactional
public class ClassesService {

    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;

    /**
     * 获取所有菜单
     * @return
     */
    public String getAllClassess(){
        List<Classes> list = classesRepository.findAllBySort();
        List<TreeTable> treeTables = new ArrayList<TreeTable>();
        list.forEach(classes -> {
            TreeTable treeTable = new TreeTable();
            treeTable.setId(classes.getId()+"");
            treeTable.setpId(classes.getPid()==0?"":classes.getPid().toString());
            treeTable.setName(classes.getName());
            String[] td = {classes.getSort()==null?"":classes.getSort().toString()};
            treeTable.setTd(td);
            treeTables.add(treeTable);
        });
        return JSONArray.toJSONString(treeTables);
        }

    /**
     * 保存
     * @param classes
     * @return
     */
    public Classes saveEntity(Classes classes){
        return classesRepository.save(classes);
    }

    /**
    * 删除菜单
    * @param ids
    */
    public void deleteClasses(String ids){
        Stream.of(ids.split(",")).forEach(id->{
            classesRepository.deleteById(Integer.parseInt(id));
        });
    }

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    public Classes getClassesById(Integer id){
        return classesRepository.getOne(id);
    }

    /**
     * 根据id获取编辑信息
     * @param id
     * @return
     */
    public String getEditInfo(Integer id){
        Map<String,Object> map = new HashMap<>();
        Classes classes = classesRepository.getOne(id);
        map.put("id",classes.getId());
        map.put("name",classes.getName());
        map.put("sort",classes.getSort());
        map.put("pid",classes.getPid());
        if(classes.getPid()!=0){
            map.put("parent",classesRepository.getOne(classes.getPid()).getName());
        }else{
            map.put("parent","");
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 判断重复
     * @param name
     * @param id
     * @return
     */
    public boolean verifyTheRepeat(String name,String id,String pid){
        Integer num = null;
        Integer pId = 0;
        if(StringUtils.isNotBlank(pid)){
            pId = Integer.parseInt(pid);
        }
        if(StringUtils.isNotBlank(id)){
            num = classesRepository.getClassesNumByIdAndName(Integer.parseInt(id), name,pId);
        }else{
            num = classesRepository.getClassesNumByName( name,pId);
        }
        return num > 0?false:true;
    }

    /**
    * 判断菜单是否被使用
    * @param ids
    * @return
    */
    public Boolean checkClassesUsed(String ids){
//        for (String id : ids.split(",")) {
//            Integer num = classesRepository.getRsRoleMenuNumByMenuId(Integer.parseInt(id));
//            if(num > 0){
//                return false;
//            }
//        }
        return true;
    }

    /**
     *
     * @param id
     * @return
     */
    public String getKnowledgeByClassId(Integer id){
        List<Knowledge> list = knowledgeRepository.findKnowledgeByClassesId(id);
        return JSONArray.toJSONString(list);
    }

    /**
     * 保存知识配置
     * @param classesId
     * @param knowledgeIds
     */
    public void saveKnowledgeSet(Integer classesId,String knowledgeIds){
        classesRepository.deleteKnowledgeSet(classesId);
        if(StringUtils.isNotBlank(knowledgeIds)){
            Stream.of(knowledgeIds.split(",")).forEach(id->{
                classesRepository.saveKnowledgeSet(classesId,Integer.parseInt(id));
            });
        }
    }

}
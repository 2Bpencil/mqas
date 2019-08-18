package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
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
public class KnowledgeService {

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    /**
     * 获取所有菜单
     * @return
     */
    public String getAllKnowledges(){
        List<Knowledge> list = knowledgeRepository.findAllBySort();
        List<TreeTable> treeTables = new ArrayList<TreeTable>();
        list.forEach(knowledge -> {
            TreeTable treeTable = new TreeTable();
            treeTable.setId(knowledge.getId()+"");
            treeTable.setpId(knowledge.getPid()==0?"":knowledge.getPid().toString());
            treeTable.setName(knowledge.getName());
            String[] td = {knowledge.getType()!= null?(knowledge.getType()==0?"非重点":"重点"):"",knowledge.getSort()==null?"":knowledge.getSort().toString()};
            treeTable.setTd(td);
            treeTables.add(treeTable);
        });
        return JSONArray.toJSONString(treeTables);
    }

    /**
     * 保存
     * @param knowledge
     * @return
     */
    public Knowledge saveEntity(Knowledge knowledge){
        return knowledgeRepository.save(knowledge);
    }

    /**
    * 删除菜单
    * @param ids
    */
    public void deleteKnowledge(String ids){
        Stream.of(ids.split(",")).forEach(id->{
            knowledgeRepository.deleteById(Integer.parseInt(id));
        });
    }

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    public Knowledge getKnowledgeById(Integer id){
        return knowledgeRepository.getOne(id);
    }

    /**
     * 根据id获取编辑信息
     * @param id
     * @return
     */
    public String getEditInfo(Integer id){
        Map<String,Object> map = new HashMap<>();
        Knowledge knowledge = knowledgeRepository.getOne(id);
        map.put("id",knowledge.getId());
        map.put("name",knowledge.getName());
        map.put("sort",knowledge.getSort());
        map.put("pid",knowledge.getPid());
        if(knowledge.getPid()!=0){
            map.put("parent",knowledgeRepository.getOne(knowledge.getPid()).getName());
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
    public boolean verifyTheRepeat(String name,String id){
        Integer num = null;
        if(StringUtils.isNotBlank(id)){
            num = knowledgeRepository.getKnowledgeNumByIdAndName(Integer.parseInt(id), name);
        }else{
            num = knowledgeRepository.getKnowledgeNumByName( name);
        }
        return num > 0?false:true;
    }

    /**
    * 判断是否被使用
    * @param ids
    * @return
    */
    public Boolean checkKnowledgeUsed(String ids){
        for (String id : ids.split(",")) {
            Integer num = knowledgeRepository.getRsNumByKnowledgeId(Integer.parseInt(id));
            if(num > 0){
                return false;
            }
        }
        return true;
    }

}
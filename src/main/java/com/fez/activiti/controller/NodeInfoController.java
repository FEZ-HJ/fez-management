package com.fez.activiti.controller;

import com.fez.activiti.entity.TbNodeInfo;
import com.fez.activiti.service.NodeInfoService;
import com.fez.utils.KeyUtil;
import com.fez.utils.ReturnJson;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by H.J
 * 2019/1/19
 */
@RestController
public class NodeInfoController {

    @Autowired
    private NodeInfoService service;

    private Gson gson = new Gson();

    @DeleteMapping("delete")
    public String delete(String operationId){
        return gson.toJson(ReturnJson.SUCCESS());
    }

    @GetMapping("list")
    public Map<String,Object> list(String nodeId){
        List<TbNodeInfo> list = service.findAllByNodeId(nodeId);
        return ReturnJson.RETURN("成功",list,list.size(),0);
    }

    @PostMapping("save")
    public String save(TbNodeInfo tbNodeInfo){
        if(StringUtils.isEmpty(tbNodeInfo.getOperationId())){
            tbNodeInfo.setOperationId(KeyUtil.getUniqueKey());
        }
        service.save(tbNodeInfo);
        return gson.toJson(ReturnJson.SUCCESS());
    }
}

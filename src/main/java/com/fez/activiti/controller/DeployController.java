package com.fez.activiti.controller;

import com.fez.activiti.entity.TbNode;
import com.fez.activiti.service.DeployService;
import com.fez.activiti.service.TbNodeService;
import com.fez.utils.ReturnJson;
import com.google.gson.Gson;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.J
 * 2019/1/19
 */
@RestController
@RequestMapping("deploy")
public class DeployController {

    @Autowired
    private DeployService service;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TbNodeService tbNodeService;

    private Gson gson = new Gson();

    @RequestMapping("deploy")
    public String deploy(String modelId){
        try {
            service.deploy(modelId);
            return gson.toJson(ReturnJson.SUCCESS());
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ReturnJson.ERROR());
        }
    }

    @GetMapping("deployed")
    public Map<String,Object> deployed(){
        List<Map<String,String>> listS = new ArrayList<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionKey().asc().list();
        for(ProcessDefinition processDefinition : list){
            Map<String,String> map = new HashMap<>();
            map.put("ID",processDefinition.getId());
            map.put("name",processDefinition.getName());
            map.put("deploymentId",processDefinition.getDeploymentId());
            listS.add(map);
        }
        return ReturnJson.RETURN("成功",listS,listS.size(),0);
    }

    /**
     * 删除已部署模型
     * @param deploymentId 模型部署ID
     * @param procDefId 流程定义ID
     */
    @DeleteMapping("delete")
    public String delete(String deploymentId,String procDefId){
        try{
            String[] deploymentIds = deploymentId.split(",");
            String[] procDefIds = procDefId.split(",");
            for(int i = 0 ; i < deploymentIds.length ; i++){
                if(StringUtils.isNotEmpty(deploymentIds[i])){
                    service.delete(deploymentIds[i],procDefIds[i]);
                }
            }
            return gson.toJson(ReturnJson.SUCCESS());
        }catch (Exception e){
            e.printStackTrace();
            return gson.toJson(ReturnJson.ERROR());
        }
    }

    /**
     * 已部署模型的节点列表
     * @param procDefId 流程定义ID
     */
    @RequestMapping("nodes")
    public Map<String,Object> nodeIndex(String procDefId){
        List<TbNode> list = tbNodeService.findAllByProcDefId(procDefId);
        return ReturnJson.RETURN("成功",list,list.size(),0);
    }

}

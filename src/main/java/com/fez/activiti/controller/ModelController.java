package com.fez.activiti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fez.utils.ReturnJson;
import com.google.gson.Gson;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.J
 * 2019/1/14
 */
@RestController
@RequestMapping("model")
public class ModelController {

    @Autowired
    private RepositoryService repositoryService;

    private Gson gson = new Gson();

    @PostMapping("create")
    public String createModel(String modelName,String modelKey,String description){
        try{
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            RepositoryService repositoryService = processEngine.getRepositoryService();

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put("name", modelName);
            modelObjectNode.put("revision", 1);
            modelObjectNode.put("description", description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(modelName);
            modelData.setKey(modelKey);

            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
//            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
            return gson.toJson(modelData.getId());
        }catch (Exception e){
            e.printStackTrace();
            return gson.toJson(ReturnJson.ERROR());
        }
    }

    @GetMapping(value="/list")
    public Map<String,Object> selectAll(){
        List<Model> resultList =  repositoryService.createModelQuery().orderByCreateTime().desc().list();
        Map<String,Object> map = new HashMap<>();
        map.put("data",resultList);
        map.put("count",resultList.size());
        map.put("code",0);
        return map;
    }

    /**
     * 删除模型
     * @param modelId modelID
     */
    @DeleteMapping("delete")
    public String delete(String modelId){
        try{
            String modelIds[] = modelId.split(",");
            for(String model : modelIds){
                if(StringUtils.isNotEmpty(model)){
                    repositoryService.deleteModel(model);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return gson.toJson(ReturnJson.ERROR());
        }
        return gson.toJson(ReturnJson.SUCCESS());
    }

    /**
     * 获取model的XML
     * @param modelId modelID
     */
    @GetMapping("getBpmnXML")
    public Map<String,String> getBpmnXML(String modelId, org.springframework.ui.Model modelview){
        Map<String,String> map = new HashMap<>();
        try{
            Model model = repositoryService.getModel(modelId);
            byte[] modelEditorSource = repositoryService.getModelEditorSource(model.getId());


            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree((new String(modelEditorSource,"UTF-8")).getBytes("UTF-8"));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] exportBytes = xmlConverter.convertToXML(bpmnModel,"UTF-8");

            String XML =  new String(exportBytes,"UTF-8");

            map.put("xml",XML);
            map.put("modelId",modelId);
        }catch (Exception e){
            e.printStackTrace();
            map.put("xml","获取数据失败！");
            map.put("modelId","error");
        }
        return map;
    }

    /**
     * 保存XML
     * @param modelId modelID
     * @param bpmnXML XML
     * @return
     */
    @PostMapping("saveModelXML")
    public String saveModelXML(String modelId,String bpmnXML){
        try {
            String unescapeHtml = StringUtils.trim(StringEscapeUtils.unescapeHtml4(bpmnXML));
            InputStream in_nocode = new ByteArrayInputStream(unescapeHtml.getBytes("UTF-8"));
            XMLInputFactory xmlFactory  = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlFactory.createXMLStreamReader(in_nocode);

            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(reader);

            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode j =jsonConverter.convertToJson(bpmnModel);

            byte[] modelEditorSource = new ObjectMapper().writeValueAsBytes(j);

            System.out.println(new String(modelEditorSource,"UTF-8"));
            repositoryService.addModelEditorSource(modelId, modelEditorSource);
            return gson.toJson(ReturnJson.SUCCESS());
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ReturnJson.ERROR());
        }
    }


}

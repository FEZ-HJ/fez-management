package com.fez.activiti.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fez.activiti.cmd.ProcessInfoCmd;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by H.J
 * 2019/1/19
 */
@Service
public class DeployService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TbNodeService tbNodeService;

    public void deploy(String modelId) throws Exception{
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode;
        modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        byte[] bpmnBytes;

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        bpmnBytes = new BpmnXMLConverter().convertToXML(model,"UTF-8");

        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(
                processName, new String(bpmnBytes,"UTF-8")).deploy();

        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).list();

        for (ProcessDefinition pdf : processDefinitions) {
            processEngine.getManagementService().executeCommand(new ProcessInfoCmd(pdf.getId(),tbNodeService));
        }
    }

    @Transactional
    public void delete(String deploymentId,String procDefId){
        repositoryService.deleteDeployment(deploymentId);
        tbNodeService.deleteAllByProcDefId(procDefId);
    }

}

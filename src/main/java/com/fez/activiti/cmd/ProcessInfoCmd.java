package com.fez.activiti.cmd;

import com.fez.activiti.entity.TbNode;
import com.fez.activiti.service.TbNodeService;
import com.fez.utils.KeyUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by H.J
 * 2019/1/19
 * 保存流程节点信息
 */
public class ProcessInfoCmd implements Command<Void> {

    private String processDefinitionId;

    private TbNodeService repository;

    public ProcessInfoCmd(String processDefinitionId,TbNodeService repository){
        this.processDefinitionId = processDefinitionId;
        this.repository = repository;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        BpmnModel bpmnModel = new GetBpmnModelCmd(processDefinitionId).execute(commandContext);
        setUser(bpmnModel);
        return null;
    }

    private void setUser(@NotNull BpmnModel bpmnModel){
        Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        List<TbNode> nodes = new ArrayList<>();
        for(FlowElement flowElement : flowElements){
            TbNode tbNode = new TbNode();
            if(flowElement instanceof UserTask){
                tbNode.setId(KeyUtil.getUniqueKey());
                tbNode.setProcDefId(processDefinitionId);
                tbNode.setTaskDefKey(flowElement.getId());
                tbNode.setNodeName(flowElement.getName());
                tbNode.setNextUser(((UserTask) flowElement).getAssignee());
                nodes.add(tbNode);
            }else if(flowElement instanceof StartEvent){
            }
        }
        repository.save(nodes);
    }

}

package com.fez.activiti.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by H.J
 * 2019/1/19
 * 流程节点基础信息
 */
@Entity
@Data
public class TbNode {

    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 流程定义ID
     */
    private String procDefId;

    /**
     * 节点ID
     */
    private String taskDefKey;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点处理人表达式
     */
    private String nextUser;
}

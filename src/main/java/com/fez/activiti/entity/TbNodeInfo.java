package com.fez.activiti.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by H.J
 * 2019/1/19
 * 节点操作项
 */
@Entity
@Data
public class TbNodeInfo {

    /**
     * ID
     */
    @Id
    private String operationId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 操作项名称
     */
    private String operationName;

    /**
     * 下一步处理人SQL
     */
    private String operationSql;

    /**
     * 下一步选人模式
     * 0无 1单人 2多人
     */
    private String userType;

    /**
     * 操作项ID
     */
    private String operationKey;

    /**
     * 操作项展示条件
     */
    private String operationCondition;

}

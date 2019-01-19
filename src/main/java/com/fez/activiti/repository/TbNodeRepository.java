package com.fez.activiti.repository;

import com.fez.activiti.entity.TbNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by H.J
 * 2019/1/19
 */
public interface TbNodeRepository extends JpaRepository<TbNode,String> {
    void deleteAllByProcDefId(String procDefId);
    List<TbNode> findAllByProcDefIdAndNodeType(String procDefId, String noteType);
}

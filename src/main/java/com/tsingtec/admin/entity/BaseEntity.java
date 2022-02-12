package com.tsingtec.admin.entity;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tsingtec.admin.constants.Constants;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "bigint comment 'id'")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id =  IdUtil.getSnowflake(1).nextId();

    @LastModifiedDate
    @Column(columnDefinition = "datetime(0) comment '最后更新时间'")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;//最后更新时间

    @CreatedDate
    @Column(columnDefinition = "datetime(0) comment '创建时间'")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;//创建时间

    @Column(columnDefinition = "smallint comment '删除状态'")
    private Boolean delStatus = Constants.NO_DELETE_FLAG;
}
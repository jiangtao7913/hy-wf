package com.hy.wf.entity.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: hy-wf
 * @description: 实体基类
 * @author: jt
 * @create: 2019-01-04 10:11
 **/
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 92719013057818912L;

    /**
     * 主键
     */
    protected Long id;

    /**
     * 创建日期
     */
    protected Date createDate;

    /**
     * 修改日期
     */
    protected Date modifyDate;

    public enum DataStatus{
        invalid(0,"无效的"),
        valid(1,"有效的");

        public final int value;
        public final String remark;

        DataStatus(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }

    }
    /**
     * 数据状态
     */
    protected int dataStatus;

    /**
    * @Description: 初始化方法
    * @Param: [] 
    * @return: void 
    * @Author: jt 
    * @Date: 2019/1/4 
    */ 
    public void init(){
        Date date = new Date();
        this.createDate = date;
        this.modifyDate = date;
        this.dataStatus = DataStatus.valid.value;
    }
}

package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jtase
 * @create: 2019-03-12 17:13
 **/
@TableName("t_invitation_detail")
@Data
public class InvitationDetail extends BaseEntity {
    private static final long serialVersionUID = 521904836067820893L;

    private Long invitationId;
    private Long preticeId;
    private String preticeUid;
    private String name;

    private String meno;
    private BigDecimal incomeTotal;

    private int type;

    public enum Type{
        One(0,"一级徒弟"),
        Two(1,"二级徒弟"),

        ;
        public final  int value;
        public final String remark;

        Type(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
        public static Type valueOf(int value) {
            for (Type type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

}

package com.example.guanwang.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 陈志浩123
 * @since 2023-01-14
 */
@TableName("t_test")
public class TTest extends Model<TTest> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String test;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TTest{" +
        ", id=" + id +
        ", test=" + test +
        "}";
    }
}

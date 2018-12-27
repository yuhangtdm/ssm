package com.dity.ssm.source;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:yuhang
 * @Date:2018/12/27
 */
public class TestBeanWrapper {

    /**
     * 使用默认的属性编辑器
     */
    @Test
    public void test1(){
        TestModel model  = new TestModel();
        BeanWrapper beanWrapper = new BeanWrapperImpl(model);
        beanWrapper.setPropertyValue("good","yes");
        System.out.println(model.isGood());
    }

    /**
     * 不使用默认的属性编辑器
     * 报错
     */
    @Test
    public void test2(){
        TestModel tm = new TestModel();
        BeanWrapperImpl bw = new BeanWrapperImpl(false);
        bw.setWrappedInstance(tm);
        bw.setPropertyValue("good", "1");
        System.out.println(tm);
    }

    @Test
    public void test3(){
        TestModel tm = new TestModel();
        BeanWrapper bw = new BeanWrapperImpl(tm);
        bw.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        bw.setPropertyValue("birth", "1990-01-01");
        System.out.println(tm.getBirth());
    }
}

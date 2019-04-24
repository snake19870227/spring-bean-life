package com.github.snake.spring.cars;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author BuHuaYang
 * @date 2019/4/23
 */
public abstract class AbstractCar implements Car, BeanNameAware, InitializingBean, DisposableBean {

    private String beanName;

    private String name;

    public AbstractCar(String name) {
        this.name = name;
        System.out.println(this.name + " 准备组装");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void run() {
        System.out.println(this.name + " 正在行驶...");
    }

    @Override
    public void stop() {
        System.out.println(this.name + " 停止行驶");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this.name + " 组装中...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println(this.name + " 正在拆解...");
    }
}

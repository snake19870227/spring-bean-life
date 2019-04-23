package com.github.snake.spring.cars;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author BuHuaYang
 * @date 2019/4/23
 */
public abstract class AbstractCar implements Car, InitializingBean {

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
    public void run() {
        System.out.println(this.name + " 正在行驶...");
    }

    @Override
    public void stop() {
        System.out.println(this.name + " 停止行驶");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this.name + " 组装中...");
    }
}

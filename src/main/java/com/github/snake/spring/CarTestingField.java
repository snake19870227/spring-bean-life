package com.github.snake.spring;

import com.github.snake.spring.cars.Car;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * @author BuHuaYang
 * @date 2019/4/23
 */
@Component
public class CarTestingField implements InitializingBean {

    private Car[] cars;

    public CarTestingField(ObjectProvider<Car[]> cars) {
        this.cars = cars.getIfAvailable();
    }

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StringBuilder carNameStr = new StringBuilder();
        for (Car car : cars) {
            carNameStr.append(car.getName()).append(" ");
        }
        System.out.println(carNameStr + "已进入试车场");
    }

    public void startTesting() {
        System.out.println("开始试车...");
        for (Car car : cars) {
            car.run();
        }
        System.out.println("已全部进行试车");
    }

    public void stopTesting() {
        System.out.println("停止试车...");
        for (Car car : cars) {
            car.stop();
        }
        System.out.println("已全部停止试车");
    }
}

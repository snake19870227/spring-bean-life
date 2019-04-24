package com.github.snake.spring;

import com.github.snake.spring.cars.Car;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * @author BuHuaYang
 * @date 2019/4/2
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.github.snake.spring"
        }
)
public class BeanLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanLifeApplication.class);
    }

    @Component
    public static class CustomBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof Car) {
                Car car = (Car) bean;
                System.out.println(car.getName() + " 开始组装");
            }
            if (bean instanceof CarTestingField) {
                System.out.println("试车场开始准备");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof Car) {
                Car car = (Car) bean;
                System.out.println(car.getName() + " 完成组装 ============");
            }
            if (bean instanceof CarTestingField) {
                System.out.println("试车场准备就绪");
            }
            return bean;
        }

    }

    @Configuration
    public static class CustomBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

        private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

        private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

            String basePackage = StringUtils.substringBeforeLast(BeanLifeApplication.class.getName(), ".") + ".cars";
            String basePackagePath = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackagePath + '/' + "**/*.class";
            try {
                Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
                        if (Modifier.isAbstract(clazz.getModifiers())) {
                            continue;
                        }
                        Class[] ifaces = ClassUtils.getAllInterfacesForClass(clazz);
                        for (Class iface : ifaces) {
                            if (iface == Car.class) {
                                BeanDefinition carBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition();
                                registry.registerBeanDefinition(this.beanNameGenerator.generateBeanName(carBeanDefinition, registry), carBeanDefinition);
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        }
    }

    @Component
    public static class CarTestingFieldWorking implements CommandLineRunner {

        private CarTestingField carTestingField;

        public CarTestingFieldWorking(CarTestingField carTestingField) {
            this.carTestingField = carTestingField;
        }

        @Override
        public void run(String... args) throws Exception {
            this.carTestingField.startTesting();
            this.carTestingField.stopTesting();
        }
    }

}

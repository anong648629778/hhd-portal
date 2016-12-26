package cn.hhd.portal.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * spring bean工具类，用于动态获取spring容器中的bean
 *
 */
public class SpringBeanUtil implements BeanFactoryAware {
	
	private static BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		if (null != beanFactory && StringUtils.isNotEmpty(beanName)) {
			return (T) beanFactory.getBean(beanName);
		}
		return null;
	}
}

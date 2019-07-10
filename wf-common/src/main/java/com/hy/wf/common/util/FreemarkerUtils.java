package com.hy.wf.common.util;

import com.hy.wf.common.Constant;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utils - Freemarker <br/>
 *
 * @author JinXing Team
 * @email
 * @date: 2018-4-8 上午2:51:21
 * @version 1.0
 */
public class FreemarkerUtils {

	/** ConvertUtilsBean */
	private static final ConvertUtilsBean convertUtils;

	static {
		convertUtils = new ConvertUtilsBean() {
			@Override
			public String convert(Object value) {
				if (value != null) {
					Class<?> type = value.getClass();
					if (type.isEnum() && super.lookup(type) == null) {
						super.register(new EnumConverter(type), type);
					} else if (type.isArray() && type.getComponentType().isEnum()) {
						if (super.lookup(type) == null) {
							ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
							arrayConverter.setOnlyFirstToString(false);
							super.register(arrayConverter, type);
						}
						Converter converter = super.lookup(type);
						return ((String) converter.convert(String.class, value));
					}
				}
				return super.convert(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum() && super.lookup(clazz) == null) {
					super.register(new EnumConverter(clazz), clazz);
				}
				return super.convert(value, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String[] values, Class clazz) {
				if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
					super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
				}
				return super.convert(values, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object value, Class targetType) {
				if (super.lookup(targetType) == null) {
					if (targetType.isEnum()) {
						super.register(new EnumConverter(targetType), targetType);
					} else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
						ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, targetType);
					}
				}
				return super.convert(value, targetType);
			}
		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(Constant.DATE_PATTERNS);
		convertUtils.register(dateConverter, Date.class);
	}

	/**
	 * 不可实例化
	 */
	private FreemarkerUtils() {
	}

	/**
	 * 解析字符串模板
	 *
	 * @param template 字符串模板
	 * @param model 数据
	 * @return 解析后内容
	 */
	public static String process(String template, Map<String, ?> model) throws IOException, TemplateException {
		return process(template, model, new Configuration(Configuration.VERSION_2_3_23));
	}

	/**
	 * 解析字符串模板
	 *
	 * @param template 字符串模板
	 * @param model 数据
	 * @param configuration  配置
	 * @return 解析后内容
	 */
	public static String process(String template, Map<String, ?> model, Configuration configuration) throws IOException, TemplateException {
		if (template == null) {
			return null;
		}
		if (configuration == null) {
			configuration = new Configuration(Configuration.VERSION_2_3_23);
		}
		StringWriter out = new StringWriter();
		new Template("template", new StringReader(template), configuration).process(model, out);
		return out.toString();
	}

	/**
	 * 获取参数
	 *
	 * @param name 名称
	 * @param type 类型
	 * @param params 参数
	 * @return 参数,若不存在则返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getParameter(String name, Class<T> type, Map<String, TemplateModel> params) throws TemplateModelException {
		Assert.hasText(name, "name must not be empty");
		Assert.notNull(type, "type is required");
		Assert.notNull(params, "params is required");
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return null;
		}
		Object value = DeepUnwrap.unwrap(templateModel);
		return (T) convertUtils.convert(value, type);
	}

	/**
	 * 获取变量
	 *
	 * @param name 名称
	 * @param environment Environment
	 * @return 变量
	 */
	public static TemplateModel getVariable(String name, Environment environment) throws TemplateModelException {
		Assert.hasText(name, "name must not be empty");
		Assert.notNull(environment, "environment is required");
		return environment.getVariable(name);
	}

	/**
	 * 设置变量
	 *
	 * @param name 名称
	 * @param value 变量值
	 * @param environment Environment
	 */
	public static void setVariable(String name, Object value, Environment environment) throws TemplateException {
		Assert.hasText(name, "name must not be empty");
		Assert.notNull(environment, "environment is required");
		if (value instanceof TemplateModel) {
			environment.setVariable(name, (TemplateModel) value);
		} else {
			BeansWrapperBuilder beansWrapperBuilder = new BeansWrapperBuilder(Configuration.VERSION_2_3_23);
			beansWrapperBuilder.setExposeFields(true);
			ObjectWrapper objectWrapper = beansWrapperBuilder.build();
			environment.setVariable(name, objectWrapper.wrap(value));
		}
	}

	/**
	 * 设置变量
	 *
	 * @param variables 变量
	 * @param  environment
	 */
	public static void setVariables(Map<String, Object> variables, Environment environment) throws TemplateException {
		Assert.notNull(variables, "variables environment is required");
		Assert.notNull(environment, "environment is required");
		for (Entry<String, Object> entry : variables.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof TemplateModel) {
				environment.setVariable(name, (TemplateModel) value);
			} else {
				BeansWrapperBuilder beansWrapperBuilder = new BeansWrapperBuilder(Configuration.VERSION_2_3_23);
				beansWrapperBuilder.setExposeFields(true);
				ObjectWrapper objectWrapper = beansWrapperBuilder.build();
				environment.setVariable(name, objectWrapper.wrap(value));
			}
		}
	}
}
package com.asl.config;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.asl.enums.ReportMenu;
import com.asl.interceptor.MenuAccessAuthorizationInterceptor;
import com.asl.interceptor.ReportAccessAuthorizationInterceptor;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(
				"classpath:/messages/messages",
				"classpath:/messages/messages-salesandinvoice"
		);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public MenuAccessAuthorizationInterceptor menuAccessInterceptor() {
		return new MenuAccessAuthorizationInterceptor();
	}

	@Bean
	public ReportAccessAuthorizationInterceptor reportAccessInterceptor() {
		return new ReportAccessAuthorizationInterceptor();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(menuAccessInterceptor()).addPathPatterns(getMenuPaths()); 
		//registry.addInterceptor(reportAccessInterceptor()).addPathPatterns(getReportPaths());
		registry.addInterceptor(localeChangeInterceptor());
	}

	private String[] getMenuPaths() {
		List<String> paths = new ArrayList<>();
		EnumSet.allOf(com.asl.enums.MenuProfile.class).forEach(mp -> {
			paths.add("/" + mp.getMenuPath() + "/**");
		});
		return paths.toArray(new String[paths.size()]);
	}

	private String[] getReportPaths() {
		List<String> paths = new ArrayList<>();
		EnumSet.allOf(ReportMenu.class).forEach(rm -> {
			paths.add("/report/" + rm.name() + "/**");
		});
		return paths.toArray(new String[paths.size()]);
	}
}

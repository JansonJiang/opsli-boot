/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.conf;

import org.opsli.core.waf.WafProperties;
import org.opsli.core.waf.filter.WafFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * 软件防火墙
 * 防止XSS SQL 攻击
 *
 * @author Parker
 * @date 2020-10-09
 */
@Configuration
@EnableConfigurationProperties({WafProperties.class})
public class WafConfig {

	@Autowired
	WafProperties wafProperties;

	@Bean
	@ConditionalOnProperty(prefix = WafProperties.WAF, name = "enable", havingValue = "true", matchIfMissing = false)
	public FilterRegistrationBean xssFilterRegistration() {
		WafFilter wafFilter = new WafFilter();
		wafFilter.setUrlExclusion(wafProperties.getUrlExclusion());
		wafFilter.setEnableSqlFilter(wafProperties.isSqlFilter());
		wafFilter.setEnableXssFilter(wafProperties.isXssFilter());

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(wafFilter);
		registration.addUrlPatterns(wafProperties.getUrlPatterns());
		registration.setName(wafProperties.getName());
		registration.setOrder(wafProperties.getOrder());
		return registration;
	}


}

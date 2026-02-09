package com.ahmete.budget_app.budget.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "budget.alert.thresholds")
public class BudgetAlertProperties {
	private BigDecimal warning;
	private BigDecimal exceeded;
	private BigDecimal critical;
}
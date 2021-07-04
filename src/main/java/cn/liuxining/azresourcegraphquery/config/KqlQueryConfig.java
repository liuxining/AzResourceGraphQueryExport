package cn.liuxining.azresourcegraphquery.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p>Title: KqlQueryConfig </p >
 * <p>Description: TODO </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 23:16
 * @version: v1.0
 */
@Configuration
@ConfigurationProperties(prefix = "azure")
public class KqlQueryConfig {

    private List<String> subscriptionId;

    public List<String> getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(List<String> subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    public String toString() {
        return "KqlQueryConfig{" +
                "subscriptionId=" + subscriptionId +
                '}';
    }
}

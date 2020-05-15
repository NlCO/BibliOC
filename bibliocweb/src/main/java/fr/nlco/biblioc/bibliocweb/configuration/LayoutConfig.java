package fr.nlco.biblioc.bibliocweb.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingRespectLayoutTitleStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration du Layout Dialect de Thymeleaf pour le templating des pages HTML
 */
@Configuration
public class LayoutConfig {
    /**
     * Configuration de la strategie de templating
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect(new GroupingRespectLayoutTitleStrategy());
    }
}

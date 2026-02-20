package mcstorage;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
	
	public static final String EXCHANGE = "file.exchange";
  public static final String ROUTING_KEY_UPLOADED = "file.uploaded";
  public static final String ROUTING_KEY_DELETED = "file.deleted";

  @Bean
  public TopicExchange exchange() {
      return new TopicExchange(EXCHANGE);
  }
  
  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
  	return new Jackson2JsonMessageConverter();
  }
}

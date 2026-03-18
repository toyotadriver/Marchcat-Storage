package mcstorage;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
	
	public static final String EVENT_EXCHANGE = "file.event.exchange";
	public static final String UPLOADED_EVENT_KEY = "file.uploaded";
  public static final String DELETED_EVENT_KEY = "file.deleted";

  @Bean
  public TopicExchange exchange() {
      return new TopicExchange(EVENT_EXCHANGE);
  }
  
  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
  	return new Jackson2JsonMessageConverter();
  }
}

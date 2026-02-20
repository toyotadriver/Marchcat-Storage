package mcstorage.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import mcstorage.RabbitConfiguration;

@Component
public class EventPublisher {
	private final RabbitTemplate rabbitTemplate;
	public EventPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
		//rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter); - autowired
	}

  public void publishFileUploaded(StoredFileRecord record) {

      rabbitTemplate.convertAndSend(
              RabbitConfiguration.EXCHANGE,
              RabbitConfiguration.ROUTING_KEY_UPLOADED,
              record
      );
  }
  
  public void publishFileDeleted(StoredFileRecord record) {

    rabbitTemplate.convertAndSend(
            RabbitConfiguration.EXCHANGE,
            RabbitConfiguration.ROUTING_KEY_DELETED,
            record
    );
}
}

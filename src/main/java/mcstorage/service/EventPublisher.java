package mcstorage.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
              RabbitConfiguration.EVENT_EXCHANGE,
              RabbitConfiguration.UPLOADED_EVENT_KEY,
              record
      );
  }
  
  public void publishFileDeleted(StoredFileRecord record) {

    rabbitTemplate.convertAndSend(
            RabbitConfiguration.EVENT_EXCHANGE,
            RabbitConfiguration.DELETED_EVENT_KEY,
            record
    );
}
}

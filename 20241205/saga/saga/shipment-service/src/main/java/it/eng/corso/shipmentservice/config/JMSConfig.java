package it.eng.corso.shipmentservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@Slf4j
public class JMSConfig {
    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener( destination="shipment.spedizione", containerFactory = "jmsListenerContainerFactory")
    public void paga(Double importo ){
        log.info("è stata richiesto il pagamento di un importo pari a {}", importo);

        jmsTemplate.convertAndSend("order.orchestratore", "SPEDIZIONEOK");
    }

}

package it.eng.corso.storeservce.config;

import it.eng.corso.commonsaga.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;

@Configuration
@Slf4j
public class JMSConfig {
    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener( destination="store.prenota", containerFactory = "jmsListenerContainerFactory")
    public void prenota(List<ItemDTO> items ){
        log.info("è stata richiesta la prenotazione per i prodotti {}", items);

        jmsTemplate.convertAndSend("order.orchestratore", "PRENOTAZIONEOK");
    }

    @JmsListener( destination="store.annullaprenotazione", containerFactory = "jmsListenerContainerFactory")
    public void annullaprenotazione(List<ItemDTO> items ){
        log.info("è stata richiesto l'annullamento della prenotazione per i prodotti {}", items);

        jmsTemplate.convertAndSend("order.orchestratore", "ANNULLAMENTOPRENOTAZIONEOK");
    }

}

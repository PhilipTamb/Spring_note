package it.eng.corso.orderservice.saga;

import it.eng.corso.commonsaga.dto.OrderDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class OrderSaga {

    private OrderDTO orderDTO;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void startSaga() {
        log.info("avvio saga");
        chiamaStore();
    }
    public void chiamaStore(){
        log.info("invio un messaggio al servizio store-service");
        jmsTemplate.convertAndSend("store.prenota", orderDTO.getItems() );
    }

    public void annullaPrenotazione() {
        log.info("invio un messaggio al servizio store-service per annullare una prenotazione");
        jmsTemplate.convertAndSend("store.annullaprenotazione", orderDTO.getItems() );
    }

    public void chiamaPayment(){
        log.info("invio un messaggio al servizio payment-service");
        jmsTemplate.convertAndSend("payment.paga", 1000.0 );
    }

    public void chiamaShipment(){
        log.info("invio un messaggio al servizio shipment-service");
        jmsTemplate.convertAndSend("shipment.spedizione", orderDTO.getItems() );
    }

    public void annullaStore(){
        log.info("invio un messaggio di annullamento al servizio store-service");
    }

    public void annullaPayment(){
        log.info("invio un messaggio di annullamento al servizio payment-service");
    }

    public void annullaShipment(){
        log.info("invio un messaggio di annullamento al servizio shipment-service");
    }


    public void finish() {
    }

    public void annullaPagamento() {
    }
}

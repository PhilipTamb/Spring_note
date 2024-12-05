package it.eng.corso.orderservice.config;

import it.eng.corso.commonsaga.dto.ItemDTO;
import it.eng.corso.orderservice.saga.OrderSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;

import java.util.List;

@Configuration
@Slf4j
public class JMSConfig {

    @Autowired
    private OrderSaga orderSaga;

    @JmsListener( destination="order.orchestratore", containerFactory = "jmsListenerContainerFactory")
    public void orchestra(String esito){
        log.info("ho ricevuto l'esito di una operazione : {}", esito);

        if( "PRENOTAZIONEOK".equals( esito ) ){
            orderSaga.chiamaPayment();
        } else if( "PAGAMENTOOK".equals( esito ) ){
            orderSaga.chiamaShipment();
        } if( "SPEDIZIONE_OK".equals( esito ) ){
            orderSaga.finish();
        } else if( "PAGAMENTO_KO".equals( esito ) ){
            orderSaga.annullaPrenotazione();
        } else if( "SPEDIZIONE_KO".equals( esito ) ){
            orderSaga.annullaPagamento();
        } else if( "ANNULLAMENTO_PAGAMENTO_OK".equals( esito ) ){
            orderSaga.annullaPrenotazione();
        }
    }
}

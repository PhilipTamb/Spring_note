package it.eng.corso.orderservice.controller;

import it.eng.corso.commonsaga.dto.ItemDTO;
import it.eng.corso.commonsaga.dto.OrderDTO;
import it.eng.corso.orderservice.saga.OrderSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SagaController {

    @Autowired
    private OrderSaga orderSaga;

    @GetMapping("/ordina")
    public String ordina(){
        orderSaga.setOrderDTO(
                OrderDTO.builder().orderID(1l).customerID(1l)
                        .items( List.of(
                                ItemDTO.builder().productID(1l).quantity(2d).price(9.99).build(),
                                ItemDTO.builder().productID(2l).quantity(2.5).price(0.99).build(),
                                ItemDTO.builder().productID(3l).quantity(21d).price(19.99).build()
                                )
                        ).build()
        );

        orderSaga.startSaga();

        return "ordine inviato";
    }
}

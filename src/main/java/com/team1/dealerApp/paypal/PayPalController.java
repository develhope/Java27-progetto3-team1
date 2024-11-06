package com.team1.dealerApp.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/paypal" )
public class PayPalController {

	private final PayPalService payPalService;

	@GetMapping( "/success" )
	public ResponseEntity < ? > successPayment( @RequestParam( "paymentId" ) String paymentId, @RequestParam( "PayerID" ) String payerId ) throws PayPalRESTException {

		// Esegui il pagamento con l'ID ricevuto
		Payment payment = payPalService.executePayment(paymentId, payerId);

		if ( "approved".equals(payment.getState()) ) {
			// Se il pagamento è approvato, ritorna una conferma
			return ResponseEntity.ok("Pagamento completato con successo");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pagamento non approvato");
		}
	}

	@GetMapping( "/cancel" )
	public ResponseEntity < ? > cancelPayment() {
		// Restituisci un messaggio di annullamento per informare l'utente
		return ResponseEntity.ok("Pagamento annullato dall'utente.");
	}
}

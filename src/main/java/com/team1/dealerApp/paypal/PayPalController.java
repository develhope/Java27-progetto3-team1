package com.team1.dealerApp.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.dealerApp.purchase.PurchaseService;
import com.team1.dealerApp.rental.RentalService;
import com.team1.dealerApp.subscription.SubscriptionService;
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
@SuppressWarnings("unused")
public class PayPalController {

	private final PayPalService payPalService;
	private final PurchaseService purchaseService;
	private final RentalService rentalService;
	private final SubscriptionService subscriptionService;

	@GetMapping( "/success/purchase" )
	public ResponseEntity < ? > successPayment( @RequestParam( "paymentId" ) String paymentId, @RequestParam( "PayerID" ) String payerId, @RequestParam ("orderId") Long orderId) throws PayPalRESTException {

		// Esegui il pagamento con l'ID ricevuto
		Payment payment = payPalService.executePayment(paymentId, payerId);

		if ( "approved".equals(payment.getState()) ) {
			purchaseService.updatePurchaseStatus(orderId, "paid");
			// Se il pagamento è approvato, ritorna una conferma
			return ResponseEntity.ok("Pagamento completato con successo");
		} else {
			purchaseService.updatePurchaseStatus(orderId, "canceled");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pagamento non approvato");
		}
	}

	@GetMapping( "/success/rental" )
	public ResponseEntity < ? > successRentalPayment( @RequestParam( "paymentId" ) String paymentId, @RequestParam( "PayerID" ) String payerId, @RequestParam ("orderId") Long orderId) throws PayPalRESTException {

		// Esegui il pagamento con l'ID ricevuto
		Payment payment = payPalService.executePayment(paymentId, payerId);

		if ( "approved".equals(payment.getState()) ) {
			rentalService.updateRentalStatus(orderId, "active");
			// Se il pagamento è approvato, ritorna una conferma
			return ResponseEntity.ok("Pagamento completato con successo");
		} else {
			rentalService.updateRentalStatus(orderId, "suspended");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pagamento non approvato");
		}
	}

	@GetMapping( "/cancel" )
	public ResponseEntity < ? > cancelPayment() {
		// Restituisci un messaggio di annullamento per informare l'utente
		return ResponseEntity.ok("Pagamento annullato dall'utente.");
	}

	@GetMapping( "/success/user" )
	public ResponseEntity < ? > successSubscriptionPayment( @RequestParam( "paymentId" ) String paymentId, @RequestParam( "PayerID" ) String payerId, @RequestParam ("orderId") Long orderId) throws PayPalRESTException {

		// Esegui il pagamento con l'ID ricevuto
		Payment payment = payPalService.executePayment(paymentId, payerId);

		if ( "approved".equals(payment.getState()) ) {
			subscriptionService.updateSubscriptionStatus(orderId, true);
			rentalService.updateRentalStatus(orderId, "active");
			// Se il pagamento è approvato, ritorna una conferma
			return ResponseEntity.ok("Pagamento completato con successo");
		} else {
			rentalService.updateRentalStatus(orderId, "false");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pagamento non approvato");
		}
	}
}

package com.team1.cineBox.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.team1.cineBox.purchase.PurchaseService;
import com.team1.cineBox.rental.RentalService;
import com.team1.cineBox.subscription.SubscriptionService;
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
			return ResponseEntity.ok("Payment completed successfully");
		} else {
			purchaseService.updatePurchaseStatus(orderId, "canceled");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved");
		}
	}

	@GetMapping( "/success/rental" )
	public ResponseEntity < ? > successRentalPayment( @RequestParam( "paymentId" ) String paymentId, @RequestParam( "PayerID" ) String payerId, @RequestParam ("orderId") Long orderId) throws PayPalRESTException {

		// Esegui il pagamento con l'ID ricevuto
		Payment payment = payPalService.executePayment(paymentId, payerId);

		if ( "approved".equals(payment.getState()) ) {
			rentalService.updateRentalStatus(orderId, "active");
			// Se il pagamento è approvato, ritorna una conferma
			return ResponseEntity.ok("Payment completed successfully");
		} else {
			rentalService.updateRentalStatus(orderId, "suspended");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved");
		}
	}

	@GetMapping( "/cancel" )
	public ResponseEntity < String > cancelPayment() {
		// Restituisci un messaggio di annullamento per informare l'utente
		return ResponseEntity.ok("Payment canceled by the user");
	}

	@GetMapping( "/success/user" )
	public ResponseEntity < ? > successSubscriptionPayment( @RequestParam( "paymentId" ) String paymentId, @RequestParam( "PayerID" ) String payerId, @RequestParam ("orderId") Long orderId) throws PayPalRESTException {

		// Esegui il pagamento con l'ID ricevuto
		Payment payment = payPalService.executePayment(paymentId, payerId);

		if ( "approved".equals(payment.getState()) ) {
			subscriptionService.updateSubscriptionStatus(orderId, true);
			// Se il pagamento è approvato, ritorna una conferma
			return ResponseEntity.ok("Payment completed successfully");
		} else {
			subscriptionService.updateSubscriptionStatus(orderId,  false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment not approved");
		}
	}
}

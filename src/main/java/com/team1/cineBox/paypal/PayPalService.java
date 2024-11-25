package com.team1.cineBox.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PayPalService {

	private final APIContext apiContext;

	public Payment createPayment( Double total, String currency, String method, String intent, String description, String returnUrl ) throws PayPalRESTException {
		Amount amount = new Amount(); //Totale acquisto
		amount.setCurrency(currency);
		amount.setTotal(String.format(Locale.US, "%.2f", total));

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);

		List < Transaction > transactions = new ArrayList <>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(method);

		Payment payment = new Payment();
		payment.setIntent(intent); //Scopo del pagamento(sale-> pagamento immediato / authorize-> crea autorizzazione che effetua pagamento posticipato / order-> crea order
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls(); //Link a cui ti reindizza paypal (possono essere sia statici che dinamici)
		redirectUrls.setCancelUrl("http://localhost:8080/api/paypal/cancel");
		redirectUrls.setReturnUrl(returnUrl);
		payment.setRedirectUrls(redirectUrls);

		return payment.create(apiContext);
	}

	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {

		Payment payment = new Payment();
		payment.setId(paymentId);

		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);

		return payment.execute(apiContext, paymentExecution);
	}
}

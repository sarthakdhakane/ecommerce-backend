package com.sarthak.ecommerce.web;
import com.sarthak.ecommerce.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/webhooks")
public class StripeWebhookController {
 private final PaymentService payments; private final String webhookSecret;
 public StripeWebhookController(PaymentService p,@Value("${stripe.webhook-secret:}") String s){payments=p;webhookSecret=s;}
 @PostMapping("/stripe") public ResponseEntity<String> stripe(@RequestBody String payload,@RequestHeader("Stripe-Signature") String signature){
  if(webhookSecret.isBlank())return ResponseEntity.status(503).body("Webhook secret is not configured");
  try{
   Event event=Webhook.constructEvent(payload,signature,webhookSecret);
   if("checkout.session.completed".equals(event.getType())){
    var obj=event.getDataObjectDeserializer().getObject();
    if(obj.isPresent() && obj.get() instanceof Session session) payments.handleCheckoutCompleted(session.getId(),session.getMetadata().get("orderId"));
   }
   return ResponseEntity.ok("received");
  }catch(Exception e){return ResponseEntity.badRequest().body("Invalid webhook");}
 }
}

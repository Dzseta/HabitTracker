const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const stripe = require('stripe')('sk_test_51PBmI3Rrfhw85rRuC2jOQka95o1mOPRnaXEL0rg4Px7IJKtKYyFSxzRi1KMHgbykbfbkGUFnG2sRDm8MytuLZO8900DWn21hJc');

exports.stripePayment = onRequest(async (request, response) => {
    const amount = request.query.amt;
    const customer = await stripe.customers.create(
        {
          'name': 'user',
          'address': {
            'line1': 'Demo address',
            'postal_code': '5700',
            'city': 'Gyula',
            'state': 'Békés',
            'country': 'HU'
          }
        }
    );
    const ephemeralKey = await stripe.ephemeralKeys.create(
        {customer: customer.id},
        {apiVersion: '2024-04-10'}
    );
    const paymentIntent = await stripe.paymentIntents.create({
        amount: amount,
        currency: 'huf',
        description: 'Payment for premium',
        customer: customer.id,
        automatic_payment_methods: {
            enabled: true,
        },
    });
    
    response.json({
        paymentIntent: paymentIntent.client_secret,
        ephemeralKey: ephemeralKey.secret,
        customer: customer.id,
        publishableKey: 'pk_test_51PBmI3Rrfhw85rRuLbkFFnlGLpmR5SFaQp3zY1m3QU5iMqn6zc4NibeLEuYKYXLsIS9gsHqzg2dUzXMV9LlXknmu00e8v0DHCF'
    });
});

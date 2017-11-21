#!/usr/bin/env bash

# generate 2048 private-public key pair
openssl genrsa -out mykey.pem 2048

# generates private key. You will use this key to sign JWT tokens.
openssl pkcs8 -topk8 -inform PEM -outform PEM -in mykey.pem -out rsa-private.pem -nocrypt

# generates rsa-public.pem that contains public key only. You can use this to test validating the token on https://jwt.io
openssl rsa -in mykey.pem -outform PEM -pubout -out rsa-public.pem

# generates public key certificate. You can skip the questions that openssl will ask. Send the generated rsa-public.cer to GK SWT.
openssl req -new -x509 -key mykey.pem -out rsa-public.cer -days 36500

# remove unnecessary files
rm mykey.pem
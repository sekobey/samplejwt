package logic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import util.PemUtils;

import javax.security.cert.CertificateException;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtOperations {

    /**
     * You can generate private and public keys by using the generate_keys.sh file under src/main/resources.
     */
    private static final String PUBLIC_KEY_CERT_FILE = "src/main/resources/rsa-public.cer";
    private static final String PRIVATE_KEY_FILE_RSA = "src/main/resources/rsa-private.pem";

    private RSAPublicKey yourPublicKey;
    private RSAPrivateKey yourPrivateKey;

    public JwtOperations() {
        try {
            yourPublicKey = (RSAPublicKey) PemUtils.readPublicKeyFromCert(PUBLIC_KEY_CERT_FILE);
            yourPrivateKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(PRIVATE_KEY_FILE_RSA, "RSA");
        } catch (IOException | CertificateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * You can use the code here to generate a new JWT. This method creates a 30-minute usable JWT with the given claim1 and claim2.
     * You can set an issuer, or remove that part.
     * We used auth0 java-jwt API, so you can find API details in here:
     * https://github.com/auth0/java-jwt
     *
     * @param claim1
     * @param claim2
     * @return
     */
    public String createJwt(String claim1, String claim2) {
        Map<String,Object> headerClaims = new HashMap<>();
        headerClaims.put("x5u", "rsa-public.pem URL");
        String signed = JWT.create()
                .withHeader(headerClaims)
                .withIssuer("testIssuer")
                .withClaim("claim1", claim1)
                .withClaim("claim2", claim2)
                .withIssuedAt(new Date())
                .withExpiresAt(createExpireDate(30 * 60))
                .sign(Algorithm.RSA256(yourPrivateKey));
        return signed;
    }

    /**
     * You can use the code here to verify a JWT. This method can be used for test purposes to see if the generated JWT is valid.
     * It will be used by GWT application when validating token generated by clarify.
     * You can also see the test code under src/main/test.
     *
     * @param token
     * @return
     */
    public DecodedJWT verifyJwt(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(yourPublicKey))
                .acceptLeeway(1) // 1 sec for nbf, iat and exp
                .acceptExpiresAt(30 * 60) // 30 minutes
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    private Date createExpireDate(int seconds) {
        return new Date(System.currentTimeMillis() + seconds * 1000);
    }


}

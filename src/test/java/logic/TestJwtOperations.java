package logic;


import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestJwtOperations {

    @Test
    public void testCreateAndVerifyJwt() {
        String claim1 = "testUser";
        String claim2 = "tier3";

        JwtOperations jwtOps = new JwtOperations();
        String jwt = jwtOps.createJwt(claim1, claim2);
        DecodedJWT decodedJWT = jwtOps.verifyJwt(jwt);

        Map<String, Claim> claims = decodedJWT.getClaims();
        assertEquals(claim1, claims.get("claim1").asString());
        assertEquals(claim2, claims.get("claim2").asString());


        System.out.println("JWT: " + jwt + "\n");
        System.out.println("If you want to test this token with an external party, paste this token into 'Encoded' textarea on https://jwt.io web site. \n" +
                "You must see the header and payload parts of the token.\n" +
                "Also, you can verify the token by passing your private and public keys into the related areas in 'Verify Signature' part.");
    }
}

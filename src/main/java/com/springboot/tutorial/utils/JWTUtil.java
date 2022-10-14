package com.springboot.tutorial.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author Mahesh
 */

/*La notación Component nos sirve también
* para poder compartirlo en todos los lugares
* y además nos permite usar también las anotaciones
* que utilizamos aquí.*/
@Component
public class JWTUtil {
    /*@value:
    * lo que hace esta anotación es cargarle
    * al atributo que declaramos el property
    * que tengamos en el archivo de propiedades
    * de nuestra preferencia en nuestro caso
    * application.properties.*/
    @Value("${security.jwt.secret}")
    private String key;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.ttlMillis}")
    private long ttlMillis;

    private final Logger log = LoggerFactory
            .getLogger(JWTUtil.class);

    /**
     * Create a new token.
     *
     * @param id
     * @param subject
     * @return
     */
    /*Metodo create:
    * este metodo basicamente crea el JWT.
    * en otra palabras creea el texto de
    * informacion que le pasamos al cliente
    * lo genera.*/
    public String create(String id, String subject) {

        // El algoritmo de firma JWT utilizado para firmar el token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //  firmar JWT con nuestro secreto ApiKey
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //  establecer las solicitudes de JWT
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // Construye el JWT y lo serializa en un String compacto y seguro para la URL.
        return builder.compact();
    }

    /**
     * Method to validate and read the JWT
     *
     * @param jwt
     * @return
     */

    /*Tanto el getValue como el getKey nos devuelven información que le hayamos
    * brindado a este token.*/
    public String getValue(String jwt) {
        // Esta línea lanzará una excepción si no es un JWT firmado (como se espera)
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt).getBody();

        return claims.getSubject();
    }

    /**
     * Method to validate and read the JWT
     *
     * @param jwt
     * @return
     */
    public String getKey(String jwt) {
        // Esta línea lanzará una excepción si no es un JWT firmado (como se espera)
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt).getBody();

        return claims.getId();
    }
}
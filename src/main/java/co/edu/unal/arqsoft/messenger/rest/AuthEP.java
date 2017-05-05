package co.edu.unal.arqsoft.messenger.rest;

import co.edu.unal.arqsoft.messenger.businesslogic.AuthBL;
import co.edu.unal.arqsoft.messenger.dto.UserDTO;
import co.edu.unal.arqsoft.messenger.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import co.edu.unal.arqsoft.messenger.security.SecurityConfig;

/**
 *
 * @author alex
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthEP {
    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        try {
            UserDTO user = AuthBL.login(email, password);
            if (user.id == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                user.token = Jwts.builder()
                    .setSubject(email)
                    .signWith(SignatureAlgorithm.HS512, SecurityConfig.key)
                    .compact();
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody UserDTO user) {
        try {
            User nuevo = AuthBL.register(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

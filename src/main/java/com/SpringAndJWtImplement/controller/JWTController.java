package com.SpringAndJWtImplement.controller;

import com.SpringAndJWtImplement.entity.RefreshToken;
import com.SpringAndJWtImplement.entity.User;
import com.SpringAndJWtImplement.helper.JwtUtil;
import com.SpringAndJWtImplement.model.JWtResponse;
import com.SpringAndJWtImplement.model.RefreshTokenRequest;
import com.SpringAndJWtImplement.model.JwtRequest;
import com.SpringAndJWtImplement.service.CustomUserDetailsService;
import com.SpringAndJWtImplement.service.RefreshTokenService;
import com.SpringAndJWtImplement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class JWTController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//    @RequestMapping(value = "/token",method = RequestMethod.POST)
//    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
//        System.out.println(jwtRequest);
//        try {
//            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    jwtRequest.getUsername(),jwtRequest.getPassword()));
//        }catch (UsernameNotFoundException e){
//            e.printStackTrace();
//            throw new Exception("Bad Credentials");
//        }
//
//        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
//        String token=this.jwtUtil.generateToken(userDetails);
//        System.out.println("JWT "+token);
//        return ResponseEntity.ok(new JWtResponse(token));
//    }
    @PostMapping("/login")
    public ResponseEntity<JWtResponse> loginSecurity(@RequestBody JwtRequest jwtRequest){
        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String jwtToken = this.jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = this.refreshTokenService.createToken(userDetails.getUsername());
        JWtResponse build = JWtResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getRefreshToken())
                .username(userDetails.getUsername())
                .build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(email,password);
        try {
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid Username or Password !!");
        }

    }
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandle(){
        return "Invalid Credentials";
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userService.createUser(user);
        return new ResponseEntity<>(user1,HttpStatus.CREATED);
    }
    @PostMapping("/refresh")
    public JWtResponse refreshJwtToken(@RequestBody RefreshTokenRequest jwtRefreshRequest){
        RefreshToken refreshToken = refreshTokenService.verifyToken(jwtRefreshRequest.getJwtRefresh());
        User user = refreshToken.getUser();
        String token = this.jwtUtil.generateToken(user);

        return JWtResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .token(token)
                .username(user.getEmail())
                .build();
    }

}

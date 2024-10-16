package vn.job.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public String generateToken(UserDetails user) {
        // TODO xu li tao ra token
        return "access-token";
    }
}

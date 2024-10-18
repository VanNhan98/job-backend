package vn.job.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.job.dto.request.LoginRequest;
import vn.job.dto.response.TokenResponse;
import vn.job.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private  final JwtService jwtService;

    public TokenResponse authenticate(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email or Password failed"));

        String accessToken = this.jwtService.generateToken(user, request.getEmail());
        String refreshToken = "DUMMY_REFRESH_TOKEN";
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .build();
    }
}

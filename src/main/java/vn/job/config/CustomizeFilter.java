package vn.job.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.job.dto.response.error.ApiErrorResponse;
import vn.job.model.Token;
import vn.job.service.JwtService;
import vn.job.service.TokenService;
import vn.job.service.UserService;
import vn.job.util.TokenType;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "CUSTOMIZE-FILTER")
public class CustomizeFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    private static final List<String> PUBLIC_API_PATHS = Arrays.asList(
            "/auth/access",  "/auth/logout"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------PreFilter----------------");
        final String authorization =  request.getHeader("Authorization");
        log.info("Authorization: {}",authorization);

        // Bỏ qua kiểm tra token nếu là API login
        final String requestPath = request.getServletPath();
        log.info("Request Path: {}", requestPath);

//        // Bỏ qua kiểm tra token cho các API public
//        if (isPublicApi(requestPath)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authorization.substring("Bearer ".length());
        log.info("token: {}",token);

//        if (!isTokenInDatabase(token)) {
//            // Token không tồn tại trong cơ sở dữ liệu
//            log.warn("Token not found in database: {}", token);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write(errorResponse("Token is invalid or expired."));
//            return;
//        }

        String email ="";
        try {
            email = jwtService.extractEmail(token, TokenType.ACCESS_TOKEN);
            log.info("email: {}", email);
        }catch (AccessDeniedException e) {
            log.info(e.getMessage());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(errorResponse(e.getMessage()));
            return;
        }


        if(StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);
            if( jwtService.isValid(token, TokenType.ACCESS_TOKEN,userDetails) ) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);

    }


//    // Kiểm tra xem token có trong cơ sở dữ liệu hay không
//    private boolean isTokenInDatabase(String token) {
//        try {
//            // Kiểm tra access token trước
//            Token currentToken = tokenService.findTokenByAccessToken(token);
//            if (currentToken == null) {
//                // Nếu không tìm thấy access token, kiểm tra refresh token
//                currentToken = tokenService.findTokenByRefreshToken(token);
//            }
//            return currentToken != null ; // Token phải tồn tại và không hết hạn
//        } catch (Exception e) {
//            return false;  // Token không tồn tại hoặc đã bị xóa
//        }
//    }
//    private boolean isPublicApi(String requestPath) {
//        return PUBLIC_API_PATHS.contains(requestPath);
//    }
    /**
     * Create error response with pretty template
     * @param message
     * @return
     */
    private String errorResponse(String message) {
        try {
            ApiErrorResponse error = new ApiErrorResponse();
            error.setTimestamp(new Date());
            error.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            error.setMessage(message);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(error);
        } catch (Exception e) {
            return ""; // Return an empty string if serialization fails
        }
    }

}

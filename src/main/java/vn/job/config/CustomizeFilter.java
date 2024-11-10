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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("-------------CustomizeFilter Activated----------------");

        // Lấy token từ header Authorization
        final String authorization = request.getHeader("Authorization");
        String email = null;

        // Kiểm tra token, nếu có thì tiến hành giải mã
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
            final String token = authorization.substring("Bearer ".length());
            log.info("Token: {}", token.substring(0, 20));

            try {
                // Giải mã và lấy email từ token
                email = jwtService.extractEmail(token, TokenType.ACCESS_TOKEN);
                log.info("Email extracted from token: {}", email);
            } catch (AccessDeniedException e) {
                log.warn("Access denied: {}", e.getMessage());
                // Không chặn request, tiếp tục luồng xử lý
            }

            // Nếu có email và chưa thiết lập SecurityContext, tạo Authentication
            if (StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);
                if (jwtService.isValid(token, TokenType.ACCESS_TOKEN, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Luôn tiếp tục chuỗi filter để đảm bảo request đi qua PermissionInterceptor
        filterChain.doFilter(request, response);
    }

    /**
     * Tạo phản hồi lỗi với định dạng JSON.
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
            return ""; // Trả về chuỗi rỗng nếu serialization gặp lỗi
        }
    }
}

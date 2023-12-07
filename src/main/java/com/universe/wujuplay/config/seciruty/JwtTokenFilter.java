//package com.universe.wujuplay.config.seciruty;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader("");
//        String username = null;
//        String authToken = null;
//        if (header != null && header.startsWith(TOKEN_PREFIX)) {
//            authToken = header.replace(TOKEN_PREFIX,"");
//            try {
//                username = jwtTokenUtil.getUsernameFromToken(authToken);
//            } catch (IllegalArgumentException e) {
//                logger.error("an error occured during getting username from token", e);
//                // JwtException (custom exception) 예외 발생시키기
//                throw new JwtException("유효하지 않은 토큰");
//            } catch (ExpiredJwtException e) {
//                logger.warn("the token is expired and not valid anymore", e);
//                throw new JwtException("토큰 기한 만료");
//            } catch(SignatureException e){
//                logger.error("Authentication Failed. Username or Password not valid.");
//                throw new JwtException("사용자 인증 실패");
//            }
//        } else {
//            logger.warn("couldn't find bearer string, will ignore the header");
//        }
//    }
//}

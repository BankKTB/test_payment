package th.com.bloomcode.paymentservice.interceptor;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import th.co.gfmis.GfmisDecryptToken;
import th.com.bloomcode.paymentservice.model.JwtBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

  @Value("${wso2.oauth2.client.provider.jwk-set-uri}")
  private String jwkSetUri;

  @Value("${wso2.oauth2.client.provider.server-name}")
  private String apiServerName;

  private GfmisDecryptToken gfmisJWT;

  @Autowired
  @Qualifier("redisPortalStringRedisTemplate")
  private StringRedisTemplate roleStringRedisTemplate;

  @PostConstruct
  public void init() throws Exception {
    log.info("init after constructor: apiServername={}", apiServerName);
    log.info("init after constructor: jwtSetUrl={}", jwkSetUri);
    try {
      gfmisJWT = new GfmisDecryptToken(jwkSetUri);
    } catch (Exception e) {
      log.info("Cannot create JWT working set");
      throw new Exception("Cannot connect to Authentication Server");
    }
  }

  @Override
  public boolean preHandle(
          HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
          throws Exception {

//    log.info("Interceptor request session id: {}", httpServletRequest.getSession().getId());
    boolean found = false;

    String header = httpServletRequest.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      httpServletResponse.setStatus(9999);
      return false;
    } else {
      String jwt = header.replace("Bearer ", "");
//      log.info("jwt : {}", jwt);
      Gson gson = new Gson();

      // New way to validate JWT
      try {
        JSONObject result = gfmisJWT.decodeToken(jwt);
        JwtBody jwtBody = gson.fromJson(result.toJSONString(), JwtBody.class);
        JSONObject jwtInfo = gfmisJWT.validatePermission(jwt, apiServerName);
        if (null != jwtInfo) {
//          log.info("JWT info: {}", jwtInfo);

//          String iat = jwtInfo.getAsString("iat");
//          String username = jwtInfo.getAsString("SN");
//          log.info("iat: {}, username: {}", iat, username);
          // Setup searchKey = iat-username
//          String searchKey = iat + '-' + username;
          String searchKey = jwtInfo.getAsString("searchKey");
//          log.info("Search Key: {}", searchKey);

          String user = roleStringRedisTemplate.opsForValue().get(searchKey); // if api uses other logic, change this.
          if (user != null) {                 // User is still logging on
//            log.info("Still logged in: {}", user);
            found = true;
            roleStringRedisTemplate.expire(searchKey, 30, TimeUnit.MINUTES);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null, null);
            authentication.setDetails(jwtBody);

            SecurityContextHolder.getContext().setAuthentication(authentication);
          } else {
            httpServletResponse.setStatus(9999);
            httpServletResponse.sendError(401);
            return false;
          }
        } else {
          log.info("JWT does not include permission in system: {}", apiServerName);
        }

      } catch (Exception e) {
        e.printStackTrace();
        httpServletResponse.setStatus(9999);
        httpServletResponse.sendError(500);
        return false;
      }

    }

    return found;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         Object o,
                         ModelAndView modelAndView)
          throws Exception {}

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse,
                              Object o,
                              Exception e)
          throws Exception {}
}

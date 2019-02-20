package com.love.plus.authservice.controller;

import com.love.plus.authservice.dto.ResultDto;
import com.love.plus.authservice.dto.ServiceErrorCode;
import com.love.plus.authservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/token")
    public ResponseEntity<ResultDto> login(@RequestParam String username, @RequestParam String password) {
        // TODO: check credential with data in database
        if (username.equalsIgnoreCase("admin") &&
                password.equals("p@ssw0rd")) {
            String token = jwtTokenProvider.generateJWTToken(1L, username);
            return ResponseEntity.ok(ResultDto.success(token));
        }

        ResultDto resultDto = ResultDto.error(ServiceErrorCode.INVALID_CREDENTIAL);
        return ResponseEntity.ok(resultDto);

    }

    @PostMapping("/token/verify")
    public ResponseEntity<ResultDto> verify(@RequestBody String token) {
        String userId = jwtTokenProvider.verifyJWT(token);
        if (userId == null) {
            ResultDto resultDto = ResultDto.error(ServiceErrorCode.INVLIAD_TOKEN);
            return ResponseEntity.ok(resultDto);
        }
        return ResponseEntity.ok(ResultDto.success(userId));
    }

//    @GetMapping("/token/refresh")
//    public Map<String,Object> refreshToken(@RequestParam String refreshToken){
//        Map<String,Object> resultMap = new HashMap<>();
//        String refreshTokenKey = String.format(jwtRefreshTokenKeyFormat, refreshToken);
//        String userName = (String)stringRedisTemplate.opsForHash().get(refreshTokenKey,
//                "userName");
//        if(StringUtils.isBlank(userName)){
//            resultMap.put("code", "10001");
//            resultMap.put("msg", "refreshToken过期");
//            return resultMap;
//        }
//        String newToken = buildJWT(userName);
//        //替换当前token，并将旧token添加到黑名单
//        String oldToken = (String)stringRedisTemplate.opsForHash().get(refreshTokenKey,
//                "token");
//        stringRedisTemplate.opsForHash().put(refreshTokenKey, "token", newToken);
//        stringRedisTemplate.opsForValue().set(String.format(jwtBlacklistKeyFormat, oldToken), "",
//                tokenExpireTime, TimeUnit.MILLISECONDS);
//        resultMap.put("code", "10000");
//        resultMap.put("data", newToken);
//        return resultMap;
//    }
}

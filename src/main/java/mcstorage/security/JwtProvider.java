package mcstorage.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import mcstorage.util.Logging;

@Component
public class JwtProvider {
	
	//HARDCODED FOR NOW
	//@Value("${STORAGE_SECRET_KEY}")
	private static String storageSecretKey = "h6pfb7rn9dkvk6hbt78xrlp8ed55wbpszsv9vtrn1yc5bhs5pnjjztqx3nba4e7v";
	
	private RedisTemplate<String, String> redisTemplate;
	
	private SecretKey secretKey;
	
	public static long accessExpTime = 360_000L; //in MILLISECONDS
	public static long refreshExpTime = 30 * 24 * 60 * 60 * 1000L; //30 days
	
	public JwtProvider(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		
		byte[] apiAccessSecretKey = DatatypeConverter.parseBase64Binary(storageSecretKey);
		this.secretKey = new SecretKeySpec(apiAccessSecretKey, "HmacSHA256");
		
	}
	
	protected boolean validateStorageToken(String storageToken) {
		return validateToken(storageToken, secretKey);
	}
	
	protected Claims getTokenClaims(@NonNull String token) {
        return getClaims(token, secretKey);
    }
	
	
	private boolean validateToken(@NonNull String token, SecretKey key) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			Logging.log("Token is not valid: " + e.getMessage());
		}
		return false;
	}
	
	protected Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
	
	public void deleteTokenFromRedis(HttpServletRequest request, String subjectName) {
		String userAgent = request.getHeader("User-Agent");
		redisTemplate.opsForHash().delete(subjectName, userAgent);
	}
}

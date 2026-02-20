package mcstorage.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StorageTempTokenManager {
	
	static String sTokenName = "storage_token";
	private RedisTemplate<String, String> redisTemplate;

	public StorageTempTokenManager(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		
	}
	
	public void verifyToken(String token) {
			//TODO
	}
}

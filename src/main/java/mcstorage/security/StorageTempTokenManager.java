package mcstorage.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import mcstorage.util.HashGen;

@Component
public class StorageTempTokenManager {
	
	static String sTokenName = "storage_token";
	private RedisTemplate<String, String> redisTemplate;

	public StorageTempTokenManager(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		
	}
	
	public boolean verifyRequest(String uploadId, String token){
			if(redisTemplate.opsForHash().get(uploadId, "hash") == HashGen.generateStringHash(token)) {
				return true;
			} else {
				return false;
			}
	}
}

package mcstorage.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import mcstorage.util.HashGen;
import mcstorage.util.Logging;

@Component
public class StorageTempTokenManager {
	
	static String sTokenName = "storage_token";
	private RedisTemplate<String, String> redisTemplate;

	public StorageTempTokenManager(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		
	}
	
	public boolean verifyRequest(String uploadId, String token){
		String tokenHash = HashGen.generateStringHash(token);
		Logging.log("GENERATED HASH TOKEN: " + tokenHash);
			if(redisTemplate.opsForHash().get(uploadId, "hash").equals(tokenHash)) {
				
				return true;
			} else {
				Logging.log("TOKEN IS INVALID");
				return false;
			}
	}
}

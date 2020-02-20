package com.lee.ficss.util;


import com.lee.ficss.pojo.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

@Component
public class Encryption {

    private SecureRandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private String algorithmName = "MD5";

    public int iterationTime = 5;

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getIterationTime() {
        return iterationTime;
    }

    public void setIterationTime(int iterationTime) {
        this.iterationTime = iterationTime;
    }

    public SecureRandomNumberGenerator getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    public void setRandomNumberGenerator(SecureRandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void encryptPassword(User user){
        if (user.getPassword() != null){
            user.setSalt(randomNumberGenerator.nextBytes().toHex());
            String password = new SimpleHash(getAlgorithmName(), user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()), getIterationTime()).toHex();
            user.setPassword(password);
        }
    }
}

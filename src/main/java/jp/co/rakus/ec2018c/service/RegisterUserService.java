package jp.co.rakus.ec2018c.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.rakus.ec2018c.domain.User;
import jp.co.rakus.ec2018c.repository.UserRepository;

/**
 * ユーザー登録関連サービスクラス.
 * 
 * @author yuta.ikeda
 *
 */
@Service
//FIXME:javadoc漏れ
public class RegisterUserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	
	/**
	 * 入力された情報を保存する.
	 * 
	 * @param user
	 * 			入力された登録情報
	 * @return
	 */
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	
	public String encodePassword(String rawPassword) {
		String encodedPassword = passwordEncoder.encode(rawPassword);
		return encodedPassword;
	}
	
	
}

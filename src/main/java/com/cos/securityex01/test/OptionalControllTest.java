package com.cos.securityex01.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.securityex01.model.User;
import com.cos.securityex01.repository.UserRepository;

@RestController
public class OptionalControllTest {
		
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test/user/{id}")
	public User 옵셔널_유저찾기(@PathVariable int id ) {
			//		<첫번째 방법>
			//		Optional<User> userOptional = userRepository.findById(id);
			//		User user;
			//		if (userOptional.isPresent()) {
			//			user = userOptional.get();
			//		} else {
			//			user = new User();
			//		}
			//		return user;
			//		--------------------------------------------------------------
			// 		<두번쨰 방법> orElseGet : 가장 많이 씀
			//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
			//
			//			@Override
			//			public User get() {
			//				return User.builder().id(5).username("hongcha").email("h@naver.com").build();
			//			
			//			}		
			//		});
			
			//		익명 함수 arrow 로 넣어줌 → 타입 필요없이 return 받는 값만 중요한 거! (함수가 하나일 때만 사용할 수 있음)
			//		User user = userRepository.findById(id).orElseGet(()-> {
			//				return User();	
			//		});
			
					User user = userRepository.findById(id).orElseThrow(()-> {
							return new NullPointerException("값 없어요");
					});
			
					return user;
				}
				
			}

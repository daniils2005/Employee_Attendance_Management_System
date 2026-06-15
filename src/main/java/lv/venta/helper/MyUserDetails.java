package lv.venta.helper;

import java.util.ArrayList;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lv.venta.model.User;

public class MyUserDetails implements UserDetails {

	private User user;
	
	public MyUserDetails(User inputUser) {
		if(inputUser != null) {
			user = inputUser;
		}
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority auth = new SimpleGrantedAuthority(user.getAuthority().getTitle());
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(auth);
		return authorities;
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

}

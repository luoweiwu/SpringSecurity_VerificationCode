package com.Gary.GaryRESTful.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

@Entity
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	
	//用户是否没有失效
	@Transient
	private boolean accountNonExpried;
	//用户是否冻结
	@Transient
	private boolean accountNonLocked;
	//证明是否过期
	@Transient
	private boolean credentialsNonExpired;
	//判断是否删除
	@Transient
	private boolean enabled;
	@Transient
	//添加	@Transient 注解可以不将  Set<GrantedAuthority>映射到数据库表上
	private Set<GrantedAuthority> authorities;
	
	//给hibernatre用的构造方法
	protected User() {
		
	}
	
	public User(Long id,String username,String password)
	{
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	//给SpringSecurity用的构造方法
	public User(String username,String password,
			Collection<? extends GrantedAuthority> authorities) {
			this(username,password,true,true,true,true,authorities);
	}
	
	public User(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

		if (((username == null) || "".equals(username)) || (password == null)) {
			throw new IllegalArgumentException(
					"Cannot pass null or empty values to constructor");
		}

		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpried = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}
	
	private static SortedSet<GrantedAuthority> sortAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
				new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
					"GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}
	
	private static class AuthorityComparator implements Comparator<GrantedAuthority>,
	Serializable {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	public int compare(GrantedAuthority g1, GrantedAuthority g2) {
	// Neither should ever be null as each entry is checked before adding it to
	// the set.
	// If the authority is null, it is a custom authority and should precede
	// others.
	if (g2.getAuthority() == null) {
		return -1;
	}

	if (g1.getAuthority() == null) {
		return 1;
	}

	return g1.getAuthority().compareTo(g2.getAuthority());
	}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//用户权限
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	//用户是否没有失效
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return accountNonExpried;
	}
	
	//用户是否被冻结
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return accountNonLocked;
	}
	
	//用户是否证明权限过期
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return credentialsNonExpired;
	}
	
	//判断用户是否删除
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}
	
	
	
}

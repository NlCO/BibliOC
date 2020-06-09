package fr.nlco.biblioc.bibliocweb.service;

import fr.nlco.biblioc.bibliocweb.model.Member;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation de l'interface UserDetailsServie
 */
@Service
@Qualifier("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final BibliocapiProxy bibliocapiProxy;

    @Autowired
    public CustomUserDetailsService(BibliocapiProxy bibliocapiProxy) {
        this.bibliocapiProxy = bibliocapiProxy;
    }

    @Override
    public UserDetails loadUserByUsername(String memberNumber) throws UsernameNotFoundException {
        Member member = bibliocapiProxy.getMemberAuthByMemberNumber(memberNumber);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("Member"));
        return new org.springframework.security.core.userdetails.User(member.getMemberNumber(), member.getPassword(), grantedAuthorities);
    }
}

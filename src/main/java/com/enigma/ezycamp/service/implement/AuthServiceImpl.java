package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.constant.UserRole;
import com.enigma.ezycamp.dto.request.LoginRequest;
import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.dto.request.RegisterRequest;
import com.enigma.ezycamp.dto.response.LoginResponse;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.entity.*;
import com.enigma.ezycamp.repository.UserAccountRepository;
import com.enigma.ezycamp.service.*;
import com.enigma.ezycamp.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ValidationUtil validationUtil;
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final GuideImageService guideImageService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LocationService locationService;
    @Value("${ezycamp.username.superadmin}")
    private String USERNAME_SUPER_ADMIN;
    @Value("${ezycamp.password.superadmin}")
    private String PASSWORD_SUPER_ADMIN;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void initAdmin(){
        Optional<UserAccount> admin = userAccountRepository.findByUsername(USERNAME_SUPER_ADMIN);
        if(admin.isPresent()) return;
        Role adminRole = roleService.getOrSave(UserRole.ROLE_ADMIN);
        userAccountRepository.saveAndFlush(UserAccount.builder()
                .username(USERNAME_SUPER_ADMIN).password(passwordEncoder.encode(PASSWORD_SUPER_ADMIN))
                .isEnable(true).roles(List.of(adminRole)).build());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(RegisterRequest request) {
        validationUtil.validate(request);
        Optional<UserAccount> customerCheck = userAccountRepository.findByUsername(request.getUsername());
        if(customerCheck.isPresent()) throw new DataIntegrityViolationException("Username sudah terdaftar");
        Role role = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        String password = passwordEncoder.encode(request.getPassword());
        UserAccount account = UserAccount.builder()
                .username(request.getUsername()).password(password)
                .roles(List.of(role)).isEnable(true).build();
        userAccountRepository.saveAndFlush(account);
        Customer customer = Customer.builder().name(request.getName())
                .phone(request.getPhone()).userAccount(account).build();
        customerService.addCustomer(customer);
        List<String> roleAuth = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder().username(account.getUsername()).roles(roleAuth).build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerGuide(RegisterGuideRequest request) {
        validationUtil.validate(request);
        Optional<UserAccount> guideCheck = userAccountRepository.findByUsername(request.getUsername());
        if(guideCheck.isPresent()) throw new DataIntegrityViolationException("Username sudah terdaftar");
        Role role = roleService.getOrSave(UserRole.ROLE_GUIDE);
        String password = passwordEncoder.encode(request.getPassword());
        UserAccount account = UserAccount.builder()
                .username(request.getUsername()).password(password)
                .roles(List.of(role)).isEnable(true).build();
        userAccountRepository.saveAndFlush(account);
        Location location = locationService.getById(request.getLocation());
        Guide guide = Guide.builder().name(request.getName()).phone(request.getPhone())
                .price(request.getPrice()).location(location).userAccount(account).build();
        List<GuideImage> images = new ArrayList<>();
        for (MultipartFile image:request.getImages()){
            GuideImage imageAdded = guideImageService.addImage(guide, image);
            images.add(imageAdded);
        }
        guide.setImages(images);
        List<String> roleAuth = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder().username(account.getUsername()).roles(roleAuth).build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        validationUtil.validate(request);
        Authentication auth =new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authenticate =authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount account = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(account);
        return LoginResponse.builder().username(account.getUsername())
                .token(token).roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    @Override
    public boolean validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = this.userAccountRepository.findByUsername(authentication.getPrincipal().toString()).orElse(null);
        return userAccount != null;
    }
}

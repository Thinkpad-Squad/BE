package com.enigma.ezycamp.service;

import com.enigma.ezycamp.constant.UserRole;
import com.enigma.ezycamp.dto.request.LoginRequest;
import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.dto.request.RegisterRequest;
import com.enigma.ezycamp.dto.response.LoginResponse;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.entity.*;
import com.enigma.ezycamp.repository.UserAccountRepository;
import com.enigma.ezycamp.service.implement.AuthServiceImpl;
import com.enigma.ezycamp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerService customerService;
    @Mock
    private GuideImageService guideImageService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private LocationService locationService;
    private AuthService authService;
    @BeforeEach
    void setUp(){
        authService = new AuthServiceImpl(validationUtil,userAccountRepository,roleService,passwordEncoder,customerService,guideImageService,authenticationManager,jwtService,locationService);
    }

    @Test
    void registerCustomer() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .password("testpassword")
                .name("Test User")
                .phone("1234567890")
                .build();
        Role role = Role.builder()
                .id("1")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        UserAccount account = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("testpassword")
                .isEnable(true)
                .roles(List.of(role))
                .build();
        when(roleService.getOrSave(UserRole.ROLE_CUSTOMER)).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(account);
        doNothing().when(customerService).addCustomer(any(Customer.class));
        RegisterResponse response = authService.registerCustomer(request);
        assertEquals("testuser", response.getUsername());
        assertEquals(List.of("ROLE_CUSTOMER"), response.getRoles());
        verify(validationUtil).validate(request);
        verify(roleService).getOrSave(UserRole.ROLE_CUSTOMER);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
        verify(customerService).addCustomer(any(Customer.class));
    }

    @Test
    void registerGuide(){
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        RegisterGuideRequest request = RegisterGuideRequest.builder().name("guide").username("username").password("password").images(List.of(image)).phone("0888").price(1L).location("lokasi").build();
        Role role = Role.builder()
                .id("1")
                .role(UserRole.ROLE_GUIDE)
                .build();
        UserAccount account = UserAccount.builder()
                .id("1")
                .username("username")
                .password("password")
                .isEnable(true)
                .roles(List.of(role))
                .build();
        Location location = Location.builder().id("1").name("location").build();
        Guide guide = Guide.builder().name(request.getName()).phone(request.getPhone())
                .price(request.getPrice()).location(location).userAccount(account).build();
        GuideImage guideImage = GuideImage.builder().id("1").name("image").originalName("image.jpg").path("path").url("url").size(image.getSize()).guide(guide).build();
        when(roleService.getOrSave(UserRole.ROLE_GUIDE)).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(account);
        when(guideImageService.addImage(any(Guide.class), any(MultipartFile.class))).thenReturn(guideImage);
        RegisterResponse response = authService.registerGuide(request);
        assertEquals(request.getUsername(), response.getUsername());
    }

    @Test
    void login() {
        LoginRequest request = new LoginRequest("username", "password");
        UserAccount userAccount = UserAccount.builder().id("tes account id")
                .password("password").username("username")
                .isEnable(true).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        String token = "token";
        LoginResponse expectedResponse = LoginResponse.builder()
                .username("username")
                .roles(List.of("ROLE_CUSTOMER"))
                .token(token)
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken(userAccount, null));
        when(jwtService.generateToken(userAccount)).thenReturn(token);
        LoginResponse actualResponse = authService.login(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userAccount);
    }
}

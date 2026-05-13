package com.prog4.EjemploDesdeCero.features.clases.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prog4.EjemploDesdeCero.features.clases.repositories.IClaseRepository;
import com.prog4.EjemploDesdeCero.features.usuario.models.Role;
import com.prog4.EjemploDesdeCero.features.usuario.models.User;
import com.prog4.EjemploDesdeCero.features.usuario.repositories.IUserRepository;
import com.prog4.EjemploDesdeCero.features.usuario.services.interfaces.domain.IJwtService;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClaseControllerE2EIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String ADMIN_USER = "admin_e2e";
    private static final String CLIENT_USER = "client_e2e";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IClaseRepository claseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IJwtService jwtService;

    private String tokenAdmin;
    private String tokenClient;

    @BeforeEach
    void setUp() {
        claseRepository.deleteAll();
        userRepository.deleteAll();

        User admin = new User();
        admin.setUsername(ADMIN_USER);
        admin.setPassword(passwordEncoder.encode("Password1!"));
        admin.setRole(Role.ROLE_ADMIN);
        userRepository.save(admin);

        User client = new User();
        client.setUsername(CLIENT_USER);
        client.setPassword(passwordEncoder.encode("Password1!"));
        client.setRole(Role.ROLE_CLIENT);
        userRepository.save(client);

        tokenAdmin = jwtService.generateToken(admin);
        tokenClient = jwtService.generateToken(client);
    }

    @Test
    @DisplayName("GET /api/clases con JWT CLIENT responde 200")
    void get_list_withClientToken_isOk() throws Exception {
        mockMvc.perform(get("/api/clases").header("Authorization", bearerClient()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("listad")));
    }

    @Test
    @DisplayName("GET /api/clases con JWT ADMIN responde 200")
    void get_list_withAdminToken_isOk() throws Exception {
        mockMvc.perform(get("/api/clases").header("Authorization", bearerAdmin()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/clases sin token responde 403 (acceso denegado a recurso protegido)")
    void get_list_withoutToken_isForbidden() throws Exception {
        mockMvc.perform(get("/api/clases")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/clases con CLIENT responde 403")
    void post_create_withClientToken_isForbidden() throws Exception {
        String body = """
                {"name":"Yoga","instructor":"Ana","maxCapacity":15}
                """;
        mockMvc.perform(
                        post("/api/clases")
                                .header("Authorization", bearerClient())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/clases con ADMIN y body válido responde 200")
    void post_create_withAdminToken_isOk() throws Exception {
        String body = """
                {"name":"Yoga","instructor":"Ana","maxCapacity":15}
                """;
        mockMvc.perform(
                        post("/api/clases")
                                .header("Authorization", bearerAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Yoga"));
    }

    @Test
    @DisplayName("POST /api/clases con body inválido responde 400")
    void post_create_invalidBody_isBadRequest() throws Exception {
        String body = """
                {"name":"","instructor":"Ana","maxCapacity":15}
                """;
        mockMvc.perform(
                        post("/api/clases")
                                .header("Authorization", bearerAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH y DELETE con ADMIN sobre id inexistente responden 404")
    void patchAndDelete_notFound_returns404() throws Exception {
        String patchBody = """
                {"instructor":"Otro","maxCapacity":20}
                """;
        mockMvc.perform(
                        patch("/api/clases/999999")
                                .header("Authorization", bearerAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patchBody))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/clases/999999").header("Authorization", bearerAdmin()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH y DELETE felices con ADMIN responden 200")
    void patchAndDelete_happyPath_isOk() throws Exception {
        String createBody = """
                {"name":"Spin","instructor":"Coach","maxCapacity":12}
                """;
        String response = mockMvc.perform(
                        post("/api/clases")
                                .header("Authorization", bearerAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = OBJECT_MAPPER.readTree(response).path("data").path("id").asLong();

        String patchBody = """
                {"instructor":"Nuevo coach","maxCapacity":18}
                """;
        mockMvc.perform(
                        patch("/api/clases/" + id)
                                .header("Authorization", bearerAdmin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patchBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.instructor").value("Nuevo coach"));

        mockMvc.perform(delete("/api/clases/" + id).header("Authorization", bearerAdmin()))
                .andExpect(status().isOk());
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }

    private String bearerAdmin() {
        return bearer(tokenAdmin);
    }

    private String bearerClient() {
        return bearer(tokenClient);
    }
}

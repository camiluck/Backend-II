package com.minimarket;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RolRepository rolRepository,
                      UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Crear roles si no existen
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombre("ROLE_ADMIN");
                    return rolRepository.save(r);
                });

        Rol rolEmpleado = rolRepository.findByNombre("ROLE_EMPLEADO")
                .orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombre("ROLE_EMPLEADO");
                    return rolRepository.save(r);
                });

        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                .orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombre("ROLE_CLIENTE");
                    return rolRepository.save(r);
                });

        // Crear usuario ADMIN si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(rolAdmin));
            usuarioRepository.save(admin);
        }

        // Crear usuario EMPLEADO si no existe
        if (usuarioRepository.findByUsername("empleado1").isEmpty()) {
            Usuario empleado = new Usuario();
            empleado.setUsername("empleado1");
            empleado.setPassword(passwordEncoder.encode("empleado123"));
            empleado.setRoles(Set.of(rolEmpleado));
            usuarioRepository.save(empleado);
        }

        // Crear usuario CLIENTE si no existe
        if (usuarioRepository.findByUsername("cliente1").isEmpty()) {
            Usuario cliente = new Usuario();
            cliente.setUsername("cliente1");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setRoles(Set.of(rolCliente));
            usuarioRepository.save(cliente);
        }

        System.out.println("=== Usuarios de prueba cargados correctamente ===");
        System.out.println("admin / admin123       -> ROLE_ADMIN");
        System.out.println("empleado1 / empleado123 -> ROLE_EMPLEADO");
        System.out.println("cliente1 / cliente123   -> ROLE_CLIENTE");
    }
}

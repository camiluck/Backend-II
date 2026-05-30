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

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository,
                      RolRepository rolRepository,
                      PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Crear roles si no existen
        Rol rolCliente  = crearRolSiNoExiste("ROLE_CLIENTE");
        Rol rolEmpleado = crearRolSiNoExiste("ROLE_EMPLEADO");
        Rol rolGerente  = crearRolSiNoExiste("ROLE_GERENTE");

        // Crear usuarios de prueba si no existen
        crearUsuarioSiNoExiste("cliente1",  "cliente123",  rolCliente);
        crearUsuarioSiNoExiste("empleado1", "empleado123", rolEmpleado);
        crearUsuarioSiNoExiste("gerente1",  "gerente123",  rolGerente);

        System.out.println("=== Usuarios de prueba cargados ===");
        System.out.println("cliente1  / cliente123  -> ROLE_CLIENTE");
        System.out.println("empleado1 / empleado123 -> ROLE_EMPLEADO");
        System.out.println("gerente1  / gerente123  -> ROLE_GERENTE");
    }

    private Rol crearRolSiNoExiste(String nombre) {
        return rolRepository.findByNombre(nombre).orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre(nombre);
            return rolRepository.save(r);
        });
    }

    private void crearUsuarioSiNoExiste(String username, String password, Rol rol) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            u.setRoles(Set.of(rol));
            usuarioRepository.save(u);
        }
    }
}

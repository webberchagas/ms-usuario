package br.com.webberchagas.ms_usuario.business;

import br.com.webberchagas.ms_usuario.business.dtos.UsuarioDTO;
import br.com.webberchagas.ms_usuario.business.mapper.UsuarioMapper;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Usuario;
import br.com.webberchagas.ms_usuario.infrastructure.exception.ConflitoException;
import br.com.webberchagas.ms_usuario.infrastructure.exception.ResourceNotFoundException;
import br.com.webberchagas.ms_usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));

        Usuario usuario = mapper.toUsuario(usuarioDTO);
        Usuario usuarioSaved = repository.save(usuario);
        return mapper.toUsuarioDTO(usuarioSaved);
    }

    private boolean verificarEmailExistente(String email) {
        return repository.existsByEmail(email);
    }

    private void emailExiste(String email) {
        try {
            if (verificarEmailExistente(email)) {
                throw new ConflitoException("Email já cadastrado: " + email);
            }
        } catch (ConflitoException e) {
            throw new ConflitoException("Email já cadastrado" + e.getMessage());
        }
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        Usuario usuario = repository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email)
        );
        return mapper.toUsuarioDTO(usuario);
    }

    public void deletarUsuarioPorEmail(String email) {
        Usuario usuario = repository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email)
        );
        repository.delete(usuario);
    }
}

package br.com.webberchagas.ms_usuario.business;

import br.com.webberchagas.ms_usuario.business.dtos.UsuarioDTO;
import br.com.webberchagas.ms_usuario.business.mapper.UsuarioMapper;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Usuario;
import br.com.webberchagas.ms_usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        Usuario usuario = mapper.toUsuario(usuarioDTO);
        Usuario usuarioSaved = repository.save(usuario);
        return mapper.toUsuarioDTO(usuarioSaved);
    }

}

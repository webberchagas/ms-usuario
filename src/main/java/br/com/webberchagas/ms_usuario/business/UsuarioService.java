package br.com.webberchagas.ms_usuario.business;

import br.com.webberchagas.ms_usuario.business.dtos.EnderecoDTO;
import br.com.webberchagas.ms_usuario.business.dtos.TelefoneDTO;
import br.com.webberchagas.ms_usuario.business.dtos.UsuarioDTO;
import br.com.webberchagas.ms_usuario.business.mapper.UsuarioMapper;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Endereco;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Telefone;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Usuario;
import br.com.webberchagas.ms_usuario.infrastructure.exception.ConflitoException;
import br.com.webberchagas.ms_usuario.infrastructure.exception.ResourceNotFoundException;
import br.com.webberchagas.ms_usuario.infrastructure.repository.EnderecoRepository;
import br.com.webberchagas.ms_usuario.infrastructure.repository.TelefoneRepository;
import br.com.webberchagas.ms_usuario.infrastructure.repository.UsuarioRepository;
import br.com.webberchagas.ms_usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final TelefoneRepository telefoneRepository;
    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));

        Usuario usuario = mapper.toUsuario(usuarioDTO);
        Usuario usuarioSaved = usuarioRepository.save(usuario);
        return mapper.toUsuarioDTO(usuarioSaved);
    }

    private boolean verificarEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
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
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email)
        );
        return mapper.toUsuarioDTO(usuario);
    }

    public void deletarUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email)
        );
        usuarioRepository.delete(usuario);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        dto.setSenha(dto.getSenha() != null ? bCryptPasswordEncoder.encode(dto.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Usuário não encontrado com o email: " + email)
        );

        Usuario usuarioAtualizado = mapper.updateUsuario(usuarioEntity, dto);
        return mapper.toUsuarioDTO(usuarioRepository.save(usuarioAtualizado));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO dto) {
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço com id: " + idEndereco + " não foi encontrado"));

        Endereco enderecoAtualizado = mapper.updateEndereco(entity, dto);

        return mapper.toEnderecoDTO(enderecoRepository.save(enderecoAtualizado));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Telefone com id: " + idTelefone + " Não foi encontrado")
        );

        Telefone telefoneAtualizado = mapper.updateTelefone(entity, dto);

        return mapper.toTelefoneDTO(telefoneRepository.save(telefoneAtualizado));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                        new ResourceNotFoundException("E-mail não localizado " + email));

        Endereco endereco = mapper.cadastraEnderecoDTO(dto, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return mapper.toEnderecoDTO(enderecoEntity);
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("E-mail não localizado " + email));

        Telefone telefone = mapper.cadastraTelefoneDTO(dto, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return mapper.toTelefoneDTO(telefoneEntity);
    }
}

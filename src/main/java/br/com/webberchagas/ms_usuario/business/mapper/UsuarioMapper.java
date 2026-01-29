package br.com.webberchagas.ms_usuario.business.mapper;

import br.com.webberchagas.ms_usuario.business.dtos.EnderecoDTO;
import br.com.webberchagas.ms_usuario.business.dtos.TelefoneDTO;
import br.com.webberchagas.ms_usuario.business.dtos.UsuarioDTO;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Endereco;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Telefone;
import br.com.webberchagas.ms_usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper {
    public Usuario toUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(toEnderecoList(usuarioDTO.getEnderecos()))
                .telefones(toTelefoneList(usuarioDTO.getTelefones()))
                .build();
    }

    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(toEnderecoDTOList(usuario.getEnderecos()))
                .telefones(toTelefoneDTOList(usuario.getTelefones()))
                .build();
    }

    public List<Endereco> toEnderecoList(List<EnderecoDTO> enderecoDTOs){
        return enderecoDTOs.stream()
                .map(this::toEndereco)
                .toList();
    }

    public Endereco toEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }

    public List<EnderecoDTO> toEnderecoDTOList(List<Endereco> enderecos){
        return enderecos.stream()
                .map(this::toEnderecoDTO)
                .toList();
    }

    public EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

    public List<Telefone> toTelefoneList(List<TelefoneDTO> telefoneDTOs){
        return telefoneDTOs.stream()
                .map(this::toTelefone)
                .toList();
    }

    public Telefone toTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .build();
    }

    public List<TelefoneDTO> toTelefoneDTOList(List<Telefone> telefones){
        return telefones.stream()
                .map(this::toTelefoneDTO)
                .toList();
    }

    public TelefoneDTO toTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .ddd(telefone.getDdd())
                .numero(telefone.getNumero())
                .build();
    }

    public Usuario updateUsuario(Usuario entity, UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .id(entity.getId())
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : entity.getSenha())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getEmail())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco updateEndereco(Endereco entity, EnderecoDTO dto) {
        return Endereco.builder()
                .id(entity.getId())
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .cidade(dto.getCidade() != null ? dto.getCidade(): entity.getCidade())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .build();
    }

    public Telefone updateTelefone(Telefone entity, TelefoneDTO dto) {
        return Telefone.builder()
                .id(entity.getId())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .build();
    }

}
